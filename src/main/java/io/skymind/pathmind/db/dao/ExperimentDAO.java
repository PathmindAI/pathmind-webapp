package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.data.Experiment;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ExperimentDAO
{
	private final DSLContext ctx;

	ExperimentDAO(DSLContext ctx) {
		this.ctx = ctx;
	}

	public Experiment getExperiment(long experimentId) {
		return ExperimentRepository.getExperiment(ctx, experimentId);
	}

	public long setupNewExperiment(Experiment experiment) {
		return ExperimentRepository.setupNewExperiment(ctx, experiment);
	}

	public int getExperimentCount(long modelId) {
		return ExperimentRepository.getExperimentCount(ctx, modelId);
	}

	public Experiment getLastExperimentForModel(long modelId) {
		return ExperimentRepository.getLastExperimentForModel(ctx, modelId);
	}

	public List<Experiment> getExperimentsForModel(long modelId) {
		return ExperimentRepository.getExperimentsForModel(ctx, modelId);
	}

	public void updateRewardFunction(Experiment experiment) {
		ExperimentRepository.updateRewardFunction(ctx, experiment);
	}

	public void archive(long experimentId, boolean isArchive) {
		ExperimentRepository.archive(ctx, experimentId, isArchive);
	}
}
