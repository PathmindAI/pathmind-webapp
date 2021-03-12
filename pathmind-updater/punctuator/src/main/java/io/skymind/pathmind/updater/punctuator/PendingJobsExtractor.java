package io.skymind.pathmind.updater.punctuator;

import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import io.skymind.pathmind.db.dao.RunDAO;
import io.skymind.pathmind.services.training.cloud.aws.api.client.AwsApiClientSQS;
import io.skymind.pathmind.shared.data.Run;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PendingJobsExtractor {

    private final RunDAO runDAO;
    private final int updateCompletingAttemptsLimit;
    private final AwsApiClientSQS sqsClient;
    private final String punctuatorQueueUrl;

    public PendingJobsExtractor(RunDAO runDAO, AwsApiClientSQS sqsClient,
                                @Value("${pathmind.aws.sqs.updater_punctuator_queue_url}") String punctuatorQueueUrl,
                                @Value("${pathmind.updater.completing.attempts}") int completingAttempts) {
        this.runDAO = runDAO;
        this.sqsClient = sqsClient;
        this.punctuatorQueueUrl = punctuatorQueueUrl;
        this.updateCompletingAttemptsLimit = completingAttempts;
    }

    public void run() {
        // Getting all these values beforehand in single database calls rather than in loops of database calls.
        // Notice that runs that are completed but doesn't have such info in the db yet will be returned by this call.
        // This means that errors after the info is saved in AWS but before the info is saved in DB won't cause the
        // system to be in an inconsistent state.
        // Also, completed runs that doesn't have policy file information in the db will be returned by this call.
        runDAO
                .getExecutingRuns(updateCompletingAttemptsLimit)
                .stream()
                .filter(this::hasJobId)
                .map(Run::getId)
                .peek(runId -> log.debug("Requesting update for run {}", runId))
                .map(String::valueOf)
                .forEach(runId -> {
                    try {
                        SendMessageRequest messageRequest = new SendMessageRequest()
                                .withMessageBody(runId)
                                .withQueueUrl(punctuatorQueueUrl);
                        SendMessageResult result = sqsClient.getSqsClient().sendMessage(messageRequest);
                        log.debug("Update request published {}@{}", runId, result.getMessageId());
                    } catch (Exception e) {
                        log.error("Error for run: " + runId + " : " + e.getMessage(), e);
                    }
                });

    }

    private boolean hasJobId(Run run) {
        if (run.getJobId() == null) {
            log.error("Run {} marked as executing but no aws run id found for it.", run.getId());
        }
        return run.getJobId() != null;
    }

}
