package io.skymind.pathmind.ui.views.experiment;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.bus.EventBus;
import io.skymind.pathmind.bus.events.PolicyUpdateBusEvent;
import io.skymind.pathmind.bus.events.RunUpdateBusEvent;
import io.skymind.pathmind.bus.subscribers.PolicyUpdateSubscriber;
import io.skymind.pathmind.bus.subscribers.RunUpdateSubscriber;
import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.Run;
import io.skymind.pathmind.data.TrainingError;
import io.skymind.pathmind.data.utils.ExperimentUtils;
import io.skymind.pathmind.data.utils.PolicyUtils;
import io.skymind.pathmind.db.dao.*;
import io.skymind.pathmind.exception.InvalidDataException;
import io.skymind.pathmind.security.Routes;
import io.skymind.pathmind.services.PolicyFileService;
import io.skymind.pathmind.services.TrainingService;
import io.skymind.pathmind.ui.components.dialog.RunConfirmDialog;
import io.skymind.pathmind.ui.components.navigation.Breadcrumbs;
import io.skymind.pathmind.ui.components.notesField.NotesField;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.ui.utils.NotificationUtils;
import io.skymind.pathmind.ui.utils.PushUtils;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.ui.views.PathMindDefaultView;
import io.skymind.pathmind.ui.views.experiment.components.*;
import io.skymind.pathmind.ui.views.policy.ExportPolicyView;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@CssImport("./styles/styles.css")
@Route(value = Routes.EXPERIMENT_URL, layout = MainLayout.class)
public class ExperimentView extends PathMindDefaultView implements HasUrlParameter<Long>
{
	private static final double DEFAULT_SPLIT_PANE_RATIO = 70;

	// We have to use a lock object rather than the experiment because we are changing it's reference which makes it not thread safe. As well we cannot lock
	// on this because part of the synchronization is in the eventbus listener in a subclass (which is also why we can't use synchronize on the method.
	private Object experimentLock = new Object();

	private Button exportPolicyButton;

	private long experimentId = -1;
	private long modelId = -1;
	private Policy policy;
	private Experiment experiment;
	private List<Experiment> experiments;

	private PolicyHighlightPanel policyHighlightPanel;
	private TrainingStatusDetailsPanel trainingStatusDetailsPanel;
	private RewardFunctionEditor rewardFunctionEditor;
	private PolicyChartPanel policyChartPanel;
	private ExperimentsNavbar experimentsNavbar;
	private NotesField notesField;

	private ExperimentViewPolicyUpdateSubscriber policyUpdateSubscriber;
    private ExperimentViewRunUpdateSubscriber runUpdateSubscriber;

	@Autowired
	private ExperimentDAO experimentDAO;
	@Autowired
	private PolicyDAO policyDAO;
	@Autowired
	private RunDAO runDAO;
	@Autowired
	private TrainingErrorDAO trainingErrorDAO;
	@Autowired
	private PolicyFileService policyFileService;
	@Autowired
	private TrainingService trainingService;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private SegmentIntegrator segmentIntegrator;

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
		return null;
	}

	@Override
	protected Component getMainContent() {
	  SplitLayout mainSplitLayout = WrapperUtils.wrapCenterAlignmentFullSplitLayoutHorizontal(
		getLeftPanel(),
		getRightPanel(),
		DEFAULT_SPLIT_PANE_RATIO);
	  // TODO -> Charts do not re-flow automatically: https://vaadin.com/forum/thread/17878341/resizable-charts (https://github.com/vaadin/vaadin-charts/issues/457)
	  mainSplitLayout.addSplitterDragendListener(evt -> getUI().ifPresent(ui -> ui.getPage().executeJs("Array.from(window.document.getElementsByTagName('vaadin-chart')).forEach( el => el.__reflow());")));

	  pageBreadcrumbs = createBreadcrumbs();

	  VerticalLayout mainLayout = WrapperUtils.wrapSizeFullVertical(
			pageBreadcrumbs,
		  	mainSplitLayout
	  );

	  return mainLayout;
	}

	private Component getLeftPanel() {
		experimentsNavbar = new ExperimentsNavbar(experimentDAO, experiments, experiment, modelId, selectedExperiment -> selectExperiment(selectedExperiment));
		policyChartPanel = new PolicyChartPanel();

		return WrapperUtils.wrapWidthFullHorizontal(
			experimentsNavbar,
			policyChartPanel
		);
	}

	private VerticalLayout getRightPanel() {
		rewardFunctionEditor = new RewardFunctionEditor();
		rewardFunctionEditor.setReadonly(true);
		rewardFunctionEditor.setSizeFull();

		policyHighlightPanel = new PolicyHighlightPanel();
		trainingStatusDetailsPanel = new TrainingStatusDetailsPanel();

		restartTraining = new Button("Restart Training", new Image("frontend/images/start.svg", "run"), click -> {
			synchronized (experimentLock) {
				trainingService.startDiscoveryRun(experiment);
				segmentIntegrator.discoveryRunStarted();
				initLoadData();
				trainingStatusDetailsPanel.updateTrainingDetailsPanel(experiment);
				clearErrorState();
				new RunConfirmDialog().open();
			}
		});
		restartTraining.setVisible(false);
		restartTraining.addClassNames("large-image-btn", "run");

		exportPolicyButton = new Button("Export Policy", click -> UI.getCurrent().navigate(ExportPolicyView.class, policy.getId()));
		exportPolicyButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		exportPolicyButton.addClassName("half-width");
		exportPolicyButton.setEnabled(false);

		notesField = createViewNotesField();

		return WrapperUtils.wrapSizeFullVertical(
				WrapperUtils.wrapWidthFullCenterHorizontal(restartTraining),
				policyHighlightPanel,
				WrapperUtils.wrapWidthFullCenterHorizontal(exportPolicyButton),
				trainingStatusDetailsPanel,
				rewardFunctionEditor,
				notesField);
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
			UI.getCurrent().getPage().getHistory().pushState(null, "experiment/" + selectedExperiment.getId());
		}
	}

	@Override
	protected void initLoadData() throws InvalidDataException {
		experiment = experimentDAO.getExperiment(experimentId)
				.orElseThrow(() -> new InvalidDataException("Attempted to access Experiment: " + experimentId));
		loadExperimentData();
		// The logic below is a bit odd in that this is almost a model view but as a result it needs to be done after the experiment is loaded.
	}

	private void loadExperimentData() {
		modelId = experiment.getModelId();
		experiment.setPolicies(policyDAO.getPoliciesForExperiment(experimentId));
		policy = selectBestPolicy(experiment.getPolicies());
		experiments = experimentDAO.getExperimentsForModel(modelId);
		// Quick and temporary solution to fix some the runs not being loaded for the individual experiment.
		experiment.setRuns(experiments.stream().filter(exp -> exp.getId() == experimentId).findFirst().get().getRuns());
	}

	@Override
	protected void initScreen(BeforeEnterEvent event) {
		updateScreenComponents();
	}

	private void updateScreenComponents() {
		rewardFunctionEditor.setValue(experiment.getRewardFunction());
		policyChartPanel.setExperiment(experiment);
		trainingStatusDetailsPanel.updateTrainingDetailsPanel(experiment);
		processSelectedPolicy(policy);
		updateRightPanelForExperiment();
	}

	private Policy selectBestPolicy(List<Policy> policies) {
		return policies.stream()
				.filter(p -> PolicyUtils.getLastScore(p) != null && !Double.isNaN(PolicyUtils.getLastScore(p)))
				.max(Comparator.comparing(PolicyUtils::getLastScore))
				.orElse(null);
	}

	private void processSelectedPolicy(Policy selectedPolicy) {
		policyHighlightPanel.update(selectedPolicy);
		if (selectedPolicy != null) {
	  		policyChartPanel.highlightPolicy(selectedPolicy);
			updateButtonEnablement();
			updateRightPanelForExperiment();
		}
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

	private void addOrUpdatePolicy(Policy updatedPolicy) {
		ArrayList<Policy> policiesToAdd = new ArrayList<>();
		experiment.getPolicies().stream()
        .filter(policy -> policy.getId() == updatedPolicy.getId())
        .findAny()
        .ifPresentOrElse(
                policy -> experiment.getPolicies().set(experiment.getPolicies().indexOf(policy), updatedPolicy),
                () -> policiesToAdd.add(updatedPolicy));
		// This is needed to avoid ConcurrentModificationException when adding values in a stream and then filtering on them.
		experiment.getPolicies().addAll(policiesToAdd);
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
		if (ExperimentUtils.getTrainingStatus(experiment) == RunStatus.Error) {
			experiment.getRuns().stream()
					.filter(r -> r.getStatusEnum() == RunStatus.Error)
					.findAny()
					.map(Run::getTrainingErrorId)
					.flatMap(trainingErrorDAO::getErrorById)
					.ifPresent(this::updateUIForError);
		}
	}

	private void updateButtonEnablement() {
		// to avoid multiple download policy file from rescale server,
		// we put the "saving" for temporary
		// policy dao will check if there's real policy file exist or not
		if (ExperimentUtils.getTrainingStatus(experiment) == RunStatus.Completed) {
			exportPolicyButton.setEnabled(policy.hasFile());
		}
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
				if (event.getPolicy().getExperiment().getId() != experimentId)
					return;
				// Update or insert the policy in experiment.getPolicies
				addOrUpdatePolicy(event.getPolicy());
				// Calculate the best policy again
				Policy bestPolicy = selectBestPolicy(experiment.getPolicies());

				// Refresh other components, existing best policy is updated or we have a new best policy
				if (event.getPolicy().equals(policy) || (bestPolicy != null && !bestPolicy.equals(policy))) {
					policy = bestPolicy;
					PushUtils.push(getUI(), () -> processSelectedPolicy(bestPolicy));
				}
				PushUtils.push(getUI(), () -> updateRightPanelForExperiment());
			}
		}

		@Override
		public boolean filterBusEvent(PolicyUpdateBusEvent event) {
			return experiment != null && experiment.getId() == event.getPolicy().getExperiment().getId();
		}

        @Override
        public Optional<UI> getUI() {
            return ExperimentView.this.getUI();
        }
    }

	class ExperimentViewRunUpdateSubscriber implements RunUpdateSubscriber {
		@Override
		public void handleBusEvent(RunUpdateBusEvent event) {
			addOrUpdateRun(event.getRun());
			updatedRunForPolicies(event.getRun());
			PushUtils.push(getUI(), () -> updateRightPanelForExperiment());
		}

		@Override
		public boolean filterBusEvent(RunUpdateBusEvent event) {
			return experiment != null && experiment.getId() == event.getRun().getExperiment().getId();
		}

		@Override
		public Optional<UI> getUI() {
			return ExperimentView.this.getUI();
		}
	}
}
