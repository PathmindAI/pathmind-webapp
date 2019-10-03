package io.skymind.pathmind.ui.views.project.components.panels;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.utils.DateAndTimeUtils;

/**
 * This is a class because it's expected to be re-used in the ConsoleView when it's brought back.
 */
public class ExperimentGrid extends Grid<Experiment>
{
	public ExperimentGrid()
	{
		// TODO -> DH -> You will need to fill in all the todo values
		addColumn(Experiment::getName)
				.setHeader("Experiment")
				.setAutoWidth(true)
				.setSortable(true);
		addColumn(new LocalDateTimeRenderer<>(Experiment::getLastActivityDate, DateAndTimeUtils.STANDARD_DATE_TIME_FOMATTER))
				.setHeader("Last Activity")
				.setAutoWidth(true)
				.setSortable(true);
		addColumn(experiment -> "TODO")
				.setHeader("Test Run")
				.setAutoWidth(true)
				.setSortable(true);
		addColumn(experiment -> "Todo")
				.setHeader("Discovery Run")
				.setAutoWidth(true)
				.setSortable(true);
		addColumn(experiment -> "Todo")
				.setHeader("Full Run")
				.setAutoWidth(true)
				.setSortable(true);

		setSelectionMode(Grid.SelectionMode.SINGLE);
		getElement().getStyle().set("padding-top", "20px");
		addThemeVariants(GridVariant.LUMO_NO_BORDER);
	}
}
