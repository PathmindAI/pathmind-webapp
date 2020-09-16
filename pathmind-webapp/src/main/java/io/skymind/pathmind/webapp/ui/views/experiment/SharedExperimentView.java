package io.skymind.pathmind.webapp.ui.views.experiment;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.db.dao.*;
import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.constants.ViewPermission;
import io.skymind.pathmind.shared.data.*;
import io.skymind.pathmind.shared.featureflag.Feature;
import io.skymind.pathmind.shared.featureflag.FeatureManager;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.shared.security.SecurityUtils;
import io.skymind.pathmind.shared.utils.ModelUtils;
import io.skymind.pathmind.shared.utils.PolicyUtils;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.bus.events.ExperimentUpdatedBusEvent;
import io.skymind.pathmind.webapp.bus.events.PolicyUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.events.RunUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.ExperimentUpdatedSubscriber;
import io.skymind.pathmind.webapp.bus.subscribers.PolicyUpdateSubscriber;
import io.skymind.pathmind.webapp.bus.subscribers.RunUpdateSubscriber;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.exception.InvalidDataException;
import io.skymind.pathmind.webapp.security.annotation.Permission;
import io.skymind.pathmind.webapp.ui.components.CodeViewer;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.webapp.ui.components.atoms.TagLabel;
import io.skymind.pathmind.webapp.ui.components.notesField.NotesField;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.utils.PushUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import io.skymind.pathmind.webapp.ui.views.experiment.components.PolicyChartPanel;
import io.skymind.pathmind.webapp.ui.views.experiment.components.TrainingStartingPlaceholder;
import io.skymind.pathmind.webapp.ui.views.experiment.components.TrainingStatusDetailsPanel;
import io.skymind.pathmind.webapp.ui.views.experiment.components.notification.StoppedTrainingNotification;
import io.skymind.pathmind.webapp.ui.views.experiment.simulationMetrics.SimulationMetricsPanel;
import io.skymind.pathmind.webapp.ui.views.model.ModelCheckerService;
import io.skymind.pathmind.webapp.ui.views.model.components.ObservationsPanel;
import io.skymind.pathmind.webapp.ui.views.policy.ExportPolicyView;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.*;

@Route(value = Routes.SHARED_EXPERIMENT, layout = MainLayout.class)
@Slf4j
@Permission(permissions = ViewPermission.EXTENDED_READ)
public class SharedExperimentView extends PathMindDefaultView implements HasUrlParameter<Long>
{
    private Object experimentLock = new Object();

    private Button exportPolicyButton;

    private long experimentId = -1;
    private long modelId = -1;
    private List<RewardVariable> rewardVariables;
    private Policy policy;
    private Experiment experiment;

    private List<Observation> modelObservations = new ArrayList<>();
    private List<Observation> experimentObservations = new ArrayList<>();

    private HorizontalLayout middlePanel;
    private TrainingStatusDetailsPanel trainingStatusDetailsPanel;
    private TagLabel archivedLabel;
    private Span panelTitle;
    private VerticalLayout rewardVariablesGroup;
    private VerticalLayout rewardFunctionGroup;
    private CodeViewer codeViewer;
    private TrainingStartingPlaceholder trainingStartingPlaceholder;
    private PolicyChartPanel policyChartPanel;
    private NotesField notesField;

    private ObservationsPanel observationsPanel;

    private StoppedTrainingNotification stoppedTrainingNotification;
    private SimulationMetricsPanel simulationMetricsPanel;

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
    private RunDAO runDAO;
    @Autowired
    private FeatureManager featureManager;
    @Autowired
    private ModelCheckerService modelCheckerService;
    @Value("${pathmind.early-stopping.url}")
    private String earlyStoppingUrl;

    private SharedExperimentViewRunUpdateSubscriber runUpdateSubscriber;

    public SharedExperimentView() {
        addClassName("experiment-view");
        runUpdateSubscriber = new SharedExperimentViewRunUpdateSubscriber(this, this::getUI);
    }

    @Override
    protected void onAttach(AttachEvent event) {
        EventBus.subscribe(this, runUpdateSubscriber,
                new SharedExperimentViewPolicyUpdateSubscriber(this::getUI),
                new SharedExperimentViewExperimentUpdatedSubscriber(this::getUI)
                );
    }

    @Override
    protected void onDetach(DetachEvent event) {
        EventBus.unsubscribe(this);
    }

    @Override
    protected Component getTitlePanel() {
        return new ScreenTitlePanel("Shared Experiment");
    }

    @Override
    protected Component getMainContent() {
        panelTitle = LabelFactory.createLabel("Experiment #"+experiment.getName(), SECTION_TITLE_LABEL);
        archivedLabel = new TagLabel("Archived", false, "small");
        trainingStatusDetailsPanel = new TrainingStatusDetailsPanel();
        setupExperimentContentPanel();

        Span modelNeedToBeUpdatedLabel = modelCheckerService.createInvalidErrorLabel(experiment.getModel());
        modelNeedToBeUpdatedLabel.getStyle().set("margin-top", "2px");

        stoppedTrainingNotification = new StoppedTrainingNotification(earlyStoppingUrl);

        VerticalLayout experimentContent = WrapperUtils.wrapWidthFullVertical(
                WrapperUtils.wrapWidthFullHorizontal(panelTitle, archivedLabel, trainingStatusDetailsPanel, getButtonsWrapper()),
                stoppedTrainingNotification,
                modelNeedToBeUpdatedLabel,
                middlePanel,
                getBottomPanel());
        experimentContent.addClassName("view-section");
        HorizontalLayout pageWrapper = WrapperUtils.wrapWidthFullHorizontal(
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
        exportPolicyButton = new Button("Export Policy", click -> getUI().ifPresent(ui -> ui.navigate(ExportPolicyView.class, policy.getId())));
        exportPolicyButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        exportPolicyButton.setVisible(false);

        notesField = createViewNotesField();

        Div buttonsWrapper = new Div(
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

    private NotesField createViewNotesField() {
        return new NotesField(
            "Notes",
            experiment.getUserNotes()
        );
    }

    @Override
    public void setParameter(BeforeEvent event, Long experimentId) {
        this.experimentId = experimentId;
    }

    @Override
    protected void initLoadData() throws InvalidDataException {
        synchronized (experimentLock) {
            experiment = experimentDAO.getSharedExperiment(experimentId, SecurityUtils.getUserId())
                    .orElseThrow(() -> new InvalidDataException("Attempted to access Experiment: " + experimentId));
            runUpdateSubscriber.setExperiment(experiment);
            loadExperimentData();
        }
    }

    private void loadExperimentData() {
        modelId = experiment.getModelId();
        experiment.setPolicies(policyDAO.getPoliciesForExperiment(experimentId));
        rewardVariables = rewardVariableDAO.getRewardVariablesForModel(modelId);
		modelObservations = observationDAO.getObservationsForModel(experiment.getModelId());
		experimentObservations = observationDAO.getObservationsForExperiment(experimentId);
        policy = PolicyUtils.selectBestPolicy(experiment.getPolicies());
        experiment.setRuns(runDAO.getRunsForExperiment(experiment));
    }

    @Override
    protected void initScreen(BeforeEnterEvent event) {
        updateScreenComponents();
    }

    private void updateScreenComponents() {
        clearErrorState();
        setPolicyChartVisibility(experiment.getTrainingStatusEnum());
        panelTitle.setText("Experiment #"+experiment.getName());
        if (ModelUtils.isValidModel(experiment.getModel())) {
            codeViewer.setValue(experiment.getRewardFunction());
        } else {
            codeViewer.setValue(experiment.getRewardFunction(), rewardVariables);
        }
        observationsPanel.setSelectedObservations(experimentObservations);
        policyChartPanel.setExperiment(experiment, policy);
        updateDetailsForExperiment();
    }

    public void setPolicyChartVisibility(RunStatus runStatus) {
        boolean isStarting = runStatus == RunStatus.Starting;
        trainingStartingPlaceholder.setVisible(isStarting);
        policyChartPanel.setVisible(!isStarting);
    }

    private void updateUIForError(String errorText) {
        stoppedTrainingNotification.showTheReasonWhyTheTrainingStopped(errorText, ERROR_LABEL, false);
    }

    private void clearErrorState() {
        stoppedTrainingNotification.clearErrorState();
        updateButtonEnablement();
    }

    public void updateDetailsForExperiment() {
        updateButtonEnablement();
        archivedLabel.setVisible(experiment.isArchived());
        trainingStatusDetailsPanel.updateTrainingDetailsPanel(experiment);
        RunStatus status = experiment.getTrainingStatusEnum();
        if (RunStatus.isError(status)) {
            ExperimentUtils.getTrainingErrorAndMessage(trainingErrorDAO, experiment)
                    .ifPresent(pair -> {
                        this.updateUIForError(pair.getRight());
                    });
        }
        else {
            ExperimentUtils.getEarlyStopReason(experiment)
                    .ifPresent(reason -> {
                        String label;
                        if (reason.isSuccess()) {
                            label = SUCCESS_LABEL;
                        }
                        else {
                            label = WARNING_LABEL;
                        }
                        stoppedTrainingNotification.showTheReasonWhyTheTrainingStopped(reason.getMessage(), label, true);
                    });
        }
    }

    private void updateButtonEnablement() {
        RunStatus trainingStatus = experiment.getTrainingStatusEnum();
        boolean isCompleted = trainingStatus == RunStatus.Completed;
        exportPolicyButton.setVisible(isCompleted && policy != null && policy.hasFile());
    }

    public static class SharedExperimentViewRunUpdateSubscriber extends RunUpdateSubscriber {

        private Experiment experiment;

        private SharedExperimentView sharedExperimentView;

        public SharedExperimentViewRunUpdateSubscriber(SharedExperimentView sharedExperimentView, Supplier<Optional<UI>> getUISupplier) {
            super(getUISupplier);
            this.sharedExperimentView = sharedExperimentView;
        }

        public void setExperiment(Experiment experiment) {
            this.experiment = experiment;
        }

        @Override
        public void handleBusEvent(RunUpdateBusEvent event) {
            ExperimentUtils.addOrUpdateRun(experiment, event.getRun());
            ExperimentUtils.updatedRunForPolicies(experiment, event.getRun());
            PushUtils.push(getUiSupplier(), () -> {
                sharedExperimentView.setPolicyChartVisibility(experiment.getTrainingStatusEnum());
                sharedExperimentView.updateDetailsForExperiment();
            });
        }

        @Override
        public boolean filterBusEvent(RunUpdateBusEvent event) {
            return isSameExperiment(event);
        }

        private boolean isSameExperiment(RunUpdateBusEvent event) {
            return ExperimentUtils.isSameExperiment(experiment, event.getRun().getExperiment());
        }
    }

    class SharedExperimentViewPolicyUpdateSubscriber extends PolicyUpdateSubscriber
    {
        public SharedExperimentViewPolicyUpdateSubscriber(Supplier<Optional<UI>> getUISupplier) {
            super(getUISupplier);
        }

        @Override
        public void handleBusEvent(PolicyUpdateBusEvent event) {
            synchronized (experimentLock) {
                // Update or insert the policy in experiment.getPolicies
                ExperimentUtils.addOrUpdatePolicies(experiment, event.getPolicies());

                // Calculate the best policy again
                policy = PolicyUtils.selectBestPolicy(experiment.getPolicies());
                PushUtils.push(getUI(), () -> {
                    if (policy != null) {
                        policyChartPanel.updateChart(event.getPolicies(), policy);
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

    class SharedExperimentViewExperimentUpdatedSubscriber extends ExperimentUpdatedSubscriber {

        public SharedExperimentViewExperimentUpdatedSubscriber(Supplier<Optional<UI>> getUISupplier) {
            super(getUISupplier);
        }

        @Override
        public void handleBusEvent(ExperimentUpdatedBusEvent event) {
            PushUtils.push(getUiSupplier(), () -> {
                if (event.isStartedTrainingEventType()) {
                    RunStatus newStatus = RunStatus.Starting;
                    trainingStatusDetailsPanel.updateTrainingDetailsPanel(experiment);
                    setPolicyChartVisibility(newStatus);
                }
            });
        }

        @Override
        public boolean filterBusEvent(ExperimentUpdatedBusEvent event) {
            if (experiment == null) {
                return false;
            }
            return ExperimentUtils.isSameExperiment(event.getExperiment(), experiment);
        }
    }

}
