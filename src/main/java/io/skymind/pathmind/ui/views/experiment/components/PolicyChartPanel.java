package io.skymind.pathmind.ui.views.experiment.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.ListSeries;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.bus.PathmindBusEvent;
import io.skymind.pathmind.bus.utils.PolicyBusEventUtils;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.ui.utils.NotificationUtils;
import io.skymind.pathmind.ui.utils.PushUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;


@Component
public class PolicyChartPanel extends VerticalLayout
{
	private Chart chart = new Chart(ChartType.SPLINE);

	private Experiment experiment;
	private Policy policy;

	private UI ui;

	public PolicyChartPanel(Flux<PathmindBusEvent> consumer)
	{
		this.ui = UI.getCurrent();

		setupChart();
		add(chart);

		subscribeToEventBus(consumer);
	}

	private void subscribeToEventBus(Flux<PathmindBusEvent> consumer) {
		PolicyBusEventUtils.consumerBusEventBasedOnExperiment(
				consumer,
				() -> getExperiment(),
				updatedPolicy -> PushUtils.push(ui, () -> updatedPolicyChart(updatedPolicy)));
	}

	private void updatedPolicyChart(Policy updatedPolicy) {
		chart.getConfiguration().getSeries().stream()
				.filter(series -> series.getName().equals(updatedPolicy.getName()))
				.findAny().ifPresent(series -> {
						// We cannot add the last item because there is no guarantee that the updates are in sequence
						((ListSeries) series).setData(updatedPolicy.getScores());
						chart.drawChart();
				});
	}

	private void setupChart() {
		chart.getConfiguration().setTitle("Reward Score");
	}

	public Experiment getExperiment() {
		return experiment;
	}

	public void update(Experiment experiment) {
		this.experiment = experiment;
		experiment.getPolicies().stream().forEach(policy ->
				chart.getConfiguration().addSeries(new ListSeries(policy.getName(), policy.getScores())));
		chart.drawChart();
	}

	public void highlightPolicy(Policy policy)
	{
		// TODO -> Steph -> Implement
//		NotificationUtils.showTodoNotification("Highlight chart line for policy");
//		this.policy = policy;
//		chart.getConfiguration().setSeries(new ListSeries(policy.getScores()));
//		chart.drawChart();
	}
}

