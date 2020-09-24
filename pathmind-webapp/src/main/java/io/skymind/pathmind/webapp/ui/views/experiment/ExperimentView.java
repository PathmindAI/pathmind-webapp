package io.skymind.pathmind.webapp.ui.views.experiment;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
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
import io.skymind.pathmind.webapp.bus.events.ExperimentCreatedBusEvent;
import io.skymind.pathmind.webapp.bus.events.ExperimentUpdatedBusEvent;
import io.skymind.pathmind.webapp.bus.events.PolicyUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.events.RunUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.ExperimentCreatedSubscriber;
import io.skymind.pathmind.webapp.bus.subscribers.ExperimentUpdatedSubscriber;
import io.skymind.pathmind.webapp.bus.subscribers.PolicyUpdateSubscriber;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.exception.InvalidDataException;
import io.skymind.pathmind.webapp.ui.components.CodeViewer;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.webapp.ui.components.atoms.TagLabel;
import io.skymind.pathmind.webapp.ui.components.navigation.Breadcrumbs;
import io.skymind.pathmind.webapp.ui.components.notesField.NotesField;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.utils.ConfirmationUtils;
import io.skymind.pathmind.webapp.ui.utils.PushUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import io.skymind.pathmind.webapp.ui.views.experiment.components.PolicyChartPanel;
import io.skymind.pathmind.webapp.ui.views.experiment.components.TrainingStartingPlaceholder;
import io.skymind.pathmind.webapp.ui.views.experiment.components.TrainingStatusDetailsPanel;
import io.skymind.pathmind.webapp.ui.views.experiment.components.navbar.ExperimentsNavBar;
import io.skymind.pathmind.webapp.ui.views.experiment.components.notification.StoppedTrainingNotification;
import io.skymind.pathmind.webapp.ui.views.experiment.simulationMetrics.SimulationMetricsPanel;
import io.skymind.pathmind.webapp.ui.views.experiment.subscribers.ExperimentViewRunUpdateSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.utils.ExperimentCapLimitVerifier;
import io.skymind.pathmind.webapp.ui.views.model.ModelCheckerService;
import io.skymind.pathmind.webapp.ui.views.model.components.DownloadModelAlpLink;
import io.skymind.pathmind.webapp.ui.views.model.components.ObservationsPanel;
import io.skymind.pathmind.webapp.ui.views.policy.ExportPolicyView;
import io.skymind.pathmind.webapp.ui.views.project.ProjectView;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.Arrays;
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
    private Policy policy;
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
    private TrainingStartingPlaceholder trainingStartingPlaceholder;
    private PolicyChartPanel policyChartPanel;
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
                new ExperimentViewPolicyUpdateSubscriber(() -> getUI()),
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
        trainingStatusDetailsPanel = new TrainingStatusDetailsPanel();
        experimentsNavbar = new ExperimentsNavBar(
                () -> getUI(),
                experimentDAO,
                experiment,
                experiments,
                selectedExperiment -> selectExperiment(selectedExperiment),
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
                segmentIntegrator.discoveryRunStarted();
                initLoadData();
                trainingStatusDetailsPanel.updateTrainingDetailsPanel(experiment);
                clearErrorState();
                setPolicyChartVisibility();
            }
        });
        restartTraining.setVisible(false);
        restartTraining.addClassNames("large-image-btn", "run");

        exportPolicyButton = new Button("Export Policy", click -> getUI().ifPresent(ui -> ui.navigate(ExportPolicyView.class, policy.getId())));
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
        policyChartPanel = new PolicyChartPanel();
        trainingStartingPlaceholder = new TrainingStartingPlaceholder();

        VerticalLayout chartWrapper = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(
                        trainingStartingPlaceholder,
                        policyChartPanel);
        chartWrapper.addClassName("row-2-of-3");

        HorizontalLayout bottomPanel = WrapperUtils.wrapWidthFullHorizontal(
                chartWrapper,
                notesField);
        bottomPanel.addClassName("bottom-panel");
        bottomPanel.setPadding(false);
        return bottomPanel;
    }

    private void showStopTrainingConfirmationDialog() {
        ConfirmDialog confirmDialog = new ConfirmDialog();
        confirmDialog.setHeader("Stop Training");
        confirmDialog.setText(new Html(
                "<div>"
                        + "<p>Are you sure you want to stop training?</p>"
                        + "<p>If you stop the training before it completes, you won't be able to download the policy. "
                        + "<b>If you decide you want to start the training again, you can start a new experiment and "
                        + "use the same reward function.</b>"
                        + "</p>"
                        + "</div>"));
        confirmDialog.setConfirmButton(
                "Stop Training",
                (e) -> {
                    trainingService.stopRun(experiment);
                    stopTrainingButton.setVisible(false);
                    trainingStartingPlaceholder.setVisible(false);
                    policyChartPanel.setVisible(true);
                    fireEvents();
                    confirmDialog.close();
                },
                StringUtils.join(
                        Arrays.asList(ButtonVariant.LUMO_ERROR.getVariantName(), ButtonVariant.LUMO_PRIMARY.getVariantName()),
                        " ")
        );
        confirmDialog.setCancelText("Cancel");
        confirmDialog.setCancelable(true);
        confirmDialog.open();
    }

    private void fireEvents() {
        // An event for each policy since we only need to update some of the policies in a run.
        if(experiment.getPolicies() != null && !experiment.getPolicies().isEmpty())
            EventBus.post(new PolicyUpdateBusEvent(experiment.getPolicies()));
        // Send run updated event, meaning that all policies under the run is updated.
        // This is needed especially in dashboard, to refresh the item only once per run, instead of after all policy updates
        experiment.getRuns().stream().forEach(
                run -> EventBus.post(new RunUpdateBusEvent(run)));
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

    private NotesField createViewNotesField() {
        return new NotesField(
            "Notes",
            experiment.getUserNotes(),
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
        synchronized (experimentLock) {
            experiment = experimentDAO.getExperiment(selectedExperiment.getId())
                    .orElseThrow(() -> new InvalidDataException("Attempted to access Experiment: " + selectedExperiment.getId()));
            experimentViewRunUpdateSubscriber.setExperiment(experiment);
            experimentId = selectedExperiment.getId();
            loadExperimentData();
            updateScreenComponents();
            notesField.setNotesText(experiment.getUserNotes());
            pageBreadcrumbs.setText(3, "Experiment #" + experiment.getName());
            simulationMetricsPanel.setExperiment(experiment);

            if (ExperimentUtils.isDraftRunType(selectedExperiment)) {
                getUI().ifPresent(ui -> ui.navigate(NewExperimentView.class, selectedExperiment.getId()));
            } else {
                getUI().ifPresent(ui -> ui.getPage().getHistory().pushState(null, "experiment/" + selectedExperiment.getId()));
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
        experiment.setPolicies(policyDAO.getPoliciesForExperiment(experimentId));
        rewardVariables = rewardVariableDAO.getRewardVariablesForModel(modelId);
		modelObservations = observationDAO.getObservationsForModel(experiment.getModelId());
		experimentObservations = observationDAO.getObservationsForExperiment(experimentId);
        policy = PolicyUtils.selectBestPolicy(experiment.getPolicies());
        experiment.setRuns(runDAO.getRunsForExperiment(experiment));
        if (!experiment.isArchived()) {
            experiments = experimentDAO.getExperimentsForModel(modelId).stream()
                                .filter(exp -> !exp.isArchived()).collect(Collectors.toList());
            experimentViewRunUpdateSubscriber.setExperiments(experiments);
        }
    }

    @Override
    protected void initScreen(BeforeEnterEvent event) {
        updateScreenComponents();
    }

    private void updateScreenComponents() {
        clearErrorState();
        setPolicyChartVisibility();
        experimentsNavbar.setVisible(!experiment.isArchived());
        panelTitle.setText("Experiment #"+experiment.getName());
        if (ModelUtils.isValidModel(experiment.getModel())) {
            codeViewer.setValue(experiment.getRewardFunction());
        } else {
            codeViewer.setValue(experiment.getRewardFunction(), rewardVariables);
            experimentsNavbar.setAllowNewExperimentCreation(false);
        }
        observationsPanel.setSelectedObservations(experimentObservations);
        policyChartPanel.setExperiment(experiment, policy);
        updateDetailsForExperiment();
    }

    public void setPolicyChartVisibility() {
        RunStatus trainingStatus = ExperimentUtils.getTrainingStatus(experiment);
        trainingStartingPlaceholder.setVisible(trainingStatus == RunStatus.Starting);
        policyChartPanel.setVisible(trainingStatus != RunStatus.Starting);
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

    private void addOrUpdatePolicies(List<Policy> updatedPolicies) {
        updatedPolicies.forEach(updatedPolicy -> {
            int index = experiment.getPolicies().indexOf(updatedPolicy);
            if (index != -1) {
                experiment.getPolicies().set(index, updatedPolicy);
            } else {
                experiment.getPolicies().add(updatedPolicy);
            }
        });
    }

    public void updateDetailsForExperiment() {
        updateButtonEnablement();
        archivedLabel.setVisible(experiment.isArchived());
        trainingStatusDetailsPanel.updateTrainingDetailsPanel(experiment);
        RunStatus status = ExperimentUtils.getTrainingStatus(experiment);
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
                            stoppedTrainingNotification.showTheReasonWhyTheTrainingStopped(firstLine(r.getSuccessMessage()), SUCCESS_LABEL, true);
                        }
                        else {
                            stoppedTrainingNotification.showTheReasonWhyTheTrainingStopped(firstLine(r.getWarningMessage()), WARNING_LABEL, true);
                        }
                    });
        }
    }

    private String firstLine(String message) {
        return message.split("\\n", 2)[0];
    }

    private void updateButtonEnablement() {
        RunStatus trainingStatus = ExperimentUtils.getTrainingStatus(experiment);
        boolean isCompleted = trainingStatus == RunStatus.Completed;
        unarchiveExperimentButton.setVisible(experiment.isArchived());
        exportPolicyButton.setVisible(isCompleted && policy != null && policy.hasFile());
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
            PushUtils.push(getUI(), ui -> ui.navigate(ProjectView.class, experiments.get(0).getProject().getId()+Routes.MODEL_PATH+experiments.get(0).getModelId()));
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

    class ExperimentViewPolicyUpdateSubscriber extends PolicyUpdateSubscriber
    {
        public ExperimentViewPolicyUpdateSubscriber(Supplier<Optional<UI>> getUISupplier) {
            super(getUISupplier);
        }

        @Override
        public void handleBusEvent(PolicyUpdateBusEvent event) {
            synchronized (experimentLock) {
                // Need a check in case the experiment was on hold waiting for the change of experiment to load
                if (event.getExperimentId() != experimentId)
                    return;
                // Update or insert the policy in experiment.getPolicies
                addOrUpdatePolicies(event.getPolicies());

                // Calculate the best policy again
                policy = PolicyUtils.selectBestPolicy(experiment.getPolicies());
                PushUtils.push(getUI(), () -> {
                    if (policy != null) {
                        policyChartPanel.highlightPolicy(policy);
                    }
                    updateDetailsForExperiment();
                });
            }
        }

        @Override
        public boolean filterBusEvent(PolicyUpdateBusEvent event) {
            return experiment != null && experiment.getId() == event.getExperimentId();
        }
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
