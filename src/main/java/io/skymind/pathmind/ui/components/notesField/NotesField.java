package io.skymind.pathmind.ui.components.notesField;

import java.util.function.Consumer;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;

@CssImport("./styles/components/notes-field.css")
public class NotesField extends HorizontalLayout {

	private String title;
	private String notesText;
	private Boolean isEditting = false;
	private Button editButton;
	private Button saveButton;
	private Div blockViewOnlyField;
	private TextArea blockEditableField;
	private Consumer<String> saveCallBack;

	public NotesField(Boolean isSingleLine, String text) {
		this(isSingleLine, null, text, null);
	}

	public NotesField(Boolean isSingleLine, String title, String text, Consumer<String> saveCallbackFn) {
		this.notesText = text;
		this.title = title;
		if (isSingleLine) {
			add(inlineViewOnlyField());
		} else {
			add(blockEditableFieldWrapper());
		}
		if (saveCallbackFn != null) {
			saveCallBack = saveCallbackFn;
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
		blockEditableField.addThemeName("notes");
	}

	private VerticalLayout blockEditableFieldWrapper() {
		HorizontalLayout headerRow = new HorizontalLayout(
			new Span(title),
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
		editableFieldWrapper.addClassName("notes-block");
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

	private Button createButton(String label, Boolean isEnabled) {
		Button button = new Button(label);
		button.setEnabled(isEnabled);
		button.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
		return button;
	}

	private void initButtons() {
		Span saveIcon = new Span("");
		saveIcon.addClassName("save-icon");
		editButton = createButton("Edit", !isEditting);
		saveButton = createButton("Save", isEditting);

		editButton.addClickListener(e -> {
			setIsEditting();
			editButton.setEnabled(!isEditting);
			saveButton.setEnabled(isEditting);
			blockEditableField.focus();
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
		String updatedNotesText = blockEditableField.getValue();
		blockViewOnlyField.setText(updatedNotesText);
		System.out.println("Clicked Save Button");
		saveCallBack.accept(updatedNotesText);
	};
}
