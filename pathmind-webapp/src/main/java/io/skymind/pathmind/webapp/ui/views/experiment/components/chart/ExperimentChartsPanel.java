package io.skymind.pathmind.webapp.ui.views.experiment.components.chart;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.components.ExperimentComponent;

import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.BOLD_LABEL;

public class ExperimentChartsPanel extends VerticalLayout implements ExperimentComponent {

    private CompareMetricsChartPanel compareMetricsChartPanel;
    private HistogramChartPanel histogramChartPanel;
    private PolicyChartPanel policyChartPanel;
    private TrainingStartingPlaceholder trainingStartingPlaceholder;

    private Tabs chartTabs;
    private Tab metricsChartTab;
    private Tab histogramChartTab;
    private Tab rewardScoreChartTab;

    private Experiment experiment;

    private ExperimentView experimentView;
    private boolean isComparisonPanel;

    public ExperimentChartsPanel(ExperimentView experimentView, boolean isComparisonPanel) {
        this.experimentView = experimentView;
        this.isComparisonPanel = isComparisonPanel;

        Tabs chartTabs = createChartTabs();
        compareMetricsChartPanel = new CompareMetricsChartPanel();
        histogramChartPanel = new HistogramChartPanel();
        policyChartPanel = new PolicyChartPanel();
        trainingStartingPlaceholder = new TrainingStartingPlaceholder();

        VerticalLayout charts = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(
                compareMetricsChartPanel,
                histogramChartPanel,
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
    }

    private Tabs createChartTabs() {
        metricsChartTab = new Tab("Metrics");
        histogramChartTab = new Tab("Histogram");
        rewardScoreChartTab = new Tab("Mean Reward Score");
        chartTabs = new Tabs(metricsChartTab, histogramChartTab, rewardScoreChartTab);
        chartTabs.addThemeVariants(TabsVariant.LUMO_SMALL);
        chartTabs.addSelectedChangeListener(event -> setVisiblePanel(true));
        return chartTabs;
    }

    private void setVisiblePanel(boolean isRedraw) {
        int tabIndex = chartTabs.getSelectedIndex();
        if (tabIndex == 0) {
            setCompareMetricsChartPanelVisible(isRedraw);
        } else if (tabIndex == 1) {
            setHistogramChartPanelVisible(isRedraw);
        } else {
            setPolicyChartPanelVisible(isRedraw);
        }
        if (experimentView.isComparisonMode()) {
            if (isComparisonPanel) {
                experimentView.getExperimentChartsPanel().setTabToIndex(tabIndex);
            } else {
                experimentView.getComparisonChartsPanel().setTabToIndex(tabIndex);
            }
        }
    }

    public void setTabToIndex(int tabIndex) {
        chartTabs.setSelectedIndex(tabIndex);
    }

    public void selectVisibleChart() {
        if (experiment.getTrainingStatusEnum() == RunStatus.NotStarted || experiment.getTrainingStatusEnum() == RunStatus.Starting) {
            setPlaceholderVisible();
        } else {
            setVisiblePanel(false);
        }
    }

    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
        histogramChartPanel.setExperiment(experiment);
        policyChartPanel.setExperiment(experiment);
        compareMetricsChartPanel.setExperiment(experiment);
        selectVisibleChart();
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
}
