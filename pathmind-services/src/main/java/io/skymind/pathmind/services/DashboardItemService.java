package io.skymind.pathmind.services;

import io.skymind.pathmind.db.data.DashboardItem;
import io.skymind.pathmind.db.data.Experiment;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.db.dao.PolicyDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardItemService {
	private static final int DEFAULT_LIMIT = 10;

	private final ExperimentDAO experimentDAO;
	private final PolicyDAO policyDao;

	public List<DashboardItem> getDashboardItemsForUser(long userId, int offset) {
		return getDashboardItemsForUser(userId, offset, DEFAULT_LIMIT);
	}

	public List<DashboardItem> getDashboardItemsForUser(long userId, int offset, int limit) {
		final List<DashboardItem> dashboardItemsForUser = experimentDAO.getDashboardItemsForUser(userId, offset, limit);

		final List<DashboardItem> inTrainingDashboardItems = dashboardItemsForUser.stream()
				.filter(DashboardItem::hasTrainingInProgress)
				.collect(Collectors.toList());

		final List<Long> inTrainingExperimentsIDs = inTrainingDashboardItems.stream()
				.map(DashboardItem::getExperiment)
				.map(Experiment::getId)
				.collect(Collectors.toList());

		if(!CollectionUtils.isEmpty(inTrainingExperimentsIDs)) {
			final Map<Long, Integer> iterationsForExperiments = policyDao.getRewardScoresCountForExperiments(inTrainingExperimentsIDs);
			inTrainingDashboardItems.forEach(
					item -> {
						final long experimentId = item.getExperiment().getId();
						if(iterationsForExperiments.containsKey(experimentId)) {
							item.setIterationsProcessed(iterationsForExperiments.get(experimentId));
						}
					}
			);
		}

		return dashboardItemsForUser;
	}

	public Optional<DashboardItem> getSingleDashboardItem(long experimentId) {
		return experimentDAO.getSingleDashboardItem(experimentId).stream()
				.peek(item -> {
					if(item.hasTrainingInProgress()) {
						final Map<Long, Integer> iterations = policyDao.getRewardScoresCountForExperiments(List.of(experimentId));
						item.setIterationsProcessed(iterations.getOrDefault(experimentId, 0));
					}
				})
				.findAny();
	}

	public int countTotalDashboardItemsForUser(long userId) {
		return experimentDAO.countDashboardItemsForUser(userId);
	}
}
