package io.skymind.pathmind.ui.views.experiment;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import io.skymind.pathmind.bus.PathmindBusEvent;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.utils.FakeDataUtils;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.exception.InvalidDataException;
import io.skymind.pathmind.services.run.RunService;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.components.buttons.NewExperimentButton;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.utils.NotificationUtils;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.ui.views.PathMindDefaultView;
import io.skymind.pathmind.ui.views.experiment.components.*;
import io.skymind.pathmind.ui.views.policy.ExportPolicyView;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;

@StyleSheet("frontend://styles/styles.css")
@Route(value = "experiment", layout = MainLayout.class)
public class ExperimentView extends PathMindDefaultView implements HasUrlParameter<String>
{
	private enum ActionButtonState {
		Start, Next, Stop
	}

	private static final int EXPERIMENT_ID_SEGMENT = 0;
	private static final int POLICY_ID_SEGMENT = 1;

	private static final double DEFAULT_SPLIT_PANE_RATIO = 70;

	private Logger log = LogManager.getLogger(ExperimentView.class);

	private final UnicastProcessor<PathmindBusEvent> publisher;
	private final Flux<PathmindBusEvent> consumer;

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

	private Button actionButton;

	public ExperimentView(UnicastProcessor<PathmindBusEvent> publisher, Flux<PathmindBusEvent> consumer)
	{
		super();
		this.publisher = publisher;
		this.consumer = consumer;
	}

	@Override
	protected Component getTitlePanel() {
		screenTitlePanel = new ScreenTitlePanel("PROJECT");
		return screenTitlePanel;
	}

	@Override
	protected Component getMainContent()
	{
		return WrapperUtils.wrapCenterAlignmentFullSplitLayoutHorizontal(
				getLeftPanel(),
				getRightPanel(),
				DEFAULT_SPLIT_PANE_RATIO);
	}

	private Component getLeftPanel()
	{
		trainingsListPanel = new TrainingsListPanel();
		trainingsListPanel.addSelectionListener(selectedPolicy -> {
			policy = selectedPolicy;
			policyHighlightPanel.update(selectedPolicy);
			policyStatusDetailsPanel.update(selectedPolicy);
			policyChartPanel.highlightPolicy(selectedPolicy);
			setActionButtonValue(selectedPolicy);
		});

		// Only show policies that have been filtered/searched in the charts
		trainingsListPanel.addSearchListener(policies -> policyChartPanel.filter(policies));

		policyChartPanel = new PolicyChartPanel(consumer);

		return WrapperUtils.wrapCenterAlignmentFullSplitLayoutVertical(
				policyChartPanel,
				trainingsListPanel);
	}

	private VerticalLayout getRightPanel()
	{
		rewardFunctionEditor = new RewardFunctionEditor();
		rewardFunctionEditor.setReadonly(true);
		rewardFunctionEditor.setSizeFull();

		policyHighlightPanel = new PolicyHighlightPanel();
		policyStatusDetailsPanel = new PolicyStatusDetailsPanel();

		actionButton = new Button(ActionButtonState.Start.name(), click -> handleActionButtonClicked());

		return WrapperUtils.wrapSizeFullVertical(
				WrapperUtils.wrapWidthFullCenterHorizontal(actionButton),
				policyHighlightPanel,
				policyStatusDetailsPanel,
				rewardFunctionEditor,
				WrapperUtils.wrapWidthFullCenterHorizontal(
						new NewExperimentButton(experimentDAO, experiment.getModelId(), ""),
						new Button("Export Policy", click -> UI.getCurrent().navigate(ExportPolicyView.class, policy.getId()))
				));
	}
	// TODO -> I don't fully understand the button logic, including when it's muted from just the screenshots.
	private void setActionButtonValue(Policy policy) {
		switch(policy.getRun().getRunTypeEnum()) {
			case TestRun:
				actionButton.setText("Next");
				break;
			case DiscoverRun:
				actionButton.setText("Stop");
				break;
			case FullRun:
				actionButton.setText("Todo");
				break;
		}
	}

	private void handleActionButtonClicked() {
		NotificationUtils.showTodoNotification("Needs to be implemented");
		RunService.run(policy, publisher);
		// TODO -> Paul -> What else needs to be updated on this action?
	}

	/**
	 * For now I'm doing a manual parse of the parameter since Vaadin only seems
	 * to have the ability to parse to a wildcard parameter if you need more than one parameter
	 * as explained in this Vaadin issue: https://github.com/vaadin/flow/issues/4213 There is an
	 * add-on but I don't think it's worth adding on yet since this is the only place we have this
	 * need and Vaadin will most likely add this capability in the future.
	 */
	@Override
	public void setParameter(BeforeEvent event, @WildcardParameter String parameter)
	{
		if(StringUtils.isEmpty(parameter)) {
			this.experimentId = -1;
			return;
		}

		String[] segments = parameter.split("/");

		if(NumberUtils.isDigits(segments[EXPERIMENT_ID_SEGMENT]))
			experimentId = Long.parseLong(segments[EXPERIMENT_ID_SEGMENT]);
		if(segments.length > 1 && NumberUtils.isDigits(segments[POLICY_ID_SEGMENT]))
			policyId = Long.parseLong(segments[POLICY_ID_SEGMENT]);
	}

	@Override
	protected void loadData() throws InvalidDataException {
		experiment = experimentDAO.getExperiment(experimentId);
		if(experiment == null)
			throw new InvalidDataException("Attempted to access Experiment: " + experimentId);
		experiment.setPolicies(FakeDataUtils.generateFakePoliciesForExperiment(experiment));
	}

	@Override
	protected void updateScreen(BeforeEnterEvent event) throws InvalidDataException
	{
		screenTitlePanel.setSubtitle(experiment.getProject().getName());
		rewardFunctionEditor.setValue(experiment.getRewardFunction());
		policyChartPanel.update(experiment);

		// TODO -> How do we get the list of policies?
		trainingsListPanel.update(experiment, policyId);
	}
}
