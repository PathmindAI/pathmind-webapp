package io.skymind.pathmind.webapp.ui.views.project.filter;

import io.skymind.pathmind.db.data.Project;
import io.skymind.pathmind.webapp.ui.components.PathmindFilterInterface;
import io.skymind.pathmind.webapp.utils.SearchUtils;

public class ProjectFilter implements PathmindFilterInterface<Project>
{
	@Override
	public boolean isMatch(Project project, String searchValue) {
		return SearchUtils.contains(project.getName(), searchValue) ||
				SearchUtils.contains(project.getDateCreated(), searchValue) ||
				SearchUtils.contains(project.getLastActivityDate(), searchValue);
	}
}
