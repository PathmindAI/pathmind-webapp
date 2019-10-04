package io.skymind.pathmind.ui.views.project.filter;

import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.ui.components.PathmindFilterInterface;
import io.skymind.pathmind.utils.SearchUtils;

public class ProjectFilter implements PathmindFilterInterface<Project>
{
	@Override
	public boolean isMatch(Project project, String searchValue) {
		return SearchUtils.contains(project.getName(), searchValue) ||
				SearchUtils.contains(project.getDateCreated(), searchValue) ||
				SearchUtils.contains(project.getLastActivityDate(), searchValue);
	}
}
