package io.skymind.pathmind.data.utils;

import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.mock.MockDefaultValues;
import io.skymind.pathmind.security.SecurityUtils;

import java.time.LocalDateTime;

public class ProjectUtils
{
	private ProjectUtils() {
	}

	public static Project generateNewDefaultProject() {
		Project project = new Project();
		if(MockDefaultValues.isDebugAccelerate())
			project.setName(MockDefaultValues.getProjectName());
		project.setDateCreated(LocalDateTime.now());
		project.setLastActivityDate(LocalDateTime.now());
		project.setPathmindUserId(SecurityUtils.getUserId());
		return project;
	}
}
