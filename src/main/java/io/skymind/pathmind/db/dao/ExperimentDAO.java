package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.db.tables.records.ExperimentRecord;
import io.skymind.pathmind.db.repositories.ExperimentRepository;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

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
}
