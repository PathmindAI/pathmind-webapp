package io.skymind.pathmind.ui.components.notesField;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;

@CssImport("./styles/components/notes-field.css")
public class NotesField extends HorizontalLayout {

	private String notesText;
	private Boolean isEditting = false;
	private Button editButton;
	private Button saveButton;
	private Div blockViewOnlyField;
	private TextArea blockEditableField;

	public NotesField(Boolean isSingleLine, String text) {
		this.notesText = text;
		if (isSingleLine) {
			add(inlineViewOnlyField());
		} else {
			add(blockEditableFieldWrapper());
		}
		setSpacing(false);
		addClassName("notes-field-wrapper");
	}

	private Span inlineViewOnlyField() {
		if (notesText == null || notesText == "") {
			notesText = "--";
		}
		Span inlineField = new Span(notesText);
		inlineField.addClassName("inline");
		return inlineField;
	}

	private void createBlockViewOnlyField() {
		Div notesWrapper = new Div();
		notesWrapper.addClassName("notes-block-view-only");
		notesWrapper.setText(notesText);
		blockViewOnlyField = notesWrapper;
	}

	private void createBlockEditableField() {
		blockEditableField = new TextArea("", notesText, "Add Notes");
	}

	private VerticalLayout blockEditableFieldWrapper() {
		HorizontalLayout headerRow = new HorizontalLayout(
			new H4("Notes Field"),
			buttonsWrapper()
		);
		headerRow.setSpacing(false);
		headerRow.addClassName("header");
		createBlockViewOnlyField();
		createBlockEditableField();
		VerticalLayout editableFieldWrapper = new VerticalLayout(
			headerRow,
			blockViewOnlyField,
			blockEditableField
		);
		blockEditableField.setVisible(isEditting);
		editableFieldWrapper.addClassName("block");
		editableFieldWrapper.setSpacing(false);
		editableFieldWrapper.setPadding(false);
		return editableFieldWrapper;
	}

	private HorizontalLayout buttonsWrapper() {
		// TODO: set state of each button in click event listener
		initButtons();
		HorizontalLayout buttonsWrapper = new HorizontalLayout(
			editButton,
			saveButton
		);
		buttonsWrapper.setSpacing(false);
		return buttonsWrapper;
	}

	private Button createButton(String text, Boolean isEnabled) {
		Button button = new Button(text);
		button.setEnabled(isEnabled);
		return button;
	}

	private void initButtons() {		
		editButton = createButton("Edit", !isEditting);
		saveButton = createButton("Save", isEditting);

		editButton.addClickListener(e -> {
			setIsEditting();
			editButton.setEnabled(!isEditting);
			saveButton.setEnabled(isEditting);
			editButtonOnClick();
		});

		saveButton.addClickListener(e -> {
			setIsEditting();
			saveButton.setEnabled(isEditting);
			editButton.setEnabled(!isEditting);
			saveButtonOnClick();
		});
	}
 
	private void setIsEditting() {
		isEditting = !isEditting;
		blockViewOnlyField.setVisible(!isEditting);
		blockEditableField.setVisible(isEditting);
	}

	public void editButtonOnClick() {
		System.out.println("Clicked Edit Button");
	};

	public void saveButtonOnClick() {
		System.out.println("Clicked Save Button");
	};
}
