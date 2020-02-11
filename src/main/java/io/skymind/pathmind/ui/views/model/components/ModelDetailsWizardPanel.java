package io.skymind.pathmind.ui.views.model.components;

import static io.skymind.pathmind.ui.constants.CssMindPathStyles.BOLD_LABEL;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.binder.Binder;

import io.skymind.pathmind.data.Model;
import io.skymind.pathmind.ui.binders.ModelBinders;
import io.skymind.pathmind.ui.components.LabelFactory;
import io.skymind.pathmind.ui.components.PathmindTextArea;
import io.skymind.pathmind.ui.utils.GuiUtils;
import io.skymind.pathmind.ui.utils.VaadinUtils;
import io.skymind.pathmind.ui.utils.WrapperUtils;

public class ModelDetailsWizardPanel extends VerticalLayout
{
	private VerticalLayout formPanel = new VerticalLayout();

	private NumberField numberOfObservationsNumberField;
	private NumberField numberOfPossibleActionsNumberField;
	private PathmindTextArea getObservationForRewardFunctionTextArea;

	private Button nextStepButton = new Button("Next",  new Icon(VaadinIcon.CHEVRON_RIGHT));


	public ModelDetailsWizardPanel(Binder<Model> binder)
	{
		setupFields();
		setupForm();
		setupGetObservationForRewardFunctionTextArea();
		nextStepButton.setIconAfterText(true);

		add(WrapperUtils.wrapWidthFullHorizontal(
				new Icon(VaadinIcon.COMMENTS.CHECK_CIRCLE),
				LabelFactory.createLabel("Your model was successfully uploaded!", BOLD_LABEL)),
				LabelFactory.createLabel("Let's add a few details."),
				GuiUtils.getFullWidthHr(),
				formPanel,
				WrapperUtils.wrapWidthFullCenterHorizontal(nextStepButton));

		bindFields(binder);

		setWidthFull();
		setClassName("view-section"); // adds the white 'panel' style with rounded corners
	}

	private void setupFields()
	{
	 	numberOfObservationsNumberField = VaadinUtils.generateNumberField(
	 			Model.MIN_NUMBER_OF_OBSERVATIONS,
				Model.MAX_NUMBER_OF_OBSERVATIONS,
				Model.DEFAULT_NUMBER_OF_OBSERVATIONS);

		numberOfPossibleActionsNumberField = VaadinUtils.generateNumberField(
				Model.MIN_NUMBER_OF_POSSIBLE_ACTIONS,
				Model.MAX_NUMBER_OF_POSSIBLE_ACTIONS,
				Model.DEFAULT_NUMBER_OF_POSSIBLE_ACTIONS);

		getObservationForRewardFunctionTextArea = new PathmindTextArea();
		getObservationForRewardFunctionTextArea.setValue(Model.DEFAULT_GET_OBSERVATION_FOR_REWARD_FUNCTION);
	}

	private void bindFields(Binder<Model> binder) {
		ModelBinders.bindNumberOfObservations(binder, numberOfObservationsNumberField);
		ModelBinders.bindNumberOfPossibleActions(binder, numberOfPossibleActionsNumberField);
		ModelBinders.bindGetObservationForRewardFunction(binder, getObservationForRewardFunctionTextArea);
	}

	public void addButtonClickListener(ComponentEventListener<ClickEvent<Button>> listener) {
		nextStepButton.addClickListener(listener);
	}

	private void setupGetObservationForRewardFunctionTextArea() {
		getObservationForRewardFunctionTextArea.setWidthFull();
		getObservationForRewardFunctionTextArea.setHeight("200px");
		getObservationForRewardFunctionTextArea.setSpellcheck(false);
	}

	private void setupForm() {
		formPanel.add(getNumberOfObservationsPanel(),
				getNumberOfPossibleActionsPanel(),
				getObservationForRewardFunctionPanel());
	}

	private Component getObservationForRewardFunctionPanel() {
		VerticalLayout wrapper = WrapperUtils.wrapWidthFullVertical(
				LabelFactory.createLabel("Copy your Observation for Reward function in here for easy reference."),
				LabelFactory.createLabel("getObservation for Reward Function", BOLD_LABEL),
				getObservationForRewardFunctionTextArea);
		GuiUtils.removeMarginsPaddingAndSpacing(wrapper);
		return wrapper;
	}

	private VerticalLayout getNumberOfObservationsPanel() {
		VerticalLayout wrapper = new VerticalLayout(
				LabelFactory.createLabel("Number of Observations for Training", BOLD_LABEL),
				LabelFactory.createLabel("Enter the number of observations present in the 'observation of training' array"),
				numberOfObservationsNumberField);
		GuiUtils.removeMarginsPaddingAndSpacing(wrapper);
		return wrapper;
	}

	private VerticalLayout getNumberOfPossibleActionsPanel() {
		VerticalLayout wrapper = new VerticalLayout(
				LabelFactory.createLabel("Number of Possible Actions", BOLD_LABEL),
				LabelFactory.createLabel("This is the number of possible actions in doAction()"),
				numberOfPossibleActionsNumberField);
		GuiUtils.removeMarginsPaddingAndSpacing(wrapper);
		return wrapper;
	}
}