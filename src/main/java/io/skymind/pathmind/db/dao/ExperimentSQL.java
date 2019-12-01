package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Model;
import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.data.db.Tables;
import io.skymind.pathmind.data.db.tables.records.ExperimentRecord;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static io.skymind.pathmind.data.db.tables.Experiment.EXPERIMENT;
import static io.skymind.pathmind.data.db.tables.Model.MODEL;
import static io.skymind.pathmind.data.db.tables.Project.PROJECT;

@Repository
class ExperimentSQL
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
}
