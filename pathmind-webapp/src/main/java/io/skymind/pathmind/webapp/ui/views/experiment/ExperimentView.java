package io.skymind.pathmind.webapp.ui.views.experiment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import io.skymind.pathmind.shared.security.SecurityUtils;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.exception.InvalidDataException;
import io.skymind.pathmind.webapp.ui.components.notesField.NotesField;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.utils.ConfirmationUtils;
import io.skymind.pathmind.webapp.ui.utils.PushUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import io.skymind.pathmind.webapp.ui.views.experiment.components.ExperimentsNavbar;
import io.skymind.pathmind.webapp.ui.views.experiment.components.PolicyChartPanel;
import io.skymind.pathmind.webapp.ui.views.experiment.components.PolicyHighlightPanel;
import io.skymind.pathmind.webapp.ui.views.experiment.components.TrainingStartingPlaceholder;
import io.skymind.pathmind.webapp.ui.views.experiment.components.TrainingStatusDetailsPanel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;

import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.db.dao.PolicyDAO;
import io.skymind.pathmind.db.dao.RewardVariableDAO;
import io.skymind.pathmind.db.dao.RunDAO;
import io.skymind.pathmind.db.dao.TrainingErrorDAO;
import io.skymind.pathmind.services.TrainingService;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.bus.events.PolicyUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.events.RunUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.PolicyUpdateSubscriber;
import io.skymind.pathmind.webapp.bus.subscribers.RunUpdateSubscriber;
import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.shared.data.Run;
import io.skymind.pathmind.shared.data.TrainingError;
import io.skymind.pathmind.shared.utils.PolicyUtils;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.webapp.ui.components.CodeViewer;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.webapp.ui.components.SparkLine;
import io.skymind.pathmind.shared.featureflag.Feature;
import io.skymind.pathmind.shared.featureflag.FeatureManager;
import io.skymind.pathmind.webapp.ui.components.navigation.Breadcrumbs;
import io.skymind.pathmind.webapp.ui.views.model.ModelView;
import io.skymind.pathmind.webapp.ui.views.model.components.RewardVariablesTable;
import io.skymind.pathmind.webapp.ui.views.policy.ExportPolicyView;

import static io.skymind.pathmind.webapp.ui.constants.CssMindPathStyles.BOLD_LABEL;
import static io.skymind.pathmind.webapp.ui.constants.CssMindPathStyles.SECTION_TITLE_LABEL;

@Route(value = Routes.EXPERIMENT_URL, layout = MainLayout.class)
public class ExperimentView extends PathMindDefaultView implements HasUrlParameter<Long>
{

    // We have to use a lock object rather than the experiment because we are changing it's reference which makes it not thread safe. As well we cannot lock
    // on this because part of the synchronization is in the eventbus listener in a subclass (which is also why we can't use synchronize on the method.
    private Object experimentLock = new Object();

    private Button exportPolicyButton;
    private Button stopTrainingButton;
    private Button unarchiveExperimentButton;

    private long experimentId = -1;
    private long modelId = -1;
    private List<RewardVariable> rewardVariables;
    private Policy policy;
    private Experiment experiment;
    private List<Experiment> experiments = new ArrayList<>();
    private List<Float> simulationMetrics = new ArrayList<>();
    private List<float[]> sparklinesData = new ArrayList<>();

    private HorizontalLayout middlePanel;
    private HorizontalLayout simulationMetricsWrapper;
    private PolicyHighlightPanel policyHighlightPanel;
    private TrainingStatusDetailsPanel trainingStatusDetailsPanel;
    private Span panelTitle;
    private VerticalLayout rewardVariablesGroup;
    private VerticalLayout rewardFunctionGroup;
    private CodeViewer codeViewer;
    private TrainingStartingPlaceholder trainingStartingPlaceholder;
    private PolicyChartPanel policyChartPanel;
    private ExperimentsNavbar experimentsNavbar;
    private NotesField notesField;
    private Span errorDescriptionLabel;
    private RewardVariablesTable rewardVariablesTable;
    private ExperimentViewPolicyUpdateSubscriber policyUpdateSubscriber;
    private ExperimentViewRunUpdateSubscriber runUpdateSubscriber;

    @Autowired
    private ExperimentDAO experimentDAO;
    @Autowired
    private RewardVariableDAO rewardVariableDAO;
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

    private Breadcrumbs pageBreadcrumbs;
    private Button restartTraining;

    public ExperimentView() {
        super();
        addClassName("experiment-view");
        policyUpdateSubscriber = new ExperimentViewPolicyUpdateSubscriber();
        runUpdateSubscriber = new ExperimentViewRunUpdateSubscriber();
    }

    @Override
    protected void onDetach(DetachEvent event) {
        EventBus.unsubscribe(policyUpdateSubscriber);
        EventBus.unsubscribe(runUpdateSubscriber);
    }

    @Override
    protected void onAttach(AttachEvent event) {
        EventBus.subscribe(policyUpdateSubscriber);
        EventBus.subscribe(runUpdateSubscriber);
    }

    @Override
    protected Component getTitlePanel() {
        pageBreadcrumbs = createBreadcrumbs();
        return new ScreenTitlePanel(pageBreadcrumbs);
    }

    @Override
    protected Component getMainContent() {
        panelTitle = LabelFactory.createLabel("Experiment #"+experiment.getName(), SECTION_TITLE_LABEL);
        trainingStatusDetailsPanel = new TrainingStatusDetailsPanel();
        experimentsNavbar = new ExperimentsNavbar(experimentDAO, modelId, selectedExperiment -> selectExperiment(selectedExperiment), experimentToArchive -> archiveExperiment(experimentToArchive));
        setupExperimentContentPanel();
	    errorDescriptionLabel = LabelFactory.createLabel("", "tag", "error-label");

        VerticalLayout experimentContent = WrapperUtils.wrapWidthFullVertical(
                WrapperUtils.wrapWidthFullHorizontal(panelTitle, trainingStatusDetailsPanel, getButtonsWrapper()),
                errorDescriptionLabel,
                middlePanel,
                getBottomPanel());
        experimentContent.addClassName("view-section");
        HorizontalLayout pageWrapper = WrapperUtils.wrapWidthFullHorizontal(
                experimentsNavbar,
                experimentContent);
        pageWrapper.addClassName("page-content");
        pageWrapper.setPadding(true);
        return pageWrapper;
    }

    private void setupExperimentContentPanel() {
        codeViewer = new CodeViewer();
        rewardFunctionGroup = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(
            LabelFactory.createLabel("Reward Function", BOLD_LABEL), codeViewer
        );
        simulationMetricsWrapper = getSimulationMetricsTable(featureManager.isEnabled(Feature.SIMULATION_METRICS));
        String simulationMetricsHeaderText = featureManager.isEnabled(Feature.SIMULATION_METRICS) ? "Simulation Metrics" : "Reward Variables";
        rewardVariablesGroup = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(
            LabelFactory.createLabel(simulationMetricsHeaderText, BOLD_LABEL), simulationMetricsWrapper
        );

        middlePanel = WrapperUtils.wrapWidthFullHorizontal();
        middlePanel.add(rewardVariablesGroup, rewardFunctionGroup);
        middlePanel.addClassName("middle-panel");
        middlePanel.setPadding(false);
    }

    private HorizontalLayout getSimulationMetricsTable(Boolean showSimulationMetrics) {
        HorizontalLayout tableWrapper = new HorizontalLayout();
        tableWrapper.addClassName("simulation-metrics-table-wrapper");

        rewardVariablesTable = new RewardVariablesTable();
        rewardVariablesTable.setCodeEditorMode();
        rewardVariablesTable.setSizeFull();
        tableWrapper.add(rewardVariablesTable);

        if (showSimulationMetrics) {
            VerticalLayout metricsWrapper = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing();
            metricsWrapper.addClassName("metrics-wrapper");
            VerticalLayout sparklinesWrapper = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing();
            sparklinesWrapper.addClassName("sparklines-wrapper");

            IntStream.range(0, simulationMetrics.size())
                    .forEach(idx -> {
                        metricsWrapper.add(new Span(simulationMetrics.get(idx).toString()));
                        SparkLine sparkLine = new SparkLine();
                        sparkLine.setSparkLine(sparklinesData.get(idx), idx);
                        sparklinesWrapper.add(sparkLine);
                    });

            tableWrapper.add(metricsWrapper, sparklinesWrapper);
        }

        return tableWrapper;
    }

    private Div getButtonsWrapper() {
        restartTraining = new Button("Restart Training", new Image("frontend/images/start.svg", "run"), click -> {
            synchronized (experimentLock) {
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
        policyHighlightPanel = new PolicyHighlightPanel();

        VerticalLayout chartWrapper = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(
                        trainingStartingPlaceholder,
                        policyChartPanel);
        chartWrapper.addClassName("row-2-of-3");

        HorizontalLayout bottomPanel = WrapperUtils.wrapWidthFullHorizontal(
                policyHighlightPanel,
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
                    trainingService.stopRun(experiment, EventBus::fireEventBusUpdates);
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

    private void archiveExperiment(Experiment experimentToArchive) {
        ConfirmationUtils.archive("Experiment #"+experimentToArchive.getName(), () -> {
            experimentDAO.archive(experimentToArchive.getId(), true);
            experiments.remove(experimentToArchive);
            if (experiments.isEmpty()) {
                getUI().ifPresent(ui -> ui.navigate(ModelView.class, experimentToArchive.getModelId()));
            } else {
                Experiment currentExperiment = (experimentToArchive.getId() == experimentId) ? experiments.get(0) : experiment;
                if (experimentToArchive.getId() == experimentId) {
                    selectExperiment(currentExperiment);
                }
                getUI().ifPresent(ui -> experimentsNavbar.setExperiments(ui, experiments, currentExperiment));
            }
        });
    }

    private void unarchiveExperiment() {
        ConfirmationUtils.unarchive("experiment", () -> {
            experimentDAO.archive(experiment.getId(), false);
            getUI().ifPresent(ui -> ui.navigate(ExperimentView.class, experiment.getId()));
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
            experimentId = selectedExperiment.getId();
            loadExperimentData();
            updateScreenComponents();
            notesField.setNotesText(experiment.getUserNotes());
            pageBreadcrumbs.setText(3, "Experiment #" + experiment.getName());
            experimentsNavbar.setCurrentExperiment(selectedExperiment);
            
            if (ExperimentUtils.isDraftRunType(selectedExperiment)) {
                getUI().ifPresent(ui -> ui.navigate(NewExperimentView.class, selectedExperiment.getId()));
            } else {
                getUI().ifPresent(ui -> ui.getPage().getHistory().pushState(null, "experiment/" + selectedExperiment.getId()));
            }
        }
    }

    @Override
    protected void initLoadData() throws InvalidDataException {
        experiment = experimentDAO.getExperimentIfAllowed(experimentId, SecurityUtils.getUserId())
                .orElseThrow(() -> new InvalidDataException("Attempted to access Experiment: " + experimentId));
        loadExperimentData();
    }

    private void loadExperimentData() {
        modelId = experiment.getModelId();
        experiment.setPolicies(policyDAO.getPoliciesForExperiment(experimentId));
        rewardVariables = rewardVariableDAO.getRewardVariablesForModel(modelId);
        policy = selectBestPolicy(experiment.getPolicies());
        experiment.setRuns(runDAO.getRunsForExperiment(experiment));
        if (!experiment.isArchived()) {
            experiments = experimentDAO.getExperimentsForModel(modelId).stream()
                                .sorted(Comparator.comparing(Experiment::getDateCreated).reversed())
                                .filter(exp -> !exp.isArchived()).collect(Collectors.toList());
        }

        // This are mock data to be removed once the backend for simulation metrics is implemented
        simulationMetrics.add(123f);
        simulationMetrics.add(2.1f);
        simulationMetrics.add(0.3234234f);
        simulationMetrics.add(12323.1f);

        // This are mock data to be removed once the backend for simulation metrics is implemented
        float f0[] = {123f, 120f, 116f, 128f, 125f, 123f, 124f, 129f, 122f};
        float f1[] = {2.1f, 2.2f, 2.0f, 2.34f, 2.334f, 2.211f, 2.23f, 2.24f, 2.1f};
        float f2[] = {0.3234234f, 0.3234434f, 0.3234264f, 0.3234834f, 0.3214234f, 0.321734f, 0.3234934f, 0.3234534f, 0.3234234f};
        float f3[] = {12322.1f, 12323.1f, 12325.1f, 12323.8f, 12323.4f, 12323.0f, 12353.1f, 12323.8f, 12324.1f};
        sparklinesData.add(f0);
        sparklinesData.add(f1);
        sparklinesData.add(f2);
        sparklinesData.add(f3);
    }

    @Override
    protected void initScreen(BeforeEnterEvent event) {
        updateScreenComponents();
        experimentsNavbar.setExperiments(event.getUI(), experiments, experiment);
    }

    private void updateScreenComponents() {
        clearErrorState();
        setPolicyChartVisibility();
        experimentsNavbar.setVisible(!experiment.isArchived());
        panelTitle.setText("Experiment #"+experiment.getName());
        codeViewer.setValue(experiment.getRewardFunction(), rewardVariables);
        rewardVariablesTable.setIsReadOnly(true);
        if (!rewardVariables.isEmpty()) {
            rewardVariablesTable.setValue(rewardVariables);
        } else {
            rewardVariablesTable.setVariableSize(experiment.getModel().getRewardVariablesCount());
        }
        policyChartPanel.setExperiment(experiment, policy);
        updateDetailsForExperiment();
    }

    private void setPolicyChartVisibility() {
        RunStatus trainingStatus = ExperimentUtils.getTrainingStatus(experiment);
        trainingStartingPlaceholder.setVisible(trainingStatus == RunStatus.Starting);
        policyChartPanel.setVisible(trainingStatus != RunStatus.Starting);
    }

    private Policy selectBestPolicy(List<Policy> policies) {
        return policies.stream()
                .filter(p -> PolicyUtils.getLastScore(p) != null && !Double.isNaN(PolicyUtils.getLastScore(p)))
                .max(Comparator.comparing(PolicyUtils::getLastScore).thenComparing(PolicyUtils::getLastIteration))
                .orElse(null);
    }

    private void updateUIForError(TrainingError error) {
        errorDescriptionLabel.setText(error.getDescription());
		errorDescriptionLabel.setVisible(true);
        restartTraining.setVisible(error.isRestartable());
        restartTraining.setEnabled(error.isRestartable());
    }

    private void clearErrorState() {
        errorDescriptionLabel.setText(null);
		errorDescriptionLabel.setVisible(false);
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

    private void addOrUpdateRun(Run updatedRun) {
        experiment.getRuns().stream()
                .filter(run -> run.getId() == updatedRun.getId())
                .findAny()
                .ifPresentOrElse(
                        run -> experiment.getRuns().set(experiment.getRuns().indexOf(run), updatedRun),
                        () -> experiment.getRuns().add(updatedRun));
    }

    private void updateDetailsForExperiment() {
        updateButtonEnablement();
        trainingStatusDetailsPanel.updateTrainingDetailsPanel(experiment);
        RunStatus status = ExperimentUtils.getTrainingStatus(experiment);
        if (status == RunStatus.Error || status == RunStatus.Killed) {
            experiment.getRuns().stream()
                    .filter(r -> r.getStatusEnum() == RunStatus.Error || r.getStatusEnum() == RunStatus.Killed)
                    .findAny()
                    .map(Run::getTrainingErrorId)
                    .flatMap(trainingErrorDAO::getErrorById)
                    .ifPresent(this::updateUIForError);
        }
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

    private void updatedRunForPolicies(Run run) {
        experiment.getPolicies().stream()
            .filter(policy -> policy.getRunId() == run.getId())
            .forEach(policy -> policy.setRun(run));
    }

    class ExperimentViewPolicyUpdateSubscriber implements PolicyUpdateSubscriber
    {
        @Override
        public void handleBusEvent(PolicyUpdateBusEvent event) {
            synchronized (experimentLock) {
                // Need a check in case the experiment was on hold waiting for the change of experiment to load
                if (event.getExperimentId() != experimentId)
                    return;
                // Update or insert the policy in experiment.getPolicies

                addOrUpdatePolicies(event.getPolicies());

                // Calculate the best policy again
                policy = selectBestPolicy(experiment.getPolicies());
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

        @Override
        public boolean isAttached() {
            return ExperimentView.this.getUI().isPresent();
        }
    }

    class ExperimentViewRunUpdateSubscriber implements RunUpdateSubscriber {
        @Override
        public void handleBusEvent(RunUpdateBusEvent event) {
            if (isSameExperiment(event)) {
                addOrUpdateRun(event.getRun());
                updatedRunForPolicies(event.getRun());
                PushUtils.push(getUI(), () -> {
                    setPolicyChartVisibility();
                    updateDetailsForExperiment();
                });
            } else if (isSameModel(event)) {
                if (!experiments.contains(event.getRun().getExperiment())) {
                    experiments = experimentDAO.getExperimentsForModel(modelId).stream().filter(exp -> !exp.isArchived()).collect(Collectors.toList());
                    PushUtils.push(getUI(), ui -> experimentsNavbar.setExperiments(ui, experiments, experiment));
                }
            }
        }

        @Override
        public boolean filterBusEvent(RunUpdateBusEvent event) {
            return isSameExperiment(event) || isSameModel(event);
        }

        @Override
        public boolean isAttached() {
            return ExperimentView.this.getUI().isPresent();
        }
        
        private boolean isSameExperiment(RunUpdateBusEvent event) {
            return experiment != null && experiment.getId() == event.getRun().getExperiment().getId();
        }
        
        private boolean isSameModel(RunUpdateBusEvent event) {
            return experiment != null && experiment.getModelId() == event.getRun().getModel().getId();
        }
    }
}
