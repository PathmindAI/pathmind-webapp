package io.skymind.pathmind.webapp.ui.views.experiment.filter;

import io.skymind.pathmind.db.data.Experiment;
import io.skymind.pathmind.webapp.ui.components.PathmindFilterInterface;
import io.skymind.pathmind.webapp.utils.SearchUtils;

public class ExperimentFilter implements PathmindFilterInterface<Experiment>
{
	@Override
	public boolean isMatch(Experiment experiment, String searchValue) {
		return SearchUtils.contains(experiment.getName(), searchValue) ||
				SearchUtils.contains(experiment.getLastActivityDate(), searchValue); // ||
//				SearchUtils.contains(experiment.getDiscoveryRun(), searchValue) ||
//				SearchUtils.contains(experiment.getFullRun(), searchValue) ||
//				SearchUtils.contains(experiment.getArchive(), searchValue) ||
//				SearchUtils.contains(experiment.getNotes(), searchValue);
	}
}
