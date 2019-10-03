package io.skymind.pathmind.ui.views.policy.components;

import com.vaadin.flow.component.grid.Grid;
import io.skymind.pathmind.constants.Algorithm;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.ui.components.SearchBox;
import io.skymind.pathmind.utils.SearchUtils;

import java.util.List;
import java.util.function.Supplier;

public class PolicySearchBox extends SearchBox<Policy>
{
	public PolicySearchBox(Grid<Policy> grid, Supplier<List<Policy>> itemListSupplier, boolean isSelectFirstOnSearch) {
		super(grid, itemListSupplier, isSelectFirstOnSearch);
	}

	// TODO -> DH -> I'm not sure where some values comes from so please add that to the search
	@Override
	protected boolean isMatch(Policy policy, String searchValue) {
		return SearchUtils.contains(policy.getRun().getStatusEnum().toString(), searchValue) ||
//				SearchUtils.contains(policy.getCompleted().toString(), searchValue) ||
//				SearchUtils.contains(policy.getProgress().toString(), searchValue) ||
				SearchUtils.contains(policy.getName(), searchValue) ||
				SearchUtils.contains(policy.getRun().getRunTypeEnum().toString(), searchValue) ||
				SearchUtils.contains(policy.getAlgorithm().toString(), searchValue);
//				SearchUtils.contains(policy.getNotes(), searchValue)
	}
}
