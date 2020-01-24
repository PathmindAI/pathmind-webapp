package io.skymind.pathmind.ui.views.experiment;

import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

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
import com.vaadin.flow.router.WildcardParameter;

import io.skymind.pathmind.bus.EventBus;
import io.skymind.pathmind.bus.events.PolicyUpdateBusEvent;
import io.skymind.pathmind.bus.subscribers.PolicyUpdateSubscriber;
import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.constants.RunType;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.utils.ExperimentUtils;
import io.skymind.pathmind.data.utils.PolicyUtils;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.db.dao.PolicyDAO;
import io.skymind.pathmind.db.dao.UserDAO;
import io.skymind.pathmind.exception.InvalidDataException;
import io.skymind.pathmind.security.Routes;
import io.skymind.pathmind.services.TrainingService;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.components.dialog.RunConfirmDialog;
import io.skymind.pathmind.ui.components.navigation.Breadcrumbs;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.ui.utils.PushUtils;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.ui.views.PathMindDefaultView;
import io.skymind.pathmind.ui.views.experiment.components.PolicyChartPanel;
import io.skymind.pathmind.ui.views.experiment.components.PolicyHighlightPanel;
import io.skymind.pathmind.ui.views.experiment.components.PolicyStatusDetailsPanel;
import io.skymind.pathmind.ui.views.experiment.components.RewardFunctionEditor;
import io.skymind.pathmind.ui.views.experiment.utils.ExperimentViewNavigationUtils;
import io.skymind.pathmind.ui.views.policy.ExportPolicyView;

@CssImport("./styles/styles.css")
@Route(value = Routes.EXPERIMENT_URL, layout = MainLayout.class)
public class ExperimentView extends PathMindDefaultView implements HasUrlParameter<String>, PolicyUpdateSubscriber {
	private Button exportPolicyButton;

	private static final int EXPERIMENT_ID_SEGMENT = 0;
	private static final int POLICY_ID_SEGMENT = 1;

	private static final double DEFAULT_SPLIT_PANE_RATIO = 70;

	private long experimentId = -1;
	private long policyId = -1;
	private Policy policy;
	private Experiment experiment;

	private ScreenTitlePanel screenTitlePanel;

	private PolicyHighlightPanel policyHighlightPanel;
	private PolicyStatusDetailsPanel policyStatusDetailsPanel;
	private RewardFunctionEditor rewardFunctionEditor;
	private PolicyChartPanel policyChartPanel;

	@Autowired
	private ExperimentDAO experimentDAO;
	@Autowired
	private PolicyDAO policyDAO;
	@Autowired
	private TrainingService trainingService;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private SegmentIntegrator segmentIntegrator;

	private String projectName;
	private Button runFullTraining;

	public ExperimentView() {
		super();
		addClassName("experiment-view");
	}
	
	@Override
	protected void onDetach(DetachEvent event) {
		EventBus.unsubscribe(this);
	}

	@Override
	protected void onAttach(AttachEvent event) {
		EventBus.subscribe(this);
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
	
	  VerticalLayout mainLayout = WrapperUtils.wrapSizeFullVertical(
		  createBreadcrumbs(),
		  mainSplitLayout
	  );
	  
	  return mainLayout;
	}

	private Component getLeftPanel() {
		policyChartPanel = new PolicyChartPanel();
		policyChartPanel.addSeriesClickListener(policyId -> {
			policy = experiment.getPolicies().stream().filter(p -> Long.toString(p.getId()).equals(policyId)).findFirst().get();
			processSelectedPolicy(policy);
		});

		return policyChartPanel;
	}

	private VerticalLayout getRightPanel() {
		rewardFunctionEditor = new RewardFunctionEditor();
		rewardFunctionEditor.setReadonly(true);
		rewardFunctionEditor.setSizeFull();

		policyHighlightPanel = new PolicyHighlightPanel();
		policyStatusDetailsPanel = new PolicyStatusDetailsPanel(policyDAO);

		runFullTraining = new Button("Start Full Run", new Image("frontend/images/start.svg", "run"), click -> {
			final Experiment experiment = experimentDAO.getExperiment(policy.getRun().getExperimentId());
			trainingService.startFullRun(experiment, policy);
			segmentIntegrator.fullRunStarted();
			new RunConfirmDialog().open();
		});
		runFullTraining.setVisible(false);
		runFullTraining.addClassNames("large-image-btn", "run");

		Button editRewardFunctionButton = new Button("Edit Reward Function");
		editRewardFunctionButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		editRewardFunctionButton.addClassName("half-width");
		editRewardFunctionButton.addClickListener(evt -> 
			ExperimentViewNavigationUtils.createAndNavigateToNewExperiment(UI.getCurrent(), experimentDAO, experiment.getModelId(), experiment.getRewardFunction()));
		
		exportPolicyButton = new Button("Export Policy", click -> UI.getCurrent().navigate(ExportPolicyView.class, policy.getId()));
		exportPolicyButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		exportPolicyButton.addClassName("half-width");
		exportPolicyButton.setEnabled(false);

		return WrapperUtils.wrapSizeFullVertical(
				WrapperUtils.wrapWidthFullCenterHorizontal(runFullTraining),
				policyHighlightPanel,
				WrapperUtils.wrapWidthFullCenterHorizontal(exportPolicyButton, editRewardFunctionButton),
				policyStatusDetailsPanel,
				rewardFunctionEditor);
	}

	private Breadcrumbs createBreadcrumbs() {        
		return new Breadcrumbs(experiment.getProject(), experiment.getModel(), experiment);
	}

	/**
	 * For now I'm doing a manual parse of the parameter since Vaadin only seems
	 * to have the ability to parse to a wildcard parameter if you need more than one parameter
	 * as explained in this Vaadin issue: https://github.com/vaadin/flow/issues/4213 There is an
	 * add-on but I don't think it's worth adding on yet since this is the only place we have this
	 * need and Vaadin will most likely add this capability in the future.
	 */
	@Override
	public void setParameter(BeforeEvent event, @WildcardParameter String parameter) {
		if (StringUtils.isEmpty(parameter)) {
			this.experimentId = -1;
			return;
		}

		String[] segments = parameter.split("/");

		if (NumberUtils.isDigits(segments[EXPERIMENT_ID_SEGMENT]))
			experimentId = Long.parseLong(segments[EXPERIMENT_ID_SEGMENT]);
		if (segments.length > 1 && NumberUtils.isDigits(segments[POLICY_ID_SEGMENT]))
			policyId = Long.parseLong(segments[POLICY_ID_SEGMENT]);
	}

	@Override
	protected boolean isAccessAllowedForUser() {
		return userDAO.isUserAllowedAccessToExperiment(experimentId);
	}

	@Override
	protected void initLoadData() throws InvalidDataException {
		experiment = experimentDAO.getExperiment(experimentId);
		if (experiment == null)
			throw new InvalidDataException("Attempted to access Experiment: " + experimentId);
		experiment.setPolicies(policyDAO.getPoliciesForExperiment(experimentId));
		policy = selectBestPolicy(experiment.getPolicies());
	}

	@Override
	protected void initScreen(BeforeEnterEvent event) throws InvalidDataException {
		screenTitlePanel.setSubtitle(projectName);
		rewardFunctionEditor.setValue(experiment.getRewardFunction());
		policyChartPanel.init(experiment);
		processSelectedPolicy(policy);
	}
	
	private Policy selectBestPolicy(List<Policy> policies) {
		boolean hasFullRun = policies.stream().anyMatch(p -> p.getRun().getRunTypeEnum() == RunType.FullRun);
		if (hasFullRun) {
			return selectHighestPerformingPolicy(policies, RunType.FullRun);
		} else {
			return selectHighestPerformingPolicy(policies, RunType.DiscoveryRun);
		}
		
	}

	private Policy selectHighestPerformingPolicy(List<Policy> policies, RunType runType) {
		return policies.stream()
				.filter(p -> p.getRun().getRunTypeEnum() == runType)
				.max(Comparator.comparing(p -> PolicyUtils.getLastScore(p), Comparator.nullsLast(Comparator.naturalOrder()))).get();
	}
	
	private void processSelectedPolicy(Policy selectedPolicy) {
		policyHighlightPanel.update(selectedPolicy);
		policyStatusDetailsPanel.update(selectedPolicy);
		policyChartPanel.init(selectedPolicy);
		policyChartPanel.highlightPolicy(selectedPolicy);


		// to avoid multiple download policy file from rescale server,
		// we put the "saving" for temporary
		// policy dao will check if there's real policy file exist or not
		exportPolicyButton.setEnabled(policyDAO.hasPolicyFile(selectedPolicy.getId()));

		RunType selectedRunType = selectedPolicy.getRun().getRunTypeEnum();
		boolean canStartFurtherRuns = PolicyUtils.getRunStatus(selectedPolicy) == RunStatus.Completed;
		if (selectedRunType == RunType.DiscoveryRun) {
			runFullTraining.setVisible(true);
			runFullTraining.setEnabled(canStartFurtherRuns);
		} else if (selectedRunType == RunType.FullRun) {
			runFullTraining.setVisible(false);
		}
		
	}
	private void addOrUpdatePolicy(Policy updatedPolicy) {
		experiment.getPolicies().stream()
        .filter(policy -> policy.getId() == updatedPolicy.getId())
        .findAny()
        .ifPresentOrElse(
                policy -> {
                    experiment.getPolicies().set(experiment.getPolicies().indexOf(policy), updatedPolicy);
                },
                () -> {
                    experiment.getPolicies().add(updatedPolicy);
                });
	}

	@Override
	public void handleBusEvent(PolicyUpdateBusEvent event) {
		// Update or insert the policy in experiment.getPolicies
		addOrUpdatePolicy(event.getPolicy());
		// Calculate the best policy again
		Policy bestPolicy = selectBestPolicy(experiment.getPolicies());
		
		// Refresh other components, existing best policy is updated or we have a new best policy 
		if (policy.equals(event.getPolicy()) || !policy.equals(bestPolicy)) {
			policy = bestPolicy;
			PushUtils.push(this, () -> processSelectedPolicy(bestPolicy));
		}
	}
	

	@Override
	public boolean filterBusEvent(PolicyUpdateBusEvent event) {
		return experiment.getId() == event.getPolicy().getExperiment().getId();
	}
	
	
}
