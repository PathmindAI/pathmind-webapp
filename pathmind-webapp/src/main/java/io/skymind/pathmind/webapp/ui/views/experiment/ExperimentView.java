package io.skymind.pathmind.webapp.ui.views.experiment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.exception.InvalidDataException;
import io.skymind.pathmind.webapp.ui.components.notesField.NotesField;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.utils.ConfirmationUtils;
import io.skymind.pathmind.webapp.ui.utils.NotificationUtils;
import io.skymind.pathmind.webapp.ui.utils.PushUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import io.skymind.pathmind.webapp.ui.views.experiment.components.ExperimentsNavbar;
import io.skymind.pathmind.webapp.ui.views.experiment.components.PolicyChartPanel;
import io.skymind.pathmind.webapp.ui.views.experiment.components.PolicyHighlightPanel;
import io.skymind.pathmind.webapp.ui.views.experiment.components.RewardFunctionEditor;
import io.skymind.pathmind.webapp.ui.views.experiment.components.TrainingStartingPlaceholder;
import io.skymind.pathmind.webapp.ui.views.experiment.components.TrainingStatusDetailsPanel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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
import io.skymind.pathmind.db.dao.UserDAO;
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
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.shared.featureflag.Feature;
import io.skymind.pathmind.shared.featureflag.FeatureManager;
import io.skymind.pathmind.webapp.ui.components.navigation.Breadcrumbs;
import io.skymind.pathmind.webapp.ui.views.model.ModelView;
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
	private Button archiveExperimentButton;
	private Button unarchiveExperimentButton;

	private long experimentId = -1;
	private long modelId = -1;
	private List<RewardVariable> rewardVariables;
	private Policy policy;
	private Experiment experiment;
	private List<Experiment> experiments = new ArrayList<>();

	private VerticalLayout middlePanel;
	private PolicyHighlightPanel policyHighlightPanel;
	private TrainingStatusDetailsPanel trainingStatusDetailsPanel;
	private Span panelTitle;
	private VerticalLayout rewardFunctionGroup;
	private RewardFunctionEditor rewardFunctionEditor;
	private TrainingStartingPlaceholder trainingStartingPlaceholder;
	private PolicyChartPanel policyChartPanel;
	private ExperimentsNavbar experimentsNavbar;
	private NotesField notesField;

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
	private UserDAO userDAO;
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
		setupLeftPanel();
		HorizontalLayout pageWrapper = WrapperUtils.wrapWidthFullHorizontal(
				experimentsNavbar,
				middlePanel,
				getRightPanel());
		pageWrapper.addClassName("page-content");
		pageWrapper.setPadding(true);

		return pageWrapper;
	}

	private void setupLeftPanel() {
		experimentsNavbar = new ExperimentsNavbar(experimentDAO, modelId, selectedExperiment -> selectExperiment(selectedExperiment));
		panelTitle = LabelFactory.createLabel("Experiment #"+experiment.getName(), SECTION_TITLE_LABEL);
		policyChartPanel = new PolicyChartPanel();
		policyChartPanel.setPadding(false);
		rewardFunctionEditor = new RewardFunctionEditor();
		rewardFunctionEditor.setReadonly(true);
		rewardFunctionEditor.setSizeFull();
		rewardFunctionGroup = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(
			LabelFactory.createLabel("Reward Function", BOLD_LABEL), rewardFunctionEditor
		);
		trainingStartingPlaceholder = new TrainingStartingPlaceholder();
		middlePanel = WrapperUtils.wrapWidthFullVertical(
				panelTitle,
				rewardFunctionGroup,
				trainingStartingPlaceholder,
				policyChartPanel);
		middlePanel.addClassName("view-section");
	}

	private VerticalLayout getRightPanel() {
		policyHighlightPanel = new PolicyHighlightPanel();
		trainingStatusDetailsPanel = new TrainingStatusDetailsPanel();

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
		exportPolicyButton.addClassName("half-width");
		exportPolicyButton.setVisible(false);

		stopTrainingButton = new Button("Stop Training", click -> {
			showStopTrainingConfirmationDialog();
		});
		stopTrainingButton.addThemeName("secondary");
		stopTrainingButton.addClassName("half-width");
		stopTrainingButton.setVisible(true);

		archiveExperimentButton = new Button("Archive", VaadinIcon.ARCHIVE.create(), click -> archiveExperiment());
		archiveExperimentButton.addThemeName("secondary");
		archiveExperimentButton.addClassName("half-width");

		unarchiveExperimentButton = new Button("Unarchive", VaadinIcon.ARROW_BACKWARD.create(), click -> unarchiveExperiment());
		unarchiveExperimentButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		unarchiveExperimentButton.addClassName("half-width");

		notesField = createViewNotesField();

		VerticalLayout buttonsWrapper = WrapperUtils.wrapWidthFullCenterVertical(
			exportPolicyButton,
			restartTraining,
			stopTrainingButton,
			archiveExperimentButton,
			unarchiveExperimentButton
		);
		buttonsWrapper.setPadding(false);

		VerticalLayout rightPanel = WrapperUtils.wrapSizeFullVertical(
				buttonsWrapper,
				trainingStatusDetailsPanel,
				policyHighlightPanel,
				notesField);
		rightPanel.addClassName("right-panel");
		return rightPanel;
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

	private void archiveExperiment() {
		ConfirmationUtils.archive("experiment", () -> {
			experimentDAO.archive(experiment.getId(), true);
			experiments.remove(experiment);
			if (experiments.isEmpty()) {
				getUI().ifPresent(ui -> ui.navigate(ModelView.class, experiment.getModelId()));
			} else {
				Experiment currentExperiment = experiments.get(0);
				selectExperiment(currentExperiment);
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
			"Experiment Notes",
			experiment.getUserNotes(),
			updatedNotes -> {
				experimentDAO.updateUserNotes(experimentId, updatedNotes);
				NotificationUtils.showSuccess("Notes saved");
				segmentIntegrator.updatedNotesExperimentView();
			}
		);
	}

	@Override
	public void setParameter(BeforeEvent event, Long experimentId) {
		this.experimentId = experimentId;
	}

	@Override
	protected boolean isAccessAllowedForUser() {
		return userDAO.isUserAllowedAccessToExperiment(experimentId);
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
			getUI().ifPresent(ui -> ui.getPage().getHistory().pushState(null, "experiment/" + selectedExperiment.getId()));
		}
	}

	@Override
	protected void initLoadData() throws InvalidDataException {
		experiment = experimentDAO.getExperiment(experimentId)
				.orElseThrow(() -> new InvalidDataException("Attempted to access Experiment: " + experimentId));
		loadExperimentData();
	}

	private void loadExperimentData() {
		modelId = experiment.getModelId();
		experiment.setPolicies(policyDAO.getPoliciesForExperiment(experimentId));
		if (featureManager.isEnabled(Feature.REWARD_VARIABLES_FEATURE)) {
			rewardVariables = rewardVariableDAO.getRewardVariablesForModel(modelId);
		}
		policy = selectBestPolicy(experiment.getPolicies());
		experiment.setRuns(runDAO.getRunsForExperiment(experiment));
		if (!experiment.isArchived()) {
			experiments = experimentDAO.getExperimentsForModel(modelId).stream().filter(exp -> !exp.isArchived()).collect(Collectors.toList());
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
		rewardFunctionEditor.setValue(experiment.getRewardFunction());
		if (featureManager.isEnabled(Feature.REWARD_VARIABLES_FEATURE)) {
			rewardFunctionEditor.setVariableNames(rewardVariables);
		}
		policyChartPanel.setExperiment(experiment, policy);
		trainingStatusDetailsPanel.updateTrainingDetailsPanel(experiment);
		updateRightPanelForExperiment();
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
		policyHighlightPanel.setErrorDescription(error.getDescription());
		restartTraining.setVisible(error.isRestartable());
		restartTraining.setEnabled(error.isRestartable());
	}

	private void clearErrorState() {
		policyHighlightPanel.setErrorDescription(null);
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

	private void updateRightPanelForExperiment() {
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
		archiveExperimentButton.setVisible(!experiment.isArchived());
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
					updateRightPanelForExperiment();
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
					updateRightPanelForExperiment();
				});
			} else if (isSameModel(event)) {
				if (!experiments.contains(event.getRun().getExperiment())) {
					experiments = experimentDAO.getExperimentsForModel(modelId).stream().filter(exp -> !exp.isArchived()).collect(Collectors.toList());
					getUI().ifPresent(ui -> PushUtils.push(ui, () -> experimentsNavbar.setExperiments(ui, experiments, experiment)));
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
