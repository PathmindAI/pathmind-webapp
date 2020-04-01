package io.skymind.pathmind.webapp.ui.binders;

import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.Binder;
import io.skymind.pathmind.shared.data.Model;

public class ModelBinders
{
	public static void bindNotesFieldTextArea(Binder<Model> binder, TextArea notesFieldTextArea)
	{
		binder.forField(notesFieldTextArea)
				.bind(Model::getUserNotes, Model::setUserNotes);
	}
}
