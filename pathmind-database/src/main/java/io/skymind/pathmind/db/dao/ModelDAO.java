package io.skymind.pathmind.db.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.shared.data.SimulationParameter;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class ModelDAO {
    private final DSLContext ctx;

    public ModelDAO(DSLContext ctx) {
        this.ctx = ctx;
    }

    public List<Model> getModelsForProject(long projectId) {
        return ModelRepository.getModelsForProject(ctx, projectId);
    }

    public void archive(long modelId, boolean isArchive) {
        ModelRepository.update(ctx, new ModelUpdateRequest(modelId).isArchived(isArchive));
    }

    public Optional<Model> getModel(long modelId) {
        return Optional.ofNullable(ModelRepository.getModel(ctx, modelId));
    }

    public Optional<Model> getPrevModelForProject(long projectId, long currentModelId) {
        return Optional.ofNullable(ModelRepository.getLastModelForProject(ctx, projectId, currentModelId));
    }

    public void addDraftModelToProject(Model model, long projectId, String userNotes) {
        ctx.transaction(configuration ->
        {
            DSLContext transactionCtx = DSL.using(configuration);
            LocalDateTime dateCreated = LocalDateTime.now();
            String modelName = Integer.toString(ModelRepository.getModelCount(transactionCtx, projectId) + 1);
            long modelId = ModelRepository.insertModel(transactionCtx, model, modelName, userNotes, dateCreated, projectId);
            model.setId(modelId);
        });
    }

    public int getModelCountForProject(long projectId) {
        return ModelRepository.getModelCount(ctx, projectId);
    }

    public void updateUserNotes(long modelId, String userNotes) {
        ModelRepository.update(ctx, new ModelUpdateRequest(modelId).userNotes(userNotes));
    }

    public void renameModel(long modelId, String newName) {
        ModelRepository.update(ctx, new ModelUpdateRequest(modelId).name(newName));
    }

    public void assignProject(long modelId, long projectId) {
        ModelRepository.update(ctx, new ModelUpdateRequest(modelId).projectId(projectId));
    }

    public Experiment resumeModelCreation(Model model, String modelNotes) {
        return ctx.transactionResult(configuration ->
        {
            DSLContext transactionCtx = DSL.using(configuration);
            model.setDraft(false);
            ModelRepository.update(transactionCtx, new ModelUpdateRequest(model.getId()).isDraft( false).userNotes(modelNotes));
            Experiment experiment = ExperimentRepository.createNewExperiment(transactionCtx, model.getId(), model.isHasGoals());
            experiment.setSelectedObservations(ObservationRepository.getObservationsForModel(transactionCtx, model.getId()));
            ObservationRepository.insertExperimentObservations(transactionCtx, experiment.getId(), experiment.getSelectedObservations());
            List<SimulationParameter> simulationParameters = SimulationParameterRepository.getSimulationParametersForModel(ctx, model.getId());
            simulationParameters.forEach(p -> p.setExperimentId(experiment.getId()));
            SimulationParameterRepository.insertOrUpdateSimulationParameter(ctx, simulationParameters);
            experiment.setSimulationParameters(simulationParameters);
            return experiment;
        });
    }

    public Optional<Model> getModelIfAllowed(long modelId, long userId) {
        return ModelRepository.getModelIfAllowed(ctx, modelId, userId);
    }

    public long getUserForModel(long modelId) {
        return ModelRepository.getUserIdForModel(ctx, modelId);
    }
}
