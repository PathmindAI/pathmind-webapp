package io.skymind.pathmind.ui.views.experiment.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.ListSeries;
import com.vaadin.flow.component.charts.model.XAxis;
import com.vaadin.flow.component.charts.model.YAxis;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.bus.PathmindBusEvent;
import io.skymind.pathmind.bus.utils.PolicyBusEventUtils;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.ui.components.FilterableComponent;
import io.skymind.pathmind.ui.utils.PushUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;


@Component
public class PolicyChartPanel extends VerticalLayout implements FilterableComponent<Policy> {
    private Chart chart = new Chart(ChartType.SPLINE);

    private Experiment experiment;
    private Policy policy;

    private Flux<PathmindBusEvent> consumer;

    public PolicyChartPanel(Flux<PathmindBusEvent> consumer) {
        this.consumer = consumer;

        setupChart();
        add(chart);
    }

    private void subscribeToEventBus(UI ui, Flux<PathmindBusEvent> consumer) {
        PolicyBusEventUtils.consumerBusEventBasedOnExperiment(
                consumer,
                () -> getExperiment(),
                updatedPolicy -> PushUtils.push(ui, () -> updateData(updatedPolicy)));
    }

    private void updateData(Policy updatedPolicy) {
        updatedPolicyChart(updatedPolicy);
        // If it's an initial run the policy may be null.
        if (policy != null && policy.getId() == updatedPolicy.getId())
            update(updatedPolicy);
    }

    private void updatedPolicyChart(Policy updatedPolicy) {
        // TODO -> Do we need to keep experiment up to date if there are new policies, etc.? I don't believe it's necessary
        // but we should confirm it.
        // During a training run, additional policies will be created, i.e. for a discovery run, the policies will
        // be created as they actually start training. -- pdubs, 20190927

        // We cannot add the last item because there is no guarantee that the updates are in sequence
        chart.getConfiguration().getSeries().stream()
                .filter(series -> series.getId().equals(Long.toString(updatedPolicy.getId())))
                .findAny()
                .ifPresentOrElse(
                        series -> {
                            ListSeries listSeries = ((ListSeries) series);
                            listSeries.setData(updatedPolicy.getScores());

                            if (!series.getName().equals(updatedPolicy.getName())) {
                                listSeries.setName(updatedPolicy.getName());
                            }
                        },
                        () -> addPolicyToChart(updatedPolicy));
        chart.drawChart();
    }

    private void setupChart() {
        XAxis xAxis = new XAxis();
        xAxis.setTitle("Iterations");

        YAxis yAxis = new YAxis();
        yAxis.setTitle("Mean Reward Score");

        chart.getConfiguration().setTitle("Reward Score");
        chart.getConfiguration().addxAxis(xAxis);
        chart.getConfiguration().addyAxis(yAxis);
        chart.setSizeFull();
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
        policies.stream().forEach(policy -> updatedPolicyChart(policy));
        chart.drawChart();
    }

    private void addPolicyToChart(Policy policy) {
        ListSeries listSeries = new ListSeries(policy.getName(), policy.getScores());
        listSeries.setId(Long.toString(policy.getId()));
        chart.getConfiguration().addSeries(listSeries);
    }

    // TODO -> https://github.com/SkymindIO/pathmind-webapp/issues/129 -> Does not seem possible yet: https://vaadin.com/forum/thread/17856633/is-it-possible-to-highlight-a-series-in-a-chart-programmatically
    public void highlightPolicy(Policy policy) {
    }

    public void update(Policy policy) {
        this.policy = policy;
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

