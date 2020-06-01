package io.skymind.pathmind.webapp.ui.views.project.components.panels;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import io.skymind.pathmind.shared.data.Project;
import io.skymind.pathmind.db.dao.ProjectDAO;
import io.skymind.pathmind.webapp.ui.binders.ProjectBinders;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.constants.CssMindPathStyles;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;

public class CreateANewProjectPanel extends VerticalLayout
{
	private TextField projectNameTextField = new TextField("Give your project a name");
	private Button createProjectButton = new Button("Create Project");

	public CreateANewProjectPanel(Binder<Project> binder, ProjectDAO projectDao)
	{
		projectNameTextField.setWidthFull();
		createProjectButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		createProjectButton.setClassName(CssMindPathStyles.CREATE_PROJECT_BUTTON);

		add(	LabelFactory.createLabel("Start a New Project!", CssMindPathStyles.SECTION_TITLE_LABEL),
				LabelFactory.createLabel("Projects organize your Pathmind Experiments based on your AnyLogic model", CssMindPathStyles.SECTION_SUBTITLE_LABEL),
				projectNameTextField,
				WrapperUtils.wrapWidthFullCenterHorizontal(createProjectButton));

		setClassName("view-section"); // adds the white 'panel' style with rounded corners

		bindFields(binder, projectDao);

		createProjectButton.addClickShortcut(Key.ENTER);
		projectNameTextField.focus();
	}

	public void addButtonClickListener(ComponentEventListener<ClickEvent<Button>> listener) {
		createProjectButton.addClickListener(listener);
	}

	private void bindFields(Binder<Project> binder, ProjectDAO projectDao) {
		ProjectBinders.bindProjectName(binder, projectDao, projectNameTextField);
	}
	
}
