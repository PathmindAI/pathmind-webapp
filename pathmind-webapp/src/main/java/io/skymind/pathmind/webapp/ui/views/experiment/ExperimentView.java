package io.skymind.pathmind.webapp.ui.views.experiment;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
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
import io.skymind.pathmind.db.dao.*;
import io.skymind.pathmind.services.TrainingService;
import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.data.*;
import io.skymind.pathmind.shared.featureflag.Feature;
import io.skymind.pathmind.shared.featureflag.FeatureManager;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.shared.security.SecurityUtils;
import io.skymind.pathmind.shared.utils.PolicyUtils;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.bus.events.PolicyUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.events.RunUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.PolicyUpdateSubscriber;
import io.skymind.pathmind.webapp.bus.subscribers.RunUpdateSubscriber;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.exception.InvalidDataException;
import io.skymind.pathmind.webapp.ui.components.CodeViewer;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.webapp.ui.components.SparkLine;
import io.skymind.pathmind.webapp.ui.components.navigation.Breadcrumbs;
import io.skymind.pathmind.webapp.ui.components.notesField.NotesField;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.utils.ConfirmationUtils;
import io.skymind.pathmind.webapp.ui.utils.PushUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import io.skymind.pathmind.webapp.ui.views.experiment.components.*;
import io.skymind.pathmind.webapp.ui.views.model.ModelView;
import io.skymind.pathmind.webapp.ui.views.model.components.RewardVariablesTable;
import io.skymind.pathmind.webapp.ui.views.policy.ExportPolicyView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    private List<Double> simulationMetrics = new ArrayList<>();
    private List<double[]> sparklinesData = new ArrayList<>();
    private Boolean showSimulationMetrics;

    private HorizontalLayout middlePanel;
    private HorizontalLayout simulationMetricsWrapper;
    private VerticalLayout metricsWrapper;
    private VerticalLayout sparklinesWrapper;
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
        showSimulationMetrics = featureManager.isEnabled(Feature.SIMULATION_METRICS);
        simulationMetricsWrapper = getSimulationMetricsTable();
        String simulationMetricsHeaderText = showSimulationMetrics ? "Simulation Metrics" : "Reward Variables";
        rewardVariablesGroup = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(
            LabelFactory.createLabel(simulationMetricsHeaderText, BOLD_LABEL), simulationMetricsWrapper
        );

        middlePanel = WrapperUtils.wrapWidthFullHorizontal();
        middlePanel.add(rewardVariablesGroup, rewardFunctionGroup);
        middlePanel.addClassName("middle-panel");
        middlePanel.setPadding(false);
    }

    private HorizontalLayout getSimulationMetricsTable() {
        HorizontalLayout tableWrapper = new HorizontalLayout();
        tableWrapper.addClassName("simulation-metrics-table-wrapper");

        rewardVariablesTable = new RewardVariablesTable();
        rewardVariablesTable.setCodeEditorMode();
        rewardVariablesTable.setSizeFull();
        tableWrapper.add(rewardVariablesTable);

        if (showSimulationMetrics) {
            metricsWrapper = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing();
            metricsWrapper.addClassName("metrics-wrapper");
            sparklinesWrapper = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing();
            sparklinesWrapper.addClassName("sparklines-wrapper");

            updateSimulationMetrics();
            tableWrapper.add(metricsWrapper, sparklinesWrapper);
        }

        return tableWrapper;
    }

    private void updateSimulationMetrics() {
        metricsWrapper.removeAll();
        sparklinesWrapper.removeAll();

        updateSimulationMetricsData();

        IntStream.range(0, simulationMetrics.size())
                .forEach(idx -> {
                    metricsWrapper.add(new Span(simulationMetrics.get(idx).toString()));
                    SparkLine sparkLine = new SparkLine();
                    sparkLine.setSparkLine(sparklinesData.get(idx), idx);
                    sparklinesWrapper.add(sparkLine);
                });
    }

    private void updateSimulationMetricsData() {
        List<Metrics> metricsList = policy == null ? null : policy.getMetrics();
        sparklinesData.clear();
        simulationMetrics.clear();

        if (metricsList != null && metricsList.size() > 0) {
            // set the last metrics
            Metrics lastMetrics = metricsList.get(metricsList.size() - 1);
            lastMetrics.getMetricsThisIter().stream()
                .forEach(metricsThisIter -> simulationMetrics.add(metricsThisIter.getMean()));

            // index, metrics list
            Map<Integer, List<Double>> sparkLineMap = new HashMap<>();
            metricsList.stream().forEach(metrics ->
                metrics.getMetricsThisIter().forEach(mIter -> {
                    int index = mIter.getIndex();

                    List<Double> data = sparkLineMap.containsKey(index) ? sparkLineMap.get(index) : new ArrayList<>();
                    data.add(mIter.getMean());
                    sparkLineMap.put(index, data);
                })
            );

            // convert List<Double> to double[] because sparLine needs an array of primitive types
            sparkLineMap.entrySet().stream()
                .map(e -> e.getValue().stream().mapToDouble(Double::doubleValue).toArray())
                .forEach(arr -> sparklinesData.add(arr));
        }
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
        stopTrainingButton.addThemeName("secondary");
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
        if (showSimulationMetrics) {
            updateSimulationMetrics();
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
                    if (showSimulationMetrics && policy.getMetrics() != null && policy.getMetrics().size() > 0) {
                        updateSimulationMetrics();
                    }
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
