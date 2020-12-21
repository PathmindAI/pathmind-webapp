package io.skymind.pathmind.webapp.ui.views.experiment;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.featureflag.Feature;
import io.skymind.pathmind.shared.featureflag.FeatureManager;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.shared.utils.ModelUtils;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.bus.EventBusSubscriber;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.alp.DownloadModelAlpLink;
import io.skymind.pathmind.webapp.ui.components.atoms.TagLabel;
import io.skymind.pathmind.webapp.ui.components.modelChecker.ModelCheckerService;
import io.skymind.pathmind.webapp.ui.components.observations.ObservationsPanel;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.utils.GuiUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.actions.experiment.ExportPolicyAction;
import io.skymind.pathmind.webapp.ui.views.experiment.actions.experiment.RestartTrainingAction;
import io.skymind.pathmind.webapp.ui.views.experiment.actions.experiment.ShareWithSupportAction;
import io.skymind.pathmind.webapp.ui.views.experiment.actions.experiment.StopTrainingAction;
import io.skymind.pathmind.webapp.ui.views.experiment.actions.shared.UnarchiveExperimentAction;
import io.skymind.pathmind.webapp.ui.views.experiment.components.ExperimentComponent;
import io.skymind.pathmind.webapp.ui.views.experiment.components.chart.ExperimentChartsPanel;
import io.skymind.pathmind.webapp.ui.views.experiment.components.codeViewer.CodeViewer;
import io.skymind.pathmind.webapp.ui.views.experiment.components.experimentNotes.ExperimentNotesField;
import io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.action.ExperimentSelectAction;
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

    private Button exportPolicyButton;
    private Button stopTrainingButton;
    private Button unarchiveExperimentButton;
    private Anchor downloadModelAlpLink;
    private Button shareButton;

    private TagLabel archivedLabel;
    private TagLabel sharedWithSupportLabel;

    private VerticalLayout compareExperimentVerticalLayout;

    private StoppedTrainingNotification stoppedTrainingNotification;

    // Experiment Components
    protected ExperimentNotesField experimentNotesField;
    private CodeViewer experimentCodeViewer;
    private ExperimentChartsPanel experimentChartsPanel;
    private ObservationsPanel experimentObservationsPanel;
    private TrainingStatusDetailsPanel experimentTrainingStatusDetailsPanel;
    private SimulationMetricsPanel experimentSimulationMetricsPanel;

    // Experiment Comparison Components
    private Boolean isComparisonMode = true;
    private ExperimentChartsPanel comparisonChartsPanel;
    protected ExperimentNotesField comparisonNotesField;
    private ObservationsPanel comparisonObservationsPanel;
    private CodeViewer comparisonCodeViewer;
    private SimulationMetricsPanel comparisonSimulationMetricsPanel;

    @Autowired
    private FeatureManager featureManager;
    @Autowired
    private ModelCheckerService modelCheckerService;
    @Value("${pathmind.early-stopping.url}")
    private String earlyStoppingUrl;

    private Button restartTrainingButton;

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
        updateComparisonComponents();
    }

    public Experiment getComparisonExperiment() {
        return comparisonExperiment;
    }

    public void updateComparisonComponents() {
        comparisonExperimentComponents.forEach(comparisonExperimentComponent -> comparisonExperimentComponent.setExperiment(this.experiment));
    }

    @Override
    protected void onAttach(AttachEvent event) {
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
    protected BiConsumer<Experiment, DefaultExperimentView> getNavBarSelectedExperimentAction() {
        return (experiment, defaultExperimentView) -> ExperimentSelectAction.selectExperiment(experiment, defaultExperimentView);
    }

    @Override
    protected Component getMainContent() {
        archivedLabel = new TagLabel("Archived", false, "small");
        sharedWithSupportLabel = new TagLabel("Shared with Support", true, "small");

        Span modelNeedToBeUpdatedLabel = modelCheckerService.createInvalidErrorLabel(experiment.getModel());
        modelNeedToBeUpdatedLabel.getStyle().set("margin-top", "2px");

        // It is the same for all experiments from the same model so it doesn't have to be updated as long
        // as the user is on the Experiment View (the nav bar only allows navigation to experiments from the same model)
        // If in the future we allow navigation to experiments from other models, then we'll need to update the button accordingly on navigation
        downloadModelAlpLink = new DownloadModelAlpLink(experiment.getProject().getName(), experiment.getModel(), modelService, segmentIntegrator);

        compareExperimentVerticalLayout = getComparisonExperimentPanel();

        HorizontalLayout titleBar = WrapperUtils.wrapWidthFullHorizontal(
                WrapperUtils.wrapVerticalWithNoPaddingOrSpacingAndWidthAuto(
                        experimentPanelTitle, archivedLabel, sharedWithSupportLabel),
                downloadModelAlpLink, experimentTrainingStatusDetailsPanel, getButtonsWrapper());
        titleBar.setPadding(true);

        SplitLayout experimentContent = WrapperUtils.wrapCenterAlignmentFullSplitLayoutHorizontal(
                WrapperUtils.wrapVerticalWithNoPaddingOrSpacingAndWidthAuto(
                    titleBar,
                    stoppedTrainingNotification,
                    modelNeedToBeUpdatedLabel,
                    getMiddlePanel(),
                    getBottomPanel()
                ),
                WrapperUtils.wrapVerticalWithNoPaddingOrSpacingAndWidthAuto(
                        titleBar,
                        stoppedTrainingNotification,
                        modelNeedToBeUpdatedLabel,
                        compareExperimentVerticalLayout));
        experimentContent.addClassName("view-section");
        if (isComparisonMode) {
            experimentContent.addClassName("comparison-mode");
        }
        HorizontalLayout pageWrapper = isShowNavBar() ? WrapperUtils.wrapWidthFullHorizontal(experimentsNavbar, experimentContent) : WrapperUtils.wrapSizeFullHorizontal(experimentContent);
        pageWrapper.addClassName("page-content");
        pageWrapper.setSpacing(false);
        return pageWrapper;
    }

    private VerticalLayout getComparisonExperimentPanel() {
        VerticalLayout comparisonPanel = WrapperUtils.wrapVerticalWithNoPaddingOrSpacingAndWidthAuto(
                WrapperUtils.wrapCenterAlignmentFullSplitLayoutHorizontal(
                        generateSimulationsMetricsPanelGroup(comparisonSimulationMetricsPanel),
                        comparisonObservationsPanel,
                        60),
                generateRewardFunctionGroup(comparisonCodeViewer),
                comparisonChartsPanel,
                comparisonNotesField);
        comparisonPanel.addClassName("comparison-panel");
        comparisonPanel.setPadding(false);
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
        boolean showSimulationMetrics = featureManager.isEnabled(Feature.SIMULATION_METRICS);
        String simulationMetricsHeaderText = showSimulationMetrics ? "Simulation Metrics" : "Reward Variables";
        return WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(
                LabelFactory.createLabel(simulationMetricsHeaderText, BOLD_LABEL), simulationMetricsPanel
        );
    }

    protected Div getButtonsWrapper() {
        createButtons();
        restartTrainingButton.setVisible(false);
        restartTrainingButton.addClassNames("large-image-btn", "run");
        stopTrainingButton.setVisible(true);

        Div buttonsWrapper = new Div(getActionButtonList());
        buttonsWrapper.addClassName("buttons-wrapper");
        return buttonsWrapper;
    }

    private void createButtons() {
        restartTrainingButton = new Button("Restart Training", click -> RestartTrainingAction.restartTraining(this, runDAO, trainingService));
        stopTrainingButton = new Button("Stop Training", click -> StopTrainingAction.stopTraining(this, trainingService, stopTrainingButton));
        shareButton = new Button("Share with support", click -> ShareWithSupportAction.shareWithSupportAction(experimentDAO, experiment, sharedWithSupportLabel, shareButton));
        unarchiveExperimentButton = GuiUtils.getPrimaryButton("Unarchive", VaadinIcon.ARROW_BACKWARD.create(), click -> UnarchiveExperimentAction.unarchiveExperiment(experimentDAO, experiment, segmentIntegrator, getUI()));
        exportPolicyButton = GuiUtils.getPrimaryButton("Export Policy", click -> ExportPolicyAction.exportPolicy(experiment, getUI()), false);
    }

    /**
     * This is overwritten by ShareExperimentView where we only want a subset of buttons.
     */
    protected Component[] getActionButtonList() {
        return new Button[]{
                unarchiveExperimentButton,
                restartTrainingButton,
                stopTrainingButton,
                shareButton,
                exportPolicyButton};
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

    /************************************** UI element creations are above this line **************************************/

    @Override
    protected void updateComponentEnablements() {
        unarchiveExperimentButton.setVisible(experiment.isArchived());
        exportPolicyButton.setVisible(experiment.isTrainingCompleted() && experiment.getBestPolicy() != null && experiment.getBestPolicy().hasFile());
        stopTrainingButton.setVisible(experiment.isTrainingRunning());
        restartTrainingButton.setVisible(false);

        boolean allowRestart = experiment.isAllowRestartTraining() && ModelUtils.isValidModel(experiment.getModel());
        restartTrainingButton.setVisible(allowRestart);
        restartTrainingButton.setEnabled(allowRestart);

        archivedLabel.setVisible(experiment.isArchived());

        // Update components with SharedExperimentView (share through support).
        sharedWithSupportLabel.setVisible(experiment.isSharedWithSupport());
        shareButton.setVisible(!experiment.isSharedWithSupport());
    }

    @Override
    protected boolean isValidViewForExperiment() {
        if(experimentDAO.isDraftExperiment(experimentId)) {
            // TODO -> STEPH -> Why is this not forwarding correctly to the right page? Is there a ui.navigate somewhere else?
            // For some reason I have to use UI.getCurrent() rather than getUI().ifPresent() because it's the only way to navigate at this stage.
            UI.getCurrent().navigate(NewExperimentView.class, experimentId);
            return false;
        } else {
            return true;
        }
    }

    public void showCompareExperimentComponents(boolean isCompareVisible) {
        compareExperimentVerticalLayout.setVisible(isCompareVisible);
    }

    @Override
    protected void createExperimentComponents() {
        experimentNotesField = createNotesField(() -> segmentIntegrator.addedNotesNewExperimentView());
        experimentTrainingStatusDetailsPanel = new TrainingStatusDetailsPanel(getUISupplier());
        experimentChartsPanel = new ExperimentChartsPanel(getUISupplier());
        experimentCodeViewer = new CodeViewer(getUISupplier());
        experimentSimulationMetricsPanel = new SimulationMetricsPanel(featureManager.isEnabled(Feature.SIMULATION_METRICS), getUISupplier());
        // This is an exception because the modelObservations are the same for all experiments in the same group.
        experimentObservationsPanel = new ObservationsPanel(experiment.getModelObservations(), true);
        stoppedTrainingNotification = new StoppedTrainingNotification(earlyStoppingUrl);

        experimentComponentList.addAll(List.of(
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
        comparisonNotesField = createNotesField(() -> segmentIntegrator.addedNotesNewExperimentView());
        comparisonChartsPanel = new ExperimentChartsPanel(getUISupplier());
        comparisonCodeViewer = new CodeViewer(getUISupplier());
        comparisonSimulationMetricsPanel = new SimulationMetricsPanel(featureManager.isEnabled(Feature.SIMULATION_METRICS), getUISupplier());
        // This is an exception because the modelObservations are the same for all experiments in the same group.
        comparisonObservationsPanel = new ObservationsPanel(experiment.getModelObservations(), true);

        comparisonExperimentComponents.addAll(List.of(
                comparisonNotesField,
                comparisonChartsPanel,
                comparisonCodeViewer,
                comparisonSimulationMetricsPanel,
                comparisonObservationsPanel));
    }
}
