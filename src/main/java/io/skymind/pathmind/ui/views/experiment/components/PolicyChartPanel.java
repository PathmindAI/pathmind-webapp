package io.skymind.pathmind.ui.views.experiment.components;

import static io.skymind.pathmind.utils.ChartUtils.createActiveSeriesPlotOptions;
import static io.skymind.pathmind.utils.ChartUtils.createPassiveSeriesPlotOptions;

import java.util.List;

import org.springframework.stereotype.Component;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.XAxis;
import com.vaadin.flow.component.charts.model.YAxis;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.skymind.pathmind.bus.EventBus;
import io.skymind.pathmind.bus.events.PolicyUpdateBusEvent;
import io.skymind.pathmind.bus.subscribers.PolicyUpdateSubscriber;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.ui.components.FilterableComponent;
import io.skymind.pathmind.ui.utils.PushUtils;
import io.skymind.pathmind.utils.ChartUtils;

@Component
public class PolicyChartPanel extends VerticalLayout implements FilterableComponent<Policy>, PolicyUpdateSubscriber {
    private Chart chart = new Chart(ChartType.SPLINE);

    private Experiment experiment;
    private Policy policy;

    public PolicyChartPanel() {
        setupChart();
        add(chart);
    }

    private void updateData(Policy updatedPolicy) {
        updatedPolicyChart(updatedPolicy);
        // If it's an initial run the policy may be null.
        if (policy != null && policy.getId() == updatedPolicy.getId())
            init(updatedPolicy);
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
                            DataSeries dataSeries = (DataSeries) series;
                            dataSeries.setData(ChartUtils.getRewardScoreSeriesItems(updatedPolicy));
                            if (!series.getName().equals(updatedPolicy.getName())) {
                            	dataSeries.setName(updatedPolicy.getName());
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
        chart.getConfiguration().getLegend().setEnabled(false);
        chart.getConfiguration().addxAxis(xAxis);
        chart.getConfiguration().addyAxis(yAxis);
        chart.getConfiguration().getTooltip().setFormatter(
                "return "
                + "'<b>Iteration#:</b>' + this.x + '<br/>' + "
                + "'<b>Mean Reward:</b>' + this.y.toFixed(Math.abs(this.y) > 1 ? 1 : 6) + '<br/>' + "
                + "(this.point.episodeCount != null ? '<b>Episode Count:</b>' + this.point.episodeCount : '')");
        chart.setSizeFull();
    }

    public void init(Experiment experiment) {
        this.experiment = experiment;
        updateChart(experiment.getPolicies());
    }

    private void updateChart(List<Policy> policies) {
        policies.stream().forEach(policy -> updatedPolicyChart(policy));
        chart.drawChart();
    }

    private void addPolicyToChart(Policy policy) {
        DataSeries dataSeries = new DataSeries(policy.getName());
        dataSeries.setData(ChartUtils.getRewardScoreSeriesItems(policy));
        dataSeries.setId(Long.toString(policy.getId()));
        // Insert the series as passive by default, they will be highlighted after best policy calculation
        dataSeries.setPlotOptions(createPassiveSeriesPlotOptions());
        chart.getConfiguration().addSeries(dataSeries);
    }

    // TODO -> https://github.com/SkymindIO/pathmind-webapp/issues/129 -> Does not seem possible yet: https://vaadin.com/forum/thread/17856633/is-it-possible-to-highlight-a-series-in-a-chart-programmatically
    public void highlightPolicy(Policy policy) {
    	chart.getConfiguration().getSeries().stream().forEach(series -> {
    		if (series.getId().equals(Long.toString(policy.getId()))) {
    			series.setPlotOptions(createActiveSeriesPlotOptions());
    		} else {
    			series.setPlotOptions(createPassiveSeriesPlotOptions());
    		}
    		DataSeries.class.cast(series).updateSeries();
    	});
    }

    public void init(Policy policy) {
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

    @Override
    protected void onDetach(DetachEvent event) {
        EventBus.unsubscribe(this);
    }

    @Override
    protected void onAttach(AttachEvent event) {
        EventBus.subscribe(this);
    }

    @Override
    public void handleBusEvent(PolicyUpdateBusEvent event) {
        PushUtils.push(this, () -> updateData(event.getPolicy()));
    }

    @Override
    public boolean filterBusEvent(PolicyUpdateBusEvent event) {
        return experiment.getId() == event.getPolicy().getExperiment().getId();
    }
}

