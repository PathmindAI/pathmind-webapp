package io.skymind.pathmind.webapp.data.utils;

import io.skymind.pathmind.db.data.Project;
import io.skymind.pathmind.shared.mock.MockDefaultValues;
import io.skymind.pathmind.shared.security.SecurityUtils;

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
