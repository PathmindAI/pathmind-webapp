package io.skymind.pathmind.webapp.ui.views.experiment.components.chart;

import java.util.Optional;
import java.util.function.Supplier;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.components.ExperimentComponent;

import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.BOLD_LABEL;

public class ExperimentChartsPanel extends VerticalLayout implements ExperimentComponent {

    private CompareMetricsChartPanel compareMetricsChartPanel;
    private PolicyChartPanel policyChartPanel;
    private TrainingStartingPlaceholder trainingStartingPlaceholder;

    private Tabs chartTabs;
    private Tab metricsChartTab;
    private Tab rewardScoreChartTab;

    private Experiment experiment;

    public ExperimentChartsPanel(Supplier<Optional<UI>> getUISupplier) {

        Tabs chartTabs = createChartTabs();
        compareMetricsChartPanel = new CompareMetricsChartPanel();
        policyChartPanel = new PolicyChartPanel(getUISupplier);
        trainingStartingPlaceholder = new TrainingStartingPlaceholder();

        VerticalLayout charts = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(
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
            setCompareMetricsChartPanelVisible(isRedraw);
        } else {
            setPolicyChartPanelVisible(isRedraw);
        }
    }

    public void selectVisibleChart() {
        if (experiment.getTrainingStatusEnum() == RunStatus.NotStarted || experiment.getTrainingStatusEnum() == RunStatus.Starting) {
            setPlaceholderVisible();
        } else {
            setVisiblePanel(false);
        }
    }

    private void setCompareMetricsChartPanelVisible(boolean isRedraw) {
        trainingStartingPlaceholder.setVisible(false);
        policyChartPanel.setVisible(false);
        compareMetricsChartPanel.setVisible(true);
        if (isRedraw) {
            compareMetricsChartPanel.redrawChart();
        }
    }

    private void setPolicyChartPanelVisible(boolean isRedraw) {
        trainingStartingPlaceholder.setVisible(false);
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

    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
        policyChartPanel.setExperiment(experiment);
        compareMetricsChartPanel.setExperiment(experiment);
        selectVisibleChart();
    }
}
