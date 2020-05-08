package io.skymind.pathmind.updater.aws;

import io.skymind.pathmind.db.dao.RunDAO;
import io.skymind.pathmind.services.notificationservice.EmailNotificationService;
import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.data.ProviderJobStatus;
import io.skymind.pathmind.shared.data.Run;
import io.skymind.pathmind.shared.utils.RunUtils;
import io.skymind.pathmind.updater.ExecutionProgressUpdater;
import io.skymind.pathmind.updater.UpdaterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AWSExecutionProgressUpdater implements ExecutionProgressUpdater {

    private final RunDAO runDAO;
    private EmailNotificationService emailNotificationService;
    private final UpdaterService updaterService;

    public AWSExecutionProgressUpdater(RunDAO runDAO, 
                                       EmailNotificationService emailNotificationService,
                                       UpdaterService updaterService) {
        this.runDAO = runDAO;
        this.emailNotificationService = emailNotificationService;
        this.updaterService = updaterService;
    }

    @Override
    public void update() {
        // Getting all these values beforehand in single database calls rather than in loops of database calls.
        // Notice that runs that are completed but doesn't have such info in the db yet will be returned by this call.
        // This means that errors after the info is saved in AWS but before the info is saved in DB won't cause the
        // system to be in an inconsistent state.
        // Also, completed runs that doesn't have policy file information in the db will be returned by this call.
        final List<Run> runs = runDAO.getExecutingRuns();
        final List<Long> runIds = runs.stream().map(Run::getId).collect(Collectors.toList());
        final Map<Long, List<String>> stoppedPoliciesNamesForRuns = runDAO.getStoppedPolicyNamesForRuns(runIds);
        final List<Run> runsWithAwsJobs = runs.stream().filter(this::hasJobId).collect(Collectors.toList());

        runsWithAwsJobs.parallelStream().forEach(run -> {
            try {
                ProviderJobStatus providerJobStatus = updaterService.updateRunInformation(run, stoppedPoliciesNamesForRuns, run.getJobId());
                sendNotificationMail(providerJobStatus.getRunStatus(), run);
            } catch (Exception e) {
                log.error("Error for run: " + run.getId() + " : " + e.getMessage(), e);
                emailNotificationService.sendEmailExceptionNotification("Error for run: " + run.getId() + " : " + e.getMessage(), e);
            }
        });
    }

    /**
     * Send notification mail if Run is completed successfully or with error
     * and if the Run type is discovery or full run
     */
    private void sendNotificationMail(RunStatus jobStatus, Run run) {
        // Do not send notification if there is another run with same run type still executing or the notification is already been sent
        if (RunStatus.isFinished(jobStatus) && !RunUtils.isStoppedByUser(run) && runDAO.shouldSendNotification(run.getExperimentId(), run.getRunType())) {
            boolean isSuccessful = jobStatus == RunStatus.Completed;
            emailNotificationService.sendTrainingCompletedEmail(run.getProject().getPathmindUserId(), run.getExperiment(), run.getProject(), isSuccessful);
            runDAO.markAsNotificationSent(run.getId());
        }
    }

    private boolean hasJobId(Run run) {
        if (run.getJobId() == null)
            log.error("Run {} marked as executing but no aws run id found for it.", run.getId());
        return run.getJobId() != null;
    }
}
