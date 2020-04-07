package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.shared.data.Model;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class ModelDAO
{
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

	@Transactional
	public long addModelToProject(Model model, long projectId, String userNotes)
	{
		return ctx.transactionResult(configuration ->
		{
			DSLContext transactionCtx = DSL.using(configuration);
			LocalDateTime dateCreated = LocalDateTime.now();
			String modelName = Integer.toString(ModelRepository.getModelCount(transactionCtx, projectId) + 1);
			long modelId = ModelRepository.insertModel(transactionCtx, model, modelName, userNotes, dateCreated, projectId);
			model.setId(modelId);
			return ExperimentRepository.insertExperiment(transactionCtx, modelId, dateCreated);
		});
	}

	@Transactional
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

	public void updateDraftModel(Model model, String modelNotes) {
		ModelRepository.updateUserNotes(ctx, model.getId(), modelNotes);
	}

	public long resumeModelCreation(Model model, String modelNotes) {
		return ctx.transactionResult(configuration ->
		{
			DSLContext transactionCtx = DSL.using(configuration);
			LocalDateTime dateCreated = LocalDateTime.now();
			model.setDraft(false);
			ModelRepository.updateModel(transactionCtx, model.getId(), false, modelNotes);
			return ExperimentRepository.insertExperiment(transactionCtx, model.getId(), dateCreated);
		});
	}
}
