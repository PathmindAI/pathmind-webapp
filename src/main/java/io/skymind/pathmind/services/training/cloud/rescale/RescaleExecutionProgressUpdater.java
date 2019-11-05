package io.skymind.pathmind.services.training.cloud.rescale;

import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.services.training.ExecutionProgressUpdater;
import io.skymind.pathmind.services.training.db.integration.RunUpdateService;
import io.skymind.pathmind.services.training.progress.Progress;
import io.skymind.pathmind.services.training.progress.ProgressInterpreter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

            final RunStatus status = provider.status(rescaleJobId);
            final Map<String, String> rawProgress = provider.progress(rescaleJobId);

            final List<Progress> progresses = rawProgress.entrySet().stream()
                    .filter(e -> !finishedPolicyNamesFromDB.contains(e.getKey()))
                    .map(ProgressInterpreter::interpret)
                    .collect(Collectors.toList());

            final List<String> finishedPolicyNamesFromRescale = getTerminatedPolices(rescaleJobId);

            for (Progress progress : progresses) {
                if (progress.getStoppedAt() == null) {
                    for (String finishedPolicy : finishedPolicyNamesFromRescale) {
                        if (progress.getId().contains(finishedPolicy)) {
                            progress.setStoppedAt(LocalDateTime.now());
                        }
                    }
                }
            }

            updateService.updateRun(runId, status, progresses);

            if(status == RunStatus.Completed){
                for (String finishPolicyName : finishedPolicyNamesFromDB) {
                    final byte[] policy = provider.policy(rescaleJobId, finishPolicyName);
                    updateService.savePolicyFile(runId, finishPolicyName, policy);
                }
            }
        });
    }

    public List<String> getTerminatedPolices(String rescaleJobId) {

        List<String> terminatedTrial = new ArrayList<>();
        String content = provider.consoleAnytime(rescaleJobId);
        if (content == null) {
            return terminatedTrial;
        }

        List<String> lines = Arrays.asList(content.split("\n"));

        int lastIdx = -1;
        for (int i = lines.size() - 1; i >= 0; i--) {
            if (lines.get(i).contains("TERMINATED trials:")) {
                lastIdx = i;
                break;
            }
        }
        if (lastIdx == -1) {
            log.info("No Matching terminated trials for " + rescaleJobId);
            return terminatedTrial;
        }

        Pattern logPattern = Pattern.compile("\\[[\\w\\d:-]+\\]:.*:\\tTERMINATED");
        Pattern policyPattern = Pattern.compile("\\w+_\\w+=.+:");
        //[2019-10-09T22:33:25Z]:  - PPO_PathmindEnvironment_0_gamma=0.9,lr=0.001,sgd_minibatch_size=64:	TERMINATED, [8 CPUs, 0 GPUs], [pid=3339], 6661 s, 100 iter, 400000 ts, -167 rew

        for (int i = lastIdx + 1; i < lines.size(); i++) {
            String line = lines.get(i);
            if (!logPattern.matcher(line).find()) {
                break;
            }

            Matcher matcher = policyPattern.matcher(line);
            if (matcher.find()) {
                terminatedTrial.add(matcher.group().replace(":", ""));
            }
        }

        return terminatedTrial;
    }
}
