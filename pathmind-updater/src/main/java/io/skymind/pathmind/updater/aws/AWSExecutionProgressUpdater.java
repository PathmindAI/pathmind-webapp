package io.skymind.pathmind.updater.aws;

import io.skymind.pathmind.services.training.cloud.aws.AWSExecutionProvider;
import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.constants.RunType;
import io.skymind.pathmind.shared.data.PathmindUser;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.data.ProviderJobStatus;
import io.skymind.pathmind.shared.data.Run;
import io.skymind.pathmind.shared.data.RewardScore;
import io.skymind.pathmind.db.dao.ExecutionProviderMetaDataDAO;
import io.skymind.pathmind.db.dao.RunDAO;
import io.skymind.pathmind.db.dao.TrainingErrorDAO;
import io.skymind.pathmind.db.dao.UserDAO;
import io.skymind.pathmind.services.PolicyFileService;
import io.skymind.pathmind.services.notificationservice.EmailNotificationService;
import io.skymind.pathmind.updater.ExecutionProgressUpdater;
import io.skymind.pathmind.updater.ProgressInterpreter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.skymind.pathmind.db.dao.TrainingErrorDAO.UNKNOWN_ERROR_KEYWORD;
import static io.skymind.pathmind.db.dao.TrainingErrorDAO.KILLED_TRAINING_KEYWORD;

@Service
@Slf4j
public class AWSExecutionProgressUpdater implements ExecutionProgressUpdater {

    private final AWSExecutionProvider provider;
    private final ExecutionProviderMetaDataDAO executionProviderMetaDataDAO;
    private final RunDAO runDAO;
    private final PolicyFileService policyFileService;
    private final UserDAO userDAO;
    private final TrainingErrorDAO trainingErrorDAO;
    private EmailNotificationService emailNotificationService;

    public AWSExecutionProgressUpdater(AWSExecutionProvider provider, PolicyFileService policyFileService, RunDAO runDAO,
                                       UserDAO userDAO, ExecutionProviderMetaDataDAO executionProviderMetaDataDAO,
                                       EmailNotificationService emailNotificationService, TrainingErrorDAO trainingErrorDAO) {
        this.provider = provider;
        this.executionProviderMetaDataDAO = executionProviderMetaDataDAO;
        this.runDAO = runDAO;
        this.policyFileService = policyFileService;
        this.userDAO = userDAO;
        this.trainingErrorDAO = trainingErrorDAO;
        this.emailNotificationService = emailNotificationService;
    }

    @Override
    public void update() {
        // Getting all these values beforehand in single database calls rather than in loops of database calls.
        final List<Long> runIds = runDAO.getExecutingRuns();
        final Map<Long, List<String>> stoppedPoliciesNamesForRuns = runDAO.getStoppedPolicyNamesForRuns(runIds);
        final Map<Long, String> awsJobIds = executionProviderMetaDataDAO.getProviderRunJobIds(runIds);
        final List<Run> runsWithAwsJobs = getRunsWithAwsJobs(runIds, awsJobIds);

        runsWithAwsJobs.parallelStream().forEach(run -> {
            try {
                String jobHandle = awsJobIds.get(run.getId());
                ProviderJobStatus providerJobStatus = provider.status(jobHandle);

                final List<Policy> policies = getPoliciesFromProgressProvider(stoppedPoliciesNamesForRuns, run.getId(), jobHandle);

                setStoppedAtForFinishedPolicies(policies, jobHandle);
                setRunError(run, providerJobStatus);

                runDAO.updateRun(run, providerJobStatus, policies);

                // STEPH -> REFACTOR -> QUESTION -> Does this need to be transactional with runDAO.updateRun and put
                // into updateRun()? For now I left it here, it's no worse than what's in production today. I mainly kept it out
                // updateRun() because savePolicyFilesForCompletedRuns() calls another provider and as a result it doesn't belong
                // in the database layer. I would recommend breaking up savePolicyFilesForCompletedRuns() and pulling out the
                // service component so that if there is a policyFile (byte[]) then it's done before the updateRun()
                // method is called and we can just do a simple isPolicyFile != null check as to whether or not to
                // also update it in the database.
                savePolicyFilesAndCleanupForCompletedRuns(stoppedPoliciesNamesForRuns, run.getId(), jobHandle, providerJobStatus.getRunStatus());

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
        if (RunStatus.isFinished(jobStatus)) {
            // Do not send notification if there is another run with same run type still executing or the notification is already been sent
            if (runDAO.shouldSendNotification(run.getExperimentId(), run.getRunType())) {
                boolean isSuccessful = jobStatus == RunStatus.Completed;
                PathmindUser user = userDAO.findById(run.getProject().getPathmindUserId());
                emailNotificationService.sendTrainingCompletedEmail(user, run.getExperiment(), run.getProject(), isSuccessful);
                runDAO.markAsNotificationSent(run.getId());
            }
        }
    }

    private List<Run> getRunsWithAwsJobs(List<Long> runIds, Map<Long, String> awsJobIds) {
        final List<Long> runIdsWithRecaleJobs = runIds.stream()
                .filter(runId -> isInAwsRunJobIds(awsJobIds, runId))
                .collect(Collectors.toList());
        return runDAO.getRuns(runIdsWithRecaleJobs);
    }

    private boolean isInAwsRunJobIds(Map<Long, String> awsJobIds, Long runId) {
        if (awsJobIds.get(runId) == null)
            log.error("Run {} marked as executing but no aws run id found for it.", runId);
        return awsJobIds.get(runId) != null;
    }

    private void savePolicyFilesAndCleanupForCompletedRuns(Map<Long, List<String>> stoppedPoliciesNamesForRuns, Long runId, String jobHandle, RunStatus jobStatus) {
        if (jobStatus == RunStatus.Completed) {
            stoppedPoliciesNamesForRuns.getOrDefault(runId, Collections.emptyList()).stream().forEach(finishPolicyName -> {
                // todo make saving to enum or static final variable (currently defined in PolicyDAO).
                final byte[] policyFile = provider.policy(jobHandle, finishPolicyName);
                if (policyFile == null) {
                    log.warn("policy file for " + finishPolicyName + " is not ready");
                    return;
                }

                policyFileService.savePolicyFile(runId, finishPolicyName, policyFile);

                // save the last checkpoint
                Map.Entry<String, byte[]> entry = provider.snapshot(jobHandle, finishPolicyName);
                if (entry != null) {
                    final byte[] checkPointFile = entry.getValue();
                    policyFileService.saveCheckpointFile(runId, finishPolicyName, checkPointFile);
                    log.debug("checkpoint saved for " + finishPolicyName);

                    // save meta data for checkpoint
                    if (executionProviderMetaDataDAO.getCheckPointFileKey(finishPolicyName) == null) {
                        executionProviderMetaDataDAO.putCheckPointFileKey(finishPolicyName, entry.getKey());
                        log.debug("checkpoint metadata saved for " + finishPolicyName);
                    }
                }
            });
        }
    }

    private void setStoppedAtForFinishedPolicies(List<Policy> policies, String jobHandle) {
        Map<String, LocalDateTime> terminatedTrials = provider.getTerminatedTrials(jobHandle);

        policies.stream()
                .filter(policy -> policy.getStoppedAt() == null)
                .filter(policy -> terminatedTrials.containsKey(policy.getExternalId()))
                .forEach(policy -> policy.setStoppedAt(terminatedTrials.get(policy.getExternalId())));
    }

    private List<Policy> getPoliciesFromProgressProvider(Map<Long, List<String>> stoppedPoliciesNamesForRuns, Long runId, String jobHandle) {
        final Map<String, String> rawProgress = provider.progress(jobHandle);
        return rawProgress.entrySet().stream()
                .filter(e -> !stoppedPoliciesNamesForRuns.getOrDefault(runId, Collections.emptyList()).contains(e.getKey()))
                .map(e -> {
                    List<RewardScore> previousScores = runDAO.getScores(runId, e.getKey());
                    return ProgressInterpreter.interpret(e, previousScores);
                })
                .collect(Collectors.toList());
    }

    private void setRunError(Run run, ProviderJobStatus jobStatus) {
        final var status = jobStatus.getRunStatus();
        if (status == RunStatus.Error && !CollectionUtils.isEmpty(jobStatus.getDescription())) {
            // TODO (KW): 05.02.2020 gets only first error, refactor if multiple errors scenario is possible
            final var errorMessage = jobStatus.getDescription().get(0);
            final var allErrorsKeywords = trainingErrorDAO.getAllErrorsKeywords();
            final var knownErrorMessage = allErrorsKeywords.stream()
                    .filter(errorMessage::contains)
                    .findAny()
                    .orElseGet(() -> {
                        log.warn("Unrecognized error: {}", errorMessage);
                        return UNKNOWN_ERROR_KEYWORD;
                    });

            final var foundError = trainingErrorDAO.getErrorByKeyword(knownErrorMessage);
            foundError.ifPresent(
                    e -> run.setTrainingErrorId(e.getId())
            );
        } else if (status == RunStatus.Killed) {
        	trainingErrorDAO.getErrorByKeyword(KILLED_TRAINING_KEYWORD).ifPresent(error -> {
        		run.setTrainingErrorId(error.getId());
        	});
        }
    }
}
