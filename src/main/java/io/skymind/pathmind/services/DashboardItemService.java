package io.skymind.pathmind.services;

import io.skymind.pathmind.data.DashboardItem;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardItemService {
	private static final int DEFAULT_LIMIT = 10;

	private final ExperimentDAO experimentDAO;

	public List<DashboardItem> getDashboardItemsForUser(long userId, int offset) {
		return getDashboardItemsForUser(userId, offset, DEFAULT_LIMIT);
	}

	public List<DashboardItem> getDashboardItemsForUser(long userId, int offset, int limit) {
		return experimentDAO.getDashboardItemsForUser(userId, offset, limit);
	}

	public int countTotalDashboardItemsForUser(long userId) {
		return experimentDAO.countDashboardItemsForUser(userId);
	}
}
