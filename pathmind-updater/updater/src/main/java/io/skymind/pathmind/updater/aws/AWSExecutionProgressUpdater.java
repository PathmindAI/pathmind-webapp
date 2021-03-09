package io.skymind.pathmind.updater.aws;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import io.skymind.pathmind.db.dao.RunDAO;
import io.skymind.pathmind.services.analytics.SegmentTrackerService;
import io.skymind.pathmind.services.notificationservice.EmailNotificationService;
import io.skymind.pathmind.services.training.cloud.aws.api.client.AwsApiClientSQS;
import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.data.ProviderJobStatus;
import io.skymind.pathmind.shared.data.Run;
import io.skymind.pathmind.shared.utils.RunUtils;
import io.skymind.pathmind.updater.ExecutionProgressUpdater;
import io.skymind.pathmind.updater.UpdaterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AWSExecutionProgressUpdater implements ExecutionProgressUpdater {

    private final RunDAO runDAO;
    private final EmailNotificationService emailNotificationService;
    private final UpdaterService updaterService;
    private final int updateCompletingAttemptsLimit;
    private final SegmentTrackerService segmentTrackerService;
    private final AwsApiClientSQS sqsClient;
    private final String punctuatorQueueUrl;

    public AWSExecutionProgressUpdater(RunDAO runDAO, AwsApiClientSQS sqsClient,
                                       @Value("${pathmind.aws.sqs.updater_punctuator_queue_url}") String punctuatorQueueUrl,
                                       @Value("${pathmind.updater.completing.attempts}") int completingAttempts,
                                       EmailNotificationService emailNotificationService,
                                       UpdaterService updaterService,
                                       SegmentTrackerService segmentTrackerService) {
        this.runDAO = runDAO;
        this.sqsClient = sqsClient;
        this.punctuatorQueueUrl = punctuatorQueueUrl;
        this.emailNotificationService = emailNotificationService;
        this.updaterService = updaterService;
        this.updateCompletingAttemptsLimit = completingAttempts;
        this.segmentTrackerService = segmentTrackerService;
    }

    @Override
    public void run() {
        log.info("Launching updater");
        do {
            log.debug("pulling next message");
            ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(punctuatorQueueUrl)
//                .withMessageAttributeNames("All")
                    .withWaitTimeSeconds(20)
                    .withMaxNumberOfMessages(1);

            List<Message> sqsMessages = sqsClient.getSqsClient().receiveMessage(receiveMessageRequest).getMessages();

            if (!sqsMessages.isEmpty()) {
                Message message = sqsMessages.get(0);
                try {
                    long runId = Long.parseLong(message.getBody());
                    log.info("Updater is dealing with: {}", runId);
                    Run run = runDAO.getRun(runId);
                    final Map<Long, List<String>> stoppedPoliciesNamesForRuns = runDAO.getStoppedPolicyNamesForRuns(List.of(runId));
                    List<String> stoppedPoliciesNames = stoppedPoliciesNamesForRuns.getOrDefault(runId, Collections.emptyList());
                    ProviderJobStatus providerJobStatus =
                            updaterService.updateRunInformation(run, updateCompletingAttemptsLimit, stoppedPoliciesNames);
                    sendNotificationMail(providerJobStatus.getRunStatus(), run);
                    trackCompletedTrainingInSegment(run, providerJobStatus);
                } catch (Exception e) {
                    log.error("Error process message {}", message, e);
                }
            }
        } while (true);
    }

    private void trackCompletedTrainingInSegment(Run run, ProviderJobStatus providerJobStatus) {
        if (RunStatus.isFinished(providerJobStatus.getRunStatus())) {
            segmentTrackerService.trainingCompleted(runDAO.getUserIdForRun(run.getId()), run);
        }
    }

    /**
     * Send notification mail if Run is completed successfully or with error
     * and if the Run type is discovery or full run
     */
    private void sendNotificationMail(RunStatus jobStatus, Run run) {
        // Do not send notification if there is another run with same run type still executing or the notification is already been sent
        if (RunStatus.isFinished(jobStatus) && !RunUtils.isStoppedByUser(run) && runDAO.shouldSendNotification(run.getExperimentId(), run.getRunType())) {
            emailNotificationService.sendTrainingCompletedEmail(run, jobStatus);
            runDAO.markAsNotificationSent(run.getId());
        }
    }

    private boolean hasJobId(Run run) {
        if (run.getJobId() == null) {
            log.error("Run {} marked as executing but no aws run id found for it.", run.getId());
        }
        return run.getJobId() != null;
    }

}