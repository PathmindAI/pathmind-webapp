package io.skymind.pathmind.webapp.ui.views.policy.filter;

import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.webapp.ui.components.PathmindFilterInterface;
import io.skymind.pathmind.webapp.utils.SearchUtils;

public class PolicyFilter implements PathmindFilterInterface<Policy>
{
	@Override
	public boolean isMatch(Policy policy, String searchValue) {
		return SearchUtils.contains(policy.getRun().getStatusEnum().toString(), searchValue) ||
//				SearchUtils.contains(policy.getCompleted().toString(), searchValue) ||
//				SearchUtils.contains(policy.getProgress().toString(), searchValue) ||
				SearchUtils.contains(policy.getName(), searchValue) ||
				SearchUtils.contains(policy.getRun().getRunTypeEnum().toString(), searchValue);
	}
}
