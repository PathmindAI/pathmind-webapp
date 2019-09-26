package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.db.tables.records.ExperimentRecord;
import io.skymind.pathmind.db.repositories.ExperimentRepository;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

import static io.skymind.pathmind.data.db.tables.Experiment.EXPERIMENT;

@Repository
public class ExperimentDAO extends ExperimentRepository
{
	private final DSLContext ctx;

	ExperimentDAO(DSLContext ctx){
		this.ctx = ctx;
	}

	// TODO -> Almost duplicate code with ProjectDAO.setupNewProject
	public long setupNewExperiment(Experiment experiment)
	{
		final ExperimentRecord ex = EXPERIMENT.newRecord();
		ex.attach(ctx.configuration());
		ex.setDateCreated(experiment.getDateCreated());
		ex.setModelId(experiment.getModelId());
		ex.setName(experiment.getName());
		ex.setRewardFunction(experiment.getRewardFunction());
		ex.store();

		return ex.getId();
	}

	// TODO -> Paul -> I'm just stubbing it here with some hacky code since I'm not sure how it's all going to be implemented in the backend.
	// Also keep in mind that the experiment going into this method is from policy.getExperiment() which is in turn gotten from
	// left joins in PolicyRepository so only the minimal columns were retrieved from the query.
	public long setupNewClonedExperiment(Experiment experiment)
	{
		final ExperimentRecord ex = EXPERIMENT.newRecord();
		ex.attach(ctx.configuration());
		ex.setDateCreated(LocalDateTime.now());
		ex.setLastActivityDate(LocalDateTime.now());
		// TODO -> Paul -> related to the comments at the method level.
		ex.setModelId(1L);
		ex.setName("todo");
		ex.setRewardFunction("todo");
		ex.store();

		return ex.getId();
	}
}
