package io.skymind.pathmind.db;

import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Project;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static io.skymind.pathmind.data.db.tables.Experiment.EXPERIMENT;
import static io.skymind.pathmind.data.db.tables.Project.PROJECT;

@Repository
public class ExperimentRepository
{
	@Autowired
	private DSLContext dslContext;

    public Experiment getExperiment(long experimentId) {
        return dslContext
            .selectFrom(EXPERIMENT)
            .where(EXPERIMENT.ID.eq(experimentId))
            .fetchOneInto(Experiment.class);
    }

	public List<Experiment> getExperimentsForModel(long modelId) {
		return dslContext
				.selectFrom(EXPERIMENT)
				.where(EXPERIMENT.MODEL_ID.eq(modelId))
				.fetchInto(Experiment.class);
	}

//	public List<Experiment> getExperimentsForProject(long projectId) {
//		return dslContext
//				.selectFrom(EXPERIMENT)
//				.where(EXPERIMENT.PROJECT_ID.eq(projectId))
//				.fetchInto(Experiment.class);
//	}
//
//	public List<Experiment> getOtherExperimentsForSameProject(long experimentId) {
//		return dslContext
//				.selectFrom(EXPERIMENT)
//				.where(EXPERIMENT.PROJECT_ID.eq(dslContext
//						.select(EXPERIMENT.PROJECT_ID)
//						.from(EXPERIMENT)
//						.where(EXPERIMENT.ID.eq(experimentId))))
//				.fetchInto(Experiment.class);
//	}

	// TODO -> Do we want to batch or is it ever only really going to be 1 experiment almost all the time?
	// PERFORMANCE -> Database insert loop.
	// TODO -> DATA MODEL
// 	public void insertExperimentsForProject(Project project) {
//    	project.getExperiments().stream().forEach(experiment ->
//				insertExperiment(experiment));
//	}

	public long insertExperiment(Experiment experiment) {
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