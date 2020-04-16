package io.skymind.pathmind.webapp.bus;

import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.skymind.pathmind.services.training.cloud.aws.api.AWSApiClient;
import io.skymind.pathmind.services.training.cloud.aws.api.dto.UpdateEvent;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.data.Run;
import io.skymind.pathmind.webapp.bus.events.PolicyUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.events.RunUpdateBusEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;

import static io.skymind.pathmind.services.training.cloud.aws.api.dto.UpdateEvent.*;

@Slf4j
@Component
public class UpdaterListener {

    private final AWSApiClient awsApiClient;
    private final ObjectMapper objectMapper;

    public UpdaterListener(AWSApiClient awsApiClient, ObjectMapper objectMapper) {
        this.awsApiClient = awsApiClient;
        this.objectMapper = objectMapper;
    }

    @Scheduled(fixedDelay = 1_000)
    public void process() {
        log.info("Scheduled updater: Triggered");

        int count = 0;
        do {
            log.debug("pulling next 10");
            ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(awsApiClient.getUpdaterQueueUrl())
                    .withMessageAttributeNames("All")
                    .withWaitTimeSeconds(1)
                    .withMaxNumberOfMessages(10);

            List<Message> sqsMessages = awsApiClient.getSqsClient().receiveMessage(receiveMessageRequest).getMessages();
            count = sqsMessages.size();

            sqsMessages.parallelStream().forEach(message -> {
                try {

                    ByteBuffer buf = message.getMessageAttributes().get(CARGO_ATTRIBUTE).getBinaryValue();
                    byte[] arr = new byte[buf.remaining()];
                    buf.get(arr);

                    UpdateEvent event = objectMapper.readValue(message.getBody(), UpdateEvent.class);

                    try (ByteArrayInputStream bis = new ByteArrayInputStream(arr);
                         ObjectInputStream in = new ObjectInputStream(bis)) {

                        switch (event.getType().toLowerCase()) {
                            case TYPE_POLICY: {
                                Policy policy = (Policy) in.readObject();
                                EventBus.post(new PolicyUpdateBusEvent(Collections.singletonList(policy)));
                                break;
                            }
                            case TYPE_RUN: {
                                Run run = (Run) in.readObject();
                                EventBus.post(new RunUpdateBusEvent(run));
                                break;
                            }
                            default:
                                log.warn("Unexpected object event: {}", event);
                        }

                    } catch (IOException | ClassNotFoundException e) {
                        log.error("Failed to deserialize event: {}", event, e);
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

        log.info("Scheduled updater: All processed");
    }
}
