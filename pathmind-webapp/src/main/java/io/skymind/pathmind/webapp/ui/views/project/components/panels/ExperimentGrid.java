package io.skymind.pathmind.webapp.ui.views.project.components.panels;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.TemplateRenderer;

import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.ui.renderer.ZonedDateTimeRenderer;
import io.skymind.pathmind.shared.utils.DateAndTimeUtils;

import java.util.Arrays;
import java.util.Comparator;

public class ExperimentGrid extends Grid<Experiment>
{
	public ExperimentGrid()
	{
		Grid.Column<Experiment> nameColumn = addColumn(
				TemplateRenderer.<Experiment> of("[[item.name]] <span class='tag'>[[item.draft]]</span>")
					.withProperty("name", Experiment::getName)
					.withProperty("draft", experiment -> experiment.isDraft() ? "Draft" : ""))
				.setComparator(Comparator.comparingLong(experiment -> Long.parseLong(experiment.getName())))
				.setHeader("#")
				.setAutoWidth(true)
				.setFlexGrow(0)
				.setResizable(true)
				.setSortable(true);
		Grid.Column<Experiment> createdColumn = addColumn(new ZonedDateTimeRenderer<>(Experiment::getDateCreated, DateAndTimeUtils.STANDARD_DATE_AND_TIME_SHORT_FOMATTER))
				.setComparator(Comparator.comparing(Experiment::getDateCreated))
				.setHeader("Created")
				.setAutoWidth(true)
				.setFlexGrow(0)
				.setAutoWidth(true)
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
                .setClassNameGenerator(column -> "grid-notes-column")
				.setHeader("Notes")
				.setFlexGrow(1)
				.setResizable(true)
				.setSortable(false);

		// Sort by created by default
		sort(Arrays.asList(new GridSortOrder<>(createdColumn, SortDirection.DESCENDING)));

		getElement().getStyle().set("padding-top", "20px");
	}
}
