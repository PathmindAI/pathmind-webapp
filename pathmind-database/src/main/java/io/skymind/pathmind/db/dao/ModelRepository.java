package io.skymind.pathmind.db.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import io.skymind.pathmind.db.jooq.Tables;
import io.skymind.pathmind.db.jooq.tables.records.ModelRecord;
import io.skymind.pathmind.shared.data.Model;
import org.jooq.DSLContext;
import org.jooq.UpdateConditionStep;
import org.jooq.UpdateSetFirstStep;
import org.jooq.UpdateWhereStep;
import org.jooq.impl.DSL;

import static io.skymind.pathmind.db.jooq.tables.Model.MODEL;
import static io.skymind.pathmind.db.jooq.tables.Project.PROJECT;

class ModelRepository {
    protected static List<Model> getModelsForProject(DSLContext ctx, long projectId) {
        return ctx
                .select(MODEL.asterisk())
                .from(MODEL)
                .where(MODEL.PROJECT_ID.eq(projectId))
                .orderBy(MODEL.PROJECT_CHANGED_AT.desc(), MODEL.ID.desc())
                .fetchInto(Model.class);
    }

    protected static void archive(DSLContext ctx, long modelId, boolean isArchive) {
        ctx.update(MODEL)
                .set(MODEL.ARCHIVED, isArchive)
                .where(MODEL.ID.eq(modelId))
                .execute();
    }

    private static Long getProjectForModel(DSLContext ctx, Long modelId) {
        return ctx.select()
                .from(Tables.MODEL)
                .where(Tables.MODEL.ID.eq(modelId))
                .fetchOne(MODEL.PROJECT_ID);
    }


    protected static int getModelCount(DSLContext ctx, long projectId) {
        return ctx.selectCount()
                .from(MODEL)
                .where(MODEL.PROJECT_ID.eq(projectId))
                .fetchOne(0, int.class);
    }

    /**
     * Note: This doesn't return a *FULL* model. Only the fields that seems relevant at the moment.
     *
     * @param modelId
     * @return Model - beware, not all fields are initialized
     */
    protected static Model getModel(DSLContext ctx, long modelId) {
        return ctx.select(MODEL.ID, MODEL.PROJECT_ID, MODEL.NAME, MODEL.DATE_CREATED, MODEL.PACKAGE_NAME, MODEL.NUMBER_OF_OBSERVATIONS, MODEL.ARCHIVED,
                MODEL.USER_NOTES, MODEL.HAS_GOALS, MODEL.DRAFT, MODEL.REWARD_VARIABLES_COUNT, MODEL.ACTION_TUPLE_SIZE, MODEL.INVALID_MODEL, MODEL.MODEL_TYPE)
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
        mod.setNumberOfObservations(model.getNumberOfObservations());
        mod.setDraft(model.isDraft());
        mod.setRewardVariablesCount(model.getRewardVariablesCount());
        mod.setUserNotes(userNotes);
        mod.setPackageName(model.getPackageName());
        mod.setActionTupleSize(-1);
        mod.setModelType(model.getModelType());
        mod.setNumberOfAgents(model.getNumberOfAgents());
        mod.setPathmindHelper(model.getPathmindHelper());
        mod.setMainAgent(model.getMainAgent());
        mod.setExperimentClass(model.getExperimentClass());
        mod.setExperimentType(model.getExperimentType());
        mod.setActionmask(model.isActionmask());
        mod.setDateCreated(LocalDateTime.now());
        mod.setLastActivityDate(mod.getDateCreated());
        mod.store();
        ProjectRepository.update(ctx, new ProjectUpdateRequest(projectId).lastActivityDate(mod.getLastActivityDate()));
        return mod.key().get(MODEL.ID);
    }

    public static Optional<Model> getModelIfAllowed(DSLContext ctx, long modelId, long userId) {
        return Optional.ofNullable(ctx
                .select(MODEL.ID, MODEL.PROJECT_ID, MODEL.NAME, MODEL.DATE_CREATED, MODEL.NUMBER_OF_OBSERVATIONS, MODEL.PACKAGE_NAME, MODEL.ARCHIVED,
                        MODEL.USER_NOTES, MODEL.HAS_GOALS, MODEL.DRAFT, MODEL.REWARD_VARIABLES_COUNT, MODEL.ACTION_TUPLE_SIZE)
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

    protected static Model getLastModelForProject(DSLContext ctx, long projectId, long currentModelId) {
        return ctx.select(MODEL.asterisk())
                .from(MODEL)
                .where(MODEL.PROJECT_ID.eq(projectId).and(MODEL.ID.lessThan(currentModelId)))
                .orderBy(MODEL.ID.desc())
                .limit(1)
                .fetchAnyInto(Model.class);
    }

    protected static void update(DSLContext dslCtx, ModelUpdateRequest updateRequest) {
        dslCtx.transaction(conf -> {
            DSLContext ctx = DSL.using(conf);
            UpdateSetFirstStep update =  ctx.update(MODEL);
            LocalDateTime activityDate = Optional.ofNullable((LocalDateTime)updateRequest.updates.get(MODEL.LAST_ACTIVITY_DATE)).orElse(LocalDateTime.now());
            updateRequest.updates.put(MODEL.LAST_ACTIVITY_DATE, activityDate);
            updateRequest.updates.forEach((f,v) -> update.set(f, v));

            UpdateConditionStep<?> command = ((UpdateWhereStep<?>)update).where(MODEL.ID.eq(updateRequest.modelId));
            command.execute();

            Long projectId = ModelRepository.getProjectForModel(ctx, updateRequest.modelId);
            ProjectRepository.update(ctx, new ProjectUpdateRequest(projectId).lastActivityDate(activityDate));
        });

    }

}
