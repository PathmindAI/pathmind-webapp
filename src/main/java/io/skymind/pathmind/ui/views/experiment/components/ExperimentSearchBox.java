package io.skymind.pathmind.ui.views.experiment.components;

import com.vaadin.flow.component.grid.Grid;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.ui.components.SearchBox;
import io.skymind.pathmind.utils.SearchUtils;

import java.util.List;
import java.util.function.Supplier;

public class ExperimentSearchBox extends SearchBox<Experiment>
{
	public ExperimentSearchBox(Grid<Experiment> grid, Supplier<List<Experiment>> itemListSupplier) {
		super(grid, itemListSupplier);
	}

	// TODO -> Paul -> Can you add the missing column searches
	@Override
	protected boolean isMatch(Experiment experiment, String searchValue) {
		return SearchUtils.contains(experiment.getName(), searchValue) ||
				SearchUtils.contains(experiment.getLastActivityDate(), searchValue); // ||
//				SearchUtils.contains(experiment.getTestRun(), searchValue) ||
//				SearchUtils.contains(experiment.getDiscoveryRun(), searchValue) ||
//				SearchUtils.contains(experiment.getFullRun(), searchValue) ||
//				SearchUtils.contains(experiment.getArchive(), searchValue) ||
//				SearchUtils.contains(experiment.getNotes(), searchValue);
	}
}
