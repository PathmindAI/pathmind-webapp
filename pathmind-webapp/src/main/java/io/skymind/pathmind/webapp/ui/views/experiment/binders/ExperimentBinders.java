package io.skymind.pathmind.webapp.ui.views.experiment.binders;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import io.skymind.pathmind.shared.data.Experiment;

public class ExperimentBinders
{
	public static void bindName(Binder<Experiment> binder, TextField nameTextField) {
		binder.forField(nameTextField)
				.asRequired("Experiment must have a name")
				.bind(Experiment::getName, Experiment::setName);
	}
}
