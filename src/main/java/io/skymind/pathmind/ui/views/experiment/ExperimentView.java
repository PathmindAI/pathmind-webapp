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
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.exception.InvalidDataException;
import io.skymind.pathmind.ui.components.ActionMenu;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.utils.NotificationUtils;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.ui.views.PathMindDefaultView;
import io.skymind.pathmind.ui.views.experiment.components.RewardFunctionEditor;
import io.skymind.pathmind.ui.views.experiment.components.TrainingsListPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

@StyleSheet("frontend://styles/styles.css")
@Route(value = "experiment", layout = MainLayout.class)
public class ExperimentView extends PathMindDefaultView implements HasUrlParameter<Long>
{
	private static final double DEFAULT_SPLIT_PANE_RATIO = 70;

	private Logger log = LogManager.getLogger(ExperimentView.class);

	private long experimentId = -1;

	private ScreenTitlePanel screenTitlePanel;

	private RewardFunctionEditor rewardFunctionEditor;
	private TrainingsListPanel trainingsListPanel;

	@Autowired
	private ExperimentDAO experimentDAO;

	private Button backToExperimentsButton;

	public ExperimentView()
	{
		super();
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

		return WrapperUtils.wrapCenterAlignmentFullSplitLayoutVertical(
				new Label("Chart"),
				trainingsListPanel);
	}

	private VerticalLayout getRightPanel()
	{
		rewardFunctionEditor = new RewardFunctionEditor();
		rewardFunctionEditor.setReadonly(true);
		rewardFunctionEditor.setSizeFull();

		return WrapperUtils.wrapSizeFullVertical(
				new Button("Start"),
				new Label("Status panel"),
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
