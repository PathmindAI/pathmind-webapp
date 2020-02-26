package io.skymind.pathmind.ui.components.notesField;

import java.util.function.Consumer;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import org.apache.commons.lang3.StringUtils;

@CssImport("./styles/components/notes-field.css")
public class NotesField extends HorizontalLayout {

	private String title;
	private String notesText;
	private Boolean isEditting = false;
	private Button saveButton;
	private TextArea blockEditableField;
	private Consumer<String> saveConsumer;

	public NotesField(String title, String text, Consumer<String> saveConsumer) {
		this.notesText = text;
		this.title = title;
		this.saveConsumer = saveConsumer;
		add(editableFieldWrapper());
		setSpacing(false);
		addClassName("notes-field-wrapper");
	}

	private void createEditableField() {
		blockEditableField = new TextArea("", StringUtils.defaultString(notesText), "Add Notes");
		blockEditableField.addThemeName("notes");
		blockEditableField.addKeyUpListener(event -> {
			if (isEditting == false) {
				toggleIsEditting();
				saveButton.setEnabled(isEditting);
			}
		});
		blockEditableField.addValueChangeListener(event -> {
			if (isEditting && event.getValue() != notesText) {
				saveButton.click();
			}
		});
	}

	private VerticalLayout editableFieldWrapper() {
		HorizontalLayout headerRow = new HorizontalLayout(
			new Span(title),
			buttonsWrapper()
		);
		headerRow.setSpacing(false);
		headerRow.addClassName("header");
		createEditableField();
		VerticalLayout editableFieldWrapper = new VerticalLayout(
			headerRow,
			blockEditableField
		);
		editableFieldWrapper.addClassName("notes-block");
		editableFieldWrapper.setSpacing(false);
		editableFieldWrapper.setPadding(false);
		return editableFieldWrapper;
	}

	private HorizontalLayout buttonsWrapper() {
		initButtons();
		HorizontalLayout buttonsWrapper = new HorizontalLayout(
			saveButton
		);
		buttonsWrapper.setSpacing(false);
		return buttonsWrapper;
	}

	private Button createButton(String label, Boolean isEnabled) {
		Button button = new Button(label);
		button.setEnabled(isEnabled);
		button.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
		return button;
	}

	private void initButtons() {
		saveButton = createButton("Save", isEditting);
		saveButton.addClickListener(e -> {
			toggleIsEditting();
			saveButton.setEnabled(isEditting);
			saveButtonOnClick();
		});
	}
 
	private void toggleIsEditting() {
		isEditting = !isEditting;
	}

	private void setNotesText(String notesText) {
		this.notesText = notesText;
	}

	public void saveButtonOnClick() {
		notesText = blockEditableField.getValue();
		saveConsumer.accept(notesText);
	}
}
