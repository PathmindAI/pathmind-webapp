package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.shared.data.Model;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    	ModelRepository.archive(ctx, modelId, isArchive);
	}

	public Optional<Model> getModel(long modelId) {
    	return Optional.ofNullable(ModelRepository.getModel(ctx, modelId));
	}

	public void addDraftModelToProject(Model model, long projectId, String userNotes)
	{
		ctx.transaction(configuration ->
		{
			DSLContext transactionCtx = DSL.using(configuration);
			LocalDateTime dateCreated = LocalDateTime.now();
			String modelName = Integer.toString(ModelRepository.getModelCount(transactionCtx, projectId) + 1);
			long modelId = ModelRepository.insertModel(transactionCtx, model, modelName, userNotes, dateCreated, projectId);
			model.setId(modelId);
		});
	}

	public void updateUserNotes(long modelId, String userNotes) {
		ModelRepository.updateUserNotes(ctx, modelId, userNotes);
	}

	public long resumeModelCreation(Model model, String modelNotes) {
		return ctx.transactionResult(configuration ->
		{
			DSLContext transactionCtx = DSL.using(configuration);
			model.setDraft(false);
			ModelRepository.updateModel(transactionCtx, model.getId(), false, modelNotes);
			return ExperimentRepository.createNewExperiment(transactionCtx, model.getId());
		});
	}

	public Optional<Model> getModelIfAllowed(long modelId, long userId) {
		return ModelRepository.getModelIfAllowed(ctx, modelId, userId);
	}

    public long getUserForModel(long modelId) {
        return ModelRepository.getUserIdForModel(ctx, modelId);
    }
}
