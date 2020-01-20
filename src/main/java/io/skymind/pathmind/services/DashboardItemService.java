package io.skymind.pathmind.services;

import io.skymind.pathmind.data.DashboardItem;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.db.dao.PolicyDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardItemService {
	private static final int DEFAULT_LIMIT = 10;

	private final ExperimentDAO experimentDAO;
	private final PolicyDAO policyDAO;

	public List<DashboardItem> getDashboardItemsForUser(long userId, int offset) {
		return getDashboardItemsForUser(userId, offset, DEFAULT_LIMIT);
	}

	public List<DashboardItem> getDashboardItemsForUser(long userId, int offset, int limit) {
		// TODO: 20.01.2020 KW - remove if not neede
//		List<DashboardItem> dashboardItemsForUser = experimentDAO.getDashboardItemsForUser(userId, offset, limit);
//		return dashboardItemsForUser.stream()
//				.map(item -> {
//					var run = item.getLatestRun();
//					if(run != null) {
//						var policies = policyDAO.getExportedPoliciesByRunId(run.getId());
//						item.setPolicyExported(!policies.isEmpty());
//					}
//					return item;
//				}).collect(Collectors.toList());
		return experimentDAO.getDashboardItemsForUser(userId, offset, limit);
	}

	public int countTotalDashboardItemsForUser(long userId) {
		return experimentDAO.countDashboardItemsForUser(userId);
	}
}
