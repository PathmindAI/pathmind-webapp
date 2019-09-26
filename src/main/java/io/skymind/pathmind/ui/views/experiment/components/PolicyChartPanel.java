package io.skymind.pathmind.ui.views.experiment.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.ListSeries;
import com.vaadin.flow.component.charts.model.Series;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.bus.PathmindBusEvent;
import io.skymind.pathmind.bus.utils.PolicyBusEventUtils;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.ui.utils.NotificationUtils;
import io.skymind.pathmind.ui.utils.PushUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;


@Component
public class PolicyChartPanel extends VerticalLayout
{
	private Chart chart = new Chart(ChartType.SPLINE);

	private Experiment experiment;

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

	private void updatedPolicyChart(Policy updatedPolicy)
	{
		// TODO -> Do we need to keep experiment up to date if there are new policies, etc.? I don't believe it's necessary
		// but we should confirm it.

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

	public void filter(List<Policy> filteredPolicies) {
		remove(chart);
		this.chart = new Chart(ChartType.SPLINE);
		setupChart();
		add(chart);
		updateChart(filteredPolicies);
	}

	public void update(Experiment experiment) {
		this.experiment = experiment;
		updateChart(experiment.getPolicies());
	}

	private void updateChart(List<Policy> policies) {
		policies.stream().forEach(policy ->
				chart.getConfiguration().addSeries(new ListSeries(policy.getName(), policy.getScores())));
		chart.drawChart();
	}

	// TODO -> Does not seem possible yet: https://vaadin.com/forum/thread/17856633/is-it-possible-to-highlight-a-series-in-a-chart-programmatically
	public void highlightPolicy(Policy policy)
	{
	}
}

