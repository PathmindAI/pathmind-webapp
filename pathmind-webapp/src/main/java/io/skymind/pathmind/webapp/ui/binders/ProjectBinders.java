package io.skymind.pathmind.webapp.ui.binders;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

import com.vaadin.flow.data.validator.StringLengthValidator;
import io.skymind.pathmind.shared.data.Project;
import io.skymind.pathmind.db.dao.ProjectDAO;
import io.skymind.pathmind.shared.security.SecurityUtils;
import io.skymind.pathmind.webapp.ui.converter.TrimmedStringConverter;

public class ProjectBinders
{
	public static void bindProjectName(Binder<Project> binder, ProjectDAO projectDao, TextField projectNameTextField) {
		bindProjectName(binder, projectDao, projectNameTextField, -1);
	}
	
	public static void bindProjectName(Binder<Project> binder, ProjectDAO projectDao, TextField projectNameTextField, long projectId){
		binder.forField(projectNameTextField)
				.withConverter(new TrimmedStringConverter())
				.asRequired("Project must have a name")
				.withValidator(projectName -> isProjectNameUniqueForUser(projectName, projectId, projectDao), "Project name should be unique")
				.withValidator(new StringLengthValidator("Project name must not exceed 100 characters.", 0, 100))
				.bind(Project::getName, Project::setName);
	}

	private static boolean isProjectNameUniqueForUser(String name, long projectId, ProjectDAO projectDao) {
		return !projectDao.getProjectsForUser(SecurityUtils.getUserId()).stream()
				.filter(p -> p.getId() != projectId)
				.anyMatch(p -> p.getName().equalsIgnoreCase(name));
	}
}
