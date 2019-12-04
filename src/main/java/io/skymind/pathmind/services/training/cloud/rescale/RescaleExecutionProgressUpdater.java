package io.skymind.pathmind.services.training.cloud.rescale;

import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.data.Policy;
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
        final Map<Long, List<String>> stoppedPoliciesNamesForRuns = updateService.getStoppedPolicyNamesForRuns(runIds);

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
                    .filter(e -> !stoppedPoliciesNamesForRuns.getOrDefault(runId, Collections.emptyList()).contains(e.getKey()))
                    .map(ProgressInterpreter::interpret)
                    .collect(Collectors.toList());

            final List<String> finishedPolicyNamesFromRescale = getTerminatedPolices(rescaleJobId);

            policies.stream()
                    .filter(policy -> policy.getStoppedAt() == null)
                    .filter(policy -> finishedPolicyNamesFromRescale.stream().anyMatch(
                            finishedPolicy -> finishedPolicy.contains(policy.getExternalId())))
                    .forEach(policy -> policy.setStoppedAt(LocalDateTime.now()));

            updateService.updateRun(runId, jobStatus, policies);

            if(jobStatus == RunStatus.Completed){
                stoppedPoliciesNamesForRuns.getOrDefault(runId, Collections.emptyList()).stream().forEach(finishPolicyName -> {
                    // todo make saving to enum or static final variable (currently defined in PolicyDAO).
                    updateService.savePolicyFile(runId, finishPolicyName, TrainingFile.TEMPORARY_POLICY.getBytes());
                    final byte[] policy = provider.policy(rescaleJobId, finishPolicyName);
                    updateService.savePolicyFile(runId, finishPolicyName, policy);
                });
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
