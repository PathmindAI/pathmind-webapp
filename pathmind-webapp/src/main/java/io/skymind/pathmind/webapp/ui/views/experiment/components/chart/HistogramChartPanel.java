package io.skymind.pathmind.webapp.ui.views.experiment.components.chart;

import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.shared.utils.PolicyUtils;
import io.skymind.pathmind.webapp.ui.components.atoms.HistogramChart;
import io.skymind.pathmind.webapp.ui.views.experiment.components.ExperimentComponent;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class HistogramChartPanel extends VerticalLayout implements ExperimentComponent {
    private HistogramChart chart = new HistogramChart();

    private Paragraph metricMeanValue = new Paragraph();

    private Experiment experiment;

    public HistogramChartPanel() {
        metricMeanValue.addClassName("histogram-chart-mean");
        add(metricMeanValue, chart);
        setPadding(false);
        setSpacing(false);
    }

    public void updateChart() {
        Optional<Policy> opt = PolicyUtils.selectBestPolicy(this.experiment);
        if (opt.isPresent()) {
            List<RewardVariable> selectedRewardVars = experiment.getSelectedRewardVariables();
            String metricMeanValueText = "";
            StringBuilder sb = new StringBuilder();
            chart.setHistogramData(new ArrayList<>(selectedRewardVars), opt.get(), true);
            selectedRewardVars.forEach(rewardVar -> {
                if (sb.length() > 0) {
                    sb.append("; ");
                }
                sb.append(rewardVar.getName()+" mean: "+opt.get().getUncertainty().get(rewardVar.getArrayIndex()));
            });
            metricMeanValueText = sb.toString();
            metricMeanValue.setText(metricMeanValueText);
        } else {
            chart.setChartEmpty();
            metricMeanValue.setText("");
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
