package io.skymind.pathmind.ui.views.experiment;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.bus.PathmindBusEvent;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.data.utils.FakeDataUtils;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.db.dao.PolicyDAO;
import io.skymind.pathmind.exception.InvalidDataException;
import io.skymind.pathmind.ui.components.ActionMenu;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.utils.NotificationUtils;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.ui.views.PathMindDefaultView;
import io.skymind.pathmind.ui.views.experiment.components.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;

import java.util.List;

@StyleSheet("frontend://styles/styles.css")
@Route(value = "experiment", layout = MainLayout.class)
public class ExperimentView extends PathMindDefaultView implements HasUrlParameter<Long>
{
	private static final double DEFAULT_SPLIT_PANE_RATIO = 70;

	private Logger log = LogManager.getLogger(ExperimentView.class);

	private final Flux<PathmindBusEvent> consumer;

	private long experimentId = -1;

	private ScreenTitlePanel screenTitlePanel;

	private PolicyHighlightPanel policyHighlightPanel;
	private PolicyStatusDetailsPanel policyStatusDetailsPanel;
	private RewardFunctionEditor rewardFunctionEditor;
	private PolicyChartPanel policyChartPanel;
	private TrainingsListPanel trainingsListPanel;

	@Autowired
	private ExperimentDAO experimentDAO;

	private Button backToExperimentsButton;

	public ExperimentView(Flux<PathmindBusEvent> consumer)
	{
		super();
		this.consumer = consumer;
	}

	@Override
	protected ActionMenu getActionMenu()
	{
		backToExperimentsButton = new Button("Back to Experiments", new Icon(VaadinIcon.CHEVRON_LEFT));

		return new ActionMenu(
				backToExperimentsButton
		);
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
			policyHighlightPanel.update(selectedPolicy);
			policyStatusDetailsPanel.update(selectedPolicy);
			policyChartPanel.update(selectedPolicy);
		});

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

		return WrapperUtils.wrapSizeFullVertical(
				WrapperUtils.wrapWidthFullCenterHorizontal(
						new Button("Start")),
				policyHighlightPanel,
				policyStatusDetailsPanel,
				rewardFunctionEditor,
				WrapperUtils.wrapWidthFullCenterHorizontal(
						new Button("New Experiment", click -> NotificationUtils.showTodoNotification()),
						new Button("Export Policy", click -> NotificationUtils.showTodoNotification())
				));
	}

	@Override
	public void setParameter(BeforeEvent event, Long experimentId) {
		this.experimentId = experimentId;
	}

	@Override
	protected void updateScreen(BeforeEnterEvent event) throws InvalidDataException
	{
		Experiment experiment = experimentDAO.getExperiment(experimentId);

		if(experiment == null)
			throw new InvalidDataException("Attempted to access Experiment: " + experimentId);

		screenTitlePanel.setSubtitle(experiment.getProject().getName());

		rewardFunctionEditor.setValue(experiment.getRewardFunction());

		// TODO -> How do we get the list of policies?
		trainingsListPanel.update(FakeDataUtils.generateFakePoliciesForExperiment(experimentId));

		backToExperimentsButton.addClickListener(click ->
				UI.getCurrent().navigate(ExperimentsView.class, experiment.getModelId()));
	}

	private void save() {
		// TODO -> Save should be done in a systematic way throughout the application.
//				try {
//			binder.writeBean(project);
//			return true;
//		} catch (ValidationException e) {
//			return false;
//		}
	}
}
