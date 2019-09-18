package io.skymind.pathmind.ui.views.run;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.constants.PathmindConstants;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.db.dao.ProjectDAO;
import io.skymind.pathmind.db.repositories.ExperimentRepository;
import io.skymind.pathmind.exception.InvalidDataException;
import io.skymind.pathmind.ui.components.ActionMenu;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.ui.views.PathMindDefaultView;
import io.skymind.pathmind.ui.views.experiment.components.ExperimentFormPanel;
import io.skymind.pathmind.ui.views.experiment.components.RewardFunctionEditor;
import io.skymind.pathmind.ui.views.project.ProjectView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

@StyleSheet("frontend://styles/styles.css")
@Route(value = "discoveryRun", layout = MainLayout.class)
public class DiscoveryRunView extends PathMindDefaultView implements HasUrlParameter<Long>
{
	private static final double DEFAULT_SPLIT_PANE_RATIO = 60;

	private Logger log = LogManager.getLogger(DiscoveryRunView.class);

	private long experimentId = -1;

	private ScreenTitlePanel screenTitlePanel;

	private TextArea errorsTextArea;
	private TextArea getObservationTextArea;
	private TextArea tipsTextArea;

	// TODO I assume we don't need this here and that the project, etc. are all retrieved from the Experiment
	// or something along those lines but since I haven't yet setup the fake database schema for experiment
	// since I don't fully understand the hierarchy I'm just going to pull the project name directly to
	// confirm that the parameter is correctly wired up.
	@Autowired
	private ProjectDAO projectDAO;
	@Autowired
	private ExperimentRepository experimentRepository;

	private Binder<Experiment> binder;

	private RewardFunctionEditor rewardFunctionEditor;
	private ExperimentFormPanel experimentFormPanel;

	private Button backToProjectButton;

	public DiscoveryRunView()
	{
		super();
	}

	@Override
	protected ActionMenu getActionMenu()
	{
		backToProjectButton = new Button("< Back to Project");

		return new ActionMenu(
				backToProjectButton,
				new Button("+ New Experiment"),
				new Button("Test Run >", click ->
						UI.getCurrent().navigate(ProjectView.class, PathmindConstants.TODO_PARAMETER))
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
		binder = new Binder<>(Experiment.class);

		rewardFunctionEditor = new RewardFunctionEditor();
		experimentFormPanel = new ExperimentFormPanel(binder);

		return WrapperUtils.wrapCenterAlignmentFullSplitLayoutHorizontal(
				getLeftPanel(),
				getRightPanel(),
				DEFAULT_SPLIT_PANE_RATIO);
	}

	private VerticalLayout getLeftPanel() {
		return WrapperUtils.wrapSizeFullVertical(
				experimentFormPanel,
				rewardFunctionEditor);
	}

	private VerticalLayout getRightPanel()
	{
		errorsTextArea = new TextArea("Errors");
		errorsTextArea.setSizeFull();

		getObservationTextArea = new TextArea("getObservation");
		getObservationTextArea.setSizeFull();

		tipsTextArea = new TextArea("Tips");
		tipsTextArea.setSizeFull();

		return WrapperUtils.wrapSizeFullVertical(
				errorsTextArea,
				getObservationTextArea,
				tipsTextArea);
	}

	@Override
	public void setParameter(BeforeEvent event, Long experimentId) {
		this.experimentId = experimentId;
	}

	@Override
	protected void updateScreen(BeforeEnterEvent event) throws InvalidDataException
	{
		Experiment experiment = experimentRepository.getExperiment(experimentId);

		if(experiment == null)
			throw new InvalidDataException("Attempted to access Experiment: " + experimentId);

		Project project = projectDAO.getProjectForExperiment(experimentId);

		binder.readBean(experiment);

		rewardFunctionEditor.setRewardFunction(experiment.getRewardFunction());
		screenTitlePanel.setSubtitle(project.getName());
		backToProjectButton.addClickListener(click ->
				UI.getCurrent().navigate(ProjectView.class, project.getId()));
	}
}
