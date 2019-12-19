package io.skymind.pathmind.services.training.cloud.rescale;

import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.Run;
import io.skymind.pathmind.data.policy.RewardScore;
import io.skymind.pathmind.db.dao.ExecutionProviderMetaDataDAO;
import io.skymind.pathmind.db.dao.RunDAO;
import io.skymind.pathmind.services.notificationservice.EmailNotificationService;
import io.skymind.pathmind.services.training.ExecutionProgressUpdater;
import io.skymind.pathmind.services.training.constant.TrainingFile;
import io.skymind.pathmind.services.training.progress.ProgressInterpreter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RescaleExecutionProgressUpdater implements ExecutionProgressUpdater {
    private final RescaleExecutionProvider provider;
    private final ExecutionProviderMetaDataDAO executionProviderMetaDataDAO;
    private final RunDAO runDAO;
    private EmailNotificationService emailNotificationService;

    public RescaleExecutionProgressUpdater(RescaleExecutionProvider provider, ExecutionProviderMetaDataDAO executionProviderMetaDataDAO, RunDAO runDAO, EmailNotificationService emailNotificationService){
        this.provider = provider;
        this.executionProviderMetaDataDAO = executionProviderMetaDataDAO;
        this.runDAO = runDAO;
        this.emailNotificationService = emailNotificationService;
    }

    @Override
    public void update()
    {
        // Getting all these values beforehand in single database calls rather than in loops of database calls.
        final List<Long> runIds = runDAO.getExecutingRuns();
        final Map<Long, List<String>> stoppedPoliciesNamesForRuns = runDAO.getStoppedPolicyNamesForRuns(runIds);
        final Map<Long, String> rescaleJobIds = executionProviderMetaDataDAO.getRescaleRunJobIds(runIds);
        final List<Run> runsWithRescaleJobs = getRunsWithRescaleJobs(runIds, rescaleJobIds);

        // STEPH -> REFACTOR -> This is NOT transactional. That being said the only thing that may need to be is savePolicyFilesForCompletedRuns()
        // since runDAO.updateRun() is now transactional. In any case it's no worse than today and as a result I'm pushing this to another ticket.
        runsWithRescaleJobs.parallelStream().forEach(run ->
        {
            try {
                final String rescaleJobId = rescaleJobIds.get(run.getId());
                final RunStatus jobStatus = provider.status(rescaleJobId);

                final List<Policy> policies = getPoliciesFromProgressProvider(stoppedPoliciesNamesForRuns, run.getId(), rescaleJobId, jobStatus);
                final List<String> finishedPolicyNamesFromRescale = getTerminatedPolicesFromProvider(rescaleJobId);

                setStoppedAtForFinishedPolicies(policies, finishedPolicyNamesFromRescale);

                runDAO.updateRun(run, jobStatus, policies);

                // STEPH -> REFACTOR -> QUESTION -> Does this need to be transactional with runDAO.updateRun and put
                // into updateRun()? For now I left it here, it's no worse than what's in production today. I mainly kept it out
                // updateRun() because savePolicyFilesForCompletedRuns() calls another provider and as a result it doesn't belong
                // in the database layer. I would recommend breaking up savePolicyFilesForCompletedRuns() and pulling out the
                // service component so that if there is a policyFile (byte[]) then it's done before the updateRun()
                // method is called and we can just do a simple isPolicyFile != null check as to whether or not to
                // also update it in the database.
                savePolicyFilesAndCleanupForCompletedRuns(stoppedPoliciesNamesForRuns, run.getId(), rescaleJobId, jobStatus);
            } catch (Exception e) {
                log.error("Error for run: " + run.getId() + " : " + e.getMessage(), e);
                emailNotificationService.sendEmailExceptionNotification("Error for run: " + run.getId() + " : " + e.getMessage(), e);
            }
        });
    }

    private List<Run> getRunsWithRescaleJobs(List<Long> runIds, Map<Long, String> rescaleJobIds) {
        final List<Long> runIdsWithRecaleJobs = runIds.stream()
            .filter(runId -> isInRescaleRunJobIds(rescaleJobIds, runId))
            .collect(Collectors.toList());
        return runDAO.getRuns(runIdsWithRecaleJobs);
    }

    private boolean isInRescaleRunJobIds(Map<Long, String> rescaleJobIds, Long runId) {
        if(rescaleJobIds.get(runId) == null)
            log.error("Run {} marked as executing but no rescale run id found for it.", runId);
        return rescaleJobIds.get(runId) != null;
    }

    private void savePolicyFilesAndCleanupForCompletedRuns(Map<Long, List<String>> stoppedPoliciesNamesForRuns, Long runId, String rescaleJobId, RunStatus jobStatus) {
        if(jobStatus == RunStatus.Completed) {
            stoppedPoliciesNamesForRuns.getOrDefault(runId, Collections.emptyList()).stream().forEach(finishPolicyName -> {
                // todo make saving to enum or static final variable (currently defined in PolicyDAO).
                final byte[] policyFile = provider.policy(rescaleJobId, finishPolicyName);
                runDAO.savePolicyFile(runId, finishPolicyName, policyFile);

                // save the last checkpoint
                Map.Entry<String, byte[]> entry = provider.snapshot(rescaleJobId, finishPolicyName);
                if (entry != null) {
                    final byte[] checkPointFile = entry.getValue();
                    runDAO.saveCheckpointFile(runId, finishPolicyName, checkPointFile);
                }

                // save meta data for checkpoint
                if (executionProviderMetaDataDAO.getCheckPointFileKey(finishPolicyName) == null) {
                    executionProviderMetaDataDAO.putCheckPointFileKey(finishPolicyName, entry.getKey());
                }
            });
            // STEPH -> REFACTOR -> Combined so that this is transactional. For now I just left it as is for the merge
            // conflict just to process the PR and will clean it up as part of another ticket.
            runDAO.cleanUpTemporary(runId);
        }
    }


    private void setStoppedAtForFinishedPolicies(List<Policy> policies, List<String> finishedPolicyNamesFromRescale) {
        policies.stream()
                .filter(policy -> policy.getStoppedAt() == null)
                .filter(policy -> finishedPolicyNamesFromRescale.stream().anyMatch(
                        finishedPolicy -> finishedPolicy.contains(policy.getExternalId())))
                .forEach(policy -> policy.setStoppedAt(LocalDateTime.now()));
    }

    private List<Policy> getPoliciesFromProgressProvider(Map<Long, List<String>> stoppedPoliciesNamesForRuns, Long runId, String rescaleJobId, RunStatus jobStatus) {
        final Map<String, String> rawProgress = provider.progress(rescaleJobId, jobStatus);
        return rawProgress.entrySet().stream()
                .filter(e -> !stoppedPoliciesNamesForRuns.getOrDefault(runId, Collections.emptyList()).contains(e.getKey()))
                .map(e -> {
                    List<RewardScore> previousScores = runDAO.getScores(runId, e.getKey());
                    return ProgressInterpreter.interpret(e, previousScores);
                })
                .collect(Collectors.toList());
    }

    public List<String> getTerminatedPolicesFromProvider(String rescaleJobId) {
        String content = provider.getFileAnytime(rescaleJobId, TrainingFile.RAY_TRIAL_COMPLETE);
        return content == null ? Collections.emptyList() : Arrays.asList(content.split("\n"));
    }
}
