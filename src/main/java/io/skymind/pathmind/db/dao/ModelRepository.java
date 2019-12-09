package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.data.Model;
import io.skymind.pathmind.data.db.tables.records.ModelRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static io.skymind.pathmind.data.db.Tables.MODEL;

class ModelRepository
{
    protected static List<Model> getModelsForProject(DSLContext ctx, long projectId) {
        return ctx
				.select(MODEL.ID, MODEL.PROJECT_ID, MODEL.NAME, MODEL.DATE_CREATED, MODEL.LAST_ACTIVITY_DATE, MODEL.NUMBER_OF_OBSERVATIONS, MODEL.NUMBER_OF_POSSIBLE_ACTIONS, MODEL.GET_OBSERVATION_FOR_REWARD_FUNCTION, MODEL.ARCHIVED)
				.from(MODEL)
				.where(MODEL.PROJECT_ID.eq(projectId))
				.fetchInto(Model.class);
    }

	protected static void archive(DSLContext ctx, long modelId, boolean isArchive) {
    	ctx.update(MODEL)
				.set(MODEL.ARCHIVED, isArchive)
				.where(MODEL.ID.eq(modelId))
				.execute();
	}

	protected static int getModelCount(DSLContext ctx, long projectId) {
		return ctx.selectCount()
				.from(MODEL)
				.where(MODEL.PROJECT_ID.eq(projectId))
				.fetchOne(0, int.class);
	}

	protected static byte[] getModelFile(DSLContext ctx, long id) {
		return ctx.select(MODEL.FILE).from(MODEL).where(MODEL.ID.eq(id)).fetchOne(MODEL.FILE);
	}

	/**
	 * Note: This doesn't return a *FULL* model. Only the fields that seems relevant at the moment.
	 * @param modelId
	 * @return Model - beware, not all fields are initialized
	 */
	protected static Model getModel(DSLContext ctx, long modelId) {
		return ctx.select(MODEL.ID, MODEL.PROJECT_ID, MODEL.NAME, MODEL.NUMBER_OF_OBSERVATIONS, MODEL.NUMBER_OF_POSSIBLE_ACTIONS, MODEL.GET_OBSERVATION_FOR_REWARD_FUNCTION)
				.from(MODEL)
				.where(MODEL.ID.eq(modelId))
				.fetchOneInto(Model.class);
	}

	protected static long insertModel(DSLContext ctx, Model model, String name, LocalDateTime dateCreated, long projectId) {
		final ModelRecord mod = MODEL.newRecord();
		mod.attach(ctx.configuration());
		mod.setName(name);
		mod.setDateCreated(dateCreated);
		mod.setLastActivityDate(dateCreated);
		mod.setProjectId(projectId);
		mod.setNumberOfPossibleActions(model.getNumberOfPossibleActions());
		mod.setNumberOfObservations(model.getNumberOfObservations());
		mod.setGetObservationForRewardFunction(model.getGetObservationForRewardFunction());
		mod.setFile(model.getFile());
		mod.store();
		return mod.key().get(MODEL.ID);
	}
}
