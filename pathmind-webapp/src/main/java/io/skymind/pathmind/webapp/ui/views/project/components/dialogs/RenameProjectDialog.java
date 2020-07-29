package io.skymind.pathmind.webapp.ui.views.project.components.dialogs;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.function.SerializableConsumer;

import io.skymind.pathmind.db.dao.ProjectDAO;
import io.skymind.pathmind.shared.data.Project;
import io.skymind.pathmind.webapp.ui.binders.ProjectBinders;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles;

public class RenameProjectDialog extends Dialog {
	private TextField projectName;
	private Button rename;
	private Button cancel;

	private Binder<Project> binder;

	public RenameProjectDialog(Project project, ProjectDAO projectDao, SerializableConsumer<String> updatedProjectNameConsumer) {
		binder = new Binder<>();
		projectName = new TextField("Project name");

		rename = new Button("Rename Project", evt -> updateProjectName(projectDao, updatedProjectNameConsumer));
		rename.setDisableOnClick(true);
		rename.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		rename.addClickShortcut(Key.ENTER);

		cancel = new Button("Cancel", evt -> close());
		HorizontalLayout footer = new HorizontalLayout(cancel, rename);
		footer.setClassName("dialog-footer");

		add(LabelFactory.createLabel("Rename project", CssPathmindStyles.SECTION_TITLE_LABEL), projectName, footer);

		projectName.focus();
		ProjectBinders.bindProjectName(binder, projectDao, projectName, project.getId());
		binder.setBean(project);
	}

	private void updateProjectName(ProjectDAO projectDao, SerializableConsumer<String> updatedProjectNameConsumer) {
		if (binder.validate().isOk()) {
			Project project = binder.getBean();
			projectDao.updateProjectName(project.getId(), project.getName());
			updatedProjectNameConsumer.accept(project.getName());
			close();
		} else {
			rename.setEnabled(true);
		}
	}
}
