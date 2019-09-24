package io.skymind.pathmind.ui.views.experiment.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.ListSeries;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.bus.PathmindBusEvent;
import io.skymind.pathmind.bus.utils.PolicyBusEventUtils;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.ui.utils.PushUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;


@Component
public class PolicyChartPanel extends VerticalLayout
{
	private Chart chart = new Chart(ChartType.SPLINE);

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
		PolicyBusEventUtils.consumerBusEvent(
				consumer,
				() -> getPolicy(),
				updatedPolicy -> PushUtils.push(ui, () -> update(updatedPolicy)));
	}

	private void setupChart() {
		chart.getConfiguration().setTitle("Reward Score");
	}

	private Policy getPolicy() {
		return policy;
	}

	public void update(Policy policy)
	{
		this.policy = policy;

		chart.getConfiguration().setSeries(new ListSeries(policy.getScores()));
//				FakeDataUtils.generateFakePolicyChartScores()));
		chart.drawChart();
	}
}

