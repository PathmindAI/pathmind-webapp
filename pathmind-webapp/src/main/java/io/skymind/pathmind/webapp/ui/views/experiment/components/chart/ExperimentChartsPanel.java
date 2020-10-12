package io.skymind.pathmind.webapp.ui.views.experiment.components.chart;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.bus.events.RunUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.events.view.ExperimentChangedViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.RunUpdateSubscriber;
import io.skymind.pathmind.webapp.bus.subscribers.view.ExperimentChangedViewSubscriber;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
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
        setAllMetricsChartPanelVisible();
    }

    private Tabs createChartTabs() {
        metricsChartTab = new Tab("Metrics");
        rewardScoreChartTab = new Tab("Mean Reward Score");
        chartTabs = new Tabs(metricsChartTab, rewardScoreChartTab);
        chartTabs.addThemeVariants(TabsVariant.LUMO_SMALL);
        chartTabs.addSelectedChangeListener(event -> {
            // have to redraw chart to make sure the chart size is right
            if (chartTabs.getSelectedIndex() == 0) {
                setAllMetricsChartPanelVisible();
            } else {
                setPolicyChartPanelVisible();
            }
        });
        return chartTabs;
    }

    public AllMetricsChartPanel getAllMetricsChartPanel() {
        return allMetricsChartPanel;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        EventBus.subscribe(this,
                new ExperimentChartsPanelRunUpdateSubscriber(getUISupplier),
                new ExperimentChartsPanelExperimentChangedViewSubscriber(getUISupplier));
    }

    public void setupCharts(Experiment experiment, List<RewardVariable> rewardVariables) {

        this.experiment = experiment;
        this.rewardVariables = rewardVariables;

        policyChartPanel.setExperiment(experiment);
        allMetricsChartPanel.setupChart(experiment, rewardVariables);

        System.out.println("training status: "+experiment.getTrainingStatusEnum());

        if (experiment.getTrainingStatusEnum() == RunStatus.NotStarted || experiment.getTrainingStatusEnum() == RunStatus.Starting) {
            setPlaceholderVisible();
        } else {
            chartTabs.setSelectedTab(metricsChartTab);
        }
    }

    public void setStopTrainingVisibility() {
        trainingStartingPlaceholder.setVisible(false);
        setPolicyChartPanelVisible();
    }

    private void setAllMetricsChartPanelVisible() {
        trainingStartingPlaceholder.setVisible(false);
        policyChartPanel.setVisible(false);
        allMetricsChartPanel.setVisible(true);
        allMetricsChartPanel.redrawChart();
    }

    private void setPolicyChartPanelVisible() {
        trainingStartingPlaceholder.setVisible(false);
        policyChartPanel.setVisible(true);
        allMetricsChartPanel.setVisible(false);
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
                // TODO -> FIONA -> Why do we do this on every event update? What about Policy updates like in the other experiment view components?
                ExperimentUtils.addOrUpdateRun(experiment, event.getRun());
                setupCharts(experiment, rewardVariables);
            });
        }

        @Override
        public boolean filterBusEvent(RunUpdateBusEvent event) {
            return isSameExperiment(event.getRun().getExperiment());
        }
    }

    class ExperimentChartsPanelExperimentChangedViewSubscriber extends ExperimentChangedViewSubscriber {

        public ExperimentChartsPanelExperimentChangedViewSubscriber(Supplier<Optional<UI>> getUISupplier) {
            super(getUISupplier);
        }

        @Override
        public void handleBusEvent(ExperimentChangedViewBusEvent event) {
            PushUtils.push(getUiSupplier(), () ->
                setupCharts(event.getExperiment(), rewardVariables));
        }
    }
}
