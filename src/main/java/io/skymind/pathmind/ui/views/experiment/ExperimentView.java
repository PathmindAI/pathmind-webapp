package io.skymind.pathmind.ui.views.experiment;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.WildcardParameter;

import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.constants.RunType;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.utils.ExperimentUtils;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.utils.PolicyUtils;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.db.dao.PolicyDAO;
import io.skymind.pathmind.db.dao.UserDAO;
import io.skymind.pathmind.exception.InvalidDataException;
import io.skymind.pathmind.security.Routes;
import io.skymind.pathmind.services.TrainingService;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.components.navigation.Breadcrumbs;
import io.skymind.pathmind.ui.components.buttons.NewExperimentButton;
import io.skymind.pathmind.ui.components.dialog.RunConfirmDialog;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.ui.views.PathMindDefaultView;
import io.skymind.pathmind.ui.views.experiment.components.PolicyChartPanel;
import io.skymind.pathmind.ui.views.experiment.components.PolicyHighlightPanel;
import io.skymind.pathmind.ui.views.experiment.components.PolicyStatusDetailsPanel;
import io.skymind.pathmind.ui.views.experiment.components.RewardFunctionEditor;
import io.skymind.pathmind.ui.views.experiment.components.TrainingsListPanel;
import io.skymind.pathmind.ui.views.policy.ExportPolicyView;

@CssImport("./styles/styles.css")
@Route(value = Routes.EXPERIMENT_URL, layout = MainLayout.class)
public class ExperimentView extends PathMindDefaultView implements HasUrlParameter<String> {
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
	private TrainingsListPanel trainingsListPanel;

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
		trainingsListPanel = new TrainingsListPanel();

		trainingsListPanel.addSelectionListener(selectedPolicy -> {
			policy = selectedPolicy;
			policyHighlightPanel.update(selectedPolicy);
			policyStatusDetailsPanel.update(selectedPolicy);
			policyChartPanel.init(selectedPolicy);
			policyChartPanel.highlightPolicy(selectedPolicy);


			// to avoid multiple download policy file from rescale server,
			// we put the "saving" for temporary
			// policy dao will check if there's real policy file exist or not
			exportPolicyButton.setVisible(policyDAO.hasPolicyFile(selectedPolicy.getId()));

			RunType selectedRunType = selectedPolicy.getRun().getRunTypeEnum();
			boolean canStartFurtherRuns = PolicyUtils.getRunStatus(selectedPolicy) != RunStatus.Error;
			if (selectedRunType == RunType.DiscoveryRun) {
				runFullTraining.setVisible(true);
				runFullTraining.setEnabled(canStartFurtherRuns);
			} else if (selectedRunType == RunType.FullRun) {
				runFullTraining.setVisible(false);
			}
		});

		policyChartPanel = new PolicyChartPanel();
		policyChartPanel.addSeriesClickListener(policyId -> trainingsListPanel.selectPolicyWithId(policyId));

		SplitLayout leftSplitPanel = WrapperUtils.wrapCenterAlignmentFullSplitLayoutVertical(
				policyChartPanel,
				trainingsListPanel);
		// TODO -> Charts do not reflow automatically: https://vaadin.com/forum/thread/17878341/resizable-charts (https://github.com/vaadin/vaadin-charts/issues/457)
		leftSplitPanel.addSplitterDragendListener(evt -> getUI().ifPresent(ui -> ui.getPage().executeJs("Array.from(window.document.getElementsByTagName('vaadin-chart')).forEach( el => el.__reflow());")));
		return leftSplitPanel;
	}

	private VerticalLayout getRightPanel() {
		rewardFunctionEditor = new RewardFunctionEditor();
		rewardFunctionEditor.setReadonly(true);
		rewardFunctionEditor.setSizeFull();

		policyHighlightPanel = new PolicyHighlightPanel();
		policyStatusDetailsPanel = new PolicyStatusDetailsPanel();

		runFullTraining = new Button("Start Full Run", new Image("frontend/images/start.svg", "run"), click -> {
			final var experiment = experimentDAO.getExperiment(policy.getRun().getExperimentId());
			if(experiment.isPresent()) {
				trainingService.startFullRun(experiment.get(), policy);
				segmentIntegrator.fullRunStarted();
				new RunConfirmDialog().open();
			}
		});
		runFullTraining.setVisible(false);
		runFullTraining.addClassNames("large-image-btn", "run");

		final HorizontalLayout buttons = WrapperUtils.wrapWidthFullCenterHorizontal(
				new NewExperimentButton(experimentDAO, experiment.getModelId(), "TODO")
		);
		exportPolicyButton = new Button("Export Policy", click -> UI.getCurrent().navigate(ExportPolicyView.class, policy.getId()));
		buttons.add(exportPolicyButton);
		exportPolicyButton.setVisible(false);

		return WrapperUtils.wrapSizeFullVertical(
				WrapperUtils.wrapWidthFullCenterHorizontal(runFullTraining),
				policyHighlightPanel,
				policyStatusDetailsPanel,
				rewardFunctionEditor,
				buttons);
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
		experiment = experimentDAO.getExperiment(experimentId)
				.orElseThrow(() -> new InvalidDataException("Attempted to access Experiment: " + experimentId));
		experiment.setPolicies(policyDAO.getPoliciesForExperiment(experimentId));
	}

	@Override
	protected void initScreen(BeforeEnterEvent event) throws InvalidDataException {
		screenTitlePanel.setSubtitle(projectName);
		rewardFunctionEditor.setValue(experiment.getRewardFunction());
		policyChartPanel.init(experiment);
		trainingsListPanel.init(experiment, policyId);
	}
}
