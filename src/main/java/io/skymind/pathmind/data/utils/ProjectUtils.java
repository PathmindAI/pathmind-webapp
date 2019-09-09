package io.skymind.pathmind.data.utils;

import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.security.SecurityUtils;

public class ProjectUtils
{
	private ProjectUtils() {
	}

	// TODO -> Correctly implement the default values for a new Project.
	public static Project generateNewDefaultProject() {
		Project project = new Project();
		project.setPathmindUser(SecurityUtils.getUser());
		project.setExperiments(ExperimentUtils.generateNewDefaultExpirementList(project));
		return project;
	}
}
