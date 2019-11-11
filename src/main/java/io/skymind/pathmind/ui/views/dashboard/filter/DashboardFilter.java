package io.skymind.pathmind.ui.views.dashboard.filter;

import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.utils.PolicyUtils;
import io.skymind.pathmind.ui.components.PathmindFilterInterface;
import io.skymind.pathmind.utils.SearchUtils;

public class DashboardFilter implements PathmindFilterInterface<Policy>
{
	@Override
	public boolean isMatch(Policy policy, String searchValue) {
		return SearchUtils.contains(policy.getRun().getStatusEnum().toString(), searchValue) ||
				SearchUtils.contains(policy.getProject().getName(), searchValue) ||
				SearchUtils.contains(policy.getModel().getName(), searchValue) ||
				SearchUtils.contains(policy.getExperiment().getName(), searchValue) ||
				SearchUtils.contains(policy.getRun().getRunTypeEnum().toString(), searchValue) ||
				SearchUtils.contains(policy.getAlgorithmEnum().toString(), searchValue) ||
				SearchUtils.contains(PolicyUtils.getElapsedTime(policy), searchValue) ||
				SearchUtils.contains(policy.getRun().getStoppedAt(), searchValue);

	}
}
