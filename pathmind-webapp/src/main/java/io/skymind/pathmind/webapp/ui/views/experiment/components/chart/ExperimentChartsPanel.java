package io.skymind.pathmind.webapp.ui.views.experiment.components.chart;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.bus.events.main.RunUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.events.view.ExperimentChangedViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.RunUpdateSubscriber;
import io.skymind.pathmind.webapp.bus.subscribers.view.ExperimentChangedViewSubscriber;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.data.utils.RewardVariablesUtils;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.utils.PushUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.BOLD_LABEL;

public class ExperimentChartsPanel extends VerticalLayout {

    private AllMetricsChartPanel allMetricsChartPanel;
    private PolicyChartPanel policyChartPanel;
    private TrainingStartingPlaceholder trainingStartingPlaceholder;

    private Tabs chartTabs;
    private Tab metricsChartTab;
    private Tab rewardScoreChartTab;

    private Experiment experiment;
    private List<RewardVariable> rewardVariables;

    private Supplier<Optional<UI>> getUISupplier;

    public ExperimentChartsPanel(Supplier<Optional<UI>> getUISupplier) {

        this.getUISupplier = getUISupplier;

        Tabs chartTabs = createChartTabs();
        allMetricsChartPanel = new AllMetricsChartPanel(getUISupplier);
        policyChartPanel = new PolicyChartPanel(getUISupplier);
        trainingStartingPlaceholder = new TrainingStartingPlaceholder();

        VerticalLayout charts = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(
                allMetricsChartPanel,
                policyChartPanel);

        VerticalLayout chartsPanel = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(
                trainingStartingPlaceholder,
                charts
        );

        setSpacing(false);
        setPadding(false);
        add(LabelFactory.createLabel("Learning Progress", BOLD_LABEL),
            chartTabs,
            chartsPanel);
        addClassName("row-2-of-3");

        setAllMetricsChartPanelVisible(true);
    }

    private Tabs createChartTabs() {
        metricsChartTab = new Tab("Metrics");
        rewardScoreChartTab = new Tab("Mean Reward Score");
        chartTabs = new Tabs(metricsChartTab, rewardScoreChartTab);
        chartTabs.addThemeVariants(TabsVariant.LUMO_SMALL);
        chartTabs.addSelectedChangeListener(event -> setVisiblePanel(true));
        return chartTabs;
    }

    private void setVisiblePanel(boolean isRedraw) {
        if (chartTabs.getSelectedIndex() == 0) {
            setAllMetricsChartPanelVisible(isRedraw);
        } else {
            setPolicyChartPanelVisible(isRedraw);
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        EventBus.subscribe(this,
                new ExperimentChartsPanelRunUpdateSubscriber(getUISupplier),
                new ExperimentChartsPanelExperimentChangedViewSubscriber(getUISupplier));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        EventBus.unsubscribe(this);
    }

    public void setupCharts(Experiment newExperiment, List<RewardVariable> newRewardVariables) {
        setExperiment(newExperiment);
        this.rewardVariables = RewardVariablesUtils.deepClone(newRewardVariables);
        updateCharts();
    }

    private void updateCharts() {
        policyChartPanel.setExperiment(experiment);
        allMetricsChartPanel.setupChart(experiment, rewardVariables);

        if (experiment.getTrainingStatusEnum() == RunStatus.NotStarted || experiment.getTrainingStatusEnum() == RunStatus.Starting) {
            setPlaceholderVisible();
        } else {
            setVisiblePanel(false);
        }
    }

    private void setExperiment(Experiment experiment) {
        this.experiment = experiment.deepClone();
        // This always needs to be done on set because we cannot rely on whoever set it to have done it. And it should be done on the cloned version.
        experiment.updateTrainingStatus();
    }

    private void setAllMetricsChartPanelVisible(boolean isRedraw) {
        trainingStartingPlaceholder.setVisible(false);
        policyChartPanel.setVisible(false);
        allMetricsChartPanel.setVisible(true);
        if(isRedraw)
            allMetricsChartPanel.redrawChart();
    }

    private void setPolicyChartPanelVisible(boolean isRedraw) {
        trainingStartingPlaceholder.setVisible(false);
        policyChartPanel.setVisible(true);
        allMetricsChartPanel.setVisible(false);
        if(isRedraw)
           policyChartPanel.redrawChart();
    }

    private void setPlaceholderVisible() {
        trainingStartingPlaceholder.setVisible(true);
        policyChartPanel.setVisible(false);
        allMetricsChartPanel.setVisible(false);
    }

    private boolean isSameExperiment(Experiment experimentToCompare) {
        return ExperimentUtils.isSameExperiment(experiment, experimentToCompare);
    }

    class ExperimentChartsPanelRunUpdateSubscriber extends RunUpdateSubscriber {

        public ExperimentChartsPanelRunUpdateSubscriber(Supplier<Optional<UI>> getUISupplier) {
            super(getUISupplier);
        }

        @Override
        public void handleBusEvent(RunUpdateBusEvent event) {
            PushUtils.push(getUiSupplier(), () -> {
                ExperimentUtils.addOrUpdateRuns(experiment, event.getRuns());
                experiment.updateTrainingStatus();
                updateCharts();
            });
        }

        @Override
        public boolean filterBusEvent(RunUpdateBusEvent event) {
            return isSameExperiment(event.getExperiment());
        }
    }

    class ExperimentChartsPanelExperimentChangedViewSubscriber extends ExperimentChangedViewSubscriber {

        public ExperimentChartsPanelExperimentChangedViewSubscriber(Supplier<Optional<UI>> getUISupplier) {
            super(getUISupplier);
        }

        @Override
        public void handleBusEvent(ExperimentChangedViewBusEvent event) {
            PushUtils.push(getUiSupplier(), () ->
                setExperiment(event.getExperiment()));
                updateCharts();
        }
    }
}
