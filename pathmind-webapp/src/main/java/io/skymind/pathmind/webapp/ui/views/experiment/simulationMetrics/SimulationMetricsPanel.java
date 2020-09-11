package io.skymind.pathmind.webapp.ui.views.experiment.simulationMetrics;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.shared.utils.PathmindNumberUtils;
import io.skymind.pathmind.shared.utils.PolicyUtils;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.ui.components.SparklineChart;
import io.skymind.pathmind.webapp.ui.components.SparklineChartNew;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.components.MetricChartPanel;
import io.skymind.pathmind.webapp.ui.views.experiment.components.SimulationMetricsInfoLink;
import io.skymind.pathmind.webapp.ui.views.experiment.simulationMetrics.subscribers.SimulationMetricsPolicyUpdateSubscriber;
import io.skymind.pathmind.webapp.ui.views.model.components.RewardVariablesTable;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class SimulationMetricsPanel extends HorizontalLayout {

    private Supplier<Optional<UI>> getUISupplier;

    private VerticalLayout metricsWrapper;
    private VerticalLayout sparklinesWrapper;

    private RewardVariablesTable rewardVariablesTable;

    private boolean showSimulationMetrics;

    private Experiment experiment;
    private List<RewardVariable> rewardVariables;
    private Dialog metricChartDialog;
    private Button metricChartDialogCloseButton;

    public SimulationMetricsPanel(Experiment experiment, boolean showSimulationMetrics, List<RewardVariable> rewardVariables, Supplier<Optional<UI>> getUISupplier) {

        super();
        this.experiment = experiment;
        this.rewardVariables= rewardVariables;
        this.showSimulationMetrics = showSimulationMetrics;
        this.getUISupplier = getUISupplier;

        setSpacing(false);
        addClassName("simulation-metrics-table-wrapper");

        createEnlargedChartDialog();
        createEnlargedChartDialogCloseButton();

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

            if(!experiment.getPolicies().isEmpty())
               updateSimulationMetrics(PolicyUtils.selectBestPolicy(experiment.getPolicies()));

            add(metricsWrapper, sparklinesWrapper);
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        if(experiment.isArchived())
            return;
        EventBus.subscribe(this,
                new SimulationMetricsPolicyUpdateSubscriber(getUISupplier, this),
                new SimulationMetricsPolicyUpdateSubscriber(getUISupplier, this));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        EventBus.unsubscribe(this);
    }

    public Experiment getExperiment() {
        return experiment;
    }

    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
        Policy bestPolicy = PolicyUtils.selectBestPolicy(experiment.getPolicies());
        if(bestPolicy != null)
            updateSimulationMetrics(bestPolicy);
    }

    public boolean isShowSimulationMetrics() {
        return showSimulationMetrics;
    }

    public void updateSimulationMetrics(Policy policy) {

        // Needed to convert the raw metrics to a format the UI can use.
        PolicyUtils.updateSimulationMetricsData(policy);

        metricsWrapper.removeAll();
        sparklinesWrapper.removeAll();

        if(policy.getSimulationMetrics() == null && policy.getSimulationMetrics().isEmpty())
            return;

        Div metricsHeader = new Div(new Span("Value"), new SimulationMetricsInfoLink());
        metricsHeader.addClassName("header");
        metricsWrapper.add(metricsHeader);

        Div sparklineHeader = new Div(new Span("Overview"), new SimulationMetricsInfoLink());
        sparklineHeader.addClassName("header");
        sparklinesWrapper.add(sparklineHeader);

        IntStream.range(0, policy.getSimulationMetrics().size())
                .forEach(idx -> {
                    SparklineChart sparkLine = new SparklineChart();
                    SparklineChartNew sparkLineNew = new SparklineChartNew();
                    sparkLine.setSparkLine(policy.getSparklinesData().get(idx), idx, rewardVariables.get(idx));
                    sparkLine.setupButton(() -> {
                        MetricChartPanel metricChartPanel = new MetricChartPanel();
                        metricChartPanel.setLines(policy.getSparklinesData().get(idx), idx, rewardVariables.get(idx));
                        addChartToDialog(metricChartPanel);
                        metricChartDialog.open();
                    });
                    sparklinesWrapper.add(sparkLine);
                    sparklinesWrapper.add(sparkLineNew);
                    Span metricSpan = new Span();
                    if (policy.getUncertainty() != null && !policy.getUncertainty().isEmpty()) {
                        String metricValueWithUncertainty = policy.getUncertainty().get(idx);
                        metricSpan = new Span(metricValueWithUncertainty);
                        metricsWrapper.add(metricSpan);
                    } else {
                        String metricValueWithoutUncertainty = PathmindNumberUtils.formatNumber(policy.getSimulationMetrics().get(idx));
                        metricSpan = new Span(metricValueWithoutUncertainty);
                        metricsWrapper.add(metricSpan);
                    }
                    if (rewardVariables.get(idx).getGoalConditionTypeEnum() != null){
                        Boolean reachedGoal = PolicyUtils.isGoalReached(rewardVariables.get(idx), policy);
                        String metricSpanColorClass = reachedGoal ? "success-text" : "failure-text";
                        metricSpan.addClassName(metricSpanColorClass);
                    }
                });
    }

    private void createEnlargedChartDialog() {
        metricChartDialog = new Dialog();
        metricChartDialog.setWidth("60vw");
    }

    private void createEnlargedChartDialogCloseButton() {
        metricChartDialogCloseButton = new Button(VaadinIcon.CLOSE_SMALL.create());
        metricChartDialogCloseButton.addClickListener(event -> metricChartDialog.close());
    }

    private void addChartToDialog(MetricChartPanel chartPanel) {
        metricChartDialog.removeAll();
        metricChartDialog.add(chartPanel, metricChartDialogCloseButton);
    }
}
