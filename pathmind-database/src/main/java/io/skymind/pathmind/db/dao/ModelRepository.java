package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.db.jooq.tables.records.ModelRecord;
import io.skymind.pathmind.shared.data.Model;
import org.jooq.DSLContext;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static io.skymind.pathmind.db.jooq.tables.Model.MODEL;
import static io.skymind.pathmind.db.jooq.tables.Project.PROJECT;

class ModelRepository
{
    protected static List<Model> getModelsForProject(DSLContext ctx, long projectId) {
        return ctx
				.select(MODEL.ID, MODEL.PROJECT_ID, MODEL.NAME, MODEL.DATE_CREATED, MODEL.LAST_ACTIVITY_DATE, MODEL.NUMBER_OF_OBSERVATIONS, MODEL.NUMBER_OF_POSSIBLE_ACTIONS, MODEL.ARCHIVED, MODEL.USER_NOTES, MODEL.DRAFT, MODEL.ACTION_TUPLE_SIZE)
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

	/**
	 * Note: This doesn't return a *FULL* model. Only the fields that seems relevant at the moment.
	 * @param modelId
	 * @return Model - beware, not all fields are initialized
	 */
	protected static Model getModel(DSLContext ctx, long modelId) {
		return ctx.select(MODEL.ID, MODEL.PROJECT_ID, MODEL.NAME, MODEL.DATE_CREATED, MODEL.PACKAGE_NAME, MODEL.NUMBER_OF_OBSERVATIONS,
				MODEL.NUMBER_OF_POSSIBLE_ACTIONS, MODEL.USER_NOTES, MODEL.DRAFT, MODEL.REWARD_VARIABLES_COUNT)
				.from(MODEL)
				.where(MODEL.ID.eq(modelId))
				.fetchOneInto(Model.class);
	}

	protected static long insertModel(DSLContext ctx, Model model, String name, String userNotes, LocalDateTime dateCreated, long projectId) {
		final ModelRecord mod = MODEL.newRecord();
		mod.attach(ctx.configuration());
		mod.setName(name);
		mod.setDateCreated(dateCreated);
		mod.setLastActivityDate(dateCreated);
		mod.setProjectId(projectId);
		mod.setNumberOfPossibleActions(model.getNumberOfPossibleActions());
		mod.setNumberOfObservations(model.getNumberOfObservations());
		mod.setDraft(model.isDraft());
		mod.setRewardVariablesCount(model.getRewardVariablesCount());
		mod.setUserNotes(userNotes);
		mod.setPackageName(model.getPackageName());
		mod.setActionTupleSize(model.getActionTupleSize());
		mod.store();
		return mod.key().get(MODEL.ID);
	}

	protected static void updateUserNotes(DSLContext ctx, long modelId, String userNotes) {
		ctx.update(MODEL)
				.set(MODEL.USER_NOTES, userNotes)
				.where(MODEL.ID.eq(modelId))
				.execute();
	}

	protected static void updateModel(DSLContext ctx,long modelId, boolean draft, String userNotes) {
		ctx.update(MODEL)
				.set(MODEL.DRAFT, draft)
				.set(MODEL.USER_NOTES, userNotes)
				.where(MODEL.ID.eq(modelId))
				.execute();
	}

	public static Optional<Model> getModelIfAllowed(DSLContext ctx, long modelId, long userId) {
		return Optional.ofNullable(ctx
				.select(MODEL.ID, MODEL.PROJECT_ID, MODEL.NAME, MODEL.DATE_CREATED, MODEL.NUMBER_OF_OBSERVATIONS, MODEL.PACKAGE_NAME,
						MODEL.NUMBER_OF_POSSIBLE_ACTIONS, MODEL.USER_NOTES, MODEL.DRAFT, MODEL.REWARD_VARIABLES_COUNT, MODEL.ACTION_TUPLE_SIZE)
				.from(MODEL)
				.leftJoin(PROJECT).on(PROJECT.ID.eq(MODEL.PROJECT_ID))
				.where(MODEL.ID.eq(modelId))
					.and(PROJECT.PATHMIND_USER_ID.eq(userId))
				.fetchOneInto(Model.class)
		);
	}

    protected static long getUserIdForModel(DSLContext ctx, long modelId) {
	    return ctx.select(PROJECT.PATHMIND_USER_ID)
                .from(PROJECT)
                .leftJoin(MODEL).on(MODEL.PROJECT_ID.eq(PROJECT.ID))
                .where(MODEL.ID.eq(modelId))
                .fetchOne(PROJECT.PATHMIND_USER_ID);
    }
}
