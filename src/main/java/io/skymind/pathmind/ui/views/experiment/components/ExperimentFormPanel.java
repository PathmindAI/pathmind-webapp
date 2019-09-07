package io.skymind.pathmind.ui.views.experiment.components;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import io.skymind.pathmind.constants.RunType;
import io.skymind.pathmind.data.Experiment;

/**
 * Left it in the code for now even though it's not used since we'll eventually need to be able to
 * edit an Experiment's name and so on.
 */
public class ExperimentFormPanel extends VerticalLayout
{
	private static final String FORM_WIDTH = "400px";
	private static final String FORM_MIN_HEIGHT = "200px";

	private TextField nameTextField = new TextField("Name");
	private ComboBox<RunType> runTypeComboBox = new ComboBox<>("Run Type", RunType.values());

	// TODO -> Add binder
	public ExperimentFormPanel(Binder<Experiment> binder)
	{
		FormLayout form = new FormLayout(
				nameTextField,
				runTypeComboBox
		);

		bindFields(binder);

		form.setResponsiveSteps(new FormLayout.ResponsiveStep(FORM_WIDTH, 1));
		form.setMaxWidth(FORM_WIDTH);

		add(form);
		setWidthFull();
	}

	// TODO -> The mapping between RunType enum and value should be done at the database level using JOOQ Enum converters:
	// https://www.jooq.org/doc/3.0/manual/sql-execution/fetching/data-type-conversion/
	private void bindFields(Binder<Experiment> binder) {
		binder.forField(nameTextField)
				.asRequired("Experiment must have a name")
				.bind(Experiment::getName, Experiment::setName);
		binder.forField(runTypeComboBox)
				.asRequired("Run Type must be selected")
				.bind(Experiment::getRunTypeEnum, Experiment::setRunTypeEnum);
	}
}
