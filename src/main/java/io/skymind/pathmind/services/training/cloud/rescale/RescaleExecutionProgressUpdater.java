package io.skymind.pathmind.services.training.cloud.rescale;

import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.policy.RewardScore;
import io.skymind.pathmind.services.training.ExecutionProgressUpdater;
import io.skymind.pathmind.services.training.constant.TrainingFile;
import io.skymind.pathmind.services.training.db.integration.RunUpdateService;
import io.skymind.pathmind.services.training.progress.ProgressInterpreter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RescaleExecutionProgressUpdater implements ExecutionProgressUpdater {
    private static Logger log = LoggerFactory.getLogger(RescaleExecutionProgressUpdater.class);
    private final RescaleExecutionProvider provider;
    private final RescaleMetaDataService metaDataService;
    private final RunUpdateService updateService;

    public RescaleExecutionProgressUpdater(RescaleExecutionProvider provider, RescaleMetaDataService metaDataService, RunUpdateService updateService){
        this.provider = provider;
        this.metaDataService = metaDataService;
        this.updateService = updateService;
    }

    @Override
    public void update()
    {
        final List<Long> runIds = updateService.getExecutingRuns();
        final List<String> finishedPolicyNamesFromDB = updateService.getStoppedPolicies(runIds)
                .stream()
                .map(p -> p.getName())
                .collect(Collectors.toList());

        runIds.parallelStream().forEach(runId ->
        {
            final String rescaleJobId = metaDataService.get(metaDataService.runIdKey(runId), String.class);

            if(rescaleJobId == null) {
                log.error("Run {} marked as executing but no rescale run id found for it.", runId);
                return;
            }

            final RunStatus jobStatus = provider.status(rescaleJobId);
            final Map<String, String> rawProgress = provider.progress(rescaleJobId, jobStatus);

            final List<Policy> policies = rawProgress.entrySet().stream()
                    .filter(e -> !finishedPolicyNamesFromDB.contains(e.getKey()))
                    .map(e -> {
                        List<RewardScore> previousScores = updateService.getScores(runId, e.getKey());
                        return ProgressInterpreter.interpret(e, previousScores);
                    })
                    .collect(Collectors.toList());

            final List<String> finishedPolicyNamesFromRescale = getTerminatedPolices(rescaleJobId);

            policies.stream()
                    .filter(policy -> policy.getStoppedAt() == null)
                    .filter(policy -> finishedPolicyNamesFromRescale.stream().anyMatch(
                            finishedPolicy -> finishedPolicy.contains(policy.getExternalId())))
                    .forEach(policy -> policy.setStoppedAt(LocalDateTime.now()));

            updateService.updateRun(runId, jobStatus, policies);

            if(jobStatus == RunStatus.Completed){
                for (String finishPolicyName : finishedPolicyNamesFromDB) {
                    // save temporary file to avoid multiple download tries
                    updateService.savePolicyFile(runId, finishPolicyName, TrainingFile.TEMPORARY_POLICY.getBytes());

                    // save a policy file
                    final byte[] policy = provider.policy(rescaleJobId, finishPolicyName);
                    updateService.savePolicyFile(runId, finishPolicyName, policy);

                    // save the last checkpoint
                    Map.Entry<String, byte[]> entry = provider.snapshot(rescaleJobId, finishPolicyName);
                    final byte[] checkPointFile = entry.getValue();
                    updateService.saveCheckpointFile(runId, finishPolicyName, checkPointFile);

                    metaDataService.put(metaDataService.checkPointFileKey(finishPolicyName), entry.getKey());
                }
                updateService.cleanUpTemporary(runId);
            }
        });
    }

    public List<String> getTerminatedPolices(String rescaleJobId) {
        String content = provider.getFileAnytime(rescaleJobId, TrainingFile.RAY_TRIAL_COMPLETE);
        if (content == null) {
            return Collections.emptyList();
        }

        return Arrays.asList(content.split("\n"));
    }
}
