package io.skymind.pathmind.ui.views.model.components;

import com.vaadin.flow.component.grid.Grid;
import io.skymind.pathmind.data.Model;
import io.skymind.pathmind.ui.components.SearchBox;
import io.skymind.pathmind.utils.SearchUtils;

import java.util.List;
import java.util.function.Supplier;

public class ModelSearchBox extends SearchBox<Model>
{
	public ModelSearchBox(Grid<Model> grid, Supplier<List<Model>> itemListSupplier) {
		super(grid, itemListSupplier);
	}

	@Override
	protected boolean isMatch(Model model, String searchValue) {
		return SearchUtils.contains(model.getName(), searchValue) ||
				SearchUtils.contains(model.getDateCreated(), searchValue) ||
				SearchUtils.contains(model.getLastActivityDate(), searchValue);
	}
}
