package io.skymind.pathmind.webapp.ui.views.experiment;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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
import io.skymind.pathmind.db.dao.*;
import io.skymind.pathmind.services.ModelService;
import io.skymind.pathmind.services.TrainingService;
import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.data.*;
import io.skymind.pathmind.shared.data.user.UserCaps;
import io.skymind.pathmind.shared.featureflag.Feature;
import io.skymind.pathmind.shared.featureflag.FeatureManager;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.shared.security.SecurityUtils;
import io.skymind.pathmind.shared.utils.ModelUtils;
import io.skymind.pathmind.shared.utils.PolicyUtils;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.bus.events.main.ExperimentCreatedBusEvent;
import io.skymind.pathmind.webapp.bus.events.main.ExperimentUpdatedBusEvent;
import io.skymind.pathmind.webapp.bus.events.main.PolicyUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.events.main.RunUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.ExperimentCreatedSubscriber;
import io.skymind.pathmind.webapp.bus.subscribers.main.ExperimentUpdatedSubscriber;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.exception.InvalidDataException;
import io.skymind.pathmind.webapp.ui.components.CodeViewer;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.webapp.ui.components.atoms.TagLabel;
import io.skymind.pathmind.webapp.ui.components.molecules.ConfirmPopup;
import io.skymind.pathmind.webapp.ui.components.molecules.NotesField;
import io.skymind.pathmind.webapp.ui.components.navigation.Breadcrumbs;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.utils.ConfirmationUtils;
import io.skymind.pathmind.webapp.ui.utils.PushUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import io.skymind.pathmind.webapp.ui.views.experiment.components.trainingStatus.TrainingStatusDetailsPanel;
import io.skymind.pathmind.webapp.ui.views.experiment.components.ExperimentNotesField;
import io.skymind.pathmind.webapp.ui.views.experiment.components.chart.ExperimentChartsPanel;
import io.skymind.pathmind.webapp.ui.views.experiment.components.navbar.ExperimentsNavBar;
import io.skymind.pathmind.webapp.ui.views.experiment.components.notification.StoppedTrainingNotification;
import io.skymind.pathmind.webapp.ui.views.experiment.simulationMetrics.SimulationMetricsPanel;
import io.skymind.pathmind.webapp.ui.views.experiment.subscribers.ExperimentViewPolicyUpdateSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.subscribers.ExperimentViewRunUpdateSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.utils.ExperimentCapLimitVerifier;
import io.skymind.pathmind.webapp.ui.views.model.ModelCheckerService;
import io.skymind.pathmind.webapp.ui.views.model.components.DownloadModelAlpLink;
import io.skymind.pathmind.webapp.ui.views.model.components.ObservationsPanel;
import io.skymind.pathmind.webapp.ui.views.policy.ExportPolicyView;
import io.skymind.pathmind.webapp.ui.views.project.ProjectView;
import io.skymind.pathmind.webapp.utils.PathmindUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.*;

@Route(value = Routes.EXPERIMENT_URL, layout = MainLayout.class)
@Slf4j
public class ExperimentView extends PathMindDefaultView implements HasUrlParameter<Long>
{

    // We have to use a lock object rather than the experiment because we are changing it's reference which makes it not thread safe. As well we cannot lock
    // on this because part of the synchronization is in the eventbus listener in a subclass (which is also why we can't use synchronize on the method.
    private Object experimentLock = new Object();

    private Button exportPolicyButton;
    private Button stopTrainingButton;
    private Button unarchiveExperimentButton;
    private Anchor downloadModelAlpLink;

    private long experimentId = -1;
    private long modelId = -1;
    private List<RewardVariable> rewardVariables;
    private Policy bestPolicy;
    private Experiment experiment;
    private List<Experiment> experiments = new ArrayList<>();

    private List<Observation> modelObservations = new ArrayList<>();
    private List<Observation> experimentObservations = new ArrayList<>();

    private UserCaps userCaps;

    private HorizontalLayout middlePanel;
    private TrainingStatusDetailsPanel trainingStatusDetailsPanel;
    private TagLabel archivedLabel;
    private Span panelTitle;
    private VerticalLayout rewardVariablesGroup;
    private VerticalLayout rewardFunctionGroup;
    private CodeViewer codeViewer;
    private ExperimentChartsPanel experimentChartsPanel;
    private ExperimentsNavBar experimentsNavbar;
    private NotesField notesField;

    private ObservationsPanel observationsPanel;

    private StoppedTrainingNotification stoppedTrainingNotification;
    private SimulationMetricsPanel simulationMetricsPanel;

    @Autowired
    private ModelService modelService;
    @Autowired
    private ExperimentDAO experimentDAO;
    @Autowired
    private RewardVariableDAO rewardVariableDAO;
	@Autowired
	private ObservationDAO observationDAO;
    @Autowired
    private PolicyDAO policyDAO;
    @Autowired
    private TrainingErrorDAO trainingErrorDAO;
    @Autowired
    private TrainingService trainingService;
    @Autowired
    private RunDAO runDAO;
    @Autowired
    private SegmentIntegrator segmentIntegrator;
    @Autowired
    private FeatureManager featureManager;
    @Autowired
    private ModelCheckerService modelCheckerService;
    @Value("${pathmind.early-stopping.url}")
    private String earlyStoppingUrl;

    private Breadcrumbs pageBreadcrumbs;
    private Button restartTraining;

    // REFACTOR -> Temporary placeholder until I finish the merging
    private ExperimentViewRunUpdateSubscriber experimentViewRunUpdateSubscriber;

    public ExperimentView(
            @Value("${pathmind.notification.newRunDailyLimit}") int newRunDailyLimit,
            @Value("${pathmind.notification.newRunMonthlyLimit}") int newRunMonthlyLimit,
            @Value("${pathmind.notification.newRunNotificationThreshold}") int newRunNotificationThreshold) {
        super();
        this.userCaps = new UserCaps(newRunDailyLimit, newRunMonthlyLimit, newRunNotificationThreshold);
        addClassName("experiment-view");
        experimentViewRunUpdateSubscriber = new ExperimentViewRunUpdateSubscriber(this, () -> getUI());
    }

    @Override
    protected void onAttach(AttachEvent event) {
        EventBus.subscribe(this,
                new ExperimentViewPolicyUpdateSubscriber(() -> getUI(), this),
                experimentViewRunUpdateSubscriber,
                new ExperimentViewExperimentCreatedSubscriber(() -> getUI()),
                new ExperimentViewExperimentUpdatedSubscriber(() -> getUI()));
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
        panelTitle = LabelFactory.createLabel("Experiment #"+experiment.getName(), SECTION_TITLE_LABEL);
        archivedLabel = new TagLabel("Archived", false, "small");
        trainingStatusDetailsPanel = new TrainingStatusDetailsPanel(() -> getUI());
        experimentsNavbar = new ExperimentsNavBar(
                () -> getUI(),
                experimentDAO,
                policyDAO,
                experiment,
                experiments,
                segmentIntegrator);
        setupExperimentContentPanel();

        Span modelNeedToBeUpdatedLabel = modelCheckerService.createInvalidErrorLabel(experiment.getModel());
        modelNeedToBeUpdatedLabel.getStyle().set("margin-top", "2px");

        stoppedTrainingNotification = new StoppedTrainingNotification(earlyStoppingUrl);

        // It is the same for all experiments from the same model so it doesn't have to be updated as long
        // as the user is on the Experiment View (the nav bar only allows navigation to experiments from the same model)
        // If in the future we allow navigation to experiments from other models, then we'll need to update the button accordingly on navigation
        downloadModelAlpLink = new DownloadModelAlpLink(experiment.getProject().getName(), experiment.getModel(), modelService, segmentIntegrator);

        VerticalLayout experimentContent = WrapperUtils.wrapWidthFullVertical(
                WrapperUtils.wrapWidthFullHorizontal(panelTitle, archivedLabel, downloadModelAlpLink, trainingStatusDetailsPanel, getButtonsWrapper()),
                stoppedTrainingNotification,
                modelNeedToBeUpdatedLabel,
                middlePanel,
                getBottomPanel());
        experimentContent.addClassName("view-section");
        HorizontalLayout pageWrapper = WrapperUtils.wrapWidthFullHorizontal(
                experimentsNavbar,
                experimentContent);
        pageWrapper.addClassName("page-content");
        pageWrapper.setSpacing(false);
        return pageWrapper;
    }

    private void setupExperimentContentPanel() {
        codeViewer = new CodeViewer();
        rewardFunctionGroup = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(
            LabelFactory.createLabel("Reward Function", BOLD_LABEL), codeViewer
        );

        boolean showSimulationMetrics = featureManager.isEnabled(Feature.SIMULATION_METRICS);
        simulationMetricsPanel = new SimulationMetricsPanel(experiment, showSimulationMetrics, rewardVariables, () -> getUI());
        String simulationMetricsHeaderText = showSimulationMetrics ? "Simulation Metrics" : "Reward Variables";

        rewardVariablesGroup = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(
            LabelFactory.createLabel(simulationMetricsHeaderText, BOLD_LABEL), simulationMetricsPanel
        );

        observationsPanel = new ObservationsPanel(true);
        observationsPanel.setupObservationTable(modelObservations, experimentObservations);

        middlePanel = WrapperUtils.wrapWidthFullHorizontal();
        middlePanel.add(rewardVariablesGroup, observationsPanel, rewardFunctionGroup);
        middlePanel.addClassName("middle-panel");
        middlePanel.setPadding(false);
    }

    private Div getButtonsWrapper() {
        restartTraining = new Button("Restart Training", click -> {
            synchronized (experimentLock) {
                if(!ExperimentCapLimitVerifier.isUserWithinCapLimits(runDAO, userCaps, segmentIntegrator))
                    return;
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

        exportPolicyButton = new Button("Export Policy", click -> getUI().ifPresent(ui -> ui.navigate(ExportPolicyView.class, bestPolicy.getId())));
        exportPolicyButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        exportPolicyButton.setVisible(false);

        stopTrainingButton = new Button("Stop Training", click -> {
            showStopTrainingConfirmationDialog();
        });
        stopTrainingButton.setVisible(true);

        unarchiveExperimentButton = new Button("Unarchive", VaadinIcon.ARROW_BACKWARD.create(), click -> unarchiveExperiment());
        unarchiveExperimentButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        notesField = createViewNotesField();

        Div buttonsWrapper = new Div(
            unarchiveExperimentButton,
            restartTraining,
            stopTrainingButton,
            exportPolicyButton
        );
        buttonsWrapper.addClassName("buttons-wrapper");
        return buttonsWrapper;
    }

    private HorizontalLayout getBottomPanel() {
        experimentChartsPanel = new ExperimentChartsPanel(() -> getUI());
        HorizontalLayout bottomPanel = WrapperUtils.wrapWidthFullHorizontal(
                experimentChartsPanel,
                notesField);
        bottomPanel.addClassName("bottom-panel");
        bottomPanel.setPadding(false);
        return bottomPanel;
    }

    private void showStopTrainingConfirmationDialog() {
        ConfirmPopup confirmPopup = new ConfirmPopup();
        confirmPopup.setHeader("Stop Training");
        confirmPopup.setMessage(new Html(
                "<div>"
                        + "<p>Are you sure you want to stop training?</p>"
                        + "<p>If you stop the training before it completes, you won't be able to download the policy. "
                        + "<b>If you decide you want to start the training again, you can start a new experiment and "
                        + "use the same reward function.</b>"
                        + "</p>"
                        + "</div>"));
        confirmPopup.setConfirmButton(
                "Stop Training",
                () -> {
                    segmentIntegrator.stopTraining();
                    trainingService.stopRun(experiment);
                    stopTrainingButton.setVisible(false);
                    fireEvents();
                },
                ButtonVariant.LUMO_ERROR.getVariantName()+" "+ButtonVariant.LUMO_PRIMARY.getVariantName()
        );
        confirmPopup.setCancelButtonText("Cancel");
        confirmPopup.open();
    }

    private void fireEvents() {
        // An event for each policy since we only need to update some of the policies in a run.
        if(experiment.getPolicies() != null && !experiment.getPolicies().isEmpty())
            EventBus.post(new PolicyUpdateBusEvent(experiment.getPolicies()));
        // Send run updated event, meaning that all policies under the run is updated.
        // This is needed especially in dashboard, to refresh the item only once per run, instead of after all policy updates
        EventBus.post(new RunUpdateBusEvent(experiment.getRuns()));
        EventBus.post(new ExperimentUpdatedBusEvent(experiment));
    }

    private void unarchiveExperiment() {
        ConfirmationUtils.unarchive("experiment", () -> {
            ExperimentUtils.archiveExperiment(experimentDAO, experiment, false);
            segmentIntegrator.archived(Experiment.class, false);
            ExperimentUtils.navigateToExperiment(getUI(), experiment);
        });
    }

    private Breadcrumbs createBreadcrumbs() {
        return new Breadcrumbs(experiment.getProject(), experiment.getModel(), experiment);
    }

    private ExperimentNotesField createViewNotesField() {
        return new ExperimentNotesField(
            () -> getUI(),
            "Notes",
            experiment,
            updatedNotes -> {
                experimentDAO.updateUserNotes(experimentId, updatedNotes);
                segmentIntegrator.updatedNotesExperimentView();
            }
        );
    }

    @Override
    public void setParameter(BeforeEvent event, Long experimentId) {
        this.experimentId = experimentId;
    }

    private void selectExperiment(Experiment selectedExperiment) {
        // The only reason I'm synchronizing here is in case an event is fired while it's still loading the data (which can take several seconds). We should still be on the
        // same experiment but just because right now loads can take up to several seconds I'm being extra cautious.
        // REFACTOR -> STEPH -> We mix and match between experiment and selectedExperiment all over the place. Here and in the loadExperiment method,.
        // REFACTOR -> STEPH -> Finish moving this code to ExperimentChangedViewSubscriber (See https://github.com/SkymindIO/pathmind-webapp/issues/2259)
        synchronized (experimentLock) {
            experiment = experimentDAO.getExperiment(selectedExperiment.getId())
                    .orElseThrow(() -> new InvalidDataException("Attempted to access Experiment: " + selectedExperiment.getId()));
            experimentViewRunUpdateSubscriber.setExperiment(selectedExperiment);
            experimentId = selectedExperiment.getId();
            loadExperimentData();
            pageBreadcrumbs.setText(3, "Experiment #" + experiment.getName());
			experimentsNavbar.setCurrentExperiment(selectedExperiment);

            if (ExperimentUtils.isDraftRunType(selectedExperiment)) {
                getUI().ifPresent(ui -> ui.navigate(NewExperimentView.class, selectedExperiment.getId()));
            } else {
                getUI().ifPresent(ui -> ui.getPage().getHistory().pushState(null, "experiment/" + selectedExperiment.getId()));
                updateScreenComponents();
            }
        }
    }

    private void navigateToExperiment(UI ui, Experiment targetExperiment) {
        if (ExperimentUtils.isDraftRunType(targetExperiment)) {
            ui.navigate(NewExperimentView.class, targetExperiment.getId());
        } else {
            ui.navigate(ExperimentView.class, targetExperiment.getId());
        }
    }

    @Override
    protected void initLoadData() throws InvalidDataException {
        experiment = experimentDAO.getExperimentIfAllowed(experimentId, SecurityUtils.getUserId())
                .orElseThrow(() -> new InvalidDataException("Attempted to access Experiment: " + experimentId));
        experimentViewRunUpdateSubscriber.setExperiment(experiment);
        loadExperimentData();
    }

    private void loadExperimentData() {
        modelId = experiment.getModelId();
        // REFACTOR -> STEPH -> This should be part of loading up the experiment along with the other items as they are needed throughout
        // and easily missed in other places.
        experiment.setPolicies(policyDAO.getPoliciesForExperiment(experimentId));
        rewardVariables = rewardVariableDAO.getRewardVariablesForModel(modelId);
		modelObservations = observationDAO.getObservationsForModel(experiment.getModelId());
		experimentObservations = observationDAO.getObservationsForExperiment(experimentId);
        bestPolicy = PolicyUtils.selectBestPolicy(experiment.getPolicies()).orElse(null);
        experiment.setRuns(runDAO.getRunsForExperiment(experiment));
        if (!experiment.isArchived()) {
            experiments = experimentDAO.getExperimentsForModel(modelId).stream()
                                .filter(exp -> !exp.isArchived()).collect(Collectors.toList());
            experimentViewRunUpdateSubscriber.setExperiments(experiments);
        }
    }

    @Override
    protected void initScreen(BeforeEnterEvent event) {
        // Part of a bigger refactoring. The goal is to eventually remove the selectExperiment code and use the ExperimentChangedViewSubscriber
        // instead because the logic and code is not the same between loading the initial screen and when updateScreenComponents is called. There's
        // a mix of logic and code between selectExperiment and updateScreenComponents that is inconsistent. By splitting these off it should
        // hopefully make it easier to manage. Again this is just a first part, I'm (Steph) planning to split this code between the initial
        // load and any event updates as well as experiment select.
        experimentChartsPanel.setupCharts(experiment, rewardVariables);
        trainingStatusDetailsPanel.setExperiment(experiment);
        updateScreenComponents();
    }

    private void updateScreenComponents() {
        clearErrorState();
        experimentsNavbar.setVisible(!experiment.isArchived());
        panelTitle.setText("Experiment #"+experiment.getName());
        if (ModelUtils.isValidModel(experiment.getModel())) {
            codeViewer.setValue(experiment.getRewardFunction());
        } else {
            codeViewer.setValue(experiment.getRewardFunction(), rewardVariables);
            experimentsNavbar.setAllowNewExperimentCreation(false);
        }
        observationsPanel.setSelectedObservations(experimentObservations);
        updateDetailsForExperiment();
    }

    private void updateUIForError(TrainingError error, String errorText) {
        stoppedTrainingNotification.showTheReasonWhyTheTrainingStopped(errorText, ERROR_LABEL, false);

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
        RunStatus status = experiment.getTrainingStatusEnum();
        if (status == RunStatus.Error || status == RunStatus.Killed) {
            experiment.getRuns().stream()
                    .filter(r -> r.getStatusEnum() == RunStatus.Error || r.getStatusEnum() == RunStatus.Killed)
                    .findAny()
                    .ifPresent(run -> {
                        trainingErrorDAO.getErrorById(run.getTrainingErrorId())
                                .ifPresent(trainingError -> this.updateUIForError(trainingError, run.getRllibError() != null ?
                                        run.getRllibError() : trainingError.getDescription()));
                    });
        }
        else {
            experiment.getRuns().stream()
                    .filter(r -> StringUtils.isNotBlank(r.getSuccessMessage()) || StringUtils.isNotBlank(r.getWarningMessage()))
                    .findAny()
                    .ifPresent(r -> {
                        if (StringUtils.isNotBlank(r.getSuccessMessage())) {
                            stoppedTrainingNotification.showTheReasonWhyTheTrainingStopped("Success! " + r.getSuccessMessage(), SUCCESS_LABEL_BRIGHT_GREEN, true);
                        }
                        else {
                            stoppedTrainingNotification.showTheReasonWhyTheTrainingStopped(r.getWarningMessage(), WARNING_LABEL, true);
                        }
                    });
        }
    }

    public void updateButtonEnablement() {
        RunStatus trainingStatus = experiment.getTrainingStatusEnum();
        boolean isCompleted = trainingStatus == RunStatus.Completed;
        unarchiveExperimentButton.setVisible(experiment.isArchived());
        exportPolicyButton.setVisible(isCompleted && bestPolicy != null && bestPolicy.hasFile());
        boolean canBeStopped = RunStatus.isRunning(trainingStatus);
        stopTrainingButton.setVisible(canBeStopped);
        restartTraining.setVisible(false);
    }

    public void updateExperimentComponents() {
        // REFACTOR -> We will want to adjust this code as it's performing a database call on almost all eventbus events which is both
        // a potential performance issue as well as cause potential multi-threading issues (racing conditions).
        experiments = experimentDAO.getExperimentsForModel(modelId).stream().filter(exp -> !exp.isArchived()).collect(Collectors.toList());
        experimentViewRunUpdateSubscriber.setExperiments(experiments);

        if (experiments.isEmpty()) {
            Model model = modelService.getModel(modelId)
					.orElseThrow(() -> new InvalidDataException("Attempted to access Invalid model: " + modelId));

            PushUtils.push(getUI(), ui -> ui.navigate(ProjectView.class, PathmindUtils.getProjectModelParameter(model.getProjectId(), modelId)));
        } else {
            boolean selectedExperimentWasArchived = experiments.stream()
                    .noneMatch(e -> e.getId() == experimentId);
            if (selectedExperimentWasArchived) {
                Experiment newSelectedExperiment = experiments.get(0);
                PushUtils.push(getUI(), ui -> navigateToExperiment(ui, newSelectedExperiment));
            }
            else {
                PushUtils.push(getUI(), ui -> selectExperiment(experiment));
            }
        }
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

    class ExperimentViewExperimentCreatedSubscriber extends ExperimentCreatedSubscriber {

        public ExperimentViewExperimentCreatedSubscriber(Supplier<Optional<UI>> getUISupplier) {
            super(getUISupplier);
        }

        @Override
        public void handleBusEvent(ExperimentCreatedBusEvent event) {
            updateExperimentComponents();
        }

        @Override
        public boolean filterBusEvent(ExperimentCreatedBusEvent event) {
            return ExperimentUtils.isNewExperimentForModel(event.getExperiment(), experiments, modelId);
        }

    }

    class ExperimentViewExperimentUpdatedSubscriber extends ExperimentUpdatedSubscriber {

        public ExperimentViewExperimentUpdatedSubscriber(Supplier<Optional<UI>> getUISupplier) {
            super(getUISupplier);
        }

        @Override
        public void handleBusEvent(ExperimentUpdatedBusEvent event) {
            updateExperimentComponents();
        }

        @Override
        public boolean filterBusEvent(ExperimentUpdatedBusEvent event) {
            if (experiment == null) {
                return false;
            }
            if (experiment.isArchived()) {
                return ExperimentUtils.isSameExperiment(event.getExperiment(), experiment);
            } else {
                return ExperimentUtils.isSameModel(experiment, event.getModelId());
            }
        }
    }
}
