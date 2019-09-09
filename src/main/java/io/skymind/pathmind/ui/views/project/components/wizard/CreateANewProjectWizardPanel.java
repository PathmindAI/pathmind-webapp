package io.skymind.pathmind.ui.views.project.components.wizard;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.ui.utils.GuiUtils;
import io.skymind.pathmind.ui.utils.WrapperUtils;

public class CreateANewProjectWizardPanel extends VerticalLayout
{
	private TextField projectNameTextField = new TextField("Give your project a name");
	private Button createProjectButton = new Button("Create Project");

	public CreateANewProjectWizardPanel(Binder<Project> binder)
	{
		projectNameTextField.setWidthFull();

		add(new H3("Start a New Project!"),
				GuiUtils.getSubtitleLabel("Projects organize your Pathmind Experiments based on your situation model"),
				GuiUtils.getHeightSpacer("40px"),
				projectNameTextField,
				WrapperUtils.wrapCenterFullWidthHorizontal(createProjectButton));

		getStyle().set("border", "1px solid #ccc");

		bindFields(binder);
	}

	public void addButtonClickListener(ComponentEventListener<ClickEvent<Button>> listener) {
		createProjectButton.addClickListener(listener);
	}

	private void bindFields(Binder<Project> binder) {
		binder.forField(projectNameTextField)
				.asRequired("Project must have a name")
				.bind(Project::getName, Project::setName);
	}
}
