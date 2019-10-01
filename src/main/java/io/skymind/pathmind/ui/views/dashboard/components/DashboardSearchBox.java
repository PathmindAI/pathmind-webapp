package io.skymind.pathmind.ui.views.dashboard.components;

import com.vaadin.flow.component.grid.Grid;
import io.skymind.pathmind.constants.Algorithm;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.utils.PolicyUtils;
import io.skymind.pathmind.ui.components.SearchBox;
import io.skymind.pathmind.utils.SearchUtils;

import java.util.List;
import java.util.function.Supplier;

public class DashboardSearchBox extends SearchBox<Policy>
{
	public DashboardSearchBox(Grid<Policy> grid, Supplier<List<Policy>> itemListSupplier) {
		super(grid, itemListSupplier);
	}

	// TODO -> DH -> I'm not sure where some values comes such as the Algorithm from so please add that to the search
	@Override
	protected boolean isMatch(Policy policy, String searchValue) {
		return SearchUtils.contains(policy.getRun().getStatusEnum().name(), searchValue) ||
				SearchUtils.contains(policy.getProject().getName(), searchValue) ||
				SearchUtils.contains(policy.getModel().getName(), searchValue) ||
				SearchUtils.contains(policy.getExperiment().getName(), searchValue) ||
				SearchUtils.contains(policy.getRun().getRunTypeEnum().name(), searchValue) ||
				SearchUtils.contains(policy.getAlgorithm().name(), searchValue) ||
				SearchUtils.contains(PolicyUtils.getDuration(policy), searchValue) ||
				SearchUtils.contains(policy.getRun().getStoppedAt(), searchValue);
	}
}
