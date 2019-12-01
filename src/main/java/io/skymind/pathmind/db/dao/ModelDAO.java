package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.data.Model;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ModelDAO
{
    private final DSLContext ctx;

    public ModelDAO(DSLContext ctx) {
        this.ctx = ctx;
    }

	public List<Model> getModelsForProject(long projectId) {
    	return ModelSQL.getModelsForProject(ctx, projectId);
	}

	public void archive(long modelId, boolean isArchive) {
    	ModelSQL.archive(ctx, modelId, isArchive);
	}

	public byte[] getModelFile(long id) {
    	return ModelSQL.getModelFile(ctx, id);
	}

	public Model getModel(long modelId) {
    	return ModelSQL.getModel(ctx, modelId);
	}

	@Transactional
	public long addModelToProject(Model model, long projectId)
	{
		return ctx.transactionResult(configuration ->
		{
			DSLContext transactionCtx = DSL.using(configuration);
			LocalDateTime dateCreated = LocalDateTime.now();
			String modelName = Integer.toString(ModelSQL.getModelCount(transactionCtx, projectId) + 1);
			long modelId = ModelSQL.insertModel(transactionCtx, model, modelName, dateCreated, projectId);
			return ExperimentSQL.insertExperiment(transactionCtx, modelId, dateCreated);
		});
	}
}
