package io.skymind.pathmind.webapp.ui.components.notesField;

import java.util.Objects;
import java.util.function.Consumer;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import org.apache.commons.lang3.StringUtils;

import io.skymind.pathmind.webapp.ui.components.LabelFactory;

@CssImport("./styles/components/notes-field.css")
public class NotesField extends HorizontalLayout {

	private String title;
	private String notesText;
	private Boolean isEditting = false;
	private Button saveButton;
	private TextArea blockEditableField;
	private Span hintWrapper;
	private Span warningWrapper;
	private Consumer<String> saveConsumer;

	public NotesField(String title, String notesText, Consumer<String> saveConsumer) {
		this.notesText = notesText;
		this.title = title;
		this.saveConsumer = saveConsumer;
		hintWrapper = LabelFactory.createLabel("Unsaved Notes!", "unsaved-draft-label");
		hintWrapper.setVisible(false);
		warningWrapper = LabelFactory.createLabel("Notes must not exceed 1000 characters", "unsaved-draft-label", "unsaved-and-too-big-text-label");
		warningWrapper.setVisible(false);
		add(editableFieldWrapper());
		setSpacing(false);
		addClassName("notes-field-wrapper");
	}

	private void createEditableField() {
		blockEditableField = new TextArea("", StringUtils.defaultString(notesText), "Add Notes");
		blockEditableField.addThemeName("notes");
		blockEditableField.addKeyUpListener(event -> {
			if (!isEditting) {
				toggleIsEditting();
				if (warningWrapper.isVisible()) {
					if (blockEditableField.getValue().length() <= 1000) {
						warningWrapper.setVisible(false);
					}
				}
				hintWrapper.setVisible(!warningWrapper.isVisible());
			}
		});
		blockEditableField.addValueChangeListener(event -> {
			if (isEditting && !Objects.equals(event.getValue(), notesText)) {
				saveButton.click();
				hintWrapper.setVisible(false);
			}
		});
	}

	private VerticalLayout editableFieldWrapper() {
		initButtons();
		Span spanTitle = new Span(title);
		spanTitle.getStyle().set("flex", "1");
		HorizontalLayout headerRow = new HorizontalLayout(
			spanTitle,
			hintWrapper,
			warningWrapper,
			saveButton
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

	private Button createButton(String label, Boolean isEnabled) {
		Button button = new Button(label);
		return button;
	}

	private void initButtons() {
		saveButton = createButton("Save", isEditting);
		saveButton.addClickListener(e -> {
			toggleIsEditting();
			saveButtonOnClick();
		});
	}
 
	private void toggleIsEditting() {
		isEditting = !isEditting;
	}

	public void setNotesText(String notesText) {
		this.notesText = notesText;
		blockEditableField.setValue(notesText);
	}

	public void setPlaceholder(String placeholderText) {
		blockEditableField.setPlaceholder(placeholderText);
	}

	public void saveButtonOnClick() {
		String updatedNotesText = blockEditableField.getValue();
		if (updatedNotesText.length() > 1000) {
			hintWrapper.setVisible(false);
			warningWrapper.setVisible(true);
		}
		else if (!Objects.equals(notesText, updatedNotesText)) {
			warningWrapper.setVisible(false);
			notesText = updatedNotesText;
			saveConsumer.accept(notesText);
		}
		else {
			warningWrapper.setVisible(false);
		}
	}
}
