package io.skymind.pathmind.services;

import io.skymind.pathmind.data.DashboardItem;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class DashboardItemService {
	private static final int DEFAULT_LIMIT = 10;

	private final ExperimentDAO experimentDAO;

	public List<DashboardItem> getDashboardItemsForUser(long userId, int offset) {
		return getDashboardItemsForUser(userId, offset, DEFAULT_LIMIT);
	}

	public List<DashboardItem> getDashboardItemsForUser(long userId, int offset, int limit) {
		var experiments = experimentDAO.getLatestExperimentsForUser(userId, offset, limit);
		return experiments.stream()
				.map(DashboardItem::ofExperiment)
				.collect(toList());
	}

	public int countTotalDashboardItemsForUser(long userId) {
		return experimentDAO.getCountExperimentsForUser(userId);
	}
}
