package io.skymind.pathmind.webapp.ui.views.model.filter;

import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.webapp.ui.components.PathmindFilterInterface;
import io.skymind.pathmind.webapp.utils.SearchUtils;

public class ModelFilter implements PathmindFilterInterface<Model>
{
	@Override
	public boolean isMatch(Model model, String searchValue) {
		return SearchUtils.contains(model.getName(), searchValue) ||
				SearchUtils.contains(model.getDateCreated(), searchValue) ||
				SearchUtils.contains(model.getLastActivityDate(), searchValue);
	}
}
