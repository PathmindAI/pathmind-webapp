package io.skymind.pathmind.webapp.ui.binders;

import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.StringLengthValidator;
import io.skymind.pathmind.shared.data.Model;

public class ModelBinders
{
	public static void bindNotesFieldTextArea(Binder<Model> binder, TextArea notesFieldTextArea)
	{
		binder.forField(notesFieldTextArea)
				.withValidator(new StringLengthValidator("Notes might have at most 1000 characters", 0, 1000))
				.bind(Model::getUserNotes, Model::setUserNotes);
	}
}
