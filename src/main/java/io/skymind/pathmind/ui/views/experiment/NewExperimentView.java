package io.skymind.pathmind.ui.views.experiment;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
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
import io.skymind.pathmind.data.utils.ExperimentUtils;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.exception.InvalidDataException;
import io.skymind.pathmind.ui.components.ActionMenu;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.utils.*;
import io.skymind.pathmind.ui.views.PathMindDefaultView;
import io.skymind.pathmind.ui.views.experiment.components.RewardFunctionEditor;
import io.skymind.pathmind.ui.views.run.DiscoveryRunConfirmationView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.ValidationUtils;

@StyleSheet("frontend://styles/styles.css")
@Route(value = "newExperiment", layout = MainLayout.class)
public class NewExperimentView extends PathMindDefaultView implements HasUrlParameter<Long>
{
	private static final double DEFAULT_SPLIT_PANE_RATIO = 60;

	private Logger log = LogManager.getLogger(NewExperimentView.class);

	private long experimentId = -1;
	private Experiment experiment;

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

	private Binder<Experiment> binder;

	private Button backToExperimentsButton;

	public NewExperimentView()
	{
		super();
	}

	@Override
	protected ActionMenu getActionMenu()
	{
		backToExperimentsButton = new Button("Back to Experiments", new Icon(VaadinIcon.CHEVRON_LEFT));
		backToExperimentsButton.addClickListener(click -> NotificationUtils.showTodoNotification());

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
				.asRequired()
				.bind(Experiment::getRewardFunction, Experiment::setRewardFunction);

		errorsTextArea = new TextArea("Errors");
		errorsTextArea.setEnabled(false);
		errorsTextArea.setSizeFull();
		errorsTextArea.setReadOnly(true);

		return WrapperUtils.wrapCenterAlignmentFullSplitLayoutVertical(
				WrapperUtils.wrapSizeFullVertical(
						new Label("Write your reward functions"),
						rewardFunctionEditor),
				WrapperUtils.wrapSizeFullVertical(errorsTextArea),
				70);
	}

	private VerticalLayout getRightPanel()
	{
		getObservationTextArea = new TextArea("getObservation");
		getObservationTextArea.setSizeFull();
		getObservationTextArea.setEnabled(false);
		getObservationTextArea.setReadOnly(true);

		tipsTextArea = new TextArea("Tips");
		tipsTextArea.setSizeFull();
		tipsTextArea.setEnabled(false);
		tipsTextArea.setReadOnly(true);

		return WrapperUtils.wrapSizeFullVertical(
				getTopButtonPanel(),
				getTopStatusPanel(),
				tipsTextArea,
				getObservationTextArea,
				getActionButtons());
	}

	private Component getTopButtonPanel()
	{
		final Button startRunButton = new Button("Start", new Icon(VaadinIcon.CHEVRON_RIGHT),
				click -> handleStartRunButtonClicked());
		startRunButton.setIconAfterText(true);

		return WrapperUtils.wrapWidthFullCenterVertical(
				startRunButton,
				new Label("Start Test Run"));
	}

	private void handleStartRunButtonClicked() {
		ExceptionWrapperUtils.handleButtonClicked(() ->
		{
			// TODO -> Case #78 -> How do we validate the Reward Function?
			NotificationUtils.showTodoNotification("Case #78 -> How do we validate the Reward Function?\n " +
					"https://github.com/SkymindIO/pathmind-webapp/issues/78");
			if(!FormUtils.isValidForm(binder, experiment))
				return;

			// TODO -> Save a Run and Policy to the database
			NotificationUtils.showTodoNotification("Save Run and Policy to the database");

			// TODO -> Case #71 -> Define exactly what last activity represents
			NotificationUtils.showTodoNotification("Case #71 -> Define exactly what last activity represents\n" +
				"https://github.com/SkymindIO/pathmind-webapp/issues/71");

			UI.getCurrent().navigate(DiscoveryRunConfirmationView.class, experimentId);
		});
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

	private HorizontalLayout getActionButtons()
	{
		final Button newExperimentButton = new Button("New Experiment", new Icon(VaadinIcon.PLUS),
				click -> handleNewExperimentClicked());
		final Button saveDraftButton = new Button("Save Draft", new Icon(VaadinIcon.FILE),
				click -> handleSaveDraftClicked());

		return WrapperUtils.wrapWidthFullCenterHorizontal(
				newExperimentButton,
				saveDraftButton);
	}

	private static long experimentCounter = 1;

	private void handleNewExperimentClicked()
	{
		// TODO -> Case #79 -> How do we get the name? Id number?
		NotificationUtils.showTodoNotification("Case 79 -> Default naming scheme\n" +
				"https://github.com/SkymindIO/pathmind-webapp/issues/79");

		// TODO -> Case #80 -> Do we use the same reward function from the experiment we're on as a default value?
		NotificationUtils.showTodoNotification("Case 80 -> Can we use the same reward function as a default value for the reward function of the new experiment?\n" +
				"I'm assuming we use what's currently in the reward function editor. And if so should we first validate or just proceed anyways?\n" +
				"https://github.com/SkymindIO/pathmind-webapp/issues/80");

		// TODO -> Case #71 -> Define exactly what last activity represents
		NotificationUtils.showTodoNotification("Case #71 -> Define exactly what last activity represents\n" +
				"https://github.com/SkymindIO/pathmind-webapp/issues/71");

		Experiment newExperiment = ExperimentUtils.generateNewDefaultExperiment(experiment, "Todo Experiment " + experimentCounter++, rewardFunctionEditor.getValue());
		long newExperimentId = experimentDAO.setupNewExperiment(newExperiment);
		UI.getCurrent().navigate(NewExperimentView.class, newExperimentId);
	}

	private void handleSaveDraftClicked() {
		ExceptionWrapperUtils.handleButtonClicked(() ->
		{
			// TODO -> Case #78 -> How do we validate the Reward Function?
			NotificationUtils.showTodoNotification("Case #78 -> How do we validate the Reward Function?\n " +
					"https://github.com/SkymindIO/pathmind-webapp/issues/78");
			if(!FormUtils.isValidForm(binder, experiment))
				return;

			// TODO -> Case #71 -> Define exactly what last activity represents
			NotificationUtils.showTodoNotification("Case #71 -> Define exactly what last activity represents\n" +
					"https://github.com/SkymindIO/pathmind-webapp/issues/71");

			// TODO -> Case #81 -> What exactly happens when we save?
			NotificationUtils.showTodoNotification("Case #81 -> What exactly happens when we save?\n" +
					"https://github.com/SkymindIO/pathmind-webapp/issues/81");
			experimentDAO.updateRewardFunction(experiment);
			NotificationUtils.showCenteredSimpleNotification("Draft successfully saved", NotificationUtils.Style.Success);
		});
	}

	@Override
	public void setParameter(BeforeEvent event, Long experimentId) {
		this.experimentId = experimentId;
	}

	@Override
	protected void updateScreen(BeforeEnterEvent event) throws InvalidDataException
	{
		experiment = experimentDAO.getExperiment(experimentId);

		if(experiment == null)
			throw new InvalidDataException("Attempted to access Experiment: " + experimentId);

		binder.readBean(experiment);

		screenTitlePanel.setSubtitle(experiment.getProject().getName());
		getObservationTextArea.setValue(experiment.getModel().getGetObservationForRewardFunction());
		updateTopStatusPanel(experiment);
	}

	private void updateTopStatusPanel(Experiment experiment) {
		projectLabel.setText(experiment.getProject().getName());
		runTypeLabel.setText(RunType.TestRun.name());
		modelRevisionLabel.setText(experiment.getModel().getName());
		experimentLabel.setText(experiment.getName());
	}
}
