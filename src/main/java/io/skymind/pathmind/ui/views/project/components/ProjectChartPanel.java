package io.skymind.pathmind.ui.views.project.components;

import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.ListSeries;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.bus.BusEventType;
import io.skymind.pathmind.bus.PathmindBusEvent;
import io.skymind.pathmind.bus.data.ExperimentUpdateBusEvent;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.ui.utils.PushUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

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
			.filter(busEvent -> project != null)
//			.filter(busEvent -> busEvent.isEventTypes(BusEventType.ProjectUpdate, BusEventType.ExperimentUpdate))
			.filter(busEvent -> busEvent.isEventType(BusEventType.ExperimentUpdate))
			.filter(busEvent -> ((ExperimentUpdateBusEvent)busEvent).isForProject(project))
			.subscribe(busEvent ->
				updateChart(busEvent));
	}

	private void updateChart(PathmindBusEvent busEvent) {
		PushUtils.push(this, () -> {
//			if(busEvent.isEventType(BusEventType.ProjectUpdate))
//				update(project);
//			else
				update(((ExperimentUpdateBusEvent)busEvent).getExperiment());
		});
	}

//	private boolean isEventForProject(PathmindBusEvent busEvent)
//	{
//		// It has to be on the parent project because it's possible that it's a brand new experiment that didn't exist before in the project.
////		if(busEvent.isEventType(BusEventType.ProjectUpdate))
////			return project.getId() == busEvent.getEventDataId();
//
//		if(project.getId() == ((ExperimentUpdateBusEvent)busEvent).getProjectId())
//
////		return project.getExperiments().stream().anyMatch(experiment ->
////				experiment.getProjectId() == ((ExperimentUpdateBusEvent)busEvent).getExperiment().getProjectId());
//	}

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
