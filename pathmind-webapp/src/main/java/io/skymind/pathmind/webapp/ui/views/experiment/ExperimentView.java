package io.skymind.pathmind.webapp.ui.views.experiment;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
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
import io.skymind.pathmind.webapp.ui.views.experiment.components.chart.ExperimentChartsPanel;
import io.skymind.pathmind.webapp.ui.views.experiment.components.codeViewer.CodeViewer;
import io.skymind.pathmind.webapp.ui.views.experiment.components.experimentNotes.ExperimentNotesField;
import io.skymind.pathmind.webapp.ui.views.experiment.components.notification.StoppedTrainingNotification;
import io.skymind.pathmind.webapp.ui.views.experiment.components.simulationMetrics.SimulationMetricsPanel;
import io.skymind.pathmind.webapp.ui.views.experiment.components.trainingStatus.TrainingStatusDetailsPanel;
import io.skymind.pathmind.webapp.ui.views.experiment.subscribers.main.experiment.ExperimentViewPolicyUpdateSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.subscribers.main.experiment.ExperimentViewRunUpdateSubscriber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.BOLD_LABEL;

@Route(value = Routes.EXPERIMENT_URL, layout = MainLayout.class)
@Slf4j
public class ExperimentView extends DefaultExperimentView {

    // Similar to DefaultExperimentView in that we have to use a lock object rather than the (comparison) experiment because we are changing it's reference which
    // makes it not thread safe. As well we cannot lock on this because part of the synchronization is in the eventbus listener in a subclass (which is also
    // why we can't use synchronize on the method).
    private Object comparisonExperimentLock = new Object();

    private VerticalLayout compareExperimentVerticalLayout;

    private StoppedTrainingNotification stoppedTrainingNotification;
    private StoppedTrainingNotification comparisonStoppedTrainingNotification;

    // Experiment Components
    private ExperimentTitleBar experimentTitleBar;
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

    @Autowired
    private ModelCheckerService modelCheckerService;
    @Value("${pathmind.early-stopping.url}")
    private String earlyStoppingUrl;
    @Value("${pathmind.al-engine-error-article.url}")
    private String alEngineErrorArticleUrl;

    // Although this is really only for the experiment view it's a lot simpler to put it at the parent level otherwise a lot of methods would have to be overriden in ExperimentView.
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
        updateComparisonComponents();
        showCompareExperimentComponents(isComparisonMode);
    }

    private void leaveComparisonMode() {
        isComparisonMode = false;
        removeClassName("comparison-mode");
        showCompareExperimentComponents(isComparisonMode);
    }

    public Experiment getComparisonExperiment() {
        return comparisonExperiment;
    }

    public void updateComparisonComponents() {
        comparisonExperimentComponents.forEach(comparisonExperimentComponent -> comparisonExperimentComponent.setExperiment(this.comparisonExperiment));
    }

    @Override
    protected void addEventBusSubscribers() {
        EventBus.subscribe(this, getUISupplier(),
                getViewSubscribers());
    }

    protected List<EventBusSubscriber> getViewSubscribers() {
        return List.of(
                new ExperimentViewPolicyUpdateSubscriber(this, experimentDAO),
                new ExperimentViewRunUpdateSubscriber(this, experimentDAO));
    }

    @Override
    protected void onDetach(DetachEvent event) {
        EventBus.unsubscribe(this);
    }

    @Override
    protected Component getMainContent() {
        Span modelNeedToBeUpdatedLabel = modelCheckerService.createInvalidErrorLabel(experiment.getModel());

        compareExperimentVerticalLayout = getComparisonExperimentPanel();

        HorizontalLayout titleBar = createTitleBar();

        SplitLayout experimentContent = WrapperUtils.wrapCenterAlignmentFullSplitLayoutHorizontal(
                WrapperUtils.wrapVerticalWithNoPaddingOrSpacingAndWidthAuto(
                    stoppedTrainingNotification,
                    titleBar,
                    getMiddlePanel(),
                    getBottomPanel()),
                compareExperimentVerticalLayout);
        experimentContent.addClassName("view-section");
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

    private HorizontalLayout createTitleBar() {
        return experimentTitleBar;
    }

    private VerticalLayout getComparisonExperimentPanel() {
        FloatingCloseButton comparisonModeCloseButton = new FloatingCloseButton("Exit Comparison Mode", () -> leaveComparisonMode());
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

    private HorizontalLayout getMiddlePanel() {
        HorizontalLayout middlePanel = WrapperUtils.wrapWidthFullHorizontal();
        middlePanel.add(
                WrapperUtils.wrapCenterAlignmentFullSplitLayoutHorizontal(
                        generateSimulationsMetricsPanelGroup(experimentSimulationMetricsPanel),
                        experimentObservationsPanel,
                        60),
                generateRewardFunctionGroup(experimentCodeViewer));
        middlePanel.addClassName("middle-panel");
        middlePanel.setPadding(false);
        middlePanel.setSpacing(false);
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
        return WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(
                LabelFactory.createLabel(simulationMetricsHeaderText, BOLD_LABEL), simulationMetricsPanel
        );
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

    private HorizontalLayout getBottomPanel() {
        HorizontalLayout bottomPanel = WrapperUtils.wrapWidthFullHorizontal(
                experimentChartsPanel,
                experimentNotesField);
        bottomPanel.addClassName("bottom-panel");
        bottomPanel.setPadding(false);
        bottomPanel.setSpacing(false);
        return bottomPanel;
    }

    @Override
    protected boolean isValidViewForExperiment(BeforeEnterEvent event) {
        if(!experimentDAO.isDraftExperiment(experimentId)) {
            return true;
        } else {
            // If incorrect then we need to both use the event.forwardTo rather than ui.navigate otherwise it will continue to process the view.
            event.forwardTo(Routes.NEW_EXPERIMENT_URL, experimentId);
            return false;
        }
    }

    public void showCompareExperimentComponents(boolean isCompareVisible) {
        compareExperimentVerticalLayout.setVisible(isCompareVisible);
    }

    @Override
    protected void createExperimentComponents() {
        experimentTitleBar = createExperimentTitleBar();
        experimentNotesField = createNotesField(() -> segmentIntegrator.updatedNotesExperimentView(), false);
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
        comparisonNotesField = createNotesField(() -> segmentIntegrator.updatedNotesExperimentView(), false);
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
