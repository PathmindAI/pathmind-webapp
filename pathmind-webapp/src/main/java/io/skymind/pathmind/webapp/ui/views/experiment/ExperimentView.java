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
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.db.dao.ObservationDAO;
import io.skymind.pathmind.db.dao.PolicyDAO;
import io.skymind.pathmind.db.dao.RewardVariableDAO;
import io.skymind.pathmind.db.dao.RunDAO;
import io.skymind.pathmind.db.dao.TrainingErrorDAO;
import io.skymind.pathmind.services.ModelService;
import io.skymind.pathmind.services.TrainingService;
import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.shared.data.TrainingError;
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
import io.skymind.pathmind.webapp.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.webapp.ui.components.alp.DownloadModelAlpLink;
import io.skymind.pathmind.webapp.ui.components.atoms.TagLabel;
import io.skymind.pathmind.webapp.ui.components.codeViewer.CodeViewer;
import io.skymind.pathmind.webapp.ui.components.modelChecker.ModelCheckerService;
import io.skymind.pathmind.webapp.ui.components.navigation.Breadcrumbs;
import io.skymind.pathmind.webapp.ui.components.observations.ObservationsPanel;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.utils.ConfirmationUtils;
import io.skymind.pathmind.webapp.ui.utils.GuiUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import io.skymind.pathmind.webapp.ui.views.experiment.components.chart.ExperimentChartsPanel;
import io.skymind.pathmind.webapp.ui.views.experiment.components.experimentNotes.ExperimentNotesField;
import io.skymind.pathmind.webapp.ui.views.experiment.components.navbar.ExperimentsNavBar;
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
import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.ERROR_LABEL;
import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.SECTION_TITLE_LABEL;
import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.SUCCESS_LABEL;
import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.WARNING_LABEL;

@Route(value = Routes.EXPERIMENT_URL, layout = MainLayout.class)
@Slf4j
public class ExperimentView extends PathMindDefaultView implements HasUrlParameter<Long> {

    // We have to use a lock object rather than the experiment because we are changing it's reference which makes it not thread safe. As well we cannot lock
    // on this because part of the synchronization is in the eventbus listener in a subclass (which is also why we can't use synchronize on the method.
    private Object experimentLock = new Object();
    private Button exportPolicyButton;
    private Button stopTrainingButton;
    private Button unarchiveExperimentButton;
    private Anchor downloadModelAlpLink;
    private Button shareButton;

    private long experimentId = -1;
    private long modelId = -1;
    private List<RewardVariable> rewardVariables;
    private Policy bestPolicy;
    private Experiment experiment;

    private UserCaps userCaps;

    // TODO -> STEPH -> Are there class instances we may not need references to here like those I just committed. For example do we really
    // a reference to middlePanel or is it just a temporary instance that can be passed as a return value in a method?
    private HorizontalLayout middlePanel;
    private TrainingStatusDetailsPanel trainingStatusDetailsPanel;
    private TagLabel archivedLabel;
    private TagLabel sharedWithSupportLabel;
    private Span panelTitle;
    // TODO -> STEPH -> Same as others, we probably no longer need an instance.
    private CodeViewer codeViewer;
    private ExperimentChartsPanel experimentChartsPanel;
    private ExperimentsNavBar experimentsNavbar;
    protected ExperimentNotesField notesField;

    private VerticalLayout compareExperimentVerticalLayout;

    // TODO -> STEPH -> We shouldn't need an instance of this as a class attribute as the subscriber should be in the observation package and not in this class.
    private ObservationsPanel observationsPanel;

    private StoppedTrainingNotification stoppedTrainingNotification;

    // Experiment Comparison components
    private ExperimentChartsPanel comparisonChartsPanel;
    protected ExperimentNotesField comparisonNotesField;
    // TODO -> STEPH -> Same as above for ObservationsPanel
    private ObservationsPanel comparisonObservationsPanel;
    private CodeViewer comparisonCodeViewer;
    // TODO -> STEPH -> Only needed because I haven't yet pushed the comparison code to a subscriber.
    private SimulationMetricsPanel comparisonSimulationMetricsPanel;


    @Autowired
    private ModelService modelService;
    @Autowired
    protected ExperimentDAO experimentDAO;
    @Autowired
    private RewardVariableDAO rewardVariableDAO;
    @Autowired
    private ObservationDAO observationDAO;
    @Autowired
    protected PolicyDAO policyDAO;
    @Autowired
    private TrainingErrorDAO trainingErrorDAO;
    @Autowired
    private TrainingService trainingService;
    @Autowired
    private RunDAO runDAO;
    @Autowired
    protected SegmentIntegrator segmentIntegrator;
    @Autowired
    private FeatureManager featureManager;
    @Autowired
    private ModelCheckerService modelCheckerService;
    @Value("${pathmind.early-stopping.url}")
    private String earlyStoppingUrl;

    private Breadcrumbs pageBreadcrumbs;
    private Button restartTraining;

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
    protected Component getTitlePanel() {
        pageBreadcrumbs = createBreadcrumbs();
        return new ScreenTitlePanel(pageBreadcrumbs);
    }

    @Override
    protected Component getMainContent() {
        panelTitle = LabelFactory.createLabel("Experiment #" + experiment.getName(), SECTION_TITLE_LABEL);
        archivedLabel = new TagLabel("Archived", false, "small");
        sharedWithSupportLabel = new TagLabel("Shared with Support", true, "small");
        trainingStatusDetailsPanel = new TrainingStatusDetailsPanel(() -> getUI());
        experimentsNavbar = new ExperimentsNavBar(
                () -> getUI(),
                experimentDAO,
                policyDAO,
                experiment,
                segmentIntegrator);

        setupExperimentContentPanel();

        Span modelNeedToBeUpdatedLabel = modelCheckerService.createInvalidErrorLabel(experiment.getModel());
        modelNeedToBeUpdatedLabel.getStyle().set("margin-top", "2px");

        stoppedTrainingNotification = new StoppedTrainingNotification(earlyStoppingUrl);

        // It is the same for all experiments from the same model so it doesn't have to be updated as long
        // as the user is on the Experiment View (the nav bar only allows navigation to experiments from the same model)
        // If in the future we allow navigation to experiments from other models, then we'll need to update the button accordingly on navigation
        downloadModelAlpLink = new DownloadModelAlpLink(experiment.getProject().getName(), experiment.getModel(), modelService, segmentIntegrator);

        setupCompareExperimentVerticalLayout();

        VerticalLayout experimentContent = WrapperUtils.wrapWidthFullVertical(
                WrapperUtils.wrapWidthFullHorizontal(
                        WrapperUtils.wrapVerticalWithNoPaddingOrSpacingAndWidthAuto(
                                panelTitle, archivedLabel, sharedWithSupportLabel),
                        downloadModelAlpLink, trainingStatusDetailsPanel, getButtonsWrapper()),
                stoppedTrainingNotification,
                modelNeedToBeUpdatedLabel,
                middlePanel,
                getBottomPanel(),
                compareExperimentVerticalLayout);
        experimentContent.addClassName("view-section");
        HorizontalLayout pageWrapper = isShowNavBar() ? WrapperUtils.wrapWidthFullHorizontal(experimentsNavbar, experimentContent) : WrapperUtils.wrapSizeFullHorizontal(experimentContent);
        pageWrapper.addClassName("page-content");
        pageWrapper.setSpacing(false);
        return pageWrapper;
    }

    private void setupCompareExperimentVerticalLayout() {
        compareExperimentVerticalLayout = WrapperUtils.wrapWidthFullVertical(
            getComparisonBottomPanel());
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
        // TODO -> STEPH -> Can we somehow combine these into one.?
        codeViewer = new CodeViewer(() -> getUI(), experiment);
        VerticalLayout rewardFunctionGroup = generateRewardFunctionGroup(codeViewer);

        // TODO -> STEPH -> Shouldn't be needed but until I move SimulationMetricsPanel comparison code is moved to a subscriber.
        SimulationMetricsPanel simulationMetricsPanel = new SimulationMetricsPanel(experiment, featureManager.isEnabled(Feature.SIMULATION_METRICS), rewardVariables, () -> getUI());

        observationsPanel = new ObservationsPanel(experiment, true);

        middlePanel = WrapperUtils.wrapWidthFullHorizontal();
        middlePanel.add(generateSimulationsMetricsPanelGroup(simulationMetricsPanel), observationsPanel, rewardFunctionGroup);
        middlePanel.addClassName("middle-panel");
        middlePanel.setPadding(false);
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
        restartTraining = new Button("Restart Training", click -> {
            synchronized (experimentLock) {
                if (!ExperimentCapLimitVerifier.isUserWithinCapLimits(runDAO, userCaps, segmentIntegrator)) {
                    return;
                }
                trainingService.startRun(experiment);
                segmentIntegrator.restartTraining();
                initLoadData();
                // REFACTOR -> https://github.com/SkymindIO/pathmind-webapp/issues/2278
                trainingStatusDetailsPanel.setExperiment(experiment);
                clearErrorState();
                experimentChartsPanel.setupCharts(experiment, rewardVariables);
            }
        });
        restartTraining.setVisible(false);
        restartTraining.addClassNames("large-image-btn", "run");

        exportPolicyButton = GuiUtils.getPrimaryButton(
                "Export Policy",
                click -> getUI().ifPresent(ui -> ui.navigate(ExportPolicyView.class, bestPolicy.getId())),
                false);

        stopTrainingButton = new Button("Stop Training", click -> showStopTrainingConfirmationDialog());
        stopTrainingButton.setVisible(true);

        shareButton = new Button("Share with support", click -> showShareConfirmationDialog());

        unarchiveExperimentButton = GuiUtils.getPrimaryButton("Unarchive", VaadinIcon.ARROW_BACKWARD.create(), click -> unarchiveExperiment());

        notesField = createViewNotesField(experiment);

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
                restartTraining,
                stopTrainingButton,
                shareButton,
                exportPolicyButton};
    }

    private HorizontalLayout getBottomPanel() {
        experimentChartsPanel = new ExperimentChartsPanel(() -> getUI(), experiment, rewardVariables);
        HorizontalLayout bottomPanel = WrapperUtils.wrapWidthFullHorizontal(
                experimentChartsPanel,
                notesField);
        bottomPanel.addClassName("bottom-panel");
        bottomPanel.setPadding(false);
        return bottomPanel;
    }

    private HorizontalLayout getComparisonBottomPanel() {
        comparisonChartsPanel = new ExperimentChartsPanel(() -> getUI(), experiment, rewardVariables);
        // TODO -> STEPH -> For now use the same experiment since it's invisible anyways. The downside is that all events, etc. are duplicated for nothing.
        // but it will be a quick solution until I can do all the null checks everywhere, setting up the subscribers, etc. (after everything has been stubbed).
        comparisonNotesField = createViewNotesField(experiment);
        comparisonObservationsPanel = new ObservationsPanel(experiment, true);
        // TODO -> STEPH -> Can we combine these?
        comparisonCodeViewer = new CodeViewer(() -> getUI(), experiment);
        VerticalLayout rewardFunctionGroup = generateRewardFunctionGroup(comparisonCodeViewer);
        // TODO -> STEPH -> Shouldn't be needed but until I move SimulationMetricsPanel comparison code is moved to a subscriber.
        SimulationMetricsPanel comparisonSimulationMetricsPanel = new SimulationMetricsPanel(experiment, featureManager.isEnabled(Feature.SIMULATION_METRICS), rewardVariables, () -> getUI());

        HorizontalLayout bottomPanel = WrapperUtils.wrapWidthFullHorizontal(
                generateSimulationsMetricsPanelGroup(comparisonSimulationMetricsPanel),
                comparisonObservationsPanel,
                rewardFunctionGroup,
                comparisonChartsPanel,
                comparisonNotesField);
        bottomPanel.addClassName("bottom-panel");
        bottomPanel.setPadding(false);
        return bottomPanel;
    }

    private Breadcrumbs createBreadcrumbs() {
        return new Breadcrumbs(experiment.getProject(), experiment.getModel(), experiment);
    }

    private ExperimentNotesField createViewNotesField(Experiment experiment) {
        return new ExperimentNotesField(
                getUISupplier(),
                experiment,
                updatedNotes -> {
                    experimentDAO.updateUserNotes(notesField.getExperiment().getId(), updatedNotes);
                    segmentIntegrator.updatedNotesExperimentView();
                },
                true,
                false
        );
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

    @Override
    public void setParameter(BeforeEvent event, Long experimentId) {
        this.experimentId = experimentId;
    }

    public void setExperiment(Experiment selectedExperiment) {
        // The only reason I'm synchronizing here is in case an event is fired while it's still loading the data (which can take several seconds). We should still be on the
        // same experiment but just because right now loads can take up to several seconds I'm being extra cautious.
        synchronized (experimentLock) {
            if (ExperimentUtils.isDraftRunType(selectedExperiment)) {
                getUI().ifPresent(ui -> ui.navigate(NewExperimentView.class, selectedExperiment.getId()));
            } else {
                experiment = getExperimentForUser(selectedExperiment.getId())
                        .orElseThrow(() -> new InvalidDataException("Attempted to access Experiment: " + selectedExperiment.getId()));
                loadExperimentData();
                getUI().ifPresent(ui -> ui.getPage().getHistory().pushState(null, "experiment/" + selectedExperiment.getId()));

                updateScreenComponents();
            }
        }
    }

    @Override
    protected void initLoadData() throws InvalidDataException {
        // REFACTOR -> STEPH -> #2203 -> https://github.com/SkymindIO/pathmind-webapp/issues/2203 Once we do that
        // we will no longer have to retrieve the user information when loading this page.
        experiment = getExperimentForUser()
                .orElseThrow(() -> new InvalidDataException("Attempted to access Experiment: " + experimentId));
        loadExperimentData();
    }

    protected Optional<Experiment> getExperimentForUser() {
        return experimentDAO.getExperimentIfAllowed(experimentId, SecurityUtils.getUserId());
    }

    // Overridden in the SharedExperimentView so that we can get it based on the type of user (normal vs support user).
    protected Optional<Experiment> getExperimentForUser(long specificExperimentId) {
        return experimentDAO.getExperimentIfAllowed(specificExperimentId, SecurityUtils.getUserId());
    }

    private void loadExperimentData() {
        modelId = experiment.getModelId();
        experimentId = experiment.getId();
        rewardVariables = rewardVariableDAO.getRewardVariablesForModel(modelId);
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
        updateScreenComponents();
    }

    protected void updateScreenComponents() {
        clearErrorState();
        setSharedWithSupportComponents();
        if (isShowNavBar()) {
            experimentsNavbar.setVisible(!experiment.isArchived());
        }
        panelTitle.setText("Experiment #" + experiment.getName());
        // Check is needed for the shared experiment view which has no breadcrumb.
        if (pageBreadcrumbs != null) {
            pageBreadcrumbs.setText(3, "Experiment #" + experiment.getName());
        }
        if (!ModelUtils.isValidModel(experiment.getModel()) && isShowNavBar()) {
            experimentsNavbar.setAllowNewExperimentCreation(false);
        }
        updateDetailsForExperiment();
        trainingStatusDetailsPanel.setExperiment(experiment);
    }

    private void updateUIForError(TrainingError error, String errorText) {
        stoppedTrainingNotification.showTheReasonWhyTheTrainingStopped(errorText, ERROR_LABEL, false);

        // Not used in the shared view but this code can be left as the buttons are not included in the shared view.
        boolean allowRestart = error.isRestartable() && ModelUtils.isValidModel(experiment.getModel());
        restartTraining.setVisible(allowRestart);
        restartTraining.setEnabled(allowRestart);
    }

    private void clearErrorState() {
        stoppedTrainingNotification.clearErrorState();
        updateButtonEnablement();
    }

    public void updateDetailsForExperiment() {
        updateButtonEnablement();
        archivedLabel.setVisible(experiment.isArchived());
        // TODO -> STEPH -> Should be part fo the experiment and not reloaded all the time.
        RunStatus status = experiment.getTrainingStatusEnum();
        if (status == RunStatus.Error || status == RunStatus.Killed) {
            ExperimentUtils.getTrainingErrorAndMessage(trainingErrorDAO, experiment)
                    .ifPresent(pair -> {
                        this.updateUIForError(pair.getLeft(), pair.getRight());
                    });
        } else {
            ExperimentUtils.getEarlyStopReason(experiment)
                    .ifPresent(reason -> {
                        String label = reason.isSuccess() ? SUCCESS_LABEL : WARNING_LABEL;
                        stoppedTrainingNotification.showTheReasonWhyTheTrainingStopped(reason.getMessage(), label, true);
                    });
        }
    }

    private void updateButtonEnablement() {
        unarchiveExperimentButton.setVisible(experiment.isArchived());
        exportPolicyButton.setVisible(experiment.isTrainingCompleted() && hasBestPolicy());
        stopTrainingButton.setVisible(experiment.isTrainingRunning());
        restartTraining.setVisible(false);
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

    // ExperimentID is also added as the experiment can be null whereas the experimentID always has to have a value. Used by the Subscribers for the view.
    public long getExperimentId() {
        return experimentId;
    }

    public Experiment getExperiment() {
        return experiment;
    }

    public long getModelId() {
        return modelId;
    }


    public void stopCompareExperiment() {
        compareExperimentVerticalLayout.setVisible(false);
    }

    // TODO -> STEPH -> On experiment switch for now we'll just reload the components with the experiment that's being switched and
    // put it to invisible. We'll set it to null when the null checks for all the components are ready.
    public void startCompareExperiment(Experiment experiment) {
        compareExperimentVerticalLayout.setVisible(true);
        // TODO -> STEPH -> Assumes it will always work. Should not assume that but at this point it's just stubbing.
        Experiment comparisonExperiment = getExperimentForUser(experiment.getId()).get();
        // TODO -> STEPH -> All code below should be done in a NotesField CompareView Subscriber but just to quickly test. That or
        // if we think we're ready we can start looking at bringing back button action listeners into the view. I'm just not sure we're
        // quite ready yet, I'm still breaking up some of the code, like the bigger cleanup in PR #2525
        // TODO -> STEPH -> Do we have to update the chart data to see anything? As in is it only completed experiments?
        comparisonNotesField.setExperiment(comparisonExperiment);
        // TODO -> STEPH -> Chart ONLY works because we need to do policyDAO.getPoliciesForExperiment()
        comparisonExperiment.setPolicies(policyDAO.getPoliciesForExperiment(experiment.getId()));
        comparisonChartsPanel.setupCharts(comparisonExperiment, rewardVariables);
        comparisonObservationsPanel.setExperiment(comparisonExperiment);
        comparisonCodeViewer.setExperiment(experiment);
        comparisonSimulationMetricsPanel.setExperiment(experiment);
    }
}
