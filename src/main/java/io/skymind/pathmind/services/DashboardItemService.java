package io.skymind.pathmind.services;

import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.data.DashboardItem;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.db.dao.PolicyDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
		dashboardItemsForUser.forEach(this::setPoliciesToDashboardItem);
		return dashboardItemsForUser;
	}

	public Optional<DashboardItem> getSingleDashboardItem(long experimentId) {
		return experimentDAO.getSingleDashboardItem(experimentId).stream().findAny();
	}

	public int countTotalDashboardItemsForUser(long userId) {
		return experimentDAO.countDashboardItemsForUser(userId);
	}

	// Since it is required mostly for the progress bar feature, it updates policies for an experiment if it has training in progress
	private void setPoliciesToDashboardItem(DashboardItem item) {
		if (item.getExperiment() != null && item.getLatestRun() != null && RunStatus.isRunning(item.getLatestRun().getStatusEnum())) {
			final List<Policy> policiesForExperiment = policyDao.getPoliciesForExperiment(item.getExperiment().getId());
			item.getExperiment().setPolicies(policiesForExperiment);
		}
	}
}
