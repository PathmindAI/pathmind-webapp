package io.skymind.pathmind.ui.views.experiment.utils;

import com.vaadin.flow.component.UI;

import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.utils.ExperimentUtils;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.ui.views.experiment.NewExperimentView;

public class ExperimentViewNavigationUtils
{
	private static String DEFAULT_REWARD_FUNNCTION = "reward = after[0] - before[0];";
	
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
	
	public static void createAndNavigateToNewExperiment(ExperimentDAO experimentDAO, long modelId) {
		String experimentName = Integer.toString (experimentDAO.getExperimentCount(modelId) + 1);
		Experiment lastExperiment = experimentDAO.getLastExperimentForModel(modelId);
		String rewardFunction = lastExperiment != null ? lastExperiment.getRewardFunction() : DEFAULT_REWARD_FUNNCTION; 
		Experiment newExperiment = ExperimentUtils.generateNewDefaultExperiment(modelId, experimentName, rewardFunction);
		long newExperimentId = experimentDAO.setupNewExperiment(newExperiment);
		UI.getCurrent().navigate(NewExperimentView.class, newExperimentId);
	}
}
