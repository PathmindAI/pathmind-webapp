package io.skymind.pathmind.webapp.ui.views.experiment.components;

import static io.skymind.pathmind.webapp.utils.ChartUtils.createActiveSeriesPlotOptions;
import static io.skymind.pathmind.webapp.utils.ChartUtils.createPassiveSeriesPlotOptions;

import java.util.List;

import io.skymind.pathmind.shared.bus.events.PolicyUpdateBusEvent;
import io.skymind.pathmind.shared.bus.subscribers.PolicyUpdateSubscriber;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.webapp.ui.utils.PushUtils;
import io.skymind.pathmind.webapp.utils.ChartUtils;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.XAxis;
import com.vaadin.flow.component.charts.model.YAxis;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.skymind.pathmind.shared.bus.EventBus;


import java.util.stream.Collectors;

public class PolicyChartPanel extends VerticalLayout implements PolicyUpdateSubscriber
{
    private Object experimentLock = new Object();

    private Chart chart = new Chart(ChartType.SPLINE);

    private Experiment experiment;

    public PolicyChartPanel() {
        setupChart();
        add(chart);
        addClassName("policy-chart-panel");
    }

    private void updatedPolicyChart(List<Policy> updatedPolicies) {
        // TODO -> Do we need to keep experiment up to date if there are new policies, etc.? I don't believe it's necessary
        // but we should confirm it.
        // During a training run, additional policies will be created, i.e. for a discovery run, the policies will
        // be created as they actually start training. -- pdubs, 20190927

        // We cannot add the last item because there is no guarantee that the updates are in sequence
    	updatedPolicies.forEach(updatedPolicy -> {
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
    	});
        chart.drawChart();
    }

    private void setupChart() {
        XAxis xAxis = new XAxis();
        xAxis.setTitle("Iterations");
        xAxis.setAllowDecimals(false);

        YAxis yAxis = new YAxis();
        yAxis.setTitle("Mean Reward Score over All Episodes");

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

    public void setExperiment(Experiment experiment) {
        synchronized (experimentLock) {
            this.experiment = experiment;
            updateChart(experiment.getPolicies());
        }
    }

    private void updateChart(List<Policy> policies) {
        // As we cannot clear the chart's ListSeries we need to do things a bit differently.
        chart.getConfiguration().setSeries(
                policies.stream()
                        .map(policy -> createDataSeriesForPolicy(policy))
                        .collect(Collectors.toList()));
        chart.drawChart(true);
    }

    private void addPolicyToChart(Policy policy) {
        DataSeries dataSeries = createDataSeriesForPolicy(policy);
        chart.getConfiguration().addSeries(dataSeries);
    }
    
    private DataSeries createDataSeriesForPolicy(Policy policy) {
    	DataSeries dataSeries = new DataSeries(policy.getName());
        dataSeries.setData(ChartUtils.getRewardScoreSeriesItems(policy));
        dataSeries.setId(Long.toString(policy.getId()));
        // Insert the series as passive by default, they will be highlighted after best policy calculation
        dataSeries.setPlotOptions(createPassiveSeriesPlotOptions());
        return dataSeries;
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
        synchronized (experimentLock) {
            // We need to check after the lock is acquired as changing experiments can take up to several seconds.
            if (event.getExperimentId() != experiment.getId())
                return;
            PushUtils.push(this, () -> updatedPolicyChart(event.getPolicies()));
        }
    }

    @Override
    public boolean filterBusEvent(PolicyUpdateBusEvent event) {
        return experiment.getId() == event.getExperimentId();
    }

    @Override
    public boolean isAttached() {
        return getUI().isPresent();
    }
}

