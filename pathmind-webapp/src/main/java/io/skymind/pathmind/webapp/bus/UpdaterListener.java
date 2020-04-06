package io.skymind.pathmind.webapp.bus;

import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.skymind.pathmind.services.training.cloud.aws.api.AWSApiClient;
import io.skymind.pathmind.services.training.cloud.aws.api.dto.UpdateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

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
                    .withWaitTimeSeconds(1)
                    .withMaxNumberOfMessages(10);

            List<Message> sqsMessages = awsApiClient.getSqsClient().receiveMessage(receiveMessageRequest).getMessages();
            count = sqsMessages.size();

            sqsMessages.parallelStream().forEach(message -> {
                try {
                    UpdateEvent event = objectMapper.readValue(message.getBody(), UpdateEvent.class);
                    log.warn("{}", event);
                    // TODO: EvenBus.....
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
