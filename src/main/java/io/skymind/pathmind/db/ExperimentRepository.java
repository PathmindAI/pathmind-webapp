package io.skymind.pathmind.db;

import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.converters.RunTypeConverter;
import org.jooq.DSLContext;
import org.jooq.impl.EnumConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static io.skymind.pathmind.data.db.tables.Experiment.EXPERIMENT;

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

	public List<Experiment> getExperimentsForProject(long projectId) {
		return dslContext
				.selectFrom(EXPERIMENT)
				.where(EXPERIMENT.PROJECT_ID.eq(projectId))
				.fetchInto(Experiment.class);
	}

	public List<Experiment> getOtherExperimentsForSameProject(long experimentId) {
		return dslContext
				.selectFrom(EXPERIMENT)
				.where(EXPERIMENT.PROJECT_ID.eq(dslContext
						.select(EXPERIMENT.PROJECT_ID)
						.from(EXPERIMENT)
						.where(EXPERIMENT.ID.eq(experimentId))))
				.fetchInto(Experiment.class);
	}

	public long insertExperiment(Experiment experiment) {
    	return dslContext
				.insertInto(EXPERIMENT)
				.set(EXPERIMENT.NAME, experiment.getName())
				.set(EXPERIMENT.DATE, experiment.getDate())
				.set(EXPERIMENT.RUN_TYPE, experiment.getRunType())
				.set(EXPERIMENT.SCORE, experiment.getScore())
				.set(EXPERIMENT.REWARD_FUNCTION, experiment.getRewardFunction())
				.set(EXPERIMENT.PROJECT_ID, experiment.getProject().getId())
				.returning(EXPERIMENT.ID)
				.fetchOne()
				.getValue(EXPERIMENT.ID);
	}
}