package io.skymind.pathmind.updater.aws;

import io.skymind.pathmind.db.dao.ExecutionProviderMetaDataDAO;
import io.skymind.pathmind.db.dao.RunDAO;
import io.skymind.pathmind.db.dao.TrainingErrorDAO;
import io.skymind.pathmind.services.PolicyFileService;
import io.skymind.pathmind.services.notificationservice.EmailNotificationService;
import io.skymind.pathmind.services.training.cloud.aws.AWSExecutionProvider;
import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.constants.RunType;
import io.skymind.pathmind.shared.data.*;
import io.skymind.pathmind.shared.data.rllib.CheckPoint;
import io.skymind.pathmind.shared.data.rllib.ExperimentState;
import io.skymind.pathmind.shared.utils.RunUtils;
import io.skymind.pathmind.updater.ExecutionProgressUpdater;
import io.skymind.pathmind.updater.ProgressInterpreter;
import io.skymind.pathmind.services.training.cloud.aws.api.dto.UpdateEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static io.skymind.pathmind.services.training.cloud.aws.api.dto.UpdateEvent.TYPE_POLICY;
import static io.skymind.pathmind.services.training.cloud.aws.api.dto.UpdateEvent.TYPE_RUN;
import static io.skymind.pathmind.shared.services.training.constant.ErrorConstants.KILLED_TRAINING_KEYWORD;
import static io.skymind.pathmind.shared.services.training.constant.ErrorConstants.UNKNOWN_ERROR_KEYWORD;

@Service
@Slf4j
public class AWSExecutionProgressUpdater implements ExecutionProgressUpdater {

    private final AWSExecutionProvider provider;
    private final AWSApiClient awsApiClient;
    private final ExecutionProviderMetaDataDAO executionProviderMetaDataDAO;
    private final RunDAO runDAO;
    private final PolicyFileService policyFileService;
    private final TrainingErrorDAO trainingErrorDAO;
    private EmailNotificationService emailNotificationService;

    public AWSExecutionProgressUpdater(AWSExecutionProvider provider, AWSApiClient awsApiClient,
                                       PolicyFileService policyFileService,
                                       RunDAO runDAO, ExecutionProviderMetaDataDAO executionProviderMetaDataDAO,
                                       EmailNotificationService emailNotificationService, TrainingErrorDAO trainingErrorDAO) {
        this.provider = provider;
        this.awsApiClient = awsApiClient;
        this.executionProviderMetaDataDAO = executionProviderMetaDataDAO;
        this.runDAO = runDAO;
        this.policyFileService = policyFileService;
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
                ExperimentState experimentState = providerJobStatus.getExperimentState();

                final List<Policy> policies = getPoliciesFromProgressProvider(stoppedPoliciesNamesForRuns, run.getId(), jobHandle, experimentState);

                setStoppedAtForFinishedPolicies(policies, experimentState);
                setRunError(run, providerJobStatus);

                runDAO.updateRun(run, providerJobStatus, policies);
                fireEventUpdates(run, policies);

                // STEPH -> REFACTOR -> QUESTION -> Does this need to be transactional with runDAO.updateRun and put
                // into updateRun()? For now I left it here, it's no worse than what's in production today. I mainly kept it out
                // updateRun() because savePolicyFilesForCompletedRuns() calls another provider and as a result it doesn't belong
                // in the database layer. I would recommend breaking up savePolicyFilesForCompletedRuns() and pulling out the
                // service component so that if there is a policyFile (byte[]) then it's done before the updateRun()
                // method is called and we can just do a simple isPolicyFile != null check as to whether or not to
                // also update it in the database.
                savePolicyFilesAndCleanupForCompletedRuns(stoppedPoliciesNamesForRuns, run.getId(), jobHandle, providerJobStatus.getRunStatus(), experimentState);

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

    private void savePolicyFilesAndCleanupForCompletedRuns(Map<Long, List<String>> stoppedPoliciesNamesForRuns,
                                                           Long runId, String jobHandle, RunStatus jobStatus, ExperimentState experimentState) {
        if (jobStatus == RunStatus.Completed) {
            List<String> unfinishedPolicyIds = runDAO.unfinishedPolicyIds(runId);
            stoppedPoliciesNamesForRuns.getOrDefault(runId, Collections.emptyList()).stream()
                    .filter(id -> unfinishedPolicyIds.contains(id))
                    .forEach(finishPolicyName -> {
                // todo make saving to enum or static final variable (currently defined in PolicyDAO).
                final byte[] policyFile = provider.policy(jobHandle, finishPolicyName);
                if (policyFile == null) {
                    log.warn("policy file for " + finishPolicyName + " is not ready");
                    return;
                }

                Policy policy = policyFileService.savePolicyFile(runId, finishPolicyName, policyFile);
                fireEventUpdates(null, Collections.singletonList(policy));

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

            List<String> validExternalIds = experimentState.getCheckpoints().stream()
                    .map(CheckPoint::getId)
                    .collect(Collectors.toList());

            runDAO.cleanUpInvalidPolicies(runId, validExternalIds);
        }
    }

    private void setStoppedAtForFinishedPolicies(List<Policy> policies, ExperimentState experimentState) {
        Map<String, LocalDateTime> terminatedTrials = provider.getTerminatedTrials(experimentState);

        policies.stream()
                .filter(policy -> policy.getStoppedAt() == null)
                .filter(policy -> terminatedTrials.containsKey(policy.getExternalId()))
                .forEach(policy -> policy.setStoppedAt(terminatedTrials.get(policy.getExternalId())));
    }

    private List<Policy> getPoliciesFromProgressProvider(Map<Long, List<String>> stoppedPoliciesNamesForRuns, Long runId, String jobHandle, ExperimentState experimentState) {
        if (experimentState == null) {
            return Collections.emptyList();
        }

        List<String> validExternalIds = experimentState.getCheckpoints().stream()
                .map(CheckPoint::getId)
                .filter(id -> !stoppedPoliciesNamesForRuns.getOrDefault(runId, Collections.emptyList()).contains(id))
                .collect(Collectors.toList());

        final Map<String, String> rawProgress = provider.progress(jobHandle, validExternalIds);

        return rawProgress.entrySet().stream()
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
        } else if (status == RunStatus.Killed && run.getStatusEnum() != RunStatus.Stopping) {
        	// Stopping status is set, when user wants to stop training. So, don't assign an error in this case
        	trainingErrorDAO.getErrorByKeyword(KILLED_TRAINING_KEYWORD).ifPresent(error -> {
        		run.setTrainingErrorId(error.getId());
        	});
        }
    }

    private void fireEventUpdates(Run run, List<Policy> policies) {
        for (Policy policy : CollectionUtils.emptyIfNull(policies)) {
            serializeAndFireEvent(policy, TYPE_POLICY);
        }
        serializeAndFireEvent(run, TYPE_RUN);
    }

    private <T extends Data> void serializeAndFireEvent(T data, String type) {
        if (data == null) {
            return;
        }
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream out = new ObjectOutputStream(bos)) {
            out.writeObject(data);
            out.flush();
            byte[] bytes = bos.toByteArray();
            fireEventUpdate(type, data.getId(), bytes);
        } catch (IOException e) {
            log.error("Failed to file event: {} {}", type, data.getId(), e);
        }
    }

    private void fireEventUpdate(String type, Long id, byte[] cargo) {
        try {
            final UpdateEvent event = new UpdateEvent(id, type, cargo.length);
            awsApiClient.sendUpdaterMessage(event, cargo);
            log.debug("fired update event [{}]", event);
        } catch (Exception e) {
            log.error("Failed to submit update request to SQS", e);
        }
    }
}
