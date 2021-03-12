package io.skymind.pathmind.webapp.ui.views.experiment.components.chart;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.shared.data.Experiment;

public class PolicyChartPanel extends VerticalLayout {
    private Object experimentLock = new Object();
    private PolicyChart chart = new PolicyChart();
    private Experiment experiment;

    public PolicyChartPanel() {
        add(chart);
        setPadding(false);
        setSpacing(false);
    }

    public void setExperiment(Experiment newExperiment) {
        synchronized (experimentLock) {
            this.experiment = newExperiment;
            chart.setPolicyChart(experiment);
            redrawChart();
        }
    }

    public void redrawChart() {
        chart.redraw();
    }
}