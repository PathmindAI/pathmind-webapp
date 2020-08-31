package io.skymind.pathmind.webapp.ui.views.experiment.simulationMetrics;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.shared.utils.PathmindNumberUtils;
import io.skymind.pathmind.webapp.ui.components.SparkLine;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.components.SimulationMetricsInfoLink;
import io.skymind.pathmind.webapp.ui.views.model.components.RewardVariablesTable;

import java.util.List;
import java.util.stream.IntStream;

public class SimulationMetricsPanel extends HorizontalLayout {

    private VerticalLayout metricsWrapper;
    private VerticalLayout sparklinesWrapper;

    private RewardVariablesTable rewardVariablesTable;

    private boolean showSimulationMetrics;

    private Experiment experiment;

    public SimulationMetricsPanel(Experiment experiment, boolean showSimulationMetrics, List<RewardVariable> rewardVariables) {

        super();
        this.experiment = experiment;
        this.showSimulationMetrics = showSimulationMetrics;

        setSpacing(false);
        addClassName("simulation-metrics-table-wrapper");

        rewardVariablesTable = new RewardVariablesTable();
        rewardVariablesTable.setCodeEditorMode();
        rewardVariablesTable.setCompactMode();
        rewardVariablesTable.setSizeFull();
        rewardVariablesTable.setRewardVariables(rewardVariables);

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

        if (policy.getSimulationMetrics().size() > 0) {
            Div metricsHeader = new Div(new Span("Value"), new SimulationMetricsInfoLink());
            metricsHeader.addClassName("header");
            metricsWrapper.add(metricsHeader);

            Div sparklineHeader = new Div(new Span("Overview"), new SimulationMetricsInfoLink());
            sparklineHeader.addClassName("header");
            sparklinesWrapper.add(sparklineHeader);
        }

        IntStream.range(0, policy.getSimulationMetrics().size())
                .forEach(idx -> {
                    SparkLine sparkLine = new SparkLine();
                    sparkLine.setSparkLine(policy.getSparklinesData().get(idx), idx);
                    sparklinesWrapper.add(sparkLine);
                    if (policy.getUncertainty() != null && !policy.getUncertainty().isEmpty()) {
                        metricsWrapper.add(new Span(policy.getUncertainty().get(idx)));
                    } else {
                        metricsWrapper.add(new Span(PathmindNumberUtils.formatNumber(policy.getSimulationMetrics().get(idx))));
                    }
                });
    }
}
