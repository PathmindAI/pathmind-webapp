package io.skymind.pathmind.webapp.ui.binders;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

import io.skymind.pathmind.db.data.Project;
import io.skymind.pathmind.db.dao.ProjectDAO;
import io.skymind.pathmind.shared.security.SecurityUtils;
import io.skymind.pathmind.webapp.ui.converter.TrimmedStringConverter;

public class ProjectBinders
{
	public static void bindProjectName(Binder<Project> binder, ProjectDAO projectDao, TextField projectNameTextField)
	{
		binder.forField(projectNameTextField)
				.withConverter(new TrimmedStringConverter())
				.asRequired("Project must have a name")
				.withValidator(projectName -> isUniqueForUser(projectDao, projectName), "Project name should be unique")
				.bind(Project::getName, Project::setName);
	}

	private static boolean isUniqueForUser(ProjectDAO projectDao, String name) {
		return !projectDao.getProjectsForUser(SecurityUtils.getUserId()).stream().anyMatch(p -> p.getName().equalsIgnoreCase(name));
	}
}
