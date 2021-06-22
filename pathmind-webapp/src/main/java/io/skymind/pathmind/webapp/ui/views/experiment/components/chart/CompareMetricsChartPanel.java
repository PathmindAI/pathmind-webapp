package io.skymind.pathmind.webapp.ui.views.experiment.components.chart;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.ui.views.experiment.components.ExperimentComponent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CompareMetricsChartPanel extends VerticalLayout implements ExperimentComponent {

    private CompareMetricsChart chart = new CompareMetricsChart();

    private Experiment experiment;

    public CompareMetricsChartPanel() {
        add(chart);
        setPadding(false);
        setSpacing(false);
    }

    public void redrawChart() {
        chart.redraw();
    }

    public Experiment getExperiment() {
        return experiment;
    }

    public long getExperimentId() {
        return experiment.getId();
    }

    public void updateChart() {
        // Update chart data
        chart.setCompareMetricsChart(experiment.getSelectedRewardVariables(), experiment.getBestPolicy());
        redrawChart();
    }

    @Override
    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
        updateChart();
    }
}