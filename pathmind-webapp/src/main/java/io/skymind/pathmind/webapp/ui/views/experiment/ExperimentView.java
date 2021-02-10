package io.skymind.pathmind.webapp.ui.views.experiment;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.GeneratedVaadinSplitLayout.SplitterDragendEvent;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.bus.EventBusSubscriber;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.atoms.FloatingCloseButton;
import io.skymind.pathmind.webapp.ui.components.modelChecker.ModelCheckerService;
import io.skymind.pathmind.webapp.ui.components.observations.ObservationsPanel;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.components.ExperimentComponent;
import io.skymind.pathmind.webapp.ui.views.experiment.components.ExperimentTitleBar;
import io.skymind.pathmind.webapp.ui.views.experiment.components.SimulationMetricsInfoLink;
import io.skymind.pathmind.webapp.ui.views.experiment.components.chart.ExperimentChartsPanel;
import io.skymind.pathmind.webapp.ui.views.experiment.components.codeViewer.CodeViewer;
import io.skymind.pathmind.webapp.ui.views.experiment.components.experimentNotes.ExperimentNotesField;
import io.skymind.pathmind.webapp.ui.views.experiment.components.notification.StoppedTrainingNotification;
import io.skymind.pathmind.webapp.ui.views.experiment.components.simulationMetrics.SimulationMetricsPanel;
import io.skymind.pathmind.webapp.ui.views.experiment.components.trainingStatus.TrainingStatusDetailsPanel;
import io.skymind.pathmind.webapp.ui.views.experiment.subscribers.main.experiment.ExperimentViewComparisonExperimentArchivedSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.subscribers.main.experiment.ExperimentViewFavoriteSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.subscribers.main.experiment.ExperimentViewPolicyUpdateSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.subscribers.main.experiment.ExperimentViewRunUpdateSubscriber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.BOLD_LABEL;

@Route(value = Routes.EXPERIMENT, layout = MainLayout.class)
@Slf4j
public class ExperimentView extends AbstractExperimentView {

    // Similar to DefaultExperimentView in that we have to use a lock object rather
    // than the (comparison) experiment because we are changing it's reference which
    // makes it not thread safe. As well we cannot lock on this because part of the
    // synchronization is in the eventbus listener in a subclass (which is also
    // why we can't use synchronize on the method).
    private final Object comparisonExperimentLock = new Object();

    private VerticalLayout compareExperimentVerticalLayout;

    private StoppedTrainingNotification stoppedTrainingNotification;
    private StoppedTrainingNotification comparisonStoppedTrainingNotification;

    // Experiment Components
    private ExperimentTitleBar experimentTitleBar;
    private SplitLayout panelsSplitWrapper;
    private SplitLayout middlePanel;
    private SplitLayout bottomPanel;
    protected ExperimentNotesField experimentNotesField;
    private CodeViewer experimentCodeViewer;
    private ExperimentChartsPanel experimentChartsPanel;
    private ObservationsPanel experimentObservationsPanel;
    private TrainingStatusDetailsPanel experimentTrainingStatusDetailsPanel;
    private SimulationMetricsPanel experimentSimulationMetricsPanel;

    // Experiment Comparison Components
    private Boolean isComparisonMode = false;
    private ExperimentTitleBar comparisonTitleBar;
    private ExperimentChartsPanel comparisonChartsPanel;
    protected ExperimentNotesField comparisonNotesField;
    private ObservationsPanel comparisonObservationsPanel;
    private CodeViewer comparisonCodeViewer;
    private SimulationMetricsPanel comparisonSimulationMetricsPanel;
    private FloatingCloseButton comparisonModeCloseButton;

    @Autowired
    private ModelCheckerService modelCheckerService;
    @Value("${pathmind.early-stopping.url}")
    private String earlyStoppingUrl;
    @Value("${pathmind.al-engine-error-article.url}")
    private String alEngineErrorArticleUrl;

    // Although this is really only for the experiment view it's a lot simpler to
    // put it at the parent level otherwise a lot of methods would have to be
    // overriden in ExperimentView.
    protected List<ExperimentComponent> comparisonExperimentComponents = new ArrayList<>();

    private Experiment comparisonExperiment;

    public ExperimentView(
            @Value("${pathmind.notification.newRunDailyLimit}") int newRunDailyLimit,
            @Value("${pathmind.notification.newRunMonthlyLimit}") int newRunMonthlyLimit,
            @Value("${pathmind.notification.newRunNotificationThreshold}") int newRunNotificationThreshold) {
        super();
        setUserCaps(newRunDailyLimit, newRunMonthlyLimit, newRunNotificationThreshold);
    }

    // Required for SharedExperimentView.
    protected ExperimentView() {
        super();
        addClassName("experiment-view");
    }

    public void setComparisonExperiment(Experiment comparisonExperiment) {
        this.comparisonExperiment = comparisonExperiment;
        isComparisonMode = true;
        addClassName("comparison-mode");
        panelsSplitWrapper.addThemeName("comparison-mode");
        middlePanel.addThemeName("comparison-mode");
        bottomPanel.addThemeName("comparison-mode");
        updateComparisonComponents();
        showCompareExperimentComponents(isComparisonMode);
        resizeChart();
    }

    public void leaveComparisonMode() {
        isComparisonMode = false;
        removeClassName("comparison-mode");
        panelsSplitWrapper.removeThemeName("comparison-mode");
        middlePanel.removeThemeName("comparison-mode");
        bottomPanel.removeThemeName("comparison-mode");
        showCompareExperimentComponents(isComparisonMode);
        resizeChart();
    }

    public boolean isComparisonMode() {
        return isComparisonMode;
    }

    public Experiment getComparisonExperiment() {
        return comparisonExperiment;
    }

    public void updateComparisonComponents() {
        comparisonExperimentComponents.forEach(comparisonExperimentComponent -> comparisonExperimentComponent
                .setExperiment(this.comparisonExperiment));
    }

    @Override
    protected void addEventBusSubscribers() {
        EventBus.subscribe(this, getUISupplier(), getViewSubscribers());
    }

    protected List<EventBusSubscriber> getViewSubscribers() {
        return List.of(
                new ExperimentViewFavoriteSubscriber(this),
                new ExperimentViewPolicyUpdateSubscriber(this, experimentDAO),
                new ExperimentViewRunUpdateSubscriber(this, experimentDAO),
                new ExperimentViewComparisonExperimentArchivedSubscriber(this));
    }

    @Override
    protected Component getMainContent() {
        Span modelNeedToBeUpdatedLabel = modelCheckerService.createInvalidErrorLabel(experiment.getModel());

        compareExperimentVerticalLayout = getComparisonExperimentPanel();

        middlePanel = getMiddlePanel();
        bottomPanel = getBottomPanel();
        panelsSplitWrapper = WrapperUtils.wrapCenterAlignmentFullSplitLayoutVertical(
                middlePanel, bottomPanel, 40);

        SplitLayout experimentContent = WrapperUtils.wrapCenterAlignmentFullSplitLayoutHorizontal(
                WrapperUtils.wrapVerticalWithNoPaddingOrSpacingAndWidthAuto(
                    stoppedTrainingNotification,
                    experimentTitleBar,
                    panelsSplitWrapper),
                compareExperimentVerticalLayout);
        experimentContent.addClassName("view-section");
        experimentContent.addSplitterDragendListener(resizeChartOnDrag());
        panelsSplitWrapper.addSplitterDragendListener(resizeChartOnDrag());
        bottomPanel.addSplitterDragendListener(resizeChartOnDrag());
        showCompareExperimentComponents(isComparisonMode);

        VerticalLayout experimentContentWrapper = WrapperUtils.wrapVerticalWithNoPaddingOrSpacingAndWidthAuto(
            modelNeedToBeUpdatedLabel, experimentContent
        );
        experimentContentWrapper.setWidthFull();

        HorizontalLayout pageWrapper = isShowNavBar() ? 
                WrapperUtils.wrapWidthFullHorizontal(experimentsNavbar, experimentContentWrapper)
                : WrapperUtils.wrapSizeFullHorizontal(experimentContentWrapper);
        pageWrapper.addClassName("page-content");
        pageWrapper.setSpacing(false);
        return pageWrapper;
    }

    private VerticalLayout getComparisonExperimentPanel() {
        comparisonModeCloseButton = new FloatingCloseButton("Exit Comparison Mode", () -> {
            leaveComparisonMode();
            resizeChart();
        });
        VerticalLayout comparisonComponents = WrapperUtils.wrapVerticalWithNoPaddingOrSpacingAndWidthAuto(
            WrapperUtils.wrapCenterAlignmentFullSplitLayoutHorizontal(
                    generateSimulationsMetricsPanelGroup(comparisonSimulationMetricsPanel),
                    comparisonObservationsPanel,
                    60),
            generateRewardFunctionGroup(comparisonCodeViewer),
            comparisonChartsPanel,
            comparisonNotesField);
        comparisonComponents.addClassName("comparison-panel");
        comparisonComponents.setPadding(false);
        VerticalLayout comparisonPanel = WrapperUtils.wrapVerticalWithNoPaddingOrSpacingAndWidthAuto(
            comparisonStoppedTrainingNotification,
            comparisonTitleBar,
            comparisonComponents,
            comparisonModeCloseButton);
        return comparisonPanel;
    }

    /**
     * This is a temporary solution until the experiment view refactoring is completed. It's done this way because until the subscribers
     * are properly broken up it's almost impossible to break out the navbar specific eventbus handling and as a result all navbar
     * code is going to be wrapped around if's which the SharedExperimentView will override to false.
     */
    protected boolean isShowNavBar() {
        return true;
    }

    private SplitLayout getMiddlePanel() {
        SplitLayout middlePanel = WrapperUtils.wrapCenterAlignmentFullSplitLayoutHorizontal(
                WrapperUtils.wrapCenterAlignmentFullSplitLayoutHorizontal(
                        generateSimulationsMetricsPanelGroup(experimentSimulationMetricsPanel),
                        experimentObservationsPanel,
                        70),
                generateRewardFunctionGroup(experimentCodeViewer),
                40);
        middlePanel.addClassName("middle-panel");
        return middlePanel;
    }

    private VerticalLayout generateRewardFunctionGroup(CodeViewer codeViewer) {
        return WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(
                LabelFactory.createLabel("Reward Function", BOLD_LABEL),
                codeViewer
        );
    }

    private VerticalLayout generateSimulationsMetricsPanelGroup(SimulationMetricsPanel simulationMetricsPanel) {
        String simulationMetricsHeaderText = "Simulation Metrics";
        HorizontalLayout simulationMetricsHeader = WrapperUtils.wrapWidthFullBetweenHorizontal(
                LabelFactory.createLabel(simulationMetricsHeaderText, BOLD_LABEL), 
                new SimulationMetricsInfoLink());
        simulationMetricsHeader.addClassName("simulation-metrics-panel-header");
        return WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(
                simulationMetricsHeader, simulationMetricsPanel);
    }

    private void resizeChart() {
        getElement().executeJs("window.dispatchEvent(new Event('resize'))");
    }

    private ComponentEventListener<SplitterDragendEvent<SplitLayout>> resizeChartOnDrag() {
        return dragend -> resizeChart();
    }

    public Object getComparisonExperimentLock() {
        return comparisonExperimentLock;
    }

    /**
     * This is overwritten by ShareExperimentView where we only want a subset of buttons.
     */
    protected ExperimentTitleBar createExperimentTitleBar() {
        return new ExperimentTitleBar(this, () -> updateComponents(), () -> getExperimentLock(), runDAO, trainingService, modelService, getUISupplier());
    }

    private ExperimentTitleBar createComparisonExperimentTitleBar() {
        return new ExperimentTitleBar(this, () -> updateComparisonComponents(), () -> getComparisonExperimentLock(), runDAO, trainingService, modelService, getUISupplier());
    }

    private SplitLayout getBottomPanel() {
        SplitLayout bottomPanel = WrapperUtils.wrapCenterAlignmentFullSplitLayoutHorizontal(
                experimentChartsPanel,
                experimentNotesField,
                70);
        bottomPanel.addClassName("bottom-panel");
        return bottomPanel;
    }

    @Override
    protected boolean isValidViewForExperiment(BeforeEnterEvent event) {
        if(!experimentDAO.isDraftExperiment(experimentId)) {
            return true;
        } else {
            // If incorrect then we need to both use the event.forwardTo rather than ui.navigate otherwise it will continue to process the view.
            event.forwardTo(Routes.NEW_EXPERIMENT, experimentId);
            return false;
        }
    }

    public void showCompareExperimentComponents(boolean isCompareVisible) {
        compareExperimentVerticalLayout.setVisible(isCompareVisible);
    }

    @Override
    protected void createExperimentComponents() {
        experimentTitleBar = createExperimentTitleBar();
        experimentNotesField = createNotesField(() -> segmentIntegrator.updatedNotesExperimentView(), true, false);
        experimentNotesField.setSecondaryStyle(true);
        experimentTrainingStatusDetailsPanel = new TrainingStatusDetailsPanel(getUISupplier());
        experimentChartsPanel = new ExperimentChartsPanel(getUISupplier());
        experimentCodeViewer = new CodeViewer(getUISupplier());
        experimentSimulationMetricsPanel = new SimulationMetricsPanel(this);
        // This is an exception because the modelObservations are the same for all experiments in the same group.
        experimentObservationsPanel = new ObservationsPanel(experiment.getModelObservations(), true);
        stoppedTrainingNotification = new StoppedTrainingNotification(earlyStoppingUrl, alEngineErrorArticleUrl);

        experimentComponentList.addAll(List.of(
                experimentTitleBar,
                experimentNotesField,
                experimentTrainingStatusDetailsPanel,
                experimentChartsPanel,
                experimentCodeViewer,
                experimentSimulationMetricsPanel,
                experimentObservationsPanel,
                stoppedTrainingNotification));

        // We also need to create the experiment comparison components.
        createComparisonComponents();
    }

    protected void createComparisonComponents() {
        comparisonTitleBar = createComparisonExperimentTitleBar();
        comparisonNotesField = createNotesField(() -> segmentIntegrator.updatedNotesExperimentView(), true, false);
        comparisonNotesField.setSecondaryStyle(true);
        comparisonChartsPanel = new ExperimentChartsPanel(getUISupplier());
        comparisonCodeViewer = new CodeViewer(getUISupplier());
        comparisonSimulationMetricsPanel = new SimulationMetricsPanel(this);
        // This is an exception because the modelObservations are the same for all experiments in the same group.
        comparisonObservationsPanel = new ObservationsPanel(experiment.getModelObservations(), true);
        comparisonStoppedTrainingNotification = new StoppedTrainingNotification(earlyStoppingUrl, alEngineErrorArticleUrl);

        comparisonExperimentComponents.addAll(List.of(
                comparisonTitleBar,
                comparisonNotesField,
                comparisonChartsPanel,
                comparisonCodeViewer,
                comparisonSimulationMetricsPanel,
                comparisonObservationsPanel,
                comparisonStoppedTrainingNotification));
    }
}
