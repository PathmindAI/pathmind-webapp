package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.db.data.Model;
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
			long modelId = ModelRepository.insertModel(transactionCtx, model, modelName, dateCreated, projectId);
			model.setId(modelId);
			ModelRepository.updateUserNotes(transactionCtx, modelId, userNotes);
			return ExperimentRepository.insertExperiment(transactionCtx, modelId, dateCreated);
		});
	}

	public void updateUserNotes(long modelId, String userNotes) {
		ModelRepository.updateUserNotes(ctx, modelId, userNotes);
	}
}
