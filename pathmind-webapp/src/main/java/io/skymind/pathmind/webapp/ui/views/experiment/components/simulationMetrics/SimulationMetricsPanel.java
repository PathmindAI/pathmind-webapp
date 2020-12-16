package io.skymind.pathmind.webapp.ui.views.experiment.components.simulationMetrics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.shared.utils.PathmindNumberUtils;
import io.skymind.pathmind.shared.utils.PolicyUtils;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.ui.components.rewardVariables.RewardVariablesTable;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.components.ExperimentComponent;
import io.skymind.pathmind.webapp.ui.views.experiment.components.SimulationMetricsInfoLink;
import io.skymind.pathmind.webapp.ui.views.experiment.components.SparklineChart;
import io.skymind.pathmind.webapp.ui.views.experiment.components.chart.MetricChartPanel;
import io.skymind.pathmind.webapp.ui.views.experiment.components.simulationMetrics.subscribers.main.PopupSimulationMetricChartPanelPolicyUpdateSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.simulationMetrics.subscribers.main.SimulationMetricsPolicyUpdateSubscriber;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimulationMetricsPanel extends HorizontalLayout implements ExperimentComponent {

    private Supplier<Optional<UI>> getUISupplier;

    private MetricChartPanel metricChartPanel;
    private Dialog metricChartDialog;
    private VerticalLayout metricsWrapper;
    private VerticalLayout sparklinesWrapper;

    private RewardVariablesTable rewardVariablesTable;

    private Experiment experiment;
    private List<Span> metricSpans = new ArrayList<>();
    private List<SparklineChart> sparklineCharts = new ArrayList<>();
    private List<Button> sparklineShowButtons = new ArrayList<>();
    private List<Registration> showButtonClickListenerRegistrations = new ArrayList<>();

    // REFACTOR -> A quick somewhat hacky solution until we have time to refactor the code
    private int indexClicked;

    // TODO -> STEPH -> Can we somehow push the experiment away from the constructor? I'm sure we can do it by modifying the parent view
    // classes with a setup() method or something along those lines so that it's only called once in the constructors rather than every setExperiment(), but
    // I'm pushing that off for now.
    public SimulationMetricsPanel(boolean showSimulationMetrics, Experiment experiment, Supplier<Optional<UI>> getUISupplier) {

        super();
        this.experiment = experiment;
        this.getUISupplier = getUISupplier;

        setSpacing(false);
        addClassName("simulation-metrics-table-wrapper");

        createEnlargedChartDialog();

        // TODO -> STEPH -> Why do we have a RewardVariablesTable here in addition to the view?
        rewardVariablesTable = new RewardVariablesTable(getUISupplier);
        rewardVariablesTable.setCodeEditorMode();
        rewardVariablesTable.setCompactMode();
        rewardVariablesTable.setSelectMode();
        rewardVariablesTable.setSizeFull();

        add(rewardVariablesTable);

        if (showSimulationMetrics) {
            createSimulationMetricsSpansAndSparklines();

            updateSimulationMetrics();

            add(metricsWrapper, sparklinesWrapper);
        }
    }

    private void createSimulationMetricsSpansAndSparklines() {
        metricsWrapper = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing();
        metricsWrapper.addClassName("metrics-wrapper");
        Div metricsHeader = new Div(new Span("Value"), new SimulationMetricsInfoLink());
        metricsHeader.addClassName("header");
        metricsWrapper.add(metricsHeader);

        sparklinesWrapper = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing();
        sparklinesWrapper.addClassName("sparklines-wrapper");
        Div sparklineHeader = new Div(new Span("Overview"), new SimulationMetricsInfoLink());
        sparklineHeader.addClassName("header");
        sparklinesWrapper.add(sparklineHeader);

        // Needed to convert the raw metrics to a format the UI can use.
        // TODO -> STEPH -> DELETE -> Confirm this can be deleted after testing.
        // PolicyUtils.updateSimulationMetricsData(experiment.getBestPolicy());

        IntStream.range(0, experiment.getRewardVariables().size())
                .forEach(index -> {
                    Span metricSpan = new Span();
                    SparklineChart sparkline = new SparklineChart();
                    metricSpans.add(metricSpan);
                    sparklineCharts.add(sparkline);

                    Button enlargeButton = new Button("Show");
                    enlargeButton.addThemeVariants(ButtonVariant.LUMO_SMALL);

                    VerticalLayout sparkLineWrapper = WrapperUtils.wrapVerticalWithNoPaddingOrSpacingAndWidthAuto(
                            sparkline,
                            enlargeButton
                    );
                    sparkLineWrapper.addClassName("sparkline");

                    metricsWrapper.add(metricSpan);
                    sparklinesWrapper.add(sparkLineWrapper);
                    sparklineShowButtons.add(enlargeButton);
                });

        showMetricValuesAndSparklines(false);
    }

    private void showMetricValuesAndSparklines(Boolean show) {
        metricsWrapper.setVisible(show);
        sparklinesWrapper.setVisible(show);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        if (experiment.isArchived()) {
            return;
        }
        EventBus.subscribe(this, getUISupplier,
                new SimulationMetricsPolicyUpdateSubscriber(this),
                new PopupSimulationMetricChartPanelPolicyUpdateSubscriber(this, metricChartPanel));
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
        // TODO -> REFACTOR -> Why are we resetting the reward variables here? Why are there are two RewardVariableTables?
        rewardVariablesTable.setExperiment(experiment);
        updateSimulationMetrics();
    }

    public void updateSimulationMetrics() {

        Policy bestPolicy = experiment.getBestPolicy();

        // Needed to convert the raw metrics to a format the UI can use.
        // TODO -> STEPH -> DELETE -> Confirm this can be deleted after testing.
        // PolicyUtils.updateSimulationMetricsData(bestPolicy);

        if (bestPolicy == null || bestPolicy.getSimulationMetrics() == null || bestPolicy.getSimulationMetrics().isEmpty()) {
            showMetricValuesAndSparklines(false);
            return;
        }

        IntStream.range(0, bestPolicy.getSimulationMetrics().size())
                .forEach(index -> {
                    Map<Integer, Double> sparklineData = bestPolicy.getSparklinesData().get(index);
                    RewardVariable rewardVariable = experiment.getRewardVariables().get(index);

                    // First conditional value is with uncertainty, second value is without uncertainty
                    String metricValue = bestPolicy.getUncertainty() != null && !bestPolicy.getUncertainty().isEmpty()
                            ? bestPolicy.getUncertainty().get(index)
                            : PathmindNumberUtils.formatNumber(bestPolicy.getSimulationMetrics().get(index));

                    if (rewardVariable.getGoalConditionTypeEnum() != null) {
                        Boolean reachedGoal = PolicyUtils.isGoalReached(rewardVariable, bestPolicy);
                        String metricSpanColorClass = reachedGoal ? "success-text" : "failure-text";
                        metricSpans.get(index).addClassName(metricSpanColorClass);
                    }
                    if (showButtonClickListenerRegistrations.size() > index) {
                        showButtonClickListenerRegistrations.get(index).remove();
                    }
                    Registration showButtonRegistration = sparklineShowButtons.get(index).addClickListener(event -> {
                        Boolean reachedGoal = PolicyUtils.isGoalReached(rewardVariable, bestPolicy);
                        indexClicked = index;
                        metricChartPanel.setGoals(rewardVariable, reachedGoal);
                        metricChartPanel.setupChart(sparklineData, rewardVariable);
                        metricChartDialog.open();
                    });
                    if (showButtonClickListenerRegistrations.size() > index) {
                        showButtonClickListenerRegistrations.set(index, showButtonRegistration);
                    } else {
                        showButtonClickListenerRegistrations.add(index, showButtonRegistration);
                    }
                    sparklineCharts.get(index).setSparkLine(sparklineData, rewardVariable, false, index);
                    metricSpans.get(index).setText(metricValue);
                });

        showMetricValuesAndSparklines(true);
    }

    private void createEnlargedChartDialog() {
        metricChartPanel = new MetricChartPanel();
        Button metricChartDialogCloseButton = new Button(VaadinIcon.CLOSE_SMALL.create());
        metricChartDialogCloseButton.addClickListener(event -> metricChartDialog.close());
        metricChartDialog = new Dialog();
        metricChartDialog.setWidth("60vw");
        metricChartDialog.add(metricChartPanel, metricChartDialogCloseButton);
    }

    public boolean isMetricChartPopupOpen() {
        return metricChartDialog.isOpened();
    }

    public int getSparklineIndexClicked() {
        return indexClicked;
    }
}
