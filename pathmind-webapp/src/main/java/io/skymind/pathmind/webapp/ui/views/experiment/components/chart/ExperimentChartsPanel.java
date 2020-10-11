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
import io.skymind.pathmind.webapp.bus.subscribers.RunUpdateSubscriber;
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

    private Experiment experiment;

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


        setAllMetricsChartPanelVisible(true);
    }

    private Tabs createChartTabs() {
        Tab metricsChartTab = new Tab("Metrics");
        Tab rewardScoreChartTab = new Tab("Mean Reward Score");
        Tabs chartTabs = new Tabs(metricsChartTab, rewardScoreChartTab);
        chartTabs.addThemeVariants(TabsVariant.LUMO_SMALL);
        chartTabs.addSelectedChangeListener(event -> {
            // have to redraw chart to make sure the chart size is right
            if (chartTabs.getSelectedIndex() == 0) {
                setAllMetricsChartPanelVisible(true);
            } else {
                setPolicyChartPanelVisible(true);
            }
        });
        return chartTabs;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        EventBus.subscribe(this, new ExperimentChartsPanelRunUpdateSubscriber(getUISupplier));
    }

    public void setupCharts(Experiment experiment, List<RewardVariable> rewardVariables) {

        this.experiment = experiment;

        policyChartPanel.setExperiment(experiment);
        allMetricsChartPanel.setupChart(experiment, rewardVariables);

        RunStatus trainingStatus = experiment.getTrainingStatusEnum();
        trainingStartingPlaceholder.setVisible(trainingStatus == RunStatus.Starting);
        // TODO -> STEPH -> Is this correct? I would think it would be the opposite but let's confirm.
        setPolicyChartPanelVisible(trainingStatus != RunStatus.Starting);
    }

    public void setStopTrainingVisibility() {
        trainingStartingPlaceholder.setVisible(false);
        setPolicyChartPanelVisible(true);
        // TODO -> STEPH -> Is this last line needed? Shouldn't it always be true?
        setVisible(true);
    }

    private void setAllMetricsChartPanelVisible(boolean isVisible) {
        policyChartPanel.setVisible(!isVisible);
        allMetricsChartPanel.setVisible(isVisible);
        allMetricsChartPanel.redrawChart();
    }

    private void setPolicyChartPanelVisible(boolean isVisible) {
        policyChartPanel.setVisible(isVisible);
        allMetricsChartPanel.setVisible(!isVisible);
        policyChartPanel.redrawChart();
    }

    private boolean isSameExperiment(RunUpdateBusEvent event) {
        return ExperimentUtils.isSameExperiment(experiment, event.getRun().getExperiment());
    }

    class ExperimentChartsPanelRunUpdateSubscriber extends RunUpdateSubscriber {

        public ExperimentChartsPanelRunUpdateSubscriber(Supplier<Optional<UI>> getUISupplier) {
            super(getUISupplier);
        }

        @Override
        public void handleBusEvent(RunUpdateBusEvent event) {
            PushUtils.push(getUiSupplier(), () -> {
                // TODO -> STEPH -> Why do we do this on every event update? This seems off? It was in the experiment RunUpdate Subscriber.
                setStopTrainingVisibility();
            });
        }

        @Override
        public boolean filterBusEvent(RunUpdateBusEvent event) {
            return isSameExperiment(event);
        }
    }
}
