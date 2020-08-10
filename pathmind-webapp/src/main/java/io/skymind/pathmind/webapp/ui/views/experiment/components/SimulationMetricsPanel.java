package io.skymind.pathmind.webapp.ui.views.experiment.components;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.shared.data.Metrics;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.shared.featureflag.Feature;
import io.skymind.pathmind.shared.featureflag.FeatureManager;
import io.skymind.pathmind.shared.utils.PathmindNumberUtils;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.SparkLine;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.model.components.RewardVariablesTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.BOLD_LABEL;

public class SimulationMetricsPanel extends VerticalLayout {

    private boolean showSimulationMetrics;

    private List<Double> simulationMetrics = new ArrayList<>();
    private List<double[]> sparklinesData = new ArrayList<>();

//    private List<RewardVariable> rewardVariables;

    private VerticalLayout metricsWrapper;
    private VerticalLayout sparklinesWrapper;
    private HorizontalLayout simulationMetricsWrapper;

    private RewardVariablesTable rewardVariablesTable;

    private Policy policy;

    public SimulationMetricsPanel(FeatureManager featureManager) {
        super();
        setPadding(false);
        setSpacing(false);

        showSimulationMetrics = featureManager.isEnabled(Feature.SIMULATION_METRICS);

        simulationMetricsWrapper = getSimulationMetricsTable();
        String simulationMetricsHeaderText = showSimulationMetrics ? "Simulation Metrics" : "Reward Variables";

        add(LabelFactory.createLabel(simulationMetricsHeaderText, BOLD_LABEL));
        add(simulationMetricsWrapper);
    }

    private HorizontalLayout getSimulationMetricsTable() {
        HorizontalLayout tableWrapper = new HorizontalLayout();
        tableWrapper.setSpacing(false);
        tableWrapper.addClassName("simulation-metrics-table-wrapper");

        rewardVariablesTable = new RewardVariablesTable();
        rewardVariablesTable.setIsReadOnly(true);
        rewardVariablesTable.setCodeEditorMode();
        rewardVariablesTable.setSizeFull();
        tableWrapper.add(rewardVariablesTable);

        if (showSimulationMetrics) {
            metricsWrapper = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing();
            metricsWrapper.addClassName("metrics-wrapper");
            sparklinesWrapper = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing();
            sparklinesWrapper.addClassName("sparklines-wrapper");

            updateSimulationMetrics();
            tableWrapper.add(metricsWrapper, sparklinesWrapper);
        }

        return tableWrapper;
    }

    private void updateSimulationMetrics() {
        metricsWrapper.removeAll();
        sparklinesWrapper.removeAll();

        updateSimulationMetricsData();

        IntStream.range(0, simulationMetrics.size())
                .forEach(idx -> {
                    metricsWrapper.add(new Span(PathmindNumberUtils.formatNumber(simulationMetrics.get(idx))));
                    SparkLine sparkLine = new SparkLine();
                    sparkLine.setSparkLine(sparklinesData.get(idx), idx);
                    sparklinesWrapper.add(sparkLine);
                });
    }

    private void updateSimulationMetricsData() {
        List<Metrics> metricsList = policy == null ? null : policy.getMetrics();
        sparklinesData.clear();
        simulationMetrics.clear();

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
    }

    // REFACTOR -> This may be updated through an event listener for the component. We'll see how the rest of the refactoring for this view goes.
    public void setPolicy(Policy policy) {
        this.policy = policy;
        if(showSimulationMetrics)
            updateSimulationMetrics();
    }

    // REFACTOR -> This may be updated through an event listener for the component. We'll see how the rest of the refactoring for this view goes.
    public void setRewardVariables(List<RewardVariable> rewardVariables) {
//        this.rewardVariables = rewardVariables;
        rewardVariablesTable.setVariableSize(rewardVariables.size());
//        rewardVariablesTable.setVariableSize(experiment.getModel().getRewardVariablesCount());
        if(showSimulationMetrics)
            updateSimulationMetrics();
    }
}
