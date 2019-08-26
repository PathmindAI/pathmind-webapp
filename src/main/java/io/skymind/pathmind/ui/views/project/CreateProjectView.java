package io.skymind.pathmind.ui.views.project;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.constants.ModelTimeUnit;
import io.skymind.pathmind.ui.components.ActionMenu;
import io.skymind.pathmind.ui.components.LabelFactory;
import io.skymind.pathmind.ui.constants.CssMindPathStyles;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.views.BasicViewInterface;
import io.skymind.pathmind.utils.UIConstants;
import io.skymind.pathmind.utils.WrapperUtils;

import java.util.Arrays;

@StyleSheet("frontend://styles/styles.css")
@Route(value = "createProject", layout = MainLayout.class)
public class CreateProjectView extends VerticalLayout implements BasicViewInterface
{
	private TextField projectNameTextField = new TextField();
	private TextField stepSizeTextfield = new TextField();
	private ComboBox<ModelTimeUnit> modelTimeUnitComboBox = new ComboBox<>();
	private Button browseButton = new Button("Browse Files");
	private Button dragAndDropButton = new Button("Drag and drop");

	public CreateProjectView()
	{
		add(getActionMenu());
		add(getTitlePanel());
		add(getMainContent());

		setWidthFull();
		setDefaultHorizontalComponentAlignment(Alignment.CENTER);
	}

	// TODO -> Button should actually do something productive here.
	public ActionMenu getActionMenu() {
		return new ActionMenu(
				new Button("Create Project >", click -> UI.getCurrent().navigate(FileCheckView.class))
		);
	}

	public Component getMainContent() {

		FormLayout form = new FormLayout();
		form.setWidth(UIConstants.CENTERED_FORM_WIDTH);
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

		return WrapperUtils.wrapCenterAlignmentHorizontal(form);
	}

	public VerticalLayout getTitlePanel() {
		return WrapperUtils.wrapCenterAlignmentFullVertical(
				LabelFactory.createLabel("Welcome to", CssMindPathStyles.SMALL_LIGHT_LABEL),
				LabelFactory.createLabel("Pathmind", CssMindPathStyles.LOGO_LABEL)
		);
	}
}
