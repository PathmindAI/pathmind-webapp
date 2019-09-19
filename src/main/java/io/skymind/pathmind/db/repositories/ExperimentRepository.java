package io.skymind.pathmind.db.repositories;

import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Model;
import io.skymind.pathmind.data.Project;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static io.skymind.pathmind.data.db.tables.Experiment.EXPERIMENT;
import static io.skymind.pathmind.data.db.tables.Model.MODEL;
import static io.skymind.pathmind.data.db.tables.Project.PROJECT;

@Repository
public class ExperimentRepository
{
	@Autowired
	private DSLContext dslContext;

    public Experiment getExperiment(long experimentId) {
        Record record = dslContext
            .select(EXPERIMENT.asterisk())
			.select(MODEL.ID, MODEL.NAME)
			.select(PROJECT.ID, PROJECT.NAME)
			.from(EXPERIMENT)
			.leftJoin(MODEL)
				.on(MODEL.ID.eq(EXPERIMENT.MODEL_ID))
			.leftJoin(PROJECT)
				.on(PROJECT.ID.eq(MODEL.PROJECT_ID))
            .where(EXPERIMENT.ID.eq(experimentId))
            .fetchOne();

        Experiment experiment = record.into(EXPERIMENT).into(Experiment.class);
        experiment.setModel(record.into(MODEL).into(Model.class));
        experiment.setProject(record.into(PROJECT).into(Project.class));
        return experiment;
    }

	public List<Experiment> getExperimentsForModel(long modelId) {
		return dslContext
				.selectFrom(EXPERIMENT)
				.where(EXPERIMENT.MODEL_ID.eq(modelId))
				.fetchInto(Experiment.class);
	}

	protected long insertExperiment(Experiment experiment) {
    	// TODO -> DATA MODEL
    	return -1;
//    	long experimentId = dslContext
//				.insertInto(EXPERIMENT)
//				.set(EXPERIMENT.NAME, experiment.getName())
//				.set(EXPERIMENT.DATE, experiment.getDate())
//				.set(EXPERIMENT.DURATION, experiment.getDuration())
//				.set(EXPERIMENT.RUN_TYPE, experiment.getRunType())
//				// TODO -> Algorithm needs to be saved.
////				.set(EXPERIMENT.ALGORITHM)
//				.set(EXPERIMENT.SCORE, experiment.getScore())
//				.set(EXPERIMENT.REWARD_FUNCTION, experiment.getRewardFunction())
//				.set(EXPERIMENT.PROJECT_ID, experiment.getProject().getId())
//				.returning(EXPERIMENT.ID)
//				.fetchOne()
//				.getValue(EXPERIMENT.ID);
//    	experiment.setId(experimentId);
//    	return experimentId;
	}
}