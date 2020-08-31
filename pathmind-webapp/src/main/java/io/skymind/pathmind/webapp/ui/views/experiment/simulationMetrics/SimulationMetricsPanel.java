package io.skymind.pathmind.webapp.ui.views.experiment.simulationMetrics;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.utils.PathmindNumberUtils;
import io.skymind.pathmind.webapp.ui.components.SparkLine;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.components.SimulationMetricsInfoLink;
import io.skymind.pathmind.webapp.ui.views.model.components.RewardVariablesTable;

import java.util.stream.IntStream;

public class SimulationMetricsPanel extends HorizontalLayout {

    private VerticalLayout metricsWrapper;
    private VerticalLayout sparklinesWrapper;


    private boolean showSimulationMetrics;

    private Experiment experiment;

    // REFACTOR -> I don't like that the rewardVariablesTable is here but possibly it's required. Need to confirm.
    public SimulationMetricsPanel(Experiment experiment, boolean showSimulationMetrics, RewardVariablesTable rewardVariablesTable) {

        super();
        this.experiment = experiment;
        this.showSimulationMetrics = showSimulationMetrics;

        setSpacing(false);
        addClassName("simulation-metrics-table-wrapper");

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
