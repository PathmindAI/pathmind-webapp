package io.skymind.pathmind.ui.views.project.components;

import com.vaadin.flow.component.grid.Grid;
import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.ui.components.SearchBox;
import io.skymind.pathmind.utils.SearchUtils;

import java.util.List;
import java.util.function.Supplier;

public class ProjectSearchBox extends SearchBox<Project>
{
	public ProjectSearchBox(Grid<Project> grid, Supplier<List<Project>> itemListSupplier) {
		super(grid, itemListSupplier);
	}

	@Override
	protected boolean isMatch(Project project, String searchValue) {
		return SearchUtils.contains(project.getName(), searchValue) ||
				SearchUtils.contains(project.getDateCreated(), searchValue) ||
				SearchUtils.contains(project.getLastActivityDate(), searchValue);
	}
}
