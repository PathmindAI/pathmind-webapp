package io.skymind.pathmind.webapp.ui.views.experiment.components.simulationMetrics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.webapp.ui.components.atoms.HistogramChart;
import io.skymind.pathmind.webapp.ui.components.rewardVariables.RewardVariablesTable;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.components.ExperimentComponent;
import io.skymind.pathmind.webapp.ui.views.experiment.components.SparklineChart;
import lombok.extern.slf4j.Slf4j;

import static io.skymind.pathmind.shared.utils.PathmindNumberUtils.SPACED_PLUS_MINUS;

@Slf4j
public class SimulationMetricsPanel extends HorizontalLayout implements ExperimentComponent {

    private VerticalLayout metricsWrapper;
    private VerticalLayout sparklinesWrapper;
    private VerticalLayout histogramsWrapper;

    private RewardVariablesTable rewardVariablesTable;

    private Experiment experiment;
    private List<Span> metricSpans = new ArrayList<>();
    private List<SparklineChart> sparklineCharts = new ArrayList<>();
    private List<HistogramChart> histogramCharts = new ArrayList<>();

    public SimulationMetricsPanel(ExperimentView experimentView) {

        super();

        setSpacing(false);
        addClassName("simulation-metrics-table-wrapper");

        rewardVariablesTable = new RewardVariablesTable(experimentView);
        rewardVariablesTable.setCodeEditorMode();
        rewardVariablesTable.setCompactMode();
        rewardVariablesTable.setSelectMode();
        rewardVariablesTable.setSizeFull();

        add(rewardVariablesTable);
    }

    private void createSimulationMetricsSpansAndSparklines() {
        metricsWrapper = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing();
        metricsWrapper.addClassName("metrics-wrapper");
        Div metricsHeader = new Div(new Span("Value"));
        metricsHeader.addClassName("header");
        metricsWrapper.add(metricsHeader);

        sparklinesWrapper = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing();
        sparklinesWrapper.addClassName("sparklines-wrapper");
        Div sparklineHeader = new Div(new Span("Overview"));
        sparklineHeader.addClassName("header");
        sparklinesWrapper.add(sparklineHeader);

        histogramsWrapper = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing();
        histogramsWrapper.addClassName("histograms-wrapper");
        Div histogramHeader = new Div(new Span("Spread"));
        histogramHeader.addClassName("header");
        histogramsWrapper.add(histogramHeader);

        IntStream.range(0, experiment.getRewardVariables().size())
                .forEach(index -> {
                    Span metricSpan = new Span();
                    SparklineChart sparkline = new SparklineChart();
                    HistogramChart histogram = new HistogramChart();
                    metricSpans.add(metricSpan);
                    sparklineCharts.add(sparkline);
                    histogramCharts.add(histogram);

                    VerticalLayout sparkLineWrapper = WrapperUtils.wrapVerticalWithNoPaddingOrSpacingAndWidthAuto(
                            sparkline
                    );
                    sparkLineWrapper.addClassName("sparkline");

                    VerticalLayout histogramWrapper = WrapperUtils.wrapVerticalWithNoPaddingOrSpacingAndWidthAuto(
                            histogram
                    );
                    histogramWrapper.addClassName("histogram");

                    metricsWrapper.add(metricSpan);
                    sparklinesWrapper.add(sparkLineWrapper);
                    histogramsWrapper.add(histogramWrapper);
                });

        showMetricValuesAndSparklines(false);
    }

    private void showMetricValuesAndSparklines(Boolean show) {
        metricsWrapper.setVisible(show);
        sparklinesWrapper.setVisible(show);
        if (show) {
            boolean isBestPolicyUncertaintyEmpty = !Optional.ofNullable(experiment)
                    .map(Experiment::getBestPolicy)
                    .map(Policy::getMetricDisplayValues)
                    .map(List::stream)
                    .flatMap(stream -> stream.filter(s -> s.contains(SPACED_PLUS_MINUS)).findAny())
                    .isPresent();
            histogramsWrapper.setVisible(!isBestPolicyUncertaintyEmpty);
        } else {
            histogramsWrapper.setVisible(false);
        }
    }

    public Experiment getExperiment() {
        return experiment;
    }

    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;

        // If it hasn't been rendered yet then render the simulation metrics components as they are dependent on the rewardvariables of the experiment.
        if (metricsWrapper == null) {
            createSimulationMetricsSpansAndSparklines();
            add(metricsWrapper, sparklinesWrapper, histogramsWrapper);
        }

        // TODO -> REFACTOR -> Why are we resetting the reward variables here? Why are there are two RewardVariableTables?
        rewardVariablesTable.setExperiment(experiment);
        updateSimulationMetrics();
    }

    public void updateSimulationMetrics() {

        Policy bestPolicy = experiment.getBestPolicy();

        if (bestPolicy == null || bestPolicy.getMetricDisplayValues() == null || bestPolicy.getMetricDisplayValues().isEmpty()) {
            showMetricValuesAndSparklines(false);
            return;
        }

        IntStream.range(0, bestPolicy.getMetricDisplayValues().size())
                .forEach(index -> {
                    Map<Integer, Double> sparklineData = bestPolicy.getSparklinesData().get(index);
                    RewardVariable rewardVariable = experiment.getRewardVariables().get(index);

                    sparklineCharts.get(index).setSparkLine(sparklineData, rewardVariable, false, index);
                    if (bestPolicy.getMetricDisplayValues().size() > index) {
                        metricSpans.get(index).setText(bestPolicy.getMetricDisplayValues().get(index));
                    }
                    List<RewardVariable> histogramRewardVarList = new ArrayList<>();
                    histogramRewardVarList.add(rewardVariable);
                    histogramCharts.get(index).setHistogramData(histogramRewardVarList, bestPolicy, false);
                });

        showMetricValuesAndSparklines(true);
    }
}
