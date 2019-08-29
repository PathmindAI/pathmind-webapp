package io.skymind.pathmind.ui.views.project.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import io.skymind.pathmind.constants.ModelTimeUnit;
import io.skymind.pathmind.ui.components.LabelFactory;
import io.skymind.pathmind.ui.constants.CssMindPathStyles;
import io.skymind.pathmind.utils.UIConstants;
import io.skymind.pathmind.utils.WrapperUtils;

import java.util.Arrays;

public class NewProjectForm extends VerticalLayout
{
	private TextField projectNameTextField = new TextField();
	private TextField stepSizeTextfield = new TextField();
	private ComboBox<ModelTimeUnit> modelTimeUnitComboBox = new ComboBox<>();
	private Button browseButton = new Button("Browse Files");
	private Button dragAndDropButton = new Button("Drag and drop");

	public NewProjectForm()
	{
		FormLayout form = new FormLayout();

		form.setWidth(UIConstants.CENTERED_FORM_WIDTH);
		form.setMaxWidth(UIConstants.CENTERED_FORM_WIDTH);
		form.setResponsiveSteps(new FormLayout.ResponsiveStep(UIConstants.CENTERED_FORM_WIDTH, 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));

		projectNameTextField.setWidthFull();
		stepSizeTextfield.setWidthFull();
		modelTimeUnitComboBox.setWidthFull();
		dragAndDropButton.setWidthFull();

		modelTimeUnitComboBox.setItems(Arrays.asList(ModelTimeUnit.values()));

		form.addFormItem(projectNameTextField, "Project Name");
		form.addFormItem(stepSizeTextfield, "Step Size");
		form.addFormItem(modelTimeUnitComboBox, "Model Time Unit");
		form.addFormItem(dragAndDropButton, browseButton);

		add(getTitlePanel());
		add(form);

		setWidth(UIConstants.CENTERED_FORM_WIDTH);
		setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
	}

	public VerticalLayout getTitlePanel() {
		return WrapperUtils.wrapCenteredFormVertical(
				LabelFactory.createLabel("Welcome to", CssMindPathStyles.SMALL_LIGHT_LABEL),
				LabelFactory.createLabel("Pathmind", CssMindPathStyles.LOGO_LABEL)
		);
	}

	// TODO -> Quick solution for testing until we fully implement the form
	public String getProjectName() {
		return projectNameTextField.getValue();
	}
}
