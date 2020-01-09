package io.skymind.pathmind.services.training.cloud.rescale;

import io.skymind.pathmind.constants.RunType;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Model;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.Run;
import io.skymind.pathmind.db.dao.ExecutionProviderMetaDataDAO;
import io.skymind.pathmind.db.dao.ModelDAO;
import io.skymind.pathmind.db.dao.PolicyDAO;
import io.skymind.pathmind.db.dao.RunDAO;
import io.skymind.pathmind.services.TrainingService;
import io.skymind.pathmind.services.training.ExecutionProvider;
import io.skymind.pathmind.services.training.JobSpec;
import lombok.extern.slf4j.Slf4j;
import org.jooq.JSONB;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RescaleTrainingService extends TrainingService {
    public RescaleTrainingService(ExecutionProvider executionProvider, RunDAO runDAO, ModelDAO modelDAO, PolicyDAO policyDAO, ExecutionProviderMetaDataDAO executionProviderMetaDataDAO) {
        super(executionProvider, runDAO, modelDAO, policyDAO, executionProviderMetaDataDAO);
    }

    protected void startRun(RunType runType, Experiment exp, int iterations, List<Double> learningRates, List<Double> gammas, List<Integer> batchSizes, int maxTimeInSec, Policy basePolicy) {
        final Run run = runDAO.createRun(exp, runType);
        // Get model from the database, as the one we can get from the experiment doesn't have all fields
        final Model model = modelDAO.getModel(exp.getModelId());

        // Get model file id, either uploading it if necessary, or just getting it from the metadata database table
        String modelFileId = executionProviderMetaDataDAO.getModelFileKey(exp.getModelId());
        if (modelFileId == null) {
            modelFileId = executionProvider.uploadModel(modelDAO.getModelFile(model.getId()));
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
                runType,
                learningRates,
                gammas,
                batchSizes,
                maxTimeInSec
        );

        JSONB progress = null;
        if (basePolicy != null) {
            progress = policyDAO.getProgress(basePolicy.getId());

            String checkpointFileId = executionProviderMetaDataDAO.getCheckPointFileKey(basePolicy.getExternalId());
            if (checkpointFileId == null) {
                checkpointFileId = executionProvider.uploadCheckpoint(policyDAO.getSnapshotFile(basePolicy.getId()));
                executionProviderMetaDataDAO.putCheckPointFileKey(basePolicy.getExternalId(), checkpointFileId);
            }

            spec.setCheckpointFileId(checkpointFileId);
        }

        // IMPORTANT -> There are multiple database calls within executionProvider.execute.
        final String executionId = executionProvider.execute(spec);
        executionProviderMetaDataDAO.putRescaleRunJobId(spec.getRunId(),executionId);

        runDAO.markAsStarting(run.getId());
        log.info("Started " + runType + " training job with id {}", executionId);

        policyDAO.insertPolicy(generateTempPolicy(spec, run, progress));
    }
}
