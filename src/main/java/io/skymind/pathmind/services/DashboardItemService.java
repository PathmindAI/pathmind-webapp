package io.skymind.pathmind.services;

import static io.skymind.pathmind.db.utils.DashboardQueryParams.QUERY_TYPE.FETCH_MULTIPLE_BY_USER;
import static io.skymind.pathmind.db.utils.DashboardQueryParams.QUERY_TYPE.FETCH_SINGLE_BY_EXPERIMENT;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import io.skymind.pathmind.data.DashboardItem;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.db.utils.DashboardQueryParams;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardItemService {
	private static final int DEFAULT_LIMIT = 10;

	private final ExperimentDAO experimentDAO;

	public List<DashboardItem> getDashboardItemsForUser(long userId, int offset) {
		return getDashboardItemsForUser(userId, offset, DEFAULT_LIMIT);
	}

	public List<DashboardItem> getDashboardItemsForUser(long userId, int offset, int limit) {
 		var dashboardQueryParams = DashboardQueryParams.builder()
 				.userId(userId)
 				.limit(limit)
 				.offset(offset)
 				.queryType(FETCH_MULTIPLE_BY_USER)
 				.build();
 		return experimentDAO.getDashboardItems(dashboardQueryParams);
 	}

 	public Optional<DashboardItem> getSingleDashboardItem(long experimentId) {
 		var dashboardQueryParams = DashboardQueryParams.builder()
 				.experimentId(experimentId)
 				.limit(1)
 				.offset(0)
 				.queryType(FETCH_SINGLE_BY_EXPERIMENT)
 				.build();
 		return experimentDAO.getDashboardItems(dashboardQueryParams).stream().findAny();
 	}

	public int countTotalDashboardItemsForUser(long userId) {
		return experimentDAO.countDashboardItemsForUser(userId);
	}
}
