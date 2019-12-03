package io.skymind.pathmind.ui.views.project.components.wizard;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.db.dao.ProjectDAO;
import io.skymind.pathmind.ui.binders.ProjectBinders;
import io.skymind.pathmind.ui.components.LabelFactory;
import io.skymind.pathmind.ui.constants.CssMindPathStyles;
import io.skymind.pathmind.ui.utils.WrapperUtils;

public class CreateANewProjectWizardPanel extends VerticalLayout
{
	private TextField projectNameTextField = new TextField("Give your project a name");
	private Button createProjectButton = new Button("Create Project");

	public CreateANewProjectWizardPanel(Binder<Project> binder, ProjectDAO projectDao)
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
	
	/**
	 * <code>createProjectButton</code> has a click shortcut that stays active as long as the button is visible and attached
	 * For this reason, changing also the visibility of the button, when visibility of panel is changed
	 * Filed an issue https://github.com/vaadin/flow/issues/7033 for this
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		createProjectButton.setVisible(visible);
	}
}
