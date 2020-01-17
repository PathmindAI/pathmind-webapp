package io.skymind.pathmind.ui.views.experiment.utils;

import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Policy;

public class ExperimentViewNavigationUtils
{
	/**
	 * Temporary solution until Vaadin adds the ability to parse multiple parameters
	 * into the view: https://github.com/vaadin/flow/issues/4213
	 */
	public static String getExperimentParameters(Policy policy) {
		return policy.getExperiment().getId() + "/" + policy.getId();
	}

	public static String getExperimentParameters(Experiment experiment) {
		return Long.toString(experiment.getId());
	}
}
