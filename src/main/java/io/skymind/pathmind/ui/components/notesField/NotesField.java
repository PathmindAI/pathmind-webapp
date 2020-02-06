package io.skymind.pathmind.ui.components.notesField;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;

@CssImport("./styles/components/notes-field.css")
public class NotesField extends HorizontalLayout {

	private String notesText;

	public NotesField(Boolean isSingleLine, String text) {
		this.notesText = text;
		if (isSingleLine) {
			add(inlineViewOnlyField());
		} else {
			add(blockEditableField());
		}
		setSpacing(false);
		addClassName("notes-field-wrapper");
	}

	private Span inlineViewOnlyField() {
		Span inlineField = new Span(notesText);
		inlineField.addClassName("inline");
		return inlineField;
	}

	private VerticalLayout blockEditableField() {
		HorizontalLayout headerRow = new HorizontalLayout(
			new H4("Notes Field"),
			buttonsWrapper()
		);
		headerRow.setSpacing(false);
		headerRow.addClassName("header");
		VerticalLayout editableFieldWrapper = new VerticalLayout(
			headerRow
		);
		editableFieldWrapper.add(new TextArea("", notesText, "Add Notes"));
		editableFieldWrapper.addClassName("block");
		editableFieldWrapper.setSpacing(false);
		editableFieldWrapper.setPadding(false);
		return editableFieldWrapper;
	}

	private HorizontalLayout buttonsWrapper() {
		// TODO: set state of each button in click event listener
		HorizontalLayout buttonsWrapper = new HorizontalLayout(
			new Button("Edit"),
			new Button("Save")
		);
		buttonsWrapper.setSpacing(false);
		return buttonsWrapper;
	}

	// TODO: method for returning component state (on edit mode? on view mode?)
}
