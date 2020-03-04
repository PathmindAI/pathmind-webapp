package io.skymind.pathmind.db.dao;

import static io.skymind.pathmind.db.utils.DashboardQueryParams.QUERY_TYPE.FETCH_MULTIPLE_BY_USER;
import static io.skymind.pathmind.db.utils.DashboardQueryParams.QUERY_TYPE.FETCH_SINGLE_BY_EXPERIMENT;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import io.skymind.pathmind.data.Run;
import io.skymind.pathmind.data.utils.DataUtils;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import io.skymind.pathmind.aspects.MonitorExecutionTime;
import io.skymind.pathmind.data.DashboardItem;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.db.utils.DashboardQueryParams;

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
		List<Experiment> experiments = ExperimentRepository.getExperimentsForModel(ctx, modelId);
		Map<Long, List<Run>> runsGroupedByExperiment = RunRepository.getRunsForExperiments(ctx, DataUtils.convertToIds(experiments));
		experiments.stream().forEach(experiment ->
				experiment.setRuns(runsGroupedByExperiment.get(experiment.getId())));
		// A quick solution to fix a bug due to null checks bot being implemented throughout the app.
		experiments.stream()
				.filter(experiment -> experiment.getRuns() == null)
				.forEach(experiment -> experiment.setRuns(new ArrayList<Run>()));
		return experiments;
	}

	public void updateExperiment(Experiment experiment) {
		ExperimentRepository.updateExperiment(ctx, experiment);
	}

	public void archive(long experimentId, boolean isArchive) {
		ExperimentRepository.archive(ctx, experimentId, isArchive);
	}

	@MonitorExecutionTime
	public List<DashboardItem> getDashboardItemsForUser(long userId, int offset, int limit) {
		var dashboardQueryParams = DashboardQueryParams.builder()
				.userId(userId)
				.limit(limit)
				.offset(offset)
				.queryType(FETCH_MULTIPLE_BY_USER)
				.build();
		return ExperimentRepository.getDashboardItems(ctx, dashboardQueryParams);
	}

	@MonitorExecutionTime
	public List<DashboardItem> getSingleDashboardItem(long experimentId) {
		var dashboardQueryParams = DashboardQueryParams.builder()
				.experimentId(experimentId)
				.limit(1)
				.offset(0)
				.queryType(FETCH_SINGLE_BY_EXPERIMENT)
				.build();
		return ExperimentRepository.getDashboardItems(ctx, dashboardQueryParams);
	}

	@MonitorExecutionTime
	public int countDashboardItemsForUser(long userId) {
		return ExperimentRepository.countDashboardItemsForUser(ctx, userId);
	}

	public long insertExperiment(long modelId, LocalDateTime createdDate) {
		return ExperimentRepository.insertExperiment(ctx, modelId, createdDate);
	}

	public void updateUserNotes(long experimentId, String userNotes) {
		ExperimentRepository.updateUserNotes(ctx, experimentId, userNotes);
	}
}
