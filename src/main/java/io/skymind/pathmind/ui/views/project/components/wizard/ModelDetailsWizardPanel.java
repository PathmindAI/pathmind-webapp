package io.skymind.pathmind.ui.views.project.components.wizard;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.Binder;
import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.ui.utils.GuiUtils;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.ui.views.project.binders.ProjectBinders;

public class ModelDetailsWizardPanel extends VerticalLayout
{
	private VerticalLayout formPanel = new VerticalLayout();

	private NumberField numberOfObservationsNumberField = new NumberField();
	private NumberField numberOfPossibleActionsNumberField = new NumberField();
	private TextArea getObservationForRewardFunctionTextArea = new TextArea();

	private Button nextStepButton = new Button("Next Step");

	public ModelDetailsWizardPanel(Binder<Project> binder) {
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
		numberOfObservationsNumberField.setValue((double) Project.DEFAULT_NUMBER_OF_OBSERVATIONS);
		numberOfPossibleActionsNumberField.setValue((double) Project.DEFAULT_NUMBER_OF_POSSIBLE_ACTIONS);
		getObservationForRewardFunctionTextArea.setValue(Project.DEFAULT_GET_OBSERVATION_FOR_REWARD_FUNCTION);
	}

	private void bindFields(Binder<Project> binder) {
		ProjectBinders.bindNumberOfObservations(binder, numberOfObservationsNumberField);
		ProjectBinders.bindNumberOfPossibleActions(binder, numberOfPossibleActionsNumberField);
		ProjectBinders.bindGetObservationForRewardFunction(binder, getObservationForRewardFunctionTextArea);
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
				numberOfObservationsNumberField);
		GuiUtils.removeMarginsPaddingAndSpacing(wrapper);
		return wrapper;
	}

	private VerticalLayout getNumberOfPossibleActionsPanel() {
		VerticalLayout wrapper = new VerticalLayout(
				GuiUtils.getBoldLabel("Number of Possible Actions"),
				new Label("This is the number of possible actions in doAction()"),
				numberOfPossibleActionsNumberField);
		GuiUtils.removeMarginsPaddingAndSpacing(wrapper);
		return wrapper;
	}
}