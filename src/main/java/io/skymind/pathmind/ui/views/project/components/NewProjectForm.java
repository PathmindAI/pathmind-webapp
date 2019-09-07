package io.skymind.pathmind.ui.views.project.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import io.skymind.pathmind.constants.ModelTimeUnit;
import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.ui.utils.UIConstants;

import java.util.Arrays;

public class NewProjectForm extends VerticalLayout
{
	private TextField projectNameTextField;
	private TextField stepSizeTextfield;
	private ComboBox<ModelTimeUnit> modelTimeUnitComboBox;
	private Button browseButton;
	private Button dragAndDropButton;

	public NewProjectForm(Binder<Project> binder)
	{
		setupFormFields();
		bindFields(binder);

		add(getForm());

		setWidth(UIConstants.CENTERED_FORM_WIDTH);
		setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
	}

	private void setupFormFields() {
		projectNameTextField = new TextField();
		stepSizeTextfield = new TextField();
		modelTimeUnitComboBox = new ComboBox<>();
		browseButton = new Button("Browse Files");
		dragAndDropButton = new Button("Drag and drop");

		projectNameTextField.setWidthFull();
		stepSizeTextfield.setWidthFull();
		modelTimeUnitComboBox.setWidthFull();
		dragAndDropButton.setWidthFull();

		modelTimeUnitComboBox.setItems(Arrays.asList(ModelTimeUnit.values()));
	}

	private FormLayout getForm() {
		FormLayout form = new FormLayout();
		form.setWidth(UIConstants.CENTERED_FORM_WIDTH);
		form.setMaxWidth(UIConstants.CENTERED_FORM_WIDTH);
		form.setResponsiveSteps(new FormLayout.ResponsiveStep(UIConstants.CENTERED_FORM_WIDTH, 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));

		form.addFormItem(projectNameTextField, "Project Name");
		form.addFormItem(stepSizeTextfield, "Step Size");
		form.addFormItem(modelTimeUnitComboBox, "Model Time Unit");
		form.addFormItem(dragAndDropButton, browseButton);
		return form;
	}

	private void bindFields(Binder<Project> binder) {
		binder.forField(projectNameTextField)
				.asRequired("Project must have a name")
				.bind(Project::getName, Project::setName);
	}

	public String getProjectName() {
		return projectNameTextField.getValue();
	}
}
