package io.skymind.pathmind.ui.views.experiment.components;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.ListSeries;
import com.vaadin.flow.component.charts.model.XAxis;
import com.vaadin.flow.component.charts.model.YAxis;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.bus.EventBus;
import io.skymind.pathmind.bus.events.PolicyUpdateBusEvent;
import io.skymind.pathmind.bus.subscribers.PolicyUpdateSubscriber;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.utils.PolicyUtils;
import io.skymind.pathmind.ui.utils.PushUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static io.skymind.pathmind.utils.ChartUtils.createActiveSeriesPlotOptions;
import static io.skymind.pathmind.utils.ChartUtils.createPassiveSeriesPlotOptions;

@Component
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
                            listSeries.setData(PolicyUtils.getMeanScores(updatedPolicy));
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
        chart.getConfiguration().getLegend().setEnabled(false);
        chart.getConfiguration().addxAxis(xAxis);
        chart.getConfiguration().addyAxis(yAxis);
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
                        .map(policy -> createListSeriesForPolicy(policy))
                        .collect(Collectors.toList()));
        chart.drawChart(true);
    }

    private void addPolicyToChart(Policy policy) {
        ListSeries listSeries = createListSeriesForPolicy(policy);
        chart.getConfiguration().addSeries(listSeries);
    }

    private ListSeries createListSeriesForPolicy(Policy policy) {
        ListSeries listSeries = new ListSeries(policy.getName(), PolicyUtils.getMeanScores(policy));
        listSeries.setId(Long.toString(policy.getId()));
        // Insert the series as passive by default, they will be highlighted after best policy calculation
        listSeries.setPlotOptions(createPassiveSeriesPlotOptions());
        return listSeries;
    }

    // TODO -> https://github.com/SkymindIO/pathmind-webapp/issues/129 -> Does not seem possible yet: https://vaadin.com/forum/thread/17856633/is-it-possible-to-highlight-a-series-in-a-chart-programmatically
    public void highlightPolicy(Policy policy) {
    	chart.getConfiguration().getSeries().stream().forEach(series -> {
    		if (series.getId().equals(Long.toString(policy.getId()))) {
    			series.setPlotOptions(createActiveSeriesPlotOptions());
    		} else {
    			series.setPlotOptions(createPassiveSeriesPlotOptions());
    		}
    		ListSeries.class.cast(series).updateSeries();
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
            if (event.getPolicy().getExperiment().getId() != experiment.getId())
                return;
            PushUtils.push(this, () -> updatedPolicyChart(event.getPolicy()));
        }
    }

    @Override
    public boolean filterBusEvent(PolicyUpdateBusEvent event) {
        return experiment.getId() == event.getPolicy().getExperiment().getId();
    }
}

