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
    Tab metricsChartTab;
    Tab rewardScoreChartTab;

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

        VerticalLayout chartsWrapper = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(
                LabelFactory.createLabel("Learning Progress", BOLD_LABEL),
                chartTabs,
                chartsPanel
        );

        chartsWrapper.addClassName("row-2-of-3");
        add(chartsWrapper);


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

        if(experiment.getTrainingStatusEnum() == RunStatus.NotStarted || experiment.getTrainingStatusEnum() == RunStatus.Starting) {
            chartTabs.setSelectedTab(metricsChartTab);
            setPlaceholderVisible();
        } else {
            chartTabs.setSelectedTab(rewardScoreChartTab);
            setPolicyChartPanelVisible();
        }
    }

    public void setStopTrainingVisibility() {
        trainingStartingPlaceholder.setVisible(false);
        setPolicyChartPanelVisible();
        // TODO -> STEPH -> Is this last line needed? Shouldn't it always be true?
        setVisible(true);
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
                // TODO -> STEPH -> Why do we do this on every event update? This seems off? It was in the experiment RunUpdate Subscriber.
                // TODO -> STEPH -> .. shouldn't we instead update the chart...
//                setStopTrainingVisibility();
                // TODO -> STEPH -> what about policies? Shouldn't we be listening to policies as well? Why runs instead of policies in the first place?
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
