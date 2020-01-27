package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.aspects.MonitorExecutionTime;
import io.skymind.pathmind.data.DashboardItem;
import io.skymind.pathmind.data.Experiment;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ExperimentDAO
{
	private final DSLContext ctx;

	ExperimentDAO(DSLContext ctx) {
		this.ctx = ctx;
	}

	public Optional<Experiment> getExperiment(long experimentId) {
		var experiment = ExperimentRepository.getExperiment(ctx, experimentId);
		return Optional.ofNullable(experiment);
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

	@MonitorExecutionTime
	public List<DashboardItem> getDashboardItemsForUser(long userId, int offset, int limit) {
		return ExperimentRepository.getDashboardItemsForUser(ctx, userId, offset, limit);
	}

	@MonitorExecutionTime
	public int countDashboardItemsForUser(long userId) {
		return ExperimentRepository.countDashboardItemsForUser(ctx, userId);
	}
}
