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
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.constants.RunType;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.exception.InvalidDataException;
import io.skymind.pathmind.services.RewardValidationService;
import io.skymind.pathmind.services.TrainingService;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.components.buttons.NewExperimentButton;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.utils.*;
import io.skymind.pathmind.ui.views.PathMindDefaultView;
import io.skymind.pathmind.ui.views.experiment.components.RewardFunctionEditor;
import io.skymind.pathmind.ui.views.experiment.utils.ExperimentViewNavigationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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

	@Autowired
	private TrainingService trainingService;

	private Binder<Experiment> binder;

	private Button backToExperimentsButton;

	public NewExperimentView()
	{
		super();
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
		errorsTextArea = new TextArea("Errors");
		errorsTextArea.setReadOnly(true);
		errorsTextArea.setSizeFull();
		errorsTextArea.setReadOnly(true);

		rewardFunctionEditor = new RewardFunctionEditor();
		rewardFunctionEditor.addValueChangeListener(changeEvent -> {
			final List<String> errors = RewardValidationService.validateRewardFunction(changeEvent.getValue());
			PushUtils.push(UI.getCurrent(), () -> {
				errorsTextArea.setValue(String.join("\n", errors));
			});
		});
		binder.forField(rewardFunctionEditor)
				.asRequired()
				.withValidator((value, _context) -> {
					final List<String> errors = RewardValidationService.validateRewardFunction(value);
					if(errors.size() == 0){
						return ValidationResult.ok();
					}else{
						return ValidationResult.error("Reward Function has compile errors!");
					}
				})
				.bind(Experiment::getRewardFunction, Experiment::setRewardFunction);



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
		getObservationTextArea.setReadOnly(true);
		getObservationTextArea.setReadOnly(true);

		tipsTextArea = new TextArea("Tips");
		tipsTextArea.setSizeFull();
		tipsTextArea.setReadOnly(true);
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
		final Button startRunButton = new Button("Start (TEST RUN)", new Icon(VaadinIcon.CHEVRON_RIGHT),
				click -> handleStartRunButtonClicked());
		startRunButton.setIconAfterText(true);


		// TODO: Make Discovery available from after a test run only
		final Button startDiscoveryButton = new Button("Start (Discovery RUN)", new Icon(VaadinIcon.CHEVRON_RIGHT),
				click -> {
					ExceptionWrapperUtils.handleButtonClicked(() ->
					{
						if(!FormUtils.isValidForm(binder, experiment))
							return;

						experimentDAO.updateRewardFunction(experiment);
						trainingService.startDiscoveryRun(experiment);
						UI.getCurrent().navigate(ExperimentView.class, ExperimentViewNavigationUtils.getExperimentParameters(experiment));
					});
				});
		startDiscoveryButton.setIconAfterText(true);



		return WrapperUtils.wrapWidthFullCenterVertical(
				startRunButton,
				startDiscoveryButton,
				new Label("Start Test Run"));
	}

	private void handleStartRunButtonClicked() {
		ExceptionWrapperUtils.handleButtonClicked(() ->
		{
			if(!FormUtils.isValidForm(binder, experiment))
				return;

			experimentDAO.updateRewardFunction(experiment);
			trainingService.startTestRun(experiment);

			UI.getCurrent().navigate(ExperimentView.class, ExperimentViewNavigationUtils.getExperimentParameters(experiment));
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
		final NewExperimentButton newExperimentButton = new NewExperimentButton(
				experimentDAO,
				experiment.getModelId(),
				rewardFunctionEditor.getValue());

		final Button saveDraftButton = new Button("Save Draft", new Icon(VaadinIcon.FILE),
				click -> handleSaveDraftClicked());

		return WrapperUtils.wrapWidthFullCenterHorizontal(
				newExperimentButton,
				saveDraftButton);
	}

	private void handleSaveDraftClicked() {
		ExceptionWrapperUtils.handleButtonClicked(() ->
		{
			// TODO -> Case #78 -> How do we validate the Reward Function?
			NotificationUtils.showTodoNotification("Case #78 -> How do we validate the Reward Function?\n " +
					"https://github.com/SkymindIO/pathmind-webapp/issues/78");
			if(!FormUtils.isValidForm(binder, experiment))
				return;

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
	protected void loadData() throws InvalidDataException {
		experiment = experimentDAO.getExperiment(experimentId);
		if(experiment == null)
			throw new InvalidDataException("Attempted to access Experiment: " + experimentId);
	}

	@Override
	protected void updateScreen(BeforeEnterEvent event) throws InvalidDataException
	{
		binder.setBean(experiment);

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
