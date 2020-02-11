package io.skymind.pathmind.webapp.ui.views.project.components.panels;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.TemplateRenderer;

import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.db.data.Experiment;
import io.skymind.pathmind.webapp.ui.renderer.ZonedDateTimeRenderer;
import io.skymind.pathmind.shared.utils.DateAndTimeUtils;

import java.util.Arrays;
import java.util.Comparator;

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
					.withProperty("draft", experiment -> experiment.getRuns() == null || experiment.getRuns().isEmpty() ? "Draft" : ""))
				.setComparator(Comparator.comparing(Experiment::getName))
				.setHeader("#")
				.setAutoWidth(true)
				.setFlexGrow(0)
				.setResizable(true)
				.setSortable(true);
		addColumn(new ZonedDateTimeRenderer<>(Experiment::getLastActivityDate, DateAndTimeUtils.STANDARD_DATE_AND_TIME_SHORT_FOMATTER))
				.setComparator(Comparator.comparing(Experiment::getLastActivityDate))
				.setHeader("Last Activity")
				.setAutoWidth(true)
				.setFlexGrow(0)
				.setAutoWidth(true)
				.setResizable(true)
				.setSortable(true);
		addColumn(experiment -> ExperimentUtils.getTrainingStatus(experiment))
				.setHeader("Status")
				.setAutoWidth(true)
				.setFlexGrow(0)
				.setResizable(true)
				.setSortable(true);
		addColumn(experiment -> {
					String userNotes = experiment.getUserNotes();
					return userNotes.isEmpty() ? "—" : userNotes;
				})
				.setHeader("Notes")
				.setFlexGrow(1)
				.setResizable(true)
				.setSortable(false);

		// Sort by name by default
		sort(Arrays.asList(new GridSortOrder<>(nameColumn, SortDirection.DESCENDING)));

		getElement().getStyle().set("padding-top", "20px");
	}
}
