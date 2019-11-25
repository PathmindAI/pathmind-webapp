package io.skymind.pathmind.ui.views.project.components.wizard;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.db.dao.ProjectDAO;
import io.skymind.pathmind.ui.binders.ProjectBinders;
import io.skymind.pathmind.ui.utils.GuiUtils;
import io.skymind.pathmind.ui.utils.WrapperUtils;

public class CreateANewProjectWizardPanel extends VerticalLayout
{
	private TextField projectNameTextField = new TextField("Give your project a name");
	private Button createProjectButton = new Button("Create Project");

	public CreateANewProjectWizardPanel(Binder<Project> binder, ProjectDAO projectDao)
	{
		projectNameTextField.setWidthFull();

		add(new H3("Start a New Project!"),
				GuiUtils.getSubtitleLabel("Projects organize your Pathmind Experiments based on your AnyLogic model"),
				GuiUtils.getHeightSpacer("40px"),
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
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		createProjectButton.setVisible(visible);
	}
}
