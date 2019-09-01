package io.skymind.pathmind.ui.views.experiment;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.constants.PathmindConstants;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.db.ExperimentRepository;
import io.skymind.pathmind.db.ProjectRepository;
import io.skymind.pathmind.ui.components.ActionMenu;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.views.BasicViewInterface;
import io.skymind.pathmind.ui.views.experiment.components.ExperimentFormPanel;
import io.skymind.pathmind.ui.views.experiment.components.RewardFunctionEditor;
import io.skymind.pathmind.ui.views.project.ProjectView;
import io.skymind.pathmind.utils.WrapperUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

@StyleSheet("frontend://styles/styles.css")
@Route(value = "experiment", layout = MainLayout.class)
public class ExperimentView extends VerticalLayout implements BasicViewInterface, HasUrlParameter<Long>
{
	private static final double DEFAULT_SPLIT_PANE_RATIO = 60;

	private Logger log = LogManager.getLogger(ExperimentView.class);

	private ScreenTitlePanel screenTitlePanel = new ScreenTitlePanel("PROJECT");

	private TextArea errorsTextArea = new TextArea("Errors");
	private TextArea getObservationTextArea = new TextArea("getObservation");
	private TextArea tipsTextArea = new TextArea("Tips");

	// TODO I assume we don't need this here and that the project, etc. are all retrieved from the Experiment
	// or something along those lines but since I haven't yet setup the fake database schema for experiment
	// since I don't fully understand the hierarchy I'm just going to pull the project name directly to
	// confirm that the parameter is correctly wired up.
	@Autowired
	private ProjectRepository projectRepository;
	@Autowired
	private ExperimentRepository experimentRepository;

	private Binder<Experiment> binder = new Binder<>(Experiment.class);

	private RewardFunctionEditor rewardFunctionEditor = new RewardFunctionEditor();
	private ExperimentFormPanel experimentFormPanel = new ExperimentFormPanel(binder);

	public ExperimentView()
	{
		add(getActionMenu());
		add(getTitlePanel());
		add(getMainContent());

		setSizeFull();
		setDefaultHorizontalComponentAlignment(Alignment.CENTER);
	}

	// TODO -> Implement the actual navigation to the new screen. For now it's a hardcoded value.
	@Override
	public ActionMenu getActionMenu() {
		return new ActionMenu(
			new Button("+ New Experiment"),
			new Button("Test Run >", click ->
					UI.getCurrent().navigate(ProjectView.class, PathmindConstants.TODO_PARAMETER))
		);
	}

	// I do NOT want to implement a default interface because this is to remind me
	// what to implement and a default would remove that ability.
	@Override
	public Component getTitlePanel() {
		return screenTitlePanel;
	}

	// TODO -> Since I'm not sure exactly what the panels on the right are I'm going to make some big
	// assumptions as to which Layout should wrap which one.
	@Override
	public Component getMainContent() {
		return WrapperUtils.wrapCenterAlignmentFullSplitLayoutHorizontal(
				getLeftPanel(),
				getRightPanel(),
				DEFAULT_SPLIT_PANE_RATIO);
	}

	private VerticalLayout getLeftPanel() {
		return WrapperUtils.wrapFullSizeVertical(
				experimentFormPanel,
				rewardFunctionEditor
		);
	}

	private VerticalLayout getRightPanel()
	{
		errorsTextArea.setSizeFull();
		getObservationTextArea.setSizeFull();
		tipsTextArea.setSizeFull();

		return WrapperUtils.wrapFullSizeVertical(
				errorsTextArea,
				getObservationTextArea,
				tipsTextArea);
	}

	@Override
	public void setParameter(BeforeEvent event, Long experimentId)
	{
		Experiment experiment = experimentRepository.getExperiment(experimentId);
		if(experiment == null) {
			log.info("INVALID -> Attempted to load Experiment: "+ experimentId);
			return;
		}

		updateScreen(
				experiment,
				projectRepository.getProjectForExperiment(experimentId));
	}

	private void updateScreen(Experiment experiment, Project project) {
		binder.readBean(experiment);
		rewardFunctionEditor.setRewardFunction(experiment.getRewardFunction());
		screenTitlePanel.setSubtitle(project.getName());
	}

	private void save() {
//				try {
//			binder.writeBean(project);
//			return true;
//		} catch (ValidationException e) {
//			return false;
//		}
	}
}
