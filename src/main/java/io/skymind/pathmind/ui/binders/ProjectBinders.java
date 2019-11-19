package io.skymind.pathmind.ui.binders;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.db.dao.ProjectDAO;
import io.skymind.pathmind.security.SecurityUtils;
import io.skymind.pathmind.utils.SpringUtils;

public class ProjectBinders
{
	public static void bindProjectName(Binder<Project> binder, TextField projectNameTextField)
	{
		binder.forField(projectNameTextField)
				.asRequired("Project must have a name")
				.withValidator(projectName -> isUniqueForUser(projectName), "Project name should be unique")
				.bind(Project::getName, Project::setName);
	}

	private static boolean isUniqueForUser(String name) {
		ProjectDAO projectDao = SpringUtils.getBean(ProjectDAO.class);
		return !projectDao.getProjectsForUser(SecurityUtils.getUserId()).stream().anyMatch(p -> p.getName().equalsIgnoreCase(name));
	}
}
