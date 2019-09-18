package io.skymind.pathmind.services.training.cloud.rescale;

import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.services.training.ExecutionProgressUpdater;
import io.skymind.pathmind.services.training.RunUpdateService;
import io.skymind.pathmind.services.training.progress.Progress;
import io.skymind.pathmind.services.training.progress.ProgressInterpreter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RescaleExecutionProgressUpdater implements ExecutionProgressUpdater {
    private final RescaleExecutionProvider provider;
    private final RescaleMetaDataService metaDataService;
    private final RunUpdateService updateService;

    public RescaleExecutionProgressUpdater(RescaleExecutionProvider provider, RescaleMetaDataService metaDataService, RunUpdateService updateService){
        this.provider = provider;
        this.metaDataService = metaDataService;
        this.updateService = updateService;
    }

    @Override
    public void update() {
        final List<Long> runIds = updateService.getExecutingRuns();

        runIds.parallelStream().forEach(runId -> {
            final String rescaleJobId = metaDataService.get(metaDataService.runIdKey(runId), String.class);

            final RunStatus status = provider.status(rescaleJobId);
            final Map<String, String> rawProgress = provider.progress(rescaleJobId);
            final List<Progress> progresses = rawProgress.entrySet().stream().map(ProgressInterpreter::interpret).collect(Collectors.toList());

            updateService.updateRun(runId, status, progresses);
        });
    }
}
