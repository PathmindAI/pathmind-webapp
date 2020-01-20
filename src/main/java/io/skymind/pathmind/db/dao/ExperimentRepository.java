package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.data.*;
import io.skymind.pathmind.data.db.Tables;
import io.skymind.pathmind.data.db.tables.records.ExperimentRecord;
import org.jooq.*;
import org.jooq.impl.DSL;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static io.skymind.pathmind.data.db.Tables.*;
import static io.skymind.pathmind.data.db.tables.Experiment.EXPERIMENT;
import static io.skymind.pathmind.data.db.tables.Model.MODEL;
import static io.skymind.pathmind.data.db.tables.Project.PROJECT;
import static io.skymind.pathmind.data.db.tables.Run.RUN;
import static org.jooq.impl.DSL.count;

class ExperimentRepository
{
	protected static Experiment getExperiment(DSLContext ctx, long experimentId) {
		Record record = ctx
				.select(EXPERIMENT.asterisk())
				.select(MODEL.ID, MODEL.NAME, MODEL.GET_OBSERVATION_FOR_REWARD_FUNCTION)
				.select(PROJECT.ID, PROJECT.NAME, PROJECT.PATHMIND_USER_ID)
				.from(EXPERIMENT)
				.leftJoin(MODEL).on(MODEL.ID.eq(EXPERIMENT.MODEL_ID))
				.leftJoin(PROJECT).on(PROJECT.ID.eq(MODEL.PROJECT_ID))
				.where(EXPERIMENT.ID.eq(experimentId))
				.fetchOne();

		Experiment experiment = record.into(EXPERIMENT).into(Experiment.class);
		addParentDataModelObjects(record, experiment);
		return experiment;
	}

	protected static List<Experiment> getExperimentsForModel(DSLContext ctx, long modelId) {
		Result<?> result = ctx
				.select(EXPERIMENT.asterisk())
				.select(MODEL.ID, MODEL.NAME, MODEL.GET_OBSERVATION_FOR_REWARD_FUNCTION)
				.select(PROJECT.ID, PROJECT.NAME, PROJECT.PATHMIND_USER_ID)
				.from(EXPERIMENT)
				.leftJoin(MODEL).on(MODEL.ID.eq(EXPERIMENT.MODEL_ID))
				.leftJoin(PROJECT).on(PROJECT.ID.eq(MODEL.PROJECT_ID))
				.where(EXPERIMENT.MODEL_ID.eq(modelId))
				.fetch();

		return result.stream().map(record -> {
			Experiment experiment = record.into(EXPERIMENT).into(Experiment.class);
			addParentDataModelObjects(record, experiment);
			return experiment;
		}).collect(Collectors.toList());
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

	protected static long insertExperiment(DSLContext ctx, long modelId, LocalDateTime dateCreated) {
		final ExperimentRecord ex = EXPERIMENT.newRecord();
		ex.attach(ctx.configuration());
		ex.setDateCreated(dateCreated);
		ex.setModelId(modelId);
		ex.setName("1");
		ex.setRewardFunction("");
		ex.store();
		return ex.key().get(EXPERIMENT.ID);
	}

	protected static long setupNewExperiment(DSLContext ctx, Experiment experiment) {
		final ExperimentRecord ex = EXPERIMENT.newRecord();
		ex.attach(ctx.configuration());
		ex.setDateCreated(experiment.getDateCreated());
		ex.setModelId(experiment.getModelId());
		ex.setName(experiment.getName());
		ex.setRewardFunction(experiment.getRewardFunction());
		ex.store();
		return ex.getId();
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

	// TODO: 20.01.2020 KW - provide javadoc
	static List<DashboardItem> getDashboardItemsForUser(DSLContext ctx, long userId, int offset, int limit) {
		final var recentRun = ctx.select(RUN.asterisk())
				.distinctOn(RUN.EXPERIMENT_ID)
				.from(RUN)
				.where(RUN.STARTED_AT.isNotNull())
				.orderBy(RUN.EXPERIMENT_ID, RUN.STARTED_AT.desc())
				.asTable("RECENT_RUN");

		final var runWithExportedPolicies = ctx.select(POLICY.RUN_ID)
				.from(POLICY)
				.where(POLICY.EXPORTED_AT.isNotNull())
				.groupBy(POLICY.RUN_ID)
				.asTable("RUN_WITH_EXPORTED_POLICIES");

		final Field<LocalDateTime> itemLastActivityDate = DSL.greatest(EXPERIMENT.LAST_ACTIVITY_DATE, MODEL.LAST_ACTIVITY_DATE,
				PROJECT.LAST_ACTIVITY_DATE);

		final Result<?> result = ctx.select(EXPERIMENT.asterisk(), MODEL.asterisk(), PROJECT.asterisk(),
				recentRun.asterisk(), itemLastActivityDate.as("ITEM_LAST_ACTIVITY_DATE"),
				runWithExportedPolicies.asterisk())
				.from(EXPERIMENT)
					.rightJoin(MODEL).on(MODEL.ID.eq(EXPERIMENT.MODEL_ID))
					.leftJoin(recentRun).on(EXPERIMENT.ID.eq(recentRun.field("experiment_id",
							RUN.EXPERIMENT_ID.getDataType())))
					.rightJoin(PROJECT).on(PROJECT.ID.eq(MODEL.PROJECT_ID))
					.leftJoin(PATHMIND_USER).on(PATHMIND_USER.ID.eq(PROJECT.PATHMIND_USER_ID))
					.leftJoin(runWithExportedPolicies).on(runWithExportedPolicies.field("run_id", POLICY.RUN_ID.getDataType()).eq(recentRun.field(
						"id", RUN.ID.getDataType())))
				.where(PATHMIND_USER.ID.eq(userId))
					.and(EXPERIMENT.ARCHIVED.isFalse().or(EXPERIMENT.ARCHIVED.isNull()))
					.and(PROJECT.ARCHIVED.isFalse().or(PROJECT.ARCHIVED.isNull()))
				.orderBy(itemLastActivityDate.desc(), EXPERIMENT.ID.desc())
				.offset(offset)
				.limit(limit)
				.fetch();

		return result.stream()
				.map(record -> mapRecordToDashboardItem(record, recentRun, runWithExportedPolicies))
				.collect(Collectors.toList());
	}

	private static DashboardItem mapRecordToDashboardItem(Record record, Table<Record> recentRunTable, Table<Record1<Long>> exported) {
		var experiment = record.into(EXPERIMENT).into(Experiment.class);
		var model = record.into(MODEL).into(Model.class);
		var project = record.into(PROJECT).into(Project.class);
		var run = record.into(recentRunTable).into(Run.class);
		var policy = record.into(exported).into(Policy.class);

		project = project.getId() == 0 ? null : project;
		model = model.getId() == 0 ? null : model;
		if (run.getId() == 0) {
			run = null;
			experiment.setRuns(List.of());
		} else {
			experiment.setRuns(List.of(run));
		}
		experiment = experiment.getId() == 0 ? null : experiment;


		return DashboardItem.builder()
				.experiment(experiment)
				.project(project)
				.model(model)
				.latestRun(run)
				.latestUpdateTime(record.getValue("ITEM_LAST_ACTIVITY_DATE", LocalDateTime.class))
				.policyExported(policy.getRunId() != 0)
				.build();
	}

	static int countDashboardItemsForUser(DSLContext ctx, long userId) {
		return ctx.selectCount()
				.from(EXPERIMENT)
					.rightJoin(MODEL).on(MODEL.ID.eq(EXPERIMENT.MODEL_ID))
					.rightJoin(PROJECT).on(PROJECT.ID.eq(MODEL.PROJECT_ID))
					.leftJoin(PATHMIND_USER).on(PATHMIND_USER.ID.eq(PROJECT.PATHMIND_USER_ID))
				.where(PATHMIND_USER.ID.eq(userId))
					.and(EXPERIMENT.ARCHIVED.isFalse().or(EXPERIMENT.ARCHIVED.isNull()))
					.and(PROJECT.ARCHIVED.isFalse().or(EXPERIMENT.ARCHIVED.isNull()))
				.fetchOne(count());
	}
}
