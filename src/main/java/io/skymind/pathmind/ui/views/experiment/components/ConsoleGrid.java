package io.skymind.pathmind.ui.views.experiment.components;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import io.skymind.pathmind.data.ConsoleEntry;

public class ConsoleGrid extends Grid<ConsoleEntry>
{
	public ConsoleGrid() {
		addColumn(ConsoleEntry::getDescription)
				.setHeader("Description")
				.setSortable(true)
				.setAutoWidth(true);
		addColumn(ConsoleEntry::getStatus)
				.setHeader("Status")
				.setSortable(true)
				.setAutoWidth(true);
		addColumn(ConsoleEntry::getDescription)
				.setHeader("Score")
				.setSortable(true)
				.setWidth("100px");
		addColumn(ConsoleEntry::getDate)
				.setHeader("Date")
				.setSortable(true)
				.setAutoWidth(true);

		setSizeFull();
		addThemeVariants(GridVariant.LUMO_NO_BORDER);

		setMinHeight("200px");
	}
}
