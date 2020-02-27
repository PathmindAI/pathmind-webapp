package io.skymind.pathmind.ui.views.project.components.panels;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.TemplateRenderer;

import io.skymind.pathmind.constants.RunType;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Run;
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
					.withProperty("draft", experiment -> experiment.getRuns() == null || experiment.getRuns().isEmpty() ? "Draft" : ""))
				.setComparator(Comparator.comparing(Experiment::getName))
				.setHeader("Experiment")
				.setAutoWidth(true)
				.setResizable(true)
				.setSortable(true);
		addColumn(new ZonedDateTimeRenderer<>(Experiment::getLastActivityDate, DateAndTimeUtils.STANDARD_DATE_AND_TIME_SHORT_FOMATTER))
				.setComparator(Comparator.comparing(Experiment::getLastActivityDate))
				.setHeader("Last Activity")
				.setAutoWidth(true)
				.setResizable(true)
				.setSortable(true);
		addColumn(experiment -> {
			if(experiment.getRuns() == null)
				return "--";
			Optional<Run> run = experiment.getRuns().stream()
					.filter(r -> r.getRunTypeEnum().equals(RunType.DiscoveryRun))
					.findAny();
			return run.isPresent() ? run.get().getStatusEnum() : "--";
		})
				.setHeader("Discovery Run")
				.setAutoWidth(true)
				.setResizable(true)
				.setSortable(true);
		addColumn(experiment -> {
			if(experiment.getRuns() == null)
				return "--";
			List<Run> runs = experiment.getRuns().stream()
					.filter(r -> r.getRunTypeEnum().equals(RunType.FullRun))
					.collect(Collectors.toList());
			return runs.size() > 0 ? runs.size() : "--";
		})
				.setHeader("Full Run")
				.setAutoWidth(true)
				.setResizable(true)
				.setSortable(true);
		addColumn(experiment -> {
			String userNotes = experiment.getUserNotes();
			return userNotes.isEmpty() ? "--" : userNotes;
		})
				.setHeader("Notes")
				.setResizable(true)
				.setSortable(false);

		// Sort by name by default
		sort(Arrays.asList(new GridSortOrder<>(nameColumn, SortDirection.DESCENDING)));

		getElement().getStyle().set("padding-top", "20px");
	}
}
