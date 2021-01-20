package io.skymind.pathmind.webapp.ui.views.experiment.components.chart;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import io.skymind.pathmind.db.utils.RewardVariablesUtils;
import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.components.chart.subscribers.main.ExperimentChartsPanelRunUpdateSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.chart.subscribers.view.ExperimentChartsPanelExperimentSwitchedViewSubscriber;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.BOLD_LABEL;

public class ExperimentChartsPanel extends VerticalLayout {

    private CompareMetricsChartPanel compareMetricsChartPanel;
    private HistogramChartPanel histogramChartPanel;
    private PolicyChartPanel policyChartPanel;
    private TrainingStartingPlaceholder trainingStartingPlaceholder;

    private Tabs chartTabs;
    private Tab histogramChartTab;
    private Tab metricsChartTab;
    private Tab rewardScoreChartTab;

    private Experiment experiment;
    private List<RewardVariable> rewardVariables;

    private Supplier<Optional<UI>> getUISupplier;

    public ExperimentChartsPanel(Supplier<Optional<UI>> getUISupplier, Experiment experiment, List<RewardVariable> rewardVariables) {

        this.getUISupplier = getUISupplier;

        Tabs chartTabs = createChartTabs();
        histogramChartPanel = new HistogramChartPanel(getUISupplier);
        compareMetricsChartPanel = new CompareMetricsChartPanel(getUISupplier);
        policyChartPanel = new PolicyChartPanel(getUISupplier);
        trainingStartingPlaceholder = new TrainingStartingPlaceholder();

        VerticalLayout charts = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(
                histogramChartPanel,
                policyChartPanel,
                compareMetricsChartPanel,
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

        setCompareMetricsChartPanelVisible(true);

        setupCharts(experiment, rewardVariables);
    }

    private Tabs createChartTabs() {
        histogramChartTab = new Tab("Histogram");
        metricsChartTab = new Tab("Metrics");
        rewardScoreChartTab = new Tab("Mean Reward Score");
        chartTabs = new Tabs(histogramChartTab, metricsChartTab, rewardScoreChartTab);
        chartTabs.addThemeVariants(TabsVariant.LUMO_SMALL);
        chartTabs.addSelectedChangeListener(event -> setVisiblePanel(true));
        return chartTabs;
    }

    private void setVisiblePanel(boolean isRedraw) {
        if (chartTabs.getSelectedIndex() == 0) {
            setHistogramChartPanelVisible(isRedraw);
        } else if (chartTabs.getSelectedIndex() == 1) {
            setCompareMetricsChartPanelVisible(isRedraw);
        } else {
            setPolicyChartPanelVisible(isRedraw);
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        EventBus.subscribe(this, getUISupplier,
                new ExperimentChartsPanelRunUpdateSubscriber(this),
                new ExperimentChartsPanelExperimentSwitchedViewSubscriber(this));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        EventBus.unsubscribe(this);
    }

    public void setupCharts(Experiment newExperiment, List<RewardVariable> newRewardVariables) {
        setExperiment(newExperiment);
        this.rewardVariables = RewardVariablesUtils.deepClone(newRewardVariables);
        histogramChartPanel.setupChart(experiment, rewardVariables);
        policyChartPanel.setExperiment(experiment);
        compareMetricsChartPanel.setupChart(experiment, rewardVariables);
        selectVisibleChart();
    }

    public void selectVisibleChart() {
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

    private void setHistogramChartPanelVisible(boolean isRedraw) {
        trainingStartingPlaceholder.setVisible(false);
        histogramChartPanel.setVisible(true);
        policyChartPanel.setVisible(false);
        compareMetricsChartPanel.setVisible(false);
        if (isRedraw) {
            histogramChartPanel.redrawChart();
        }
    }

    private void setCompareMetricsChartPanelVisible(boolean isRedraw) {
        trainingStartingPlaceholder.setVisible(false);
        histogramChartPanel.setVisible(false);
        policyChartPanel.setVisible(false);
        compareMetricsChartPanel.setVisible(true);
        if (isRedraw) {
            compareMetricsChartPanel.redrawChart();
        }
    }

    private void setPolicyChartPanelVisible(boolean isRedraw) {
        trainingStartingPlaceholder.setVisible(false);
        histogramChartPanel.setVisible(false);
        policyChartPanel.setVisible(true);
        compareMetricsChartPanel.setVisible(false);
        if (isRedraw) {
            policyChartPanel.redrawChart();
        }
    }

    private void setPlaceholderVisible() {
        trainingStartingPlaceholder.setVisible(true);
        policyChartPanel.setVisible(false);
        compareMetricsChartPanel.setVisible(false);
    }

    public Experiment getExperiment() {
        return experiment;
    }

    public List<RewardVariable> getRewardVariables() {
        return rewardVariables;
    }
}
