package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.data.Model;
import io.skymind.pathmind.data.db.tables.ModelFile;
import io.skymind.pathmind.data.db.tables.records.ModelFileRecord;
import io.skymind.pathmind.data.db.tables.records.ModelRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static io.skymind.pathmind.data.db.Tables.*;
import static io.skymind.pathmind.data.db.Tables.PROJECT;

class ModelRepository
{
    protected static List<Model> getModelsForProject(DSLContext ctx, long projectId) {
        return ctx
				.select(MODEL.ID, MODEL.PROJECT_ID, MODEL.NAME, MODEL.DATE_CREATED, MODEL.LAST_ACTIVITY_DATE, MODEL.NUMBER_OF_OBSERVATIONS, MODEL.NUMBER_OF_POSSIBLE_ACTIONS, MODEL.GET_OBSERVATION_FOR_REWARD_FUNCTION, MODEL.ARCHIVED, MODEL.USER_NOTES)
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
		return ctx.select(MODEL_FILE.FILE).from(MODEL_FILE).where(MODEL_FILE.MODEL_ID.eq(id)).fetchOne(MODEL_FILE.FILE);
	}

	/**
	 * Note: This doesn't return a *FULL* model. Only the fields that seems relevant at the moment.
	 * @param modelId
	 * @return Model - beware, not all fields are initialized
	 */
	protected static Model getModel(DSLContext ctx, long modelId) {
		return ctx.select(MODEL.ID, MODEL.PROJECT_ID, MODEL.NAME, MODEL.NUMBER_OF_OBSERVATIONS, MODEL.NUMBER_OF_POSSIBLE_ACTIONS, MODEL.GET_OBSERVATION_FOR_REWARD_FUNCTION, MODEL.USER_NOTES)
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
		mod.store();
		return mod.key().get(MODEL.ID);
	}

	protected static void insertModelFile(DSLContext ctx, long modelId, byte[] file) {
		final ModelFileRecord mod = MODEL_FILE.newRecord();
		mod.attach(ctx.configuration());
		mod.setModelId(modelId);
		mod.setFile(file);
		mod.insert();
	}

	protected static void updateUserNotes(DSLContext ctx, long modelId, String userNotes) {
		ctx.update(MODEL)
				.set(MODEL.USER_NOTES, userNotes)
				.where(MODEL.ID.eq(modelId))
				.execute();
	}
}
