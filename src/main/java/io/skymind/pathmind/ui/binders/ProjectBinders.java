package io.skymind.pathmind.ui.binders;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import io.skymind.pathmind.data.Project;

public class ProjectBinders
{
	public static void bindProjectName(Binder<Project> binder, TextField projectNameTextField)
	{
		binder.forField(projectNameTextField)
				.asRequired("Project must have a name")
				.bind(Project::getName, Project::setName);
	}
}
