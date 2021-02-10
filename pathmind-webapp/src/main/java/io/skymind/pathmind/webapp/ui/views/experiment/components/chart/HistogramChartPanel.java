package io.skymind.pathmind.webapp.ui.views.experiment.components.chart;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.shared.utils.PathmindNumberUtils;
import io.skymind.pathmind.shared.utils.PolicyUtils;
import io.skymind.pathmind.webapp.ui.components.atoms.HistogramChart;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.components.ExperimentComponent;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class HistogramChartPanel extends VerticalLayout implements ExperimentComponent {
    private HistogramChart chart = new HistogramChart();

    private HorizontalLayout metricMeanValue = new HorizontalLayout();

    private Experiment experiment;

    public HistogramChartPanel() {
        metricMeanValue.addClassName("histogram-chart-mean");
        add(metricMeanValue, chart);
        setPadding(false);
        setSpacing(false);
    }

    public void updateChart() {
        Optional<Policy> opt = PolicyUtils.selectBestPolicy(this.experiment);
        metricMeanValue.removeAll();
        if (opt.isPresent()) {
            List<RewardVariable> selectedRewardVars = experiment.getSelectedRewardVariables();
            if (!selectedRewardVars.isEmpty()) {
                final Policy bestPolicy = opt.get();
                chart.setHistogramData(new ArrayList<>(selectedRewardVars), bestPolicy, true);
                List<String> uncertainty = bestPolicy.getUncertainty();
                selectedRewardVars.forEach(rewardVar -> {
                    int arrayIndex = rewardVar.getArrayIndex();
                    String meanValue = uncertainty.size() > arrayIndex
                            ? uncertainty.get(arrayIndex)
                            : PathmindNumberUtils.formatNumber(bestPolicy.getSimulationMetrics().get(arrayIndex));
                    Span colorBox = new Span();
                    colorBox.addClassName("color-box");
                    colorBox.addClassName("variable-color-"+ arrayIndex % 10);
                    metricMeanValue.add(
                        WrapperUtils.wrapVerticalWithNoPaddingOrSpacingAndWidthAuto(
                            new HorizontalLayout(
                                colorBox,
                                new Span(rewardVar.getName())
                            ),
                            new Span("mean: "+meanValue)
                        )
                    );
                });
            }
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
