package io.skymind.pathmind.webapp.ui.views.experiment.components.chart;

import java.util.Optional;
import java.util.function.Supplier;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.shared.data.Experiment;

public class PolicyChartPanel extends VerticalLayout {
    private Object experimentLock = new Object();

    private PolicyChart chart = new PolicyChart();

    private Experiment experiment;

    private Supplier<Optional<UI>> getUISupplier;

    public PolicyChartPanel(Supplier<Optional<UI>> getUISupplier) {
        this.getUISupplier = getUISupplier;
        add(chart);
        setPadding(false);
        setSpacing(false);
    }

    public void setExperiment(Experiment newExperiment) {
        synchronized (experimentLock) {
            this.experiment = newExperiment;
            updateChart();
        }
    }

    public void updateChart() {
        chart.setPolicyChart(experiment);
        redrawChart();
    }

    public void redrawChart() {
        chart.redraw();
    }

    public Object getExperimentLock() {
        return experimentLock;
    }

    public Experiment getExperiment() {
        return experiment;
    }

    public long getExperimentId() {
        return experiment.getId();
    }
}