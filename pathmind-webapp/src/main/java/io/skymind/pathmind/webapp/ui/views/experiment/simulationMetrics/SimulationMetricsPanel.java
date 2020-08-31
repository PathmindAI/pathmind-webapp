package io.skymind.pathmind.webapp.ui.views.experiment.simulationMetrics;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Metrics;
import io.skymind.pathmind.shared.data.MetricsRaw;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.utils.MetricsRawUtils;
import io.skymind.pathmind.shared.utils.PathmindNumberUtils;
import io.skymind.pathmind.webapp.ui.components.SparkLine;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.components.SimulationMetricsInfoLink;
import io.skymind.pathmind.webapp.ui.views.model.components.RewardVariablesTable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SimulationMetricsPanel extends HorizontalLayout {

    private VerticalLayout metricsWrapper;
    private VerticalLayout sparklinesWrapper;

    private RewardVariablesTable rewardVariablesTable;

    private boolean showSimulationMetrics;

    private List<Double> simulationMetrics = new ArrayList<>();
    private List<double[]> sparklinesData = new ArrayList<>();
    private List<String> uncertainty = new ArrayList<>();

    private Experiment experiment;

    public SimulationMetricsPanel(Experiment experiment, boolean showSimulationMetrics) {

        super();
        this.experiment = experiment;
        this.showSimulationMetrics = showSimulationMetrics;

        setSpacing(false);
        addClassName("simulation-metrics-table-wrapper");

        rewardVariablesTable = new RewardVariablesTable();
        rewardVariablesTable.setCodeEditorMode();
        rewardVariablesTable.setCompactMode();
        rewardVariablesTable.setSizeFull();
        add(rewardVariablesTable);

        if (showSimulationMetrics) {
            metricsWrapper = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing();
            metricsWrapper.addClassName("metrics-wrapper");
            sparklinesWrapper = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing();
            sparklinesWrapper.addClassName("sparklines-wrapper");

            updateSimulationMetrics(null);
            add(metricsWrapper, sparklinesWrapper);
        }
    }

    public Experiment getExperiment() {
        return experiment;
    }

    public boolean isShowSimulationMetrics() {
        return showSimulationMetrics;
    }

    public void updateSimulationMetrics(Policy policy) {

        metricsWrapper.removeAll();
        sparklinesWrapper.removeAll();

        // REFACTOR -> This code should NOT be in the GUI code but rather the eventbus subscriber. Coming in the next commit.
        updateSimulationMetricsData(policy);

        if (simulationMetrics.size() > 0) {
            Div metricsHeader = new Div(new Span("Value"), new SimulationMetricsInfoLink());
            metricsHeader.addClassName("header");
            metricsWrapper.add(metricsHeader);

            Div sparklineHeader = new Div(new Span("Overview"), new SimulationMetricsInfoLink());
            sparklineHeader.addClassName("header");
            sparklinesWrapper.add(sparklineHeader);
        }

        IntStream.range(0, simulationMetrics.size())
                .forEach(idx -> {
                    SparkLine sparkLine = new SparkLine();
                    sparkLine.setSparkLine(sparklinesData.get(idx), idx);
                    sparklinesWrapper.add(sparkLine);
                    if (uncertainty != null && !uncertainty.isEmpty()) {
                        metricsWrapper.add(new Span(uncertainty.get(idx)));
                    } else {
                        metricsWrapper.add(new Span(PathmindNumberUtils.formatNumber(simulationMetrics.get(idx))));
                    }
                });
    }

    /**
     * REFACTOR -> This code should NOT be in the GUI code nor in the eventbus subscriber. Coming in the next commit.
      */
    private void updateSimulationMetricsData(Policy policy) {
        List<Metrics> metricsList = policy == null ? null : policy.getMetrics();
        sparklinesData.clear();
        simulationMetrics.clear();
        uncertainty.clear();

        if (metricsList != null && metricsList.size() > 0) {
            // set the last metrics
            Metrics lastMetrics = metricsList.get(metricsList.size() - 1);
            lastMetrics.getMetricsThisIter().stream()
                    .forEach(metricsThisIter -> simulationMetrics.add(metricsThisIter.getMean()));

            // index, metrics list
            Map<Integer, List<Double>> sparkLineMap = new HashMap<>();
            metricsList.stream().forEach(metrics ->
                    metrics.getMetricsThisIter().forEach(mIter -> {
                        int index = mIter.getIndex();

                        List<Double> data = sparkLineMap.containsKey(index) ? sparkLineMap.get(index) : new ArrayList<>();
                        data.add(mIter.getMean());
                        sparkLineMap.put(index, data);
                    })
            );

            // convert List<Double> to double[] because sparLine needs an array of primitive types
            sparkLineMap.entrySet().stream()
                    .map(e -> e.getValue().stream().mapToDouble(Double::doubleValue).toArray())
                    .forEach(arr -> sparklinesData.add(arr));
        }

        List<MetricsRaw> metricsRawList = policy == null ? null : policy.getMetricsRaws();
        if (metricsRawList != null && metricsRawList.size() > 0) {
            Collections.sort(metricsRawList, Comparator.comparingInt(MetricsRaw::getIteration));
            Map<Integer, List<Double>> uncertaintyMap = MetricsRawUtils.toIndexAndMetricRawData(metricsRawList);

            uncertainty = uncertaintyMap.values().stream()
                    .map(list -> PathmindNumberUtils.calculateUncertainty(list))
                    .collect(Collectors.toList());
        }
    }
}
