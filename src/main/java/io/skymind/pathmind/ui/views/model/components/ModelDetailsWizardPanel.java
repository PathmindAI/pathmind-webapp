package io.skymind.pathmind.ui.views.model.components;

import static io.skymind.pathmind.ui.constants.CssMindPathStyles.BOLD_LABEL;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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
	public PathmindTextArea notesFieldTextArea;

	private Button nextStepButton = new Button("Next",  new Icon(VaadinIcon.CHEVRON_RIGHT));


	public ModelDetailsWizardPanel(Binder<Model> binder)
	{
		setupFields();
		setupForm();
		setupNotesFieldTextArea();
		nextStepButton.setIconAfterText(true);
		nextStepButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

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

		notesFieldTextArea = new PathmindTextArea();
		notesFieldTextArea.setPlaceholder("Add Notes");;
	}

	private void bindFields(Binder<Model> binder) {
		ModelBinders.bindNumberOfObservations(binder, numberOfObservationsNumberField);
		ModelBinders.bindNumberOfPossibleActions(binder, numberOfPossibleActionsNumberField);
		ModelBinders.bindNotesFieldTextArea(binder, notesFieldTextArea);
	}

	public void addButtonClickListener(ComponentEventListener<ClickEvent<Button>> listener) {
		nextStepButton.addClickListener(listener);
	}

	private void setupNotesFieldTextArea() {
		notesFieldTextArea.setWidthFull();
		notesFieldTextArea.setHeight("200px");
		notesFieldTextArea.setSpellcheck(false);
	}

	private void setupForm() {
		formPanel.add(getNumberOfObservationsPanel(),
				getNumberOfPossibleActionsPanel(),
				getNotesFieldPanel());
		formPanel.setPadding(false);
	}

	private Component getNotesFieldPanel() {
		VerticalLayout wrapper = WrapperUtils.wrapWidthFullVertical(
				LabelFactory.createLabel("Model Notes", BOLD_LABEL),
				LabelFactory.createLabel("Put your notes here for the uploaded model."),
				notesFieldTextArea);
		GuiUtils.removeMarginsPaddingAndSpacing(wrapper);
		return wrapper;
	}

	private VerticalLayout getNumberOfObservationsPanel() {
		VerticalLayout wrapper = new VerticalLayout(
				LabelFactory.createLabel("Number of Observations", BOLD_LABEL),
				LabelFactory.createLabel("Enter the number of observations in your observations array."),
				numberOfObservationsNumberField);
		GuiUtils.removeMarginsPaddingAndSpacing(wrapper);
		return wrapper;
	}

	private VerticalLayout getNumberOfPossibleActionsPanel() {
		VerticalLayout wrapper = new VerticalLayout(
				LabelFactory.createLabel("Number of Actions", BOLD_LABEL),
				LabelFactory.createLabel("This is the total number of possible actions."),
				numberOfPossibleActionsNumberField);
		GuiUtils.removeMarginsPaddingAndSpacing(wrapper);
		return wrapper;
	}
}