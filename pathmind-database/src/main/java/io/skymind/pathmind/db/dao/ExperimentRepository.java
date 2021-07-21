package io.skymind.pathmind.db.dao;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import io.skymind.pathmind.db.jooq.Tables;
import io.skymind.pathmind.db.jooq.tables.records.ExperimentRecord;
import io.skymind.pathmind.db.utils.ModelExperimentsQueryParams;
import io.skymind.pathmind.shared.constants.UserRole;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.shared.data.Project;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.jooq.CommonTableExpression;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.OrderField;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.SortOrder;
import org.jooq.Table;

import static io.skymind.pathmind.db.jooq.Tables.PATHMIND_USER;
import static io.skymind.pathmind.db.jooq.Tables.POLICY;
import static io.skymind.pathmind.db.jooq.Tables.REWARD_SCORE;
import static io.skymind.pathmind.db.jooq.Tables.RUN;
import static io.skymind.pathmind.db.jooq.tables.Experiment.EXPERIMENT;
import static io.skymind.pathmind.db.jooq.tables.Model.MODEL;
import static io.skymind.pathmind.db.jooq.tables.Project.PROJECT;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.select;

@Slf4j
class ExperimentRepository {
    protected static Experiment getExperiment(DSLContext ctx, long experimentId) {
        Record record = ctx
                .select(EXPERIMENT.asterisk())
                .select(MODEL.asterisk())
                .select(PROJECT.asterisk())
                .from(EXPERIMENT)
                .leftJoin(MODEL).on(MODEL.ID.eq(EXPERIMENT.MODEL_ID))
                .leftJoin(PROJECT).on(PROJECT.ID.eq(MODEL.PROJECT_ID))
                .where(EXPERIMENT.ID.eq(experimentId))
                .fetchOne();

        if (record == null) {
            return null;
        }

        Experiment experiment = record.into(EXPERIMENT).into(Experiment.class);
        addParentDataModelObjects(record, experiment);
        return experiment;
    }

    protected static Experiment getSharedExperiment(DSLContext ctx, long experimentId, long userId) {
        Record record = ctx
                .select(EXPERIMENT.asterisk())
                .select(MODEL.asterisk())
                .select(PROJECT.asterisk())
                .from(EXPERIMENT)
                .leftJoin(MODEL).on(MODEL.ID.eq(EXPERIMENT.MODEL_ID))
                .leftJoin(PROJECT).on(PROJECT.ID.eq(MODEL.PROJECT_ID))
                .join(PATHMIND_USER).on(PATHMIND_USER.ID.eq(userId).and(PATHMIND_USER.ACCOUNT_TYPE.eq(UserRole.Support.getId())))
                .where(EXPERIMENT.ID.eq(experimentId))
                .and(EXPERIMENT.SHARED_WITH_SUPPORT.eq(true).or(PROJECT.PATHMIND_USER_ID.eq(userId)))
                .fetchOne();

        if (record == null) {
            return null;
        }

        Experiment experiment = record.into(EXPERIMENT).into(Experiment.class);
        addParentDataModelObjects(record, experiment);
        return experiment;
    }

    protected static Experiment getExperimentIfAllowed(DSLContext ctx, long experimentId, long userId) {
        Record record = ctx
                .select(EXPERIMENT.asterisk())
                .select(MODEL.asterisk())
                .select(PROJECT.asterisk())
                .from(EXPERIMENT)
                .leftJoin(MODEL).on(MODEL.ID.eq(EXPERIMENT.MODEL_ID))
                .leftJoin(PROJECT).on(PROJECT.ID.eq(MODEL.PROJECT_ID))
                .where(EXPERIMENT.ID.eq(experimentId))
                .and(PROJECT.PATHMIND_USER_ID.eq(userId))
                .fetchOne();

        if (record == null) {
            return null;
        }

        Experiment experiment = record.into(EXPERIMENT).into(Experiment.class);
        addParentDataModelObjects(record, experiment);
        return experiment;
    }

    protected static int getExperimentsWithRunStatusCountForUser(DSLContext ctx, long userId, Collection<Integer> runStatuses) {
        return ctx
                .selectCount()
                .from(EXPERIMENT)
                .leftJoin(MODEL).on(MODEL.ID.eq(EXPERIMENT.MODEL_ID))
                .leftJoin(PROJECT).on(PROJECT.ID.eq(MODEL.PROJECT_ID))
                .where(PROJECT.PATHMIND_USER_ID.eq(userId))
                .and(EXPERIMENT.TRAINING_STATUS.in(runStatuses))
                .fetchOne(0, int.class);
    }

    protected static List<Experiment> getExperimentsForModel(DSLContext ctx, long modelId, boolean isIncludeArchived) {
        Condition condition = EXPERIMENT.MODEL_ID.eq(modelId);
        if (!isIncludeArchived) {
            condition = condition.and(EXPERIMENT.ARCHIVED.isFalse());
        }

        Result<?> result = ctx
                .select(EXPERIMENT.asterisk())
                .select(MODEL.ID, MODEL.NAME, MODEL.PATHMIND_HELPER, MODEL.MAIN_AGENT, MODEL.EXPERIMENT_CLASS, MODEL.EXPERIMENT_TYPE, MODEL.ACTIONMASK)
                .select(PROJECT.ID, PROJECT.NAME, PROJECT.PATHMIND_USER_ID)
                .from(EXPERIMENT)
                .leftJoin(MODEL).on(MODEL.ID.eq(EXPERIMENT.MODEL_ID))
                .leftJoin(PROJECT).on(PROJECT.ID.eq(MODEL.PROJECT_ID))
                .where(condition)
                .orderBy(EXPERIMENT.DATE_CREATED.desc())
                .fetch();

        return result.stream().map(record -> {
            Experiment experiment = record.into(EXPERIMENT).into(Experiment.class);
            addParentDataModelObjects(record, experiment);
            return experiment;
        }).collect(Collectors.toList());
    }

    protected static List<Experiment> getExperimentsInModelForUser(DSLContext ctx, ModelExperimentsQueryParams modelExperimentsQueryParams) {
        Condition condition = EXPERIMENT.MODEL_ID.eq(modelExperimentsQueryParams.getModelId());
        condition = condition.and(PROJECT.PATHMIND_USER_ID.eq(modelExperimentsQueryParams.getUserId()));
        condition = condition.and(EXPERIMENT.ARCHIVED.eq(modelExperimentsQueryParams.isArchived()));
        OrderField<?> orderField = EXPERIMENT.DATE_CREATED.sort(SortOrder.DESC);
        if (!modelExperimentsQueryParams.getSortBy().isEmpty()) {
            SortOrder fieldSortOrder = modelExperimentsQueryParams.isDescending() ? SortOrder.DESC : SortOrder.ASC;
            switch (modelExperimentsQueryParams.getSortBy().toUpperCase()) {
                case "NAME":
                    orderField = EXPERIMENT.ID.sort(fieldSortOrder); // NAME is increasing, same as ID
                    break;
                case "DATE_CREATED":
                    orderField = EXPERIMENT.DATE_CREATED.sort(fieldSortOrder);
                    break;
                case "TRAINING_STATUS":
                    orderField = EXPERIMENT.TRAINING_STATUS.sort(fieldSortOrder);
                    break;
                default:
                    orderField = EXPERIMENT.DATE_CREATED.sort(SortOrder.DESC);
            }
        }

        Result<?> result = ctx
                .select(EXPERIMENT.asterisk())
                .select(MODEL.ID, MODEL.NAME, MODEL.EXPERIMENT_CLASS)
                .select(PROJECT.ID, PROJECT.NAME, PROJECT.PATHMIND_USER_ID)
                .from(EXPERIMENT)
                .leftJoin(MODEL).on(MODEL.ID.eq(EXPERIMENT.MODEL_ID))
                .leftJoin(PROJECT).on(PROJECT.ID.eq(MODEL.PROJECT_ID))
                .where(condition)
                .orderBy(orderField)
                .offset(modelExperimentsQueryParams.getOffset())
                .limit(modelExperimentsQueryParams.getLimit())
                .fetch();

        return result.stream().map(record -> {
            Experiment experiment = record.into(EXPERIMENT).into(Experiment.class);
            addParentDataModelObjects(record, experiment);
            return experiment;
        }).collect(Collectors.toList());
    }

    protected static int getFilteredExperimentCount(DSLContext ctx, long modelId, boolean isArchived) {
        return ctx.selectCount()
                .from(EXPERIMENT)
                .where(EXPERIMENT.MODEL_ID.eq(modelId))
                .and(EXPERIMENT.ARCHIVED.eq(isArchived))
                .fetchOne(0, int.class);
    }

    private static void addParentDataModelObjects(Record record, Experiment experiment) {
        experiment.setModel(record.into(MODEL).into(Model.class));
        experiment.setProject(record.into(PROJECT).into(Project.class));
    }

    protected static void archive(DSLContext ctx, long experimentId, boolean isArchive) {
        ctx.update(EXPERIMENT)
                .set(EXPERIMENT.ARCHIVED, isArchive)
                .where(EXPERIMENT.ID.eq(experimentId))
                .execute();
    }

    protected static void updateRewardFunction(DSLContext ctx, Experiment experiment) {
        ctx.update(EXPERIMENT)
                .set(EXPERIMENT.REWARD_FUNCTION, experiment.getRewardFunction())
                .where(EXPERIMENT.ID.eq(experiment.getId()))
                .execute();
    }

    protected static Experiment createNewExperiment(DSLContext ctx, long modelId, boolean hasGoals) {
        return createNewExperiment(ctx, modelId, "1", "", hasGoals);
    }

    protected static Experiment createNewExperiment(DSLContext ctx, long modelId, String experimentName, String rewardFunction, boolean hasGoals) {
        final ExperimentRecord ex = EXPERIMENT.newRecord();
        ex.attach(ctx.configuration());
        ex.setDateCreated(LocalDateTime.now());
        ex.setModelId(modelId);
        ex.setName(experimentName);
        ex.setRewardFunction(rewardFunction);
        ex.setHasGoals(hasGoals);
        ex.store();
        return ex.into(EXPERIMENT).into(Experiment.class);
    }

    protected static int getExperimentCount(DSLContext ctx, long modelId) {
        return ctx.selectCount()
                .from(EXPERIMENT)
                .where(EXPERIMENT.MODEL_ID.eq(modelId))
                .fetchOne(0, int.class);
    }

    protected static Experiment getLastExperimentForModel(DSLContext ctx, long modelId) {
        return ctx.select(EXPERIMENT.asterisk())
                .from(EXPERIMENT)
                .where(EXPERIMENT.MODEL_ID.eq(modelId))
                .orderBy(EXPERIMENT.ID.desc())
                .limit(1)
                .fetchAnyInto(Experiment.class);
    }

    protected static void updateLastActivityDate(DSLContext ctx, long experimentId) {
        ctx.update(Tables.EXPERIMENT)
                .set(Tables.EXPERIMENT.LAST_ACTIVITY_DATE, LocalDateTime.now())
                .where(Tables.EXPERIMENT.ID.eq(experimentId))
                .execute();
    }

    protected static void updateUserNotes(DSLContext ctx, long experimentId, String userNotes) {
        ctx.update(EXPERIMENT)
                .set(EXPERIMENT.USER_NOTES, userNotes)
                .where(EXPERIMENT.ID.eq(experimentId))
                .execute();
    }

    protected static void markAsFavorite(DSLContext ctx, long experimentId, boolean isFavorite) {
        ctx.update(EXPERIMENT)
                .set(EXPERIMENT.IS_FAVORITE, isFavorite)
                .where(EXPERIMENT.ID.eq(experimentId))
                .execute();
    }

    protected static void updateTrainingStatus(DSLContext ctx, Experiment experiment) {
        ctx.update(EXPERIMENT)
                .set(EXPERIMENT.TRAINING_STATUS, experiment.getTrainingStatus())
                .where(EXPERIMENT.ID.eq(experiment.getId()))
                .execute();
    }

    protected static void shareExperimentWithSupport(DSLContext ctx, long experimentId) {
        ctx.update(EXPERIMENT)
                .set(EXPERIMENT.SHARED_WITH_SUPPORT, true)
                .where(Tables.EXPERIMENT.ID.eq(experimentId))
                .execute();
    }

    protected static void setDeployPolicyOnSuccess(DSLContext ctx, long experimentId, boolean value) {
        ctx.update(EXPERIMENT)
                .set(EXPERIMENT.DEPLOY_POLICY_ON_SUCCESS, value)
                .where(Tables.EXPERIMENT.ID.eq(experimentId))
                .execute();
    }

    /**
     with E as (
        select id  from experiment E where E.model_id = ?
     )
     select distinct on (experiment_id) experiment_id, run_id, policy_id, score, iteration
     from (
         select
         distinct on (P.id) P.id as policy_id, R.experiment_id, R.id as run_id, RW.mean as score, RW.iteration
         from
         E left join run R on (E.id = R.experiment_id)
         left join policy P on (R.id = P.run_id)
         left join reward_score RW on (P.id = RW.policy_id)
         order by P.id, RW.iteration desc
     ) T
     order by experiment_id, score desc
     */
    public static Map<Long, Long> bestPoliciesForExperimentByModelId(DSLContext ctx, long modelId) {

        final String ID_FIELD = "id";

        CommonTableExpression<Record1<Long>> E =
                name("E")
                        .fields(ID_FIELD)
                        .as(select(EXPERIMENT.ID).from(EXPERIMENT).where(EXPERIMENT.MODEL_ID.eq(modelId)));

        final String EXPERIMENT_ID = "experiment_id";
        final String RUN_ID = "run_id";
        final String POLICY_ID = "policy_id";
        final String SCORE = "score";
        final String ITERATION = "iteration";

        Table<?> nested = ctx
                .select(POLICY.ID.as(POLICY_ID), RUN.EXPERIMENT_ID.as(EXPERIMENT_ID), RUN.ID.as(RUN_ID), REWARD_SCORE.MEAN.as(SCORE), REWARD_SCORE.ITERATION.as(ITERATION))
                .distinctOn(POLICY.ID)
                .from(E)
                .leftJoin(RUN).on(RUN.EXPERIMENT_ID.eq(E.field(ID_FIELD, Long.class)))
                .leftJoin(POLICY).on(RUN.ID.eq(POLICY.RUN_ID))
                .leftJoin(REWARD_SCORE).on(POLICY.ID.eq(REWARD_SCORE.POLICY_ID))
                .orderBy(POLICY.ID, REWARD_SCORE.ITERATION.desc())
                .asTable("T");

        final Field<Long> experimentId = field(EXPERIMENT_ID, Long.class);
        final Field<Long> policyId = field(POLICY_ID, Long.class);
        final Field<?> score = field(SCORE, REWARD_SCORE.MEAN.getDataType());

        return ctx.with(E)
                .select(experimentId, field(RUN_ID), policyId, score, field(ITERATION))
                .distinctOn(experimentId)
                .from(nested)
                .orderBy(experimentId, score.desc())
                .fetchStream()
                .filter(Objects::nonNull)
                .map(r -> Pair.of(r.get(experimentId),r.get(policyId)))
                .filter(pair -> ObjectUtils.allNotNull(pair.getLeft(), pair.getRight()))
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }
}
