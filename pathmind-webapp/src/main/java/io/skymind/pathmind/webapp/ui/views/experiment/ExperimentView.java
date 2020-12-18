package io.skymind.pathmind.webapp.ui.views.experiment;

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
import io.skymind.pathmind.webapp.ui.views.experiment.actions.experiment.CompareExperimentAction;
import io.skymind.pathmind.webapp.ui.views.experiment.actions.experiment.ExportPolicyAction;
import io.skymind.pathmind.webapp.ui.views.experiment.actions.experiment.RestartTrainingAction;
import io.skymind.pathmind.webapp.ui.views.experiment.actions.experiment.ShareWithSupportAction;
import io.skymind.pathmind.webapp.ui.views.experiment.actions.experiment.StopTrainingAction;
import io.skymind.pathmind.webapp.ui.views.experiment.actions.shared.UnarchiveExperimentAction;
import io.skymind.pathmind.webapp.ui.views.experiment.components.chart.ExperimentChartsPanel;
import io.skymind.pathmind.webapp.ui.views.experiment.components.codeViewer.CodeViewer;
import io.skymind.pathmind.webapp.ui.views.experiment.components.experimentNotes.ExperimentNotesField;
import io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.action.ExperimentSelectAction;
import io.skymind.pathmind.webapp.ui.views.experiment.components.notification.StoppedTrainingNotification;
import io.skymind.pathmind.webapp.ui.views.experiment.components.simulationMetrics.SimulationMetricsPanel;
import io.skymind.pathmind.webapp.ui.views.experiment.components.trainingStatus.TrainingStatusDetailsPanel;
import io.skymind.pathmind.webapp.ui.views.experiment.subscribers.main.experiment.ExperimentViewPolicyUpdateSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.subscribers.main.experiment.ExperimentViewRunUpdateSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.subscribers.view.experiment.ExperimentViewExperimentCompareViewSubscriber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.BOLD_LABEL;

@Route(value = Routes.EXPERIMENT_URL, layout = MainLayout.class)
@Slf4j
public class ExperimentView extends DefaultExperimentView {

    // We have to use a lock object rather than the experiment because we are changing it's reference which makes it not thread safe. As well we cannot lock
    // on this because part of the synchronization is in the eventbus listener in a subclass (which is also why we can't use synchronize on the method.
    private Object experimentLock = new Object();
    private Button exportPolicyButton;
    private Button stopTrainingButton;
    private Button unarchiveExperimentButton;
    private Anchor downloadModelAlpLink;
    private Button shareButton;

    // TODO -> STEPH -> Are there class instances we may not need references to here like those I just committed. For example do we really
    // a reference to middlePanel or is it just a temporary instance that can be passed as a return value in a method?
    private HorizontalLayout middlePanel;
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
    // TODO -> STEPH -> Only needed because I haven't yet pushed the comparison code to a subscriber.
    private SimulationMetricsPanel comparisonSimulationMetricsPanel;

    @Autowired
    private FeatureManager featureManager;
    @Autowired
    private ModelCheckerService modelCheckerService;
    @Value("${pathmind.early-stopping.url}")
    private String earlyStoppingUrl;

    private Button restartTrainingButton;

    private CompareExperimentAction compareExperimentAction;

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

    // TODO -> STEPH -> Confirm that all subscribers in the experiment view are actually still required.
    // TODO -> STEPH -> Look at all onAttach and onDetach to remove any that are no longer needed.
    @Override
    protected void onAttach(AttachEvent event) {
        EventBus.subscribe(this, () -> getUI(),
                getViewSubscribers());
    }

    protected List<EventBusSubscriber> getViewSubscribers() {
        return List.of(
                new ExperimentViewPolicyUpdateSubscriber(this),
                new ExperimentViewRunUpdateSubscriber(this),
                new ExperimentViewExperimentCompareViewSubscriber(this));
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
        // TODO -> STEPH -> Temporary solution until we have the action buttons ready to be hooked.
        compareExperimentAction = new CompareExperimentAction(this, experimentDAO);

        archivedLabel = new TagLabel("Archived", false, "small");
        sharedWithSupportLabel = new TagLabel("Shared with Support", true, "small");

        setupExperimentContentPanel();

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
                    middlePanel,
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

    private void setupExperimentContentPanel() {
        middlePanel = WrapperUtils.wrapWidthFullHorizontal();
        middlePanel.add(
                WrapperUtils.wrapCenterAlignmentFullSplitLayoutHorizontal(
                        generateSimulationsMetricsPanelGroup(experimentSimulationMetricsPanel),
                        experimentObservationsPanel,
                        60),
                generateRewardFunctionGroup(experimentCodeViewer));
        middlePanel.addClassName("middle-panel");
        middlePanel.setPadding(false);
        middlePanel.setSpacing(false);
    }

    private VerticalLayout generateRewardFunctionGroup(CodeViewer codeViewer) {
        return WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(
                LabelFactory.createLabel("Reward Function", BOLD_LABEL),
                codeViewer
        );
    }

    private VerticalLayout generateSimulationsMetricsPanelGroup(SimulationMetricsPanel simulationMetricsPanel) {
        // TODO -> STEPH -> Can we combine logic somewhere so that it's just one place. The naming isn't clear what is what to me.
        boolean showSimulationMetrics = featureManager.isEnabled(Feature.SIMULATION_METRICS);
        String simulationMetricsHeaderText = showSimulationMetrics ? "Simulation Metrics" : "Reward Variables";
        return WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(
                LabelFactory.createLabel(simulationMetricsHeaderText, BOLD_LABEL), simulationMetricsPanel
        );
    }

    protected Div getButtonsWrapper() {
        createButtons();
        // TODO -> STEPH -> Experiment lock may need to be re-introduced for racing conditions between update and laoding/switching experiments.
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

        // TODO -> STEPH -> Do we need both visible and enabled since if it's not visible there's nothing to click?
        boolean allowRestart = experiment.isAllowRestartTraining() && ModelUtils.isValidModel(experiment.getModel());
        restartTrainingButton.setVisible(allowRestart);
        restartTrainingButton.setEnabled(allowRestart);

        archivedLabel.setVisible(experiment.isArchived());

        // Update components with SharedExperimentView (share through support).
        sharedWithSupportLabel.setVisible(experiment.isSharedWithSupport());
        shareButton.setVisible(!experiment.isSharedWithSupport());
    }

    @Override
    protected void validateCorrectViewForExperiment() {
        if(experiment.isDraft()) {
            // TODO -> STEPH -> Why is this not forwarding correctly to the right page? Is there a ui.navigate somewhere else?
            // For some reason I have to use UI.getCurrent() rather than getUI().ifPresent() because it's the only way to navigate at this stage.
            UI.getCurrent().navigate(NewExperimentView.class, experimentId);
        }
    }

    public Object getExperimentLock() {
        return experimentLock;
    }

    public void stopCompareExperiment() {
        compareExperimentVerticalLayout.setVisible(false);
    }

    public void showCompareExperimentComponents(boolean isCompareVisible) {
        compareExperimentVerticalLayout.setVisible(isCompareVisible);
    }

    // TODO -> STEPH -> On experiment switch for now we'll just reload the components with the experiment that's being switched and
    // put it to invisible. We'll set it to null when the null checks for all the components are ready.
    public void startCompareExperiment(Experiment experimentToCompare) {
        compareExperimentAction.compare(experimentToCompare);
    }

    @Override
    protected void createExperimentComponents() {
        // TODO -> STEPH -> create other components
        experimentNotesField = createNotesField(() -> segmentIntegrator.addedNotesNewExperimentView());
        experimentTrainingStatusDetailsPanel = new TrainingStatusDetailsPanel(() -> getUI());
        experimentChartsPanel = new ExperimentChartsPanel(() -> getUI());
        experimentCodeViewer = new CodeViewer(() -> getUI());
        // TODO -> STEPH -> We need to be able to create the observations table without experiment in the constructor if possible.
        experimentSimulationMetricsPanel = new SimulationMetricsPanel(featureManager.isEnabled(Feature.SIMULATION_METRICS), experiment, () -> getUI());
        experimentObservationsPanel = new ObservationsPanel(experiment, true);
        stoppedTrainingNotification = new StoppedTrainingNotification(earlyStoppingUrl);

        experimentComponentList.addAll(List.of(
                experimentNotesField,
                experimentTrainingStatusDetailsPanel,
                experimentChartsPanel,
                experimentCodeViewer,
                experimentSimulationMetricsPanel,
                experimentObservationsPanel,
                stoppedTrainingNotification));
    }

    @Override
    protected void createComparisonComponents() {
        // TODO -> STEPH -> Possibly found bug? -> In dev if you do url/experiment/newExperimentID it loads but doesn't change the URL.
        // TODO -> STEPH -> Not all events needs to clone all data such as the experiment chart data.
        // TODO -> STEPH -> Should we even clone event data like experiments when in most cases we want to replace them. Especially
        // if we do the processing before the event is fired so it's done only once. Yes each component can alter the instance
        // but if it's done before the event is fired then it saves every component from having to update each instance separately, not
        // to mention all the instance creations and destructions.
        comparisonChartsPanel = new ExperimentChartsPanel(() -> getUI());
        comparisonNotesField = createNotesField(() -> segmentIntegrator.addedNotesNewExperimentView());
        comparisonCodeViewer = new CodeViewer(() -> getUI());
        comparisonSimulationMetricsPanel = new SimulationMetricsPanel(featureManager.isEnabled(Feature.SIMULATION_METRICS), experiment, () -> getUI());
        // TODO -> STEPH -> Below are the components that should not include experiment as part of the constructor because it could be null for the comparison view.
        comparisonObservationsPanel = new ObservationsPanel(experiment, true);

        comparisonExperimentComponents.addAll(List.of(
                comparisonNotesField,
                comparisonChartsPanel,
                comparisonObservationsPanel,
                comparisonCodeViewer,
                comparisonSimulationMetricsPanel));
    }
}
