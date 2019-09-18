package io.skymind.pathmind.ui.views.experiment;

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
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.db.dao.ProjectDAO;
import io.skymind.pathmind.db.repositories.ExperimentRepository;
import io.skymind.pathmind.exception.InvalidDataException;
import io.skymind.pathmind.ui.components.ActionMenu;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.views.PathMindDefaultView;
import io.skymind.pathmind.ui.views.experiment.components.RewardFunctionEditor;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.ui.views.run.DiscoveryRunConfirmationView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

@StyleSheet("frontend://styles/styles.css")
@Route(value = "newExperiment", layout = MainLayout.class)
public class NewExperimentView extends PathMindDefaultView implements HasUrlParameter<Long>
{
	private static final double DEFAULT_SPLIT_PANE_RATIO = 60;

	private Logger log = LogManager.getLogger(NewExperimentView.class);

	private long experimentId = -1;

	private ScreenTitlePanel screenTitlePanel;

	private TextArea errorsTextArea;
	private TextArea getObservationTextArea;
	private TextArea tipsTextArea;
	private RewardFunctionEditor rewardFunctionEditor;

	// TODO I assume we don't need this here and that the project, etc. are all retrieved from the Experiment
	// or something along those lines but since I haven't yet setup the fake database schema for experiment
	// since I don't fully understand the hierarchy I'm just going to pull the project name directly to
	// confirm that the parameter is correctly wired up.
	@Autowired
	private ProjectDAO projectDAO;
	@Autowired
	private ExperimentRepository experimentRepository;

	// TODO -> Add binder for the project other data fields outside of the experiment object.
	private Binder<Experiment> binder;

	private Button backToExperimentsButton;

	public NewExperimentView()
	{
		super();
	}

	@Override
	protected ActionMenu getActionMenu()
	{
		backToExperimentsButton = new Button("< Back to Experiments");

		return new ActionMenu(
				backToExperimentsButton,
				new Button("+ New Experiment"),
				new Button("Test Run >", click ->
						UI.getCurrent().navigate(DiscoveryRunConfirmationView.class, experimentId))
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

		return WrapperUtils.wrapCenterAlignmentFullSplitLayoutHorizontal(
				getLeftPanel(),
				getRightPanel(),
				DEFAULT_SPLIT_PANE_RATIO);
	}

	private Component getLeftPanel()
	{
		rewardFunctionEditor = new RewardFunctionEditor();
		binder.forField(rewardFunctionEditor)
				.bind(Experiment::getRewardFunction, Experiment::setRewardFunction);

		errorsTextArea = new TextArea("Errors");
		errorsTextArea.setEnabled(false);
		errorsTextArea.setSizeFull();

		return WrapperUtils.wrapCenterAlignmentFullSplitLayoutVertical(
				rewardFunctionEditor,
				WrapperUtils.wrapSizeFullVertical(errorsTextArea),
				70);
	}

	private VerticalLayout getRightPanel()
	{
		getObservationTextArea = new TextArea("getObservation");
		getObservationTextArea.setSizeFull();
		getObservationTextArea.setEnabled(false);

		tipsTextArea = new TextArea("Tips");
		tipsTextArea.setSizeFull();
		tipsTextArea.setEnabled(false);

		return WrapperUtils.wrapSizeFullVertical(
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
		log.error("updating");
		Experiment experiment = experimentRepository.getExperiment(experimentId);

		if(experiment == null)
			throw new InvalidDataException("Attempted to access Experiment: " + experimentId);

		Project project = projectDAO.getProjectForExperiment(experimentId);

		binder.readBean(experiment);

		// TODO -> Need to fully use the binder here. Only partially used.
//		getObservationTextArea.setValue(project.getGetObservationForRewardFunction());
//		rewardFunctionEditor.setRewardFunction(experiment.getRewardFunction());
//		screenTitlePanel.setSubtitle(project.getName());
//		backToExperimentsButton.addClickListener(click ->
//				UI.getCurrent().navigate(ExperimentsView.class, experiment.getModelId()));
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
