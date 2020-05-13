package io.skymind.pathmind.db.dao;

import static io.skymind.pathmind.db.jooq.Tables.PATHMIND_USER;
import static io.skymind.pathmind.db.jooq.Tables.POLICY;
import static io.skymind.pathmind.db.jooq.tables.Experiment.EXPERIMENT;
import static io.skymind.pathmind.db.jooq.tables.Model.MODEL;
import static io.skymind.pathmind.db.jooq.tables.Project.PROJECT;
import static io.skymind.pathmind.db.jooq.tables.Run.RUN;
import static io.skymind.pathmind.db.utils.DashboardQueryParams.QUERY_TYPE.FETCH_MULTIPLE_BY_USER;
import static org.jooq.impl.DSL.count;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record7;
import org.jooq.Result;
import org.jooq.Table;
import org.jooq.impl.DSL;

import io.skymind.pathmind.shared.data.DashboardItem;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.data.Project;
import io.skymind.pathmind.shared.data.Run;
import io.skymind.pathmind.db.jooq.Tables;
import io.skymind.pathmind.db.jooq.tables.records.ExperimentRecord;
import io.skymind.pathmind.db.utils.DashboardQueryParams;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class ExperimentRepository
{
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

		if(record == null) {
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
					.and(Tables.PROJECT.PATHMIND_USER_ID.eq(userId))
				.fetchOne();

		if(record == null) {
			return null;
		}

		Experiment experiment = record.into(EXPERIMENT).into(Experiment.class);
		addParentDataModelObjects(record, experiment);
		return experiment;
	}

	protected static List<Experiment> getExperimentsForModel(DSLContext ctx, long modelId) {
		Result<?> result = ctx
				.select(EXPERIMENT.asterisk())
				.select(MODEL.ID, MODEL.NAME)
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

	protected static void updateExperiment(DSLContext ctx, Experiment experiment) {
		ctx.update(EXPERIMENT)
				.set(EXPERIMENT.REWARD_FUNCTION, experiment.getRewardFunction())
				.set(EXPERIMENT.USER_NOTES, experiment.getUserNotes())
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

	/**
	 * Main method to retrieve List of {@link DashboardItem}.
	 * It prepares a query to get all needed data within single database call, then tries to map returned
	 * records to specific datatypes which later are set to {@link DashboardItem} object.<br/>
	 * A query has a few joined tables and two subqueries to retrive specific data from other tables.<br/>
	 * Subquery named <code>LATEST_RUN</code> searches a latest run for given Experiment ID. It returns
	 * just a one row of each set using POSTGRES
	 * <a href="https://www.postgresql.org/docs/10/sql-select.html#SQL-DISTINCT">DISTINCT ON</a> clause.<br/>
	 * Subquery named <code>POLICY_FOR_LATEST_RUN</code>  checks if there is any exported policy for latest_runs
	 * returned by a subquery above.<br />
	 * Generated query in plain SQL would look like:
	 * <pre>
	 	SELECT e.id, e.name,
	 	       m.id, m.name,
	 	       p.id, p.name,
	 	       greatest(e.last_activity_date, m.last_activity_date, p.last_activity_date) AS ITEM_LAST_ACTIVITY_DATE,
	 	       latest_run.*
	 	FROM experiment e
	 	RIGHT JOIN model m ON m.id = e.model_id AND (e.archived = FALSE OR e.archived IS NULL)
	 	RIGHT JOIN project p ON p.id = m.project_id AND (m.archived = FALSE OR m.archived IS NULL)
	 	LEFT JOIN pathmind_user u ON u.id = p.pathmind_user_id
	 	LEFT JOIN
	 	  (SELECT DISTINCT ON (experiment_id) id, experiment_id, name, run_type, started_at, stopped_at, status
	 	   FROM run
	 	   WHERE started_at IS NOT NULL
	 	   ORDER BY experiment_id,
	 	            started_at DESC) latest_run ON latest_run.experiment_id = e.id
	 	LEFT JOIN
	 	  (SELECT run_id
	 	   FROM policy
	 	   WHERE policy.exported_at IS NOT NULL
	 	   GROUP BY policy.run_id) po ON po.run_id = latest_run.id
	 	WHERE p.pathmind_user_id = $pathmind_user_id
	 	  AND (p.archived = FALSE OR p.archived IS NULL)
	 	ORDER BY ITEM_LAST_ACTIVITY_DATE DESC,
	 	         e.id DESC
	 	LIMIT $limit
	 	OFFSET $offset
	 * </pre>
	 *
	 * @param userId pathmind user ID
	 * @param offset how many items should be skipped
	 * @param limit  how many items should be returned
	 * @return List of dashboard items
	 */
	static List<DashboardItem> getDashboardItems(DSLContext ctx, DashboardQueryParams dashboardQueryParams) {
		final var latestRun = ctx.select(RUN.ID, RUN.EXPERIMENT_ID, RUN.NAME, RUN.RUN_TYPE, RUN.STARTED_AT, RUN.STOPPED_AT, RUN.STATUS)
				.distinctOn(RUN.EXPERIMENT_ID)
				.from(RUN)
				.where(RUN.STARTED_AT.isNotNull())
				.orderBy(RUN.EXPERIMENT_ID, RUN.STARTED_AT.desc())
				.asTable("LATEST_RUN");

		final var policyForLatestRun = ctx.select(POLICY.RUN_ID)
				.from(POLICY)
				.where(POLICY.EXPORTED_AT.isNotNull())
				.groupBy(POLICY.RUN_ID)
				.asTable("POLICY_FOR_LATEST_RUN");

		final Field<LocalDateTime> itemLastActivityDate = DSL.ifnull(DSL.field(EXPERIMENT.LAST_ACTIVITY_DATE),
				DSL.greatest(MODEL.LAST_ACTIVITY_DATE,PROJECT.LAST_ACTIVITY_DATE));

		final Result<?> result = ctx.select(EXPERIMENT.ID, EXPERIMENT.NAME, EXPERIMENT.USER_NOTES,
				MODEL.ID, MODEL.NAME, MODEL.DRAFT,
				PROJECT.ID, PROJECT.NAME,
				latestRun.asterisk(),
				itemLastActivityDate.as("ITEM_LAST_ACTIVITY_DATE"),
				policyForLatestRun.asterisk())
				.from(EXPERIMENT)
					.rightJoin(MODEL).on(MODEL.ID.eq(EXPERIMENT.MODEL_ID)).and(EXPERIMENT.ARCHIVED.isFalse().or(EXPERIMENT.ARCHIVED.isNull()))
					.leftJoin(latestRun).on(EXPERIMENT.ID.eq(latestRun.field("experiment_id",
							RUN.EXPERIMENT_ID.getDataType())))
					.rightJoin(PROJECT).on(PROJECT.ID.eq(MODEL.PROJECT_ID)).and(MODEL.ARCHIVED.isFalse().or(MODEL.ARCHIVED.isNull()))
					.leftJoin(PATHMIND_USER).on(PATHMIND_USER.ID.eq(PROJECT.PATHMIND_USER_ID))
					.leftJoin(policyForLatestRun).on(policyForLatestRun.field("run_id", POLICY.RUN_ID.getDataType()).eq(latestRun.field(
						"id", RUN.ID.getDataType())))
				.where(EXPERIMENT.ARCHIVED.isFalse().or(EXPERIMENT.ARCHIVED.isNull()))
 					.and(PROJECT.ARCHIVED.isFalse().or(PROJECT.ARCHIVED.isNull()))
 					.and(findByUserOrExperimentCondition(dashboardQueryParams))
 					.and(MODEL.ARCHIVED.isFalse().or(MODEL.ARCHIVED.isNull()))
				.orderBy(itemLastActivityDate.desc(), EXPERIMENT.ID.desc())
				.offset(dashboardQueryParams.getOffset())
 				.limit(dashboardQueryParams.getLimit())
				.fetch();

		return result.stream()
				.map(record -> mapRecordToDashboardItem(record, latestRun, policyForLatestRun))
				.collect(Collectors.toList());
	}
	
	private static Condition findByUserOrExperimentCondition(DashboardQueryParams dashboardQueryParams) {
 		if(dashboardQueryParams.getQueryType() == FETCH_MULTIPLE_BY_USER) {
 			return PATHMIND_USER.ID.eq(dashboardQueryParams.getUserId());
 		} else {
 			return EXPERIMENT.ID.eq(dashboardQueryParams.getExperimentId());
 		}
 	}

	/**
	 * Helper method to map received database row to {@link DashboardItem} object.<br/>
	 * It sets {@link DashboardItem#setPolicyExported(boolean)} to true if any run with an exported policy was found.
	 */
	private static DashboardItem mapRecordToDashboardItem(Record record, Table<Record7<Long, Long, String, Integer, LocalDateTime, LocalDateTime, Integer>> lastRun, Table<Record1<Long>> policyForLastRun) {
		var experiment = record.into(EXPERIMENT).into(Experiment.class);
		var model = record.into(MODEL).into(Model.class);
		var project = record.into(PROJECT).into(Project.class);
		var run = record.into(lastRun).into(Run.class);
		var policy = record.into(policyForLastRun).into(Policy.class);

		project = project.getId() == 0 ? null : project;
		model = model.getId() == 0 ? null : model;
		run = run.getId() == 0 ? null : run;
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

	/**
	 * Counts and returns total number of given user's dashboard items
	 * Generated query in plain SQL would look like:
	 * <pre>
		 SELECT COUNT(*)
		 FROM experiment e
		 RIGHT JOIN model m ON m.id = e.model_id AND (e.archived = FALSE OR e.archived IS NULL)
		 RIGHT JOIN project p ON p.id = m.project_id AND (m.archived = FALSE OR m.archived IS NULL)
		 LEFT JOIN pathmind_user u ON u.id = p.pathmind_user_id
		 WHERE p.pathmind_user_id = $pathmind_user_id
		 	AND (p.archived = FALSE OR p.archived IS NULL)
	 * </pre>
	 */
	static int countDashboardItemsForUser(DSLContext ctx, long userId) {
		return ctx.selectCount()
				.from(EXPERIMENT)
					.rightJoin(MODEL).on(MODEL.ID.eq(EXPERIMENT.MODEL_ID)).and(EXPERIMENT.ARCHIVED.isFalse().or(EXPERIMENT.ARCHIVED.isNull()))
					.rightJoin(PROJECT).on(PROJECT.ID.eq(MODEL.PROJECT_ID)).and(MODEL.ARCHIVED.isFalse().or(MODEL.ARCHIVED.isNull()))
					.leftJoin(PATHMIND_USER).on(PATHMIND_USER.ID.eq(PROJECT.PATHMIND_USER_ID))
				.where(PATHMIND_USER.ID.eq(userId))
					.and(PROJECT.ARCHIVED.isFalse().or(PROJECT.ARCHIVED.isNull()))
				.fetchOne(count());
	}

	protected static void updateUserNotes(DSLContext ctx, long experimentId, String userNotes) {
		ctx.update(Tables.EXPERIMENT)
				.set(Tables.EXPERIMENT.USER_NOTES, userNotes)
				.where(Tables.EXPERIMENT.ID.eq(experimentId))
				.execute();
	}
}
