package io.skymind.pathmind.ui.binders;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.db.dao.ProjectDAO;
import io.skymind.pathmind.security.SecurityUtils;
import io.skymind.pathmind.ui.converter.TrimmedStringConverter;

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
