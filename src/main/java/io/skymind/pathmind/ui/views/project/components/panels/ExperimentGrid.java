package io.skymind.pathmind.ui.views.project.components.panels;

import java.util.Arrays;
import java.util.Comparator;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.TemplateRenderer;

import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.utils.ExperimentUtils;
import io.skymind.pathmind.ui.renderer.ZonedDateTimeRenderer;
import io.skymind.pathmind.utils.DateAndTimeUtils;

/**
 * This is a class because it's expected to be re-used in the ConsoleView when it's brought back.
 */
public class ExperimentGrid extends Grid<Experiment>
{
	public ExperimentGrid()
	{
		Grid.Column<Experiment> nameColumn = addColumn(
				TemplateRenderer.<Experiment> of("[[item.name]] <span class='tag'>[[item.draft]]</span>")
					.withProperty("name", Experiment::getName)
					.withProperty("draft", experiment -> experiment.getRuns().isEmpty() ? "Draft" : ""))
				.setComparator(Comparator.comparing(Experiment::getName))
				.setHeader("Experiment")
				.setWidth("150px")
				.setFlexGrow(0)
				.setResizable(true)
				.setSortable(true);
		addColumn(new ZonedDateTimeRenderer<>(Experiment::getLastActivityDate, DateAndTimeUtils.STANDARD_DATE_AND_TIME_SHORT_FOMATTER))
				.setComparator(Comparator.comparing(Experiment::getLastActivityDate))
				.setHeader("Last Activity")
				.setWidth("150px")
				.setFlexGrow(0)
				.setResizable(true)
				.setSortable(true);
		addColumn(experiment -> ExperimentUtils.getTrainingStatus(experiment))
				.setHeader("Status")
				.setWidth("150px")
				.setFlexGrow(0)
				.setResizable(true)
				.setSortable(true);
		addColumn(experiment -> {
					String userNotes = experiment.getUserNotes();
					return userNotes.isEmpty() ? "--" : userNotes;
				})
				.setHeader("Notes")
				.setFlexGrow(1)
				.setResizable(true)
				.setSortable(false);

		// Sort by name by default
		sort(Arrays.asList(new GridSortOrder<Experiment>(nameColumn, SortDirection.DESCENDING)));

		getElement().getStyle().set("padding-top", "20px");
	}
}
