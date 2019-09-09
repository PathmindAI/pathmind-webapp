package io.skymind.pathmind.ui.views.project.components.wizard;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToBigDecimalConverter;
import com.vaadin.flow.data.converter.StringToLongConverter;
import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.ui.converter.PathmindStringToBigDecimalConverter;
import io.skymind.pathmind.ui.utils.GuiUtils;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.utils.BigDecimalUtils;

import java.math.BigDecimal;

public class ModelDetailsWizardPanel extends VerticalLayout
{
	private VerticalLayout formPanel = new VerticalLayout();

	private TextField numberOfObservationsTextField = new TextField();
	private TextField numberOfPossibleActionsTextField = new TextField();
	private TextArea getObservationForRewardFunctionTextArea = new TextArea();

	private Button nextStepButton = new Button("Next Step");

	public ModelDetailsWizardPanel(Binder<Project> binder)
	{
		setupForm();
		setupGetObservationForRewardFunctionTextArea();
		setupDefaultValues();

		add(WrapperUtils.wrapFullWidthHorizontal(
					new Icon(VaadinIcon.COMMENTS.CHECK_CIRCLE),
					GuiUtils.getLabel("Your model was successfully uploaded!", "16px", "bold")),
				new Label("Let's add a few details."),
				GuiUtils.getFullWidthHr(),
				formPanel,
				WrapperUtils.wrapCenterFullWidthHorizontal(nextStepButton));

		bindFields(binder);

		setWidthFull();
		getStyle().set("border", "1px solid #ccc");
	}

	/**
	 * This is required otherwise the binder doesn't work across multiple panels very well.
	 */
	private void setupDefaultValues() {
		numberOfObservationsTextField.setValue(Long.toString(Project.DEFAULT_NUMBER_OF_OBSERVATIONS));
		numberOfPossibleActionsTextField.setValue(Project.DEFAULT_NUMBER_OF_POSSIBLE_ACTIONS.toString());
		getObservationForRewardFunctionTextArea.setValue(Project.DEFAULT_GET_OBSERVATION_FOR_REWARD_FUNCTION);
	}

	private void bindFields(Binder<Project> binder) {
		binder.forField(numberOfObservationsTextField)
				.withConverter(new StringToLongConverter("Not a number"))
				.asRequired("Number of Observations is required")
				.withValidator(numberOfObservations ->
						numberOfObservations >= Project.MIN_NUMBER_OF_OBSERVATIONS && numberOfObservations <= Project.MAX_NUMBER_OF_OBSERVATIONS,
						"Must be between: " + Project.MIN_NUMBER_OF_OBSERVATIONS + " and " + Project.MAX_NUMBER_OF_OBSERVATIONS)
				.bind(Project::getNumberOfObservations, Project::setNumberOfObservations);

		binder.forField(numberOfPossibleActionsTextField)
				.withConverter(new PathmindStringToBigDecimalConverter("Not a number"))
				.asRequired("Number of Possible Actions is required")
				.withValidator(numberOfPossibleActions ->
						BigDecimalUtils.isWithin(Project.MIN_NUMBER_OF_POSSIBLE_ACTIONS, Project.MAX_NUMBER_OF_POSSIBLE_ACTIONS, numberOfPossibleActions),
						"Number of observations must be between: " + Project.MIN_NUMBER_OF_OBSERVATIONS + " and " + Project.MAX_NUMBER_OF_OBSERVATIONS)
				.bind(Project::getNumberOfPossibleActions, Project::setNumberOfPossibleActions);

		binder.forField(getObservationForRewardFunctionTextArea)
				.asRequired("Field is required")
				.bind(Project::getGetObservationForRewardFunction, Project::setGetObservationForRewardFunction);
	}

	public void addButtonClickListener(ComponentEventListener<ClickEvent<Button>> listener) {
		nextStepButton.addClickListener(listener);
	}

	private void setupGetObservationForRewardFunctionTextArea() {
		getObservationForRewardFunctionTextArea.setWidthFull();
		getObservationForRewardFunctionTextArea.setHeight("200px");
	}

	private void setupForm() {
		formPanel.add(getNumberOfObservationsPanel(),
					getNumberOfPossibleActionsPanel(),
					getObservationForRewardFunctionPanel());
	}

	private Component getObservationForRewardFunctionPanel() {
		VerticalLayout wrapper = WrapperUtils.wrapFullWidthVertical(
				GuiUtils.getBoldLabel("getObservation for Reward Function"),
				getObservationForRewardFunctionTextArea);
		GuiUtils.removeMarginsPaddingAndSpacing(wrapper);
		return wrapper;
	}

	private VerticalLayout getNumberOfObservationsPanel() {
		VerticalLayout wrapper = new VerticalLayout(
				GuiUtils.getBoldLabel("Number of Observations for Training"),
				new Label("Enter the length of the Observation for Training array"),
				getTextFieldWithButtonsPanel(
						click -> handleSubtractObservationClicked(),
						numberOfObservationsTextField,
						click -> handleAddObservationClicked()));
		GuiUtils.removeMarginsPaddingAndSpacing(wrapper);
		return wrapper;
	}

	// TODO -> Validation and min/max.
	private void handleSubtractObservationClicked() {
		numberOfObservationsTextField.setValue(
				Integer.toString(Integer.parseInt(numberOfObservationsTextField.getValue()) - 1));
	}

	private void handleAddObservationClicked() {
		numberOfObservationsTextField.setValue(
				Integer.toString(Integer.parseInt(numberOfObservationsTextField.getValue()) + 1));
	}

	private VerticalLayout getNumberOfPossibleActionsPanel() {
		VerticalLayout wrapper = new VerticalLayout(
				GuiUtils.getBoldLabel("Number of Possible Actions"),
				new Label("This is the number of possible actions in doAction()"),
				getTextFieldWithButtonsPanel(
						click -> handleSubtractActionClicked(),
						numberOfPossibleActionsTextField,
						click -> handleAddActionClicked()));
		GuiUtils.removeMarginsPaddingAndSpacing(wrapper);
		return wrapper;
	}

	// TODO -> Validation and min/max.
	private void handleAddActionClicked() {
		numberOfPossibleActionsTextField.setValue(
				getNumberOfPossibleActionsBigDecimal().add(new BigDecimal("0.1")).setScale(1).toString());
	}

	private void handleSubtractActionClicked() {
		numberOfPossibleActionsTextField.setValue(
				getNumberOfPossibleActionsBigDecimal().subtract(new BigDecimal("0.1")).setScale(1).toString());
	}

	private BigDecimal getNumberOfPossibleActionsBigDecimal() {
		return new BigDecimal(numberOfPossibleActionsTextField.getValue());
	}

	private HorizontalLayout getTextFieldWithButtonsPanel(ComponentEventListener<ClickEvent<Button>> minusClickListener, TextField textField, ComponentEventListener<ClickEvent<Button>> plusClickListener) {
		textField.setWidth("100px");
		HorizontalLayout horizontalLayout = new HorizontalLayout(
				new Button(new Icon(VaadinIcon.MINUS), minusClickListener),
				textField,
				new Button(new Icon(VaadinIcon.PLUS), plusClickListener));
		horizontalLayout.setJustifyContentMode(JustifyContentMode.START);
		GuiUtils.removeMarginsPaddingAndSpacing(horizontalLayout);
		return horizontalLayout;
	}
}
