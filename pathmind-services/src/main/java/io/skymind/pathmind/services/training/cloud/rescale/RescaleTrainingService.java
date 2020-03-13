package io.skymind.pathmind.services.training.cloud.rescale;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.data.Run;
import io.skymind.pathmind.db.dao.ExecutionProviderMetaDataDAO;
import io.skymind.pathmind.db.dao.PolicyDAO;
import io.skymind.pathmind.db.dao.RunDAO;
import io.skymind.pathmind.services.ModelService;
import io.skymind.pathmind.services.TrainingService;
import io.skymind.pathmind.shared.services.training.ExecutionProvider;
import io.skymind.pathmind.shared.services.training.JobSpec;
import lombok.extern.slf4j.Slf4j;

import static io.skymind.pathmind.shared.constants.RunType.DiscoveryRun;

//@Service
@Slf4j
public class RescaleTrainingService extends TrainingService {
    public RescaleTrainingService(ExecutionProvider executionProvider, RunDAO runDAO, ModelService modelService,
                                  PolicyDAO policyDAO, ExecutionProviderMetaDataDAO executionProviderMetaDataDAO) {
        super(false, executionProvider, runDAO, modelService, policyDAO, executionProviderMetaDataDAO);
    }

    protected void startRun(Experiment exp, int iterations, int maxTimeInSec, int numSamples, Policy basePolicy) {
        final Run run = runDAO.createRun(exp, DiscoveryRun);
        // Get model from the database, as the one we can get from the experiment doesn't have all fields
        final Model model = modelService.getModel(exp.getModelId()).get();

        // Get model file id, either uploading it if necessary, or just getting it from the metadata database table
        String modelFileId = executionProviderMetaDataDAO.getModelFileKey(exp.getModelId());
        if (modelFileId == null) {
            modelFileId = executionProvider.uploadModel(modelService.getModelFile(model.getId()));
            executionProviderMetaDataDAO.putModelFileKey(exp.getModelId(), modelFileId);
        }

        final JobSpec spec = new JobSpec(
                exp.getProject().getPathmindUserId(),
                model.getId(),
                exp.getId(),
                run.getId(),
                modelFileId,
                "", // not collected via UI yet
                "",    // not collected via UI yet
                exp.getRewardFunction(),
                model.getNumberOfPossibleActions(),
                model.getNumberOfObservations(),
                iterations,
                executionEnvironment,
                DiscoveryRun,
                maxTimeInSec,
                numSamples,
                false,
                false,
                50
        );

        if (basePolicy != null) {
            String checkpointFileId = executionProviderMetaDataDAO.getCheckPointFileKey(basePolicy.getExternalId());
            if (checkpointFileId == null) {
                log.error("policyDAO.getSnapshotFile needs to be changed with policyService to access getSnapshotFile ");
//                checkpointFileId = executionProvider.uploadCheckpoint(policyDAO.getSnapshotFile(basePolicy.getId()));
//                executionProviderMetaDataDAO.putCheckPointFileKey(basePolicy.getExternalId(), checkpointFileId);
            }

            spec.setCheckpointFileId(checkpointFileId);
        }

        // IMPORTANT -> There are multiple database calls within executionProvider.execute.
        final String executionId = executionProvider.execute(spec);
        executionProviderMetaDataDAO.putProviderRunJobId(spec.getRunId(),executionId);

        runDAO.markAsStarting(run.getId());
        log.info("Started {} training job with id {}", DiscoveryRun, executionId);
    }
}
