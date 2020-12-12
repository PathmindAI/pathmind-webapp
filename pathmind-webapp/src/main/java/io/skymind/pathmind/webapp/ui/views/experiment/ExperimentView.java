package io.skymind.pathmind.webapp.ui.views.experiment;

import java.util.List;
import java.util.Optional;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.shared.data.user.UserCaps;
import io.skymind.pathmind.shared.featureflag.Feature;
import io.skymind.pathmind.shared.featureflag.FeatureManager;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.shared.security.SecurityUtils;
import io.skymind.pathmind.shared.utils.ModelUtils;
import io.skymind.pathmind.shared.utils.PolicyUtils;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.bus.EventBusSubscriber;
import io.skymind.pathmind.webapp.bus.events.main.ExperimentUpdatedBusEvent;
import io.skymind.pathmind.webapp.bus.events.main.PolicyUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.events.main.RunUpdateBusEvent;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.exception.InvalidDataException;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.alp.DownloadModelAlpLink;
import io.skymind.pathmind.webapp.ui.components.atoms.TagLabel;
import io.skymind.pathmind.webapp.ui.components.modelChecker.ModelCheckerService;
import io.skymind.pathmind.webapp.ui.components.observations.ObservationsPanel;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.utils.ConfirmationUtils;
import io.skymind.pathmind.webapp.ui.utils.GuiUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.actions.experiment.CompareExperimentAction;
import io.skymind.pathmind.webapp.ui.views.experiment.components.chart.ExperimentChartsPanel;
import io.skymind.pathmind.webapp.ui.views.experiment.components.codeViewer.CodeViewer;
import io.skymind.pathmind.webapp.ui.views.experiment.components.experimentNotes.ExperimentNotesField;
import io.skymind.pathmind.webapp.ui.views.experiment.components.notification.StoppedTrainingNotification;
import io.skymind.pathmind.webapp.ui.views.experiment.components.observations.subscribers.view.ObservationsPanelExperimentSwitchedViewSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.simulationMetrics.SimulationMetricsPanel;
import io.skymind.pathmind.webapp.ui.views.experiment.components.trainingStatus.TrainingStatusDetailsPanel;
import io.skymind.pathmind.webapp.ui.views.experiment.subscribers.main.experiment.ExperimentViewPolicyUpdateSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.subscribers.main.experiment.ExperimentViewRunUpdateSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.subscribers.view.experiment.ExperimentViewExperimentCompareViewSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.subscribers.view.experiment.ExperimentViewExperimentSwitchedViewSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.utils.ExperimentCapLimitVerifier;
import io.skymind.pathmind.webapp.ui.views.policy.ExportPolicyView;
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

    private List<RewardVariable> rewardVariables;
    private Policy bestPolicy;

    private UserCaps userCaps;

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
    private ObservationsPanel observationsPanel;
    private TrainingStatusDetailsPanel trainingStatusDetailsPanel;

    // Experiment Comparison Components
    private Boolean isComparisonMode = true;
    private ExperimentChartsPanel comparisonChartsPanel;
    protected ExperimentNotesField comparisonNotesField;
    // TODO -> STEPH -> Same as above for ObservationsPanel
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
        this.userCaps = new UserCaps(newRunDailyLimit, newRunMonthlyLimit, newRunNotificationThreshold);
    }

    // Required for SharedExperimentView.
    protected ExperimentView() {
        super();
        addClassName("experiment-view");
    }

    @Override
    protected void onAttach(AttachEvent event) {
        EventBus.subscribe(this, () -> getUI(),
                getViewSubscribers());
    }

    protected List<EventBusSubscriber> getViewSubscribers() {
        return List.of(
                new ExperimentViewPolicyUpdateSubscriber(this),
                new ExperimentViewRunUpdateSubscriber(this),
                new ExperimentViewExperimentSwitchedViewSubscriber(this),
                new ExperimentViewExperimentCompareViewSubscriber(this),
                // TODO -> STEPH -> We should only need this once if it is moved to the ObservationsPanel.
                new ObservationsPanelExperimentSwitchedViewSubscriber(observationDAO, observationsPanel),
                new ObservationsPanelExperimentSwitchedViewSubscriber(observationDAO, comparisonObservationsPanel));
    }

    @Override
    protected void onDetach(DetachEvent event) {
        EventBus.unsubscribe(this);
    }

    @Override
    protected Component getMainContent() {
        // TODO -> STEPH -> Temporary solution until we have the action buttons ready to be hooked.
        compareExperimentAction = new CompareExperimentAction(this, experimentDAO, policyDAO);

        archivedLabel = new TagLabel("Archived", false, "small");
        sharedWithSupportLabel = new TagLabel("Shared with Support", true, "small");

        setupExperimentContentPanel();

        Span modelNeedToBeUpdatedLabel = modelCheckerService.createInvalidErrorLabel(experiment.getModel());
        modelNeedToBeUpdatedLabel.getStyle().set("margin-top", "2px");

        stoppedTrainingNotification = new StoppedTrainingNotification(earlyStoppingUrl);

        // It is the same for all experiments from the same model so it doesn't have to be updated as long
        // as the user is on the Experiment View (the nav bar only allows navigation to experiments from the same model)
        // If in the future we allow navigation to experiments from other models, then we'll need to update the button accordingly on navigation
        downloadModelAlpLink = new DownloadModelAlpLink(experiment.getProject().getName(), experiment.getModel(), modelService, segmentIntegrator);

        compareExperimentVerticalLayout = getComparisonExperimentPanel();

        HorizontalLayout titleBar = WrapperUtils.wrapWidthFullHorizontal(
                WrapperUtils.wrapVerticalWithNoPaddingOrSpacingAndWidthAuto(
                        experimentPanelTitle, archivedLabel, sharedWithSupportLabel),
                downloadModelAlpLink, trainingStatusDetailsPanel, getButtonsWrapper());
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
        VerticalLayout rewardFunctionGroup = generateRewardFunctionGroup(comparisonCodeViewer);
        VerticalLayout comparisonPanel = WrapperUtils.wrapVerticalWithNoPaddingOrSpacingAndWidthAuto(
                WrapperUtils.wrapCenterAlignmentFullSplitLayoutHorizontal(
                        generateSimulationsMetricsPanelGroup(comparisonSimulationMetricsPanel),
                        comparisonObservationsPanel,
                        60),
                rewardFunctionGroup,
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
        VerticalLayout rewardFunctionGroup = generateRewardFunctionGroup(experimentCodeViewer);

        // TODO -> STEPH -> Shouldn't be needed but until I move SimulationMetricsPanel comparison code is moved to a subscriber.
        SimulationMetricsPanel simulationMetricsPanel = new SimulationMetricsPanel(experiment, featureManager.isEnabled(Feature.SIMULATION_METRICS), rewardVariables, () -> getUI());

        observationsPanel = new ObservationsPanel(experiment, true);

        middlePanel = WrapperUtils.wrapWidthFullHorizontal();
        middlePanel.add(
                WrapperUtils.wrapCenterAlignmentFullSplitLayoutHorizontal(
                        generateSimulationsMetricsPanelGroup(simulationMetricsPanel),
                        observationsPanel,
                        60), 
                rewardFunctionGroup);
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
        // TODO -> STEPH -> Do we need this button or can it just call setExperiment() with some small adjustments?
        // TODO -> STEPH -> Let's also pull the action outside of the view for the button.
        restartTrainingButton = new Button("Restart Training", click -> {
            // TODO -> STEPH -> Why are we reloading everything? Can we not simplify this process?
            synchronized (experimentLock) {
                if (!ExperimentCapLimitVerifier.isUserWithinCapLimits(runDAO, userCaps, segmentIntegrator)) {
                    return;
                }
                trainingService.startRun(experiment);
                segmentIntegrator.restartTraining();
                initLoadData();
                // REFACTOR -> https://github.com/SkymindIO/pathmind-webapp/issues/2278
                trainingStatusDetailsPanel.setExperiment(experiment);
                updateButtonEnablement();
                experimentChartsPanel.setExperiment(experiment);
            }
        });
        restartTrainingButton.setVisible(false);
        restartTrainingButton.addClassNames("large-image-btn", "run");

        exportPolicyButton = GuiUtils.getPrimaryButton(
                "Export Policy",
                click -> getUI().ifPresent(ui -> ui.navigate(ExportPolicyView.class, bestPolicy.getId())),
                false);

        stopTrainingButton = new Button("Stop Training", click -> showStopTrainingConfirmationDialog());
        stopTrainingButton.setVisible(true);

        shareButton = new Button("Share with support", click -> showShareConfirmationDialog());

        unarchiveExperimentButton = GuiUtils.getPrimaryButton("Unarchive", VaadinIcon.ARROW_BACKWARD.create(), click -> unarchiveExperiment());

        Div buttonsWrapper = new Div(getActionButtonList());

        buttonsWrapper.addClassName("buttons-wrapper");
        return buttonsWrapper;
    }

    private void showShareConfirmationDialog() {
        ConfirmationUtils.confirmationPopupDialog(
                "Share training with support",
                "This will give Pathmind a read-only mode to the experiment to help with debugging any issues.",
                "Share Training",
                () -> {
                    experimentDAO.shareExperimentWithSupport(experiment.getId());
                    experiment.setSharedWithSupport(true);
                    setSharedWithSupportComponents();
                });
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

    private void showStopTrainingConfirmationDialog() {
        ConfirmationUtils.showStopTrainingConfirmationPopup(() -> {
            trainingService.stopRun(experiment);
            segmentIntegrator.stopTraining();
            stopTrainingButton.setVisible(false);
            fireEvents();
        });
    }

    private void unarchiveExperiment() {
        ConfirmationUtils.unarchive("experiment", () -> {
            ExperimentUtils.archiveExperiment(experimentDAO, experiment, false);
            segmentIntegrator.archived(Experiment.class, false);
            ExperimentUtils.navigateToExperiment(getUI(), experiment);
        });
    }

    /************************************** UI element creations are above this line **************************************/

    private void fireEvents() {
        // An event for each policy since we only need to update some of the policies in a run.
        if (experiment.getPolicies() != null && !experiment.getPolicies().isEmpty()) {
            EventBus.post(new PolicyUpdateBusEvent(experiment.getPolicies()));
        }
        // Send run updated event, meaning that all policies under the run is updated.
        // This is needed especially in dashboard, to refresh the item only once per run, instead of after all policy updates
        EventBus.post(new RunUpdateBusEvent(experiment.getRuns()));
        EventBus.post(new ExperimentUpdatedBusEvent(experiment));
    }

    // TODO -> STEPH -> On page load we should automatically forward to the right view if draft. This is currently not fully working. This code should
    // be in both NewExperimentView and ExperimentView when loading, before even loading any relational data from the database or any components.


    @Override
    protected void loadExperimentData() {
        experimentId = experiment.getId();
        // TODO -> STEPH -> RewardVariables should be loaded by parent class.
        rewardVariables = rewardVariableDAO.getRewardVariablesForModel(experiment.getModelId());
        // TODO -> STEPH -> This should be done in the experimentDAO class because we need it on every experiment page load here. And that logic is specific and
        // otherwise we have to remember to do this everytime we load up the experiment from the database and want the chart to display.
        experiment.setPolicies(policyDAO.getPoliciesForExperiment(experiment.getId()));
        bestPolicy = PolicyUtils.selectBestPolicy(experiment.getPolicies()).orElse(null);
    }

    private void setSharedWithSupportComponents() {
        sharedWithSupportLabel.setVisible(experiment.isSharedWithSupport());
        shareButton.setVisible(!experiment.isSharedWithSupport());
    }

    @Override
    protected void initScreen(BeforeEnterEvent event) {
        // Part of a bigger refactoring. The goal is to eventually remove the setExperiment code and use the ExperimentChangedViewSubscriber
        // instead because the logic and code is not the same between loading the initial screen and when updateScreenComponents is called. There's
        // a mix of logic and code between setExperiment and updateScreenComponents that is inconsistent. By splitting these off it should
        // hopefully make it easier to manage. Again this is just a first part, I'm (Steph) planning to split this code between the initial
        // load and any event updates as well as experiment select.

        // HERE HERE HERE -> Starting point after commit. Quick hack because after setExperiment this still needs to be loaded for now.
        updateScreenComponents();
    }

    protected void updateScreenComponents() {
        updateButtonEnablement();
        setSharedWithSupportComponents();
        updateDetailsForExperiment();
    }

    private void updateUIForError(boolean isAllowRestartTraining) {
        // Not used in the shared view but this code can be left as the buttons are not included in the shared view.
        boolean allowRestart = isAllowRestartTraining && ModelUtils.isValidModel(experiment.getModel());
        restartTrainingButton.setVisible(allowRestart);
        restartTrainingButton.setEnabled(allowRestart);
    }

    public void updateDetailsForExperiment() {
        updateButtonEnablement();
        archivedLabel.setVisible(experiment.isArchived());
        stoppedTrainingNotification.setExperiment(experiment);
        updateUIForError(experiment.isAllowRestartTraining());
    }

    private void updateButtonEnablement() {
        unarchiveExperimentButton.setVisible(experiment.isArchived());
        exportPolicyButton.setVisible(experiment.isTrainingCompleted() && hasBestPolicy());
        stopTrainingButton.setVisible(experiment.isTrainingRunning());
        restartTrainingButton.setVisible(false);
    }

    private boolean hasBestPolicy() {
        return bestPolicy != null && bestPolicy.hasFile();
    }

    public void setBestPolicy(Policy bestPolicy) {
        this.bestPolicy = bestPolicy;
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
        trainingStatusDetailsPanel = new TrainingStatusDetailsPanel(() -> getUI());
        experimentChartsPanel = new ExperimentChartsPanel(() -> getUI(), rewardVariables);
        experimentCodeViewer = new CodeViewer(() -> getUI());

        experimentComponentList.addAll(List.of(
                experimentNotesField,
                trainingStatusDetailsPanel,
                experimentChartsPanel,
                experimentCodeViewer));
    }

    @Override
    protected void createComparisonComponents() {
        // TODO -> STEPH -> Possibly found bug? -> In dev if you do url/experiment/newExperimentID it loads but doesn't change the URL.
        // TODO -> STEPH -> Not all events needs to clone all data such as the experiment chart data.
        // TODO -> STEPH -> Should we even clone event data like experiments when in most cases we want to replace them. Especially
        // if we do the processing before the event is fired so it's done only once. Yes each component can alter the instance
        // but if it's done before the event is fired then it saves every component from having to update each instance separately, not
        // to mention all the instance creations and destructions.
        comparisonChartsPanel = new ExperimentChartsPanel(() -> getUI(), rewardVariables);
        comparisonNotesField = createNotesField(() -> segmentIntegrator.addedNotesNewExperimentView());
        comparisonObservationsPanel = new ObservationsPanel(experiment, true);
        comparisonCodeViewer = new CodeViewer(() -> getUI());
        comparisonSimulationMetricsPanel = new SimulationMetricsPanel(experiment, featureManager.isEnabled(Feature.SIMULATION_METRICS), rewardVariables, () -> getUI());

        comparisonExperimentComponents.addAll(List.of(
                comparisonNotesField,
                comparisonChartsPanel,
                comparisonObservationsPanel,
                comparisonCodeViewer,
                comparisonSimulationMetricsPanel));
    }
}
