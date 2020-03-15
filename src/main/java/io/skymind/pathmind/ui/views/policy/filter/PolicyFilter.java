package io.skymind.pathmind.ui.views.policy.filter;

import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.ui.components.PathmindFilterInterface;
import io.skymind.pathmind.utils.SearchUtils;

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
