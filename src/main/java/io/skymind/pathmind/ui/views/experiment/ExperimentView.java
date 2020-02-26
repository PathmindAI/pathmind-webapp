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
import io.skymind.pathmind.constants.RunType;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.Run;
import io.skymind.pathmind.data.TrainingError;
import io.skymind.pathmind.data.utils.ExperimentUtils;
import io.skymind.pathmind.data.utils.PolicyUtils;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.db.dao.PolicyDAO;
import io.skymind.pathmind.db.dao.TrainingErrorDAO;
import io.skymind.pathmind.db.dao.UserDAO;
import io.skymind.pathmind.exception.InvalidDataException;
import io.skymind.pathmind.security.Routes;
import io.skymind.pathmind.services.TrainingService;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
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
	private Button exportPolicyButton;

	private static final double DEFAULT_SPLIT_PANE_RATIO = 70;

	private long experimentId = -1;
	private long modelId = -1;
	private Policy policy;
	private Experiment experiment;
	private List<Experiment> experiments;

	private ScreenTitlePanel screenTitlePanel;

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
	private TrainingErrorDAO trainingErrorDAO;
	@Autowired
	private TrainingService trainingService;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private SegmentIntegrator segmentIntegrator;

	private String projectName;
	private Button runDiscoveryTraining;
	private Button runFullTraining;
	private Breadcrumbs pageBreadcrumbs;

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
		screenTitlePanel = new ScreenTitlePanel("PROJECT");
		return screenTitlePanel;
	}

	@Override
	protected Component getMainContent() {
	  projectName = ExperimentUtils.getProjectName(experiment);

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

		runDiscoveryTraining = new Button("Start Discovery Run", new Image("frontend/images/start.svg", "run"), click -> {
			final var experiment = experimentDAO.getExperiment(policy.getRun().getExperimentId());
			if(experiment.isPresent()) {
				trainingService.startDiscoveryRun(experiment.get());
				segmentIntegrator.discoveryRunStarted();
				clearErrorState();
				new RunConfirmDialog().open();
			}
		});
		runDiscoveryTraining.setVisible(false);
		runDiscoveryTraining.addClassNames("large-image-btn", "run");
		
		runFullTraining = new Button("Start Full Run", new Image("frontend/images/start.svg", "run"), click -> {
			final var experiment = experimentDAO.getExperiment(policy.getRun().getExperimentId());
			if(experiment.isPresent()) {
				trainingService.startFullRun(experiment.get(), policy);
				segmentIntegrator.fullRunStarted();
				clearErrorState();
				new RunConfirmDialog().open();
			}
		});
		runFullTraining.setVisible(false);
		runFullTraining.addClassNames("large-image-btn", "run");

		exportPolicyButton = new Button("Export Policy", click -> UI.getCurrent().navigate(ExportPolicyView.class, policy.getId()));
		exportPolicyButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		exportPolicyButton.addClassName("half-width");
		exportPolicyButton.setEnabled(false);

		notesField = createViewNotesField();

		return WrapperUtils.wrapSizeFullVertical(
				WrapperUtils.wrapWidthFullCenterHorizontal(runDiscoveryTraining, runFullTraining),
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
		experiment = experimentDAO.getExperiment(selectedExperiment.getId())
				.orElseThrow(() -> new InvalidDataException("Attempted to access Experiment: " + selectedExperiment.getId()));
		experimentId = selectedExperiment.getId();
		loadExperimentData();
		updateScreenComponents();
		notesField.setNotesText(experiment.getUserNotes());
		pageBreadcrumbs.setText(3, "Experiment #"+experiment.getName());
		UI.getCurrent().getPage().getHistory().pushState(null, "experiment/" + selectedExperiment.getId());
	}

	@Override
	protected void initLoadData() throws InvalidDataException {
		experiment = experimentDAO.getExperiment(experimentId)
				.orElseThrow(() -> new InvalidDataException("Attempted to access Experiment: " + experimentId));
		loadExperimentData();
		// The logic below is a bit odd in that this is almost a model view but as a result it needs to be done after the experiment is loaded.
		modelId = experiment.getModelId();
		experiments = experimentDAO.getExperimentsForModel(modelId);
	}

	private void loadExperimentData() {
		experiment.setPolicies(policyDAO.getPoliciesForExperiment(experimentId));
		policy = selectBestPolicy(experiment.getPolicies());
	}

	@Override
	protected void initScreen(BeforeEnterEvent event) throws InvalidDataException {
		screenTitlePanel.setSubtitle(projectName);
		updateScreenComponents();
	}

	private void updateScreenComponents() {
		rewardFunctionEditor.setValue(experiment.getRewardFunction());
		policyChartPanel.setExperiment(experiment);
		trainingStatusDetailsPanel.updateTrainingDetailsPanel(experiment);
		processSelectedPolicy(policy);
	}

	private Policy selectBestPolicy(List<Policy> policies) {
		RunType runType = ExperimentUtils.getTrainingType(experiment);
		return selectHighestPerformingPolicy(policies, runType);
	}

	private Policy selectHighestPerformingPolicy(List<Policy> policies, RunType runType) {
		Optional<Policy> highestPerformingPolicy = policies.stream()
				.filter(p -> p.getRun().getRunTypeEnum() == runType)
				.filter(p -> PolicyUtils.getLastScore(p) != null)
				.max(Comparator.comparing(p -> PolicyUtils.getLastScore(p)));
		return highestPerformingPolicy.orElse(policies.get(policies.size()-1));
	}

	private void processSelectedPolicy(Policy selectedPolicy) {
		policyHighlightPanel.update(selectedPolicy);
		policyChartPanel.highlightPolicy(selectedPolicy);
		updateButtonEnablement();
		if (ExperimentUtils.getTrainingStatus(experiment) == RunStatus.Error) {
			trainingErrorDAO.getErrorById(selectedPolicy.getRun().getTrainingErrorId())
				.ifPresent(error -> updateUIForError(error, selectedPolicy.getRun().getRunTypeEnum()));
		}
	}
	
	private void updateUIForError(TrainingError error, RunType runType) {
		policyHighlightPanel.setErrorDescription(error.getDescription());
		if (error.isRestartable()) {
			if (runType == RunType.DiscoveryRun) {
				runFullTraining.setVisible(false);
				runDiscoveryTraining.setVisible(true);
				runDiscoveryTraining.setEnabled(true);
			} else if (runType == RunType.FullRun) {
				runDiscoveryTraining.setVisible(false);
				runFullTraining.setVisible(true);
				runFullTraining.setEnabled(true);
			}
		}
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

	private void processRunUpdate(Run run) {
		updatedRunForPolicies(run);
		updateButtonEnablement();
		PushUtils.push(getUI(), () -> trainingStatusDetailsPanel.updateTrainingDetailsPanel(experiment));
	}

	private void updateButtonEnablement() {
		// to avoid multiple download policy file from rescale server,
		// we put the "saving" for temporary
		// policy dao will check if there's real policy file exist or not
		boolean isTrainingFinished = ExperimentUtils.getTrainingStatus(experiment) == RunStatus.Completed;
		if (isTrainingFinished) {
			exportPolicyButton.setEnabled(policyDAO.hasPolicyFile(policy.getId()));
		}

		RunType selectedRunType = policy.getRun().getRunTypeEnum();
		if (selectedRunType == RunType.DiscoveryRun) {
			runDiscoveryTraining.setVisible(false);
			runFullTraining.setVisible(true);
			runFullTraining.setEnabled(isTrainingFinished);
		} else if (selectedRunType == RunType.FullRun) {
			runDiscoveryTraining.setVisible(false);
			runFullTraining.setVisible(false);
		}
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
			// Update or insert the policy in experiment.getPolicies
			addOrUpdatePolicy(event.getPolicy());
			// Calculate the best policy again
			Policy bestPolicy = selectBestPolicy(experiment.getPolicies());

			// Refresh other components, existing best policy is updated or we have a new best policy
			if (policy.equals(event.getPolicy()) || !policy.equals(bestPolicy)) {
				policy = bestPolicy;
				PushUtils.push(getUI(), () -> processSelectedPolicy(bestPolicy));
			}
			PushUtils.push(getUI(), () -> trainingStatusDetailsPanel.updateTrainingDetailsPanel(experiment));
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

    class ExperimentViewRunUpdateSubscriber implements RunUpdateSubscriber
    {
        @Override
        public boolean filterBusEvent(RunUpdateBusEvent event) {
            return experiment != null && experiment.getId() == event.getRun().getExperiment().getId();
        }

        @Override
        public void handleBusEvent(RunUpdateBusEvent event) {
            PushUtils.push(getUI(), () -> processRunUpdate(event.getRun()));
        }

        @Override
        public Optional<UI> getUI() {
            return ExperimentView.this.getUI();
        }
    }


}
