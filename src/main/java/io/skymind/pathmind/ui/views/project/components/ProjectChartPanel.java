package io.skymind.pathmind.ui.views.project.components;

import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.Configuration;
import com.vaadin.flow.component.charts.model.ListSeries;
import com.vaadin.flow.component.charts.model.PlotOptionsSpline;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.bus.BusEventType;
import io.skymind.pathmind.bus.PathmindBusEvent;
import io.skymind.pathmind.bus.data.ExperimentUpdateBusEvent;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.ui.utils.PushUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class ProjectChartPanel extends VerticalLayout
{
	private Chart chart = new Chart(ChartType.SPLINE);

	private Project project;

	public ProjectChartPanel(Flux<PathmindBusEvent> consumer)
	{
		setupChart();
		add(chart);

		subscribeToEventBus(consumer);
	}

	// TODO -> Project != null is due to how the components are generated with the eventBus.
	private void subscribeToEventBus(Flux<PathmindBusEvent> consumer) {
		consumer
			.filter(busEvent ->
					project != null)
			.filter(busEvent ->
					busEvent.isEventTypes(BusEventType.ProjectUpdate, BusEventType.ExperimentUpdate))
			.filter(busEvent ->
					isProjectOrExperimentUpdate(busEvent))
			.subscribe(busEvent ->
				updateChart(busEvent));

	}

	private void updateChart(PathmindBusEvent busEvent) {
		PushUtils.push(this, () -> {
			if(busEvent.isEventType(BusEventType.ProjectUpdate))
				update(project);
			else
				update(((ExperimentUpdateBusEvent)busEvent).getExperiment());
		});
	}

	private boolean isProjectOrExperimentUpdate(PathmindBusEvent busEvent)
	{
		if(busEvent.isEventType(BusEventType.ProjectUpdate))
			return project.getId() == busEvent.getEventDataId();
		return project.getExperiments().stream().anyMatch(experiment ->
				experiment.getId() == busEvent.getEventDataId());
	}

	private void setupChart() {
		chart.getConfiguration().setTitle("Project chart");
	}

	private void update(Experiment updatedExperiment)
	{
		// Replace if already an existing experiment.
		project.setExperiments(
				project.getExperiments().stream()
						.map(experiment -> experiment.getId() == updatedExperiment.getId() ? experiment : updatedExperiment)
						.collect(Collectors.toList()));

		// Add if it's a new experiment
		project.getExperiments().stream()
				.filter(experiment -> experiment.getId() != updatedExperiment.getId())
				.findAny().ifPresent(experiment -> project.getExperiments().add(experiment));

		// TODO -> In case there is more than one score that needs to be added.
//		getData().addData()

		update(project);
	}

	public void update(Project project) {
		this.project = project;
		chart.getConfiguration().setSeries(
				project.getExperiments().stream()
						.map(experiment -> new ListSeries(experiment.getName(), experiment.getScores()))
						.collect(Collectors.toList()));
		chart.drawChart();
	}
}
