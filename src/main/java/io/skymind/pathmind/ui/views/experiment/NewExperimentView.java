package io.skymind.pathmind.ui.views.experiment;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.constants.RunType;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.db.dao.ProjectDAO;
import io.skymind.pathmind.exception.InvalidDataException;
import io.skymind.pathmind.ui.components.ActionMenu;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.utils.NotificationUtils;
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

	private Label projectLabel;
	private Label runTypeLabel;
	private Label modelRevisionLabel;
	private Label experimentLabel;

	private TextArea errorsTextArea;
	private TextArea getObservationTextArea;
	private TextArea tipsTextArea;
	private RewardFunctionEditor rewardFunctionEditor;

	@Autowired
	private ExperimentDAO experimentDAO;

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
				getTopStatusPanel(),
				getObservationTextArea,
				tipsTextArea,
				getActionButtons());
	}

	private Component getTopStatusPanel()
	{
		projectLabel = new Label();
		runTypeLabel = new Label();
		modelRevisionLabel = new Label();
		experimentLabel = new Label();

		FormLayout formLayout = new FormLayout();
		formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("100px", 4, FormLayout.ResponsiveStep.LabelsPosition.TOP));

		formLayout.addFormItem(projectLabel, "Project");
		formLayout.addFormItem(runTypeLabel, "Run Type");
		formLayout.addFormItem(modelRevisionLabel, "Model Revision");
		formLayout.addFormItem(experimentLabel, "Experiment");

		return formLayout;
	}

	private HorizontalLayout getActionButtons() {
		return WrapperUtils.wrapWidthFullHorizontal(
				new Button("+ New Experiment", click -> UI.getCurrent().navigate(NewExperimentView.class)),
				new Button("Save Draft", click -> handleSaveDraftClicked()));
	}

	private void handleSaveDraftClicked() {
		NotificationUtils.showTodoNotification();
	}

	@Override
	public void setParameter(BeforeEvent event, Long experimentId) {
		this.experimentId = experimentId;
	}

	@Override
	protected void updateScreen(BeforeEnterEvent event) throws InvalidDataException
	{
		log.error("updating");
		Experiment experiment = experimentDAO.getExperiment(experimentId);

		if(experiment == null)
			throw new InvalidDataException("Attempted to access Experiment: " + experimentId);

		// TODO -> Need to load getObservations panel
		binder.readBean(experiment);

		screenTitlePanel.setSubtitle(experiment.getProject().getName());
		updateTopStatusPanel(experiment);
	}

	private void updateTopStatusPanel(Experiment experiment) {
		projectLabel.setText(experiment.getProject().getName());
		runTypeLabel.setText(RunType.TestRun.name());
		modelRevisionLabel.setText(experiment.getModel().getName());
		experimentLabel.setText(experiment.getName());
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
