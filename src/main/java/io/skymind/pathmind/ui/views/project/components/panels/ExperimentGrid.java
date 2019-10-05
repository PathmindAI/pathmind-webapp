package io.skymind.pathmind.ui.views.project.components.panels;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import io.skymind.pathmind.constants.RunType;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Run;
import io.skymind.pathmind.utils.DateAndTimeUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This is a class because it's expected to be re-used in the ConsoleView when it's brought back.
 */
public class ExperimentGrid extends Grid<Experiment>
{
	public ExperimentGrid()
	{
		addColumn(Experiment::getName)
				.setHeader("Experiment")
				.setAutoWidth(true)
				.setSortable(true);
		addColumn(new LocalDateTimeRenderer<>(Experiment::getLastActivityDate, DateAndTimeUtils.STANDARD_DATE_TIME_FOMATTER))
				.setHeader("Last Activity")
				.setAutoWidth(true)
				.setSortable(true);
		addColumn(experiment -> {
			 Optional<Run> run = experiment.getRuns().stream()
					.filter(r -> r.getRunTypeEnum().equals(RunType.TestRun))
					.findAny();

			return run.isPresent() ? run.get().getStatusEnum() : "Draft";
		})
				.setHeader("Test Run")
				.setAutoWidth(true)
				.setSortable(true);
		addColumn(experiment -> {
			Optional<Run> run = experiment.getRuns().stream()
					.filter(r -> r.getRunTypeEnum().equals(RunType.DiscoveryRun))
					.findAny();

			return run.isPresent() ? run.get().getStatusEnum() : "--";
		})
				.setHeader("Discovery Run")
				.setAutoWidth(true)
				.setSortable(true);
		addColumn(experiment -> {
			List<Run> runs = experiment.getRuns().stream()
					.filter(r -> r.getRunTypeEnum().equals(RunType.FullRun))
					.collect(Collectors.toList());

			return runs.size() > 0 ? runs.size() : "--";
		})
				.setHeader("Full Run")
				.setAutoWidth(true)
				.setSortable(true);

		setSelectionMode(Grid.SelectionMode.SINGLE);
		getElement().getStyle().set("padding-top", "20px");
		addThemeVariants(GridVariant.LUMO_NO_BORDER);
	}
}
