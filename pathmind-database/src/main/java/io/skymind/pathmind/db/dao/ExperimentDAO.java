package io.skymind.pathmind.db.dao;

import static io.skymind.pathmind.db.utils.DashboardQueryParams.QUERY_TYPE.FETCH_MULTIPLE_BY_USER;
import static io.skymind.pathmind.db.utils.DashboardQueryParams.QUERY_TYPE.FETCH_SINGLE_BY_EXPERIMENT;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import io.skymind.pathmind.shared.data.Run;
import io.skymind.pathmind.db.utils.DataUtils;
import io.skymind.pathmind.shared.data.user.UserMetrics;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import io.skymind.pathmind.shared.aspects.MonitorExecutionTime;
import io.skymind.pathmind.shared.data.DashboardItem;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Observation;
import io.skymind.pathmind.db.utils.DashboardQueryParams;

@Repository
public class ExperimentDAO
{
	private final DSLContext ctx;

	ExperimentDAO(DSLContext ctx) {
		this.ctx = ctx;
	}

	public Optional<Experiment> getExperiment(long experimentId) {
		Experiment experiment = ExperimentRepository.getExperiment(ctx, experimentId);
        return Optional.ofNullable(experiment);
	}

    public Optional<Experiment> getExperimentWithRuns(long experimentId) {
        Experiment experiment = ExperimentRepository.getExperiment(ctx, experimentId);
        Optional<Experiment> result = Optional.ofNullable(experiment);
        result.ifPresent(e -> {
            List<Run> runsForExperiment = RunRepository.getRunsForExperiment(ctx, experiment.getId());
            e.setRuns(runsForExperiment);
        });
        return result;
    }

    public void markAsFavorite(long experimentId, boolean isFavorite) {
	    ExperimentRepository.markAsFavorite(ctx, experimentId, isFavorite);
	}

    public Optional<Experiment> getExperimentIfAllowed(long experimentId, long userId) {
		return Optional.ofNullable(ExperimentRepository.getExperimentIfAllowed(ctx, experimentId, userId));
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

    public void updateTrainingStatus(DSLContext transactionCtx, Experiment experiment) {
        ExperimentRepository.updateTrainingStatus(transactionCtx, experiment);
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

	public Experiment createNewExperiment(long modelId) {
		return ctx.transactionResult(conf -> {
            DSLContext transactionCtx = DSL.using(conf);
            String experimentName = Integer.toString(ExperimentRepository.getExperimentCount(transactionCtx, modelId) + 1);
            Experiment lastExperiment = ExperimentRepository.getLastExperimentForModel(transactionCtx, modelId);
            String rewardFunction = lastExperiment != null ? lastExperiment.getRewardFunction() : "";
            boolean hasGoals = lastExperiment != null ? lastExperiment.isHasGoals() : false;
            List<Observation> observations = lastExperiment != null ? ObservationRepository.getObservationsForExperiment(transactionCtx, lastExperiment.getId()) : Collections.emptyList();
            Experiment exp = ExperimentRepository.createNewExperiment(transactionCtx, modelId, experimentName, rewardFunction, hasGoals);
            ObservationRepository.insertExperimentObservations(transactionCtx, exp.getId(), observations);
            return exp;
        });
	}

	public void updateUserNotes(long experimentId, String userNotes) {
		ExperimentRepository.updateUserNotes(ctx, experimentId, userNotes);
	}

	public Optional<Experiment> getFullExperiment(long experimentId) {
	    Optional<Experiment> optionalExperiment = getExperiment(experimentId);
	    optionalExperiment.ifPresent(experiment -> {
	        // TODO -> STEPH -> Not good enough as there is a bunch of logic to parse the data in PolicyDAO
	        experiment.setPolicies(PolicyRepository.getPoliciesForExperiment(ctx, experimentId));
            experiment.setRuns(RunRepository.getRunsForExperiment(ctx, experimentId));
        });
	    return optionalExperiment;
    }
}
