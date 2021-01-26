package io.skymind.pathmind.webapp.ui.views.experiment.components.chart;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.utils.PolicyUtils;
import io.skymind.pathmind.webapp.ui.components.atoms.HistogramChart;
import io.skymind.pathmind.webapp.ui.views.experiment.components.ExperimentComponent;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Optional;

@Slf4j
public class HistogramChartPanel extends VerticalLayout implements ExperimentComponent {
    private HistogramChart chart = new HistogramChart();

    private Experiment experiment;

    public HistogramChartPanel() {
        add(chart);
        setPadding(false);
        setSpacing(false);
    }

    public void updateChart() {
        Optional<Policy> opt = PolicyUtils.selectBestPolicy(this.experiment);
        if (opt.isPresent()) {
            chart.setHistogramData(new ArrayList<>(experiment.getSelectedRewardVariables()), opt.get());
        } else {
            chart.setChartEmpty();
        }

        redrawChart();
    }

    @Override
    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
        updateChart();
    }

    public void redrawChart() {
        chart.redraw();
    }
}
