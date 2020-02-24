package io.skymind.pathmind.ui.views.experiment.utils;

import com.vaadin.flow.component.UI;

import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.utils.ExperimentUtils;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.ui.views.experiment.NewExperimentView;

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
	
	public static void createAndNavigateToNewExperiment(UI ui, ExperimentDAO experimentDAO, long modelId, String defaultRewardFunction) {
		String experimentName = Integer.toString (experimentDAO.getExperimentCount(modelId) + 1);
    	Experiment lastExperiment = experimentDAO.getLastExperimentForModel(modelId);
    	String rewardFunction = lastExperiment != null ? lastExperiment.getRewardFunction() : defaultRewardFunction; 
    	Experiment newExperiment = ExperimentUtils.generateNewDefaultExperiment(modelId, experimentName, rewardFunction);
    	long newExperimentId = experimentDAO.setupNewExperiment(newExperiment);
    	ui.navigate(NewExperimentView.class, newExperimentId);
	}
}
