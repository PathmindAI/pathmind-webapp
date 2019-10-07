package io.skymind.pathmind.ui.views.experiment.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.ListSeries;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.bus.PathmindBusEvent;
import io.skymind.pathmind.bus.utils.PolicyBusEventUtils;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.utils.PolicyUtils;
import io.skymind.pathmind.ui.components.FilterableComponent;
import io.skymind.pathmind.ui.utils.GuiUtils;
import io.skymind.pathmind.ui.utils.PushUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;


@Component
public class PolicyChartPanel extends VerticalLayout implements FilterableComponent<Policy>
{
	private Chart chart = new Chart(ChartType.SPLINE);

	private Label policyLabel;
	private Label scoreLabel;
	private Label modelLabel;
	private Label experimentLabel;
	private Label runTypeLabel;

	private Experiment experiment;
	private Policy policy;

	private Flux<PathmindBusEvent> consumer;

	public PolicyChartPanel(Flux<PathmindBusEvent> consumer)
	{
		this.consumer = consumer;

		setupLabels();
		setupChart();

		add(getStatusbar());
		add(chart);
	}

	private FormLayout getStatusbar() {
		FormLayout formLayout = GuiUtils.getTitleBarFullWidth(5);
		formLayout.addFormItem(policyLabel, "Policy");
		formLayout.addFormItem(scoreLabel, "Score");
		formLayout.addFormItem(modelLabel, "Model");
		formLayout.addFormItem(experimentLabel, "Experiment");
		formLayout.addFormItem(runTypeLabel, "Run Type");
		return formLayout;
	}

	// TODO -> CSS ->
	private void setupLabels() {
		policyLabel = new Label();
		scoreLabel = new Label();
		modelLabel = new Label();
		experimentLabel = new Label();
		runTypeLabel = new Label();
	}

	private void subscribeToEventBus(UI ui, Flux<PathmindBusEvent> consumer) {
		PolicyBusEventUtils.consumerBusEventBasedOnExperiment(
				consumer,
				() -> getExperiment(),
				updatedPolicy -> PushUtils.push(ui, () -> updateData(updatedPolicy)));
	}

	private void updateData(Policy updatedPolicy) {
		updatedPolicyChart(updatedPolicy);
		if(policy.getId() == updatedPolicy.getId())
			update(updatedPolicy);
	}

	private void updatedPolicyChart(Policy updatedPolicy)
	{
		// TODO -> Do we need to keep experiment up to date if there are new policies, etc.? I don't believe it's necessary
		// but we should confirm it.
		// During a training run, additional policies will be created, i.e. for a discovery run, the policies will
		// be created as they actually start training. -- pdubs, 20190927

		// We cannot add the last item because there is no guarantee that the updates are in sequence
		chart.getConfiguration().getSeries().stream()
				.filter(series -> series.getName().equals(updatedPolicy.getName()))
				.findAny()
				.ifPresentOrElse(
						series -> ((ListSeries) series).setData(updatedPolicy.getScores()),
						() -> chart.getConfiguration().addSeries(new ListSeries(updatedPolicy.getName(), updatedPolicy.getScores())));
		chart.drawChart();
	}

	private void setupChart() {
		chart.getConfiguration().setTitle("Reward Score");
	}

	public Experiment getExperiment() {
		return experiment;
	}

	public void update(Experiment experiment) {
		this.experiment = experiment;
		updateChart(experiment.getPolicies());
		subscribeToEventBus(UI.getCurrent(), consumer);
	}

	private void updateChart(List<Policy> policies) {
		policies.stream().forEach(policy ->
				chart.getConfiguration().addSeries(new ListSeries(policy.getName(), policy.getScores())));
		chart.drawChart();
	}

	// TODO -> https://github.com/SkymindIO/pathmind-webapp/issues/129 -> Does not seem possible yet: https://vaadin.com/forum/thread/17856633/is-it-possible-to-highlight-a-series-in-a-chart-programmatically
	public void highlightPolicy(Policy policy) {
	}

	public void update(Policy policy) {
		this.policy = policy;
		policyLabel.setText(PolicyUtils.getParsedPolicyName(policy));
		scoreLabel.setText(PolicyUtils.getLastScore(policy));
		modelLabel.setText(policy.getModel().getName());
		experimentLabel.setText(policy.getExperiment().getName());
		runTypeLabel.setText(policy.getRun().getRunTypeEnum().toString());
	}

	@Override
	public List<Policy> getData() {
		return experiment.getPolicies();
	}

	@Override
	public void setFilteredData(List<Policy> filteredPolicies) {
		remove(chart);
		this.chart = new Chart(ChartType.SPLINE);
		setupChart();
		add(chart);
		updateChart(filteredPolicies);
	}
}

