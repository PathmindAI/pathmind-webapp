package io.skymind.pathmind.webapp.bus;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.db.dao.PolicyDAO;
import io.skymind.pathmind.db.dao.RunDAO;
import io.skymind.pathmind.services.training.cloud.aws.api.AWSApiClient;
import io.skymind.pathmind.services.training.cloud.aws.api.dto.UpdateEvent;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Metrics;
import io.skymind.pathmind.shared.data.MetricsRaw;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.data.Run;
import io.skymind.pathmind.shared.services.PolicyServerService;
import io.skymind.pathmind.webapp.bus.events.main.PolicyServerDeploymentUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.events.main.PolicyUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.events.main.RunUpdateBusEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static io.skymind.pathmind.services.training.cloud.aws.api.dto.UpdateEvent.CARGO_ATTRIBUTE;
import static io.skymind.pathmind.services.training.cloud.aws.api.dto.UpdateEvent.FILTER_ATTRIBUTE;
import static io.skymind.pathmind.services.training.cloud.aws.api.dto.UpdateEvent.TYPE_POLICY;
import static io.skymind.pathmind.services.training.cloud.aws.api.dto.UpdateEvent.TYPE_POLICY_SERVER;
import static io.skymind.pathmind.services.training.cloud.aws.api.dto.UpdateEvent.TYPE_RUN;

@Slf4j
@Component
public class UpdaterListener {

    private final AWSApiClient awsApiClient;
    private final ObjectMapper objectMapper;
    private final Integer sqsWaitSec;
    private final String sqsFilter;
    private final PolicyDAO policyDAO;
    private final RunDAO runDAO;
    private final ExperimentDAO experimentDAO;

    public UpdaterListener(AWSApiClient awsApiClient, ObjectMapper objectMapper,
                           PolicyDAO policyDAO, RunDAO runDAO, ExperimentDAO experimentDAO,
                           @Value("${pathmind.aws.sns.updater_sqs_filter}") String sqsFilter,
                           @Value("${pathmind.webapp.updater.sqs.wait.sec}") Integer sqsWaitSec) {
        this.awsApiClient = awsApiClient;
        this.objectMapper = objectMapper;
        this.sqsWaitSec = sqsWaitSec;
        this.sqsFilter = sqsFilter;
        this.policyDAO = policyDAO;
        this.runDAO = runDAO;
        this.experimentDAO = experimentDAO;
    }

    @Scheduled(fixedDelayString = "${pathmind.webapp.updater.rate.msec}")
    public void process() {
        log.debug("Scheduled updater: Triggered");

        int count = 0;
        do {
            log.debug("pulling next 10");
            ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(awsApiClient.getUpdaterQueueUrl())
                    .withMessageAttributeNames("All")
                    .withWaitTimeSeconds(sqsWaitSec)
                    .withMaxNumberOfMessages(10);

            List<Message> sqsMessages = awsApiClient.getSqsClient().receiveMessage(receiveMessageRequest).getMessages();
            count = sqsMessages.size();

            sqsMessages.parallelStream().forEach(message -> {
                try {

                    boolean doProcess = Optional.ofNullable(message.getMessageAttributes())
                            .map(attrs -> attrs.get(FILTER_ATTRIBUTE))
                            .map(MessageAttributeValue::getStringValue)
                            .map(messageFilter -> {
                                log.debug("Attrs: conf: {}, attr: {}", sqsFilter, messageFilter);
                                return StringUtils.equals(messageFilter, sqsFilter);
                            }).orElse(StringUtils.isEmpty(sqsFilter));

                    if (doProcess) {
                        Optional<ByteBuffer> buf =
                                Optional.ofNullable(message.getMessageAttributes().get(CARGO_ATTRIBUTE))
                                        .map(MessageAttributeValue::getBinaryValue);

                        UpdateEvent event = objectMapper.readValue(message.getBody(), UpdateEvent.class);

                        switch (event.getType().toLowerCase()) {
                            case TYPE_POLICY: {
                                Policy policy = deserialize(buf);
                                final long policyId = policy.getId();
                                List<Metrics> metrics =
                                        policyDAO.getMetricsForPolicies(Collections.singletonList(policyId))
                                                .getOrDefault(policyId, Collections.emptyList());
                                policy.setMetrics(metrics);
                                List<MetricsRaw> metricsRaw =
                                        policyDAO.getMetricsRawForPolicies(Collections.singletonList(policyId))
                                                .getOrDefault(policyId, Collections.emptyList());
                                policy.setMetricsRaws(metricsRaw);
                                EventBus.post(new PolicyUpdateBusEvent(Collections.singletonList(policy)));
                                break;
                            }
                            case TYPE_RUN: {
                                Run run = deserialize(buf);
                                EventBus.post(new RunUpdateBusEvent(run));
                                break;
                            }
                            case TYPE_POLICY_SERVER: {
                                Run run = runDAO.getRun(event.getId());
                                Optional<Experiment> experiment = experimentDAO.getExperiment(run.getExperimentId());
                                int statusCode = Integer.parseInt(event.getInfo());
                                PolicyServerService.DeploymentStatus status = PolicyServerService.DeploymentStatus.STATUS_BY_ID.get(statusCode);
                                if (experiment.isPresent() && status != null) {
                                    EventBus.post(new PolicyServerDeploymentUpdateBusEvent(run.getId(), experiment.get(), status));
                                }
                                break;
                            }
                            default:
                                log.warn("Unexpected object event: {}", event);
                        }


                    }

                } catch (JsonProcessingException e) {
                    log.error("Failed to process message [{}]", message, e);
                }

                awsApiClient.getSqsClient().deleteMessage(
                        new DeleteMessageRequest()
                                .withQueueUrl(awsApiClient.getUpdaterQueueUrl())
                                .withReceiptHandle(message.getReceiptHandle()));
            });
        } while (count > 0);

        log.debug("Scheduled updater: All processed");
    }

    @SuppressWarnings("unchecked")
    private <T> T deserialize(Optional<ByteBuffer> optBuf) {
        if (optBuf.isEmpty()) {
            return null;
        }
        ByteBuffer buf = optBuf.get();
        byte[] arr = new byte[buf.remaining()];
        buf.get(arr);

        try (ByteArrayInputStream bis = new ByteArrayInputStream(arr);
             ObjectInputStream in = new ObjectInputStream(bis)) {
            return (T) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
