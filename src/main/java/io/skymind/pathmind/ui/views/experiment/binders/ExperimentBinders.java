package io.skymind.pathmind.ui.views.experiment.binders;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import io.skymind.pathmind.constants.RunType;
import io.skymind.pathmind.data.Experiment;

public class ExperimentBinders
{
	public static void bindName(Binder<Experiment> binder, TextField nameTextField) {
		binder.forField(nameTextField)
				.asRequired("Experiment must have a name")
				.bind(Experiment::getName, Experiment::setName);
	}

//	public static void bindRunType(Binder<Experiment> binder, ComboBox<RunType> runTypeComboBox) {
//		binder.forField(runTypeComboBox)
//				.asRequired("Run Type must be selected")
//				.bind(Experiment::getRunTypeEnum, Experiment::setRunTypeEnum);
//	}
}
