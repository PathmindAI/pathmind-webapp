package io.skymind.pathmind.ui.views.experiment.utils;

import com.vaadin.flow.component.UI;

import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.utils.ExperimentUtils;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.ui.views.experiment.NewExperimentView;

public class ExperimentViewNavigationUtils
{
	public static void createAndNavigateToNewExperiment(UI ui, ExperimentDAO experimentDAO, long modelId, String defaultRewardFunction) {
		String experimentName = Integer.toString (experimentDAO.getExperimentCount(modelId) + 1);
    	Experiment lastExperiment = experimentDAO.getLastExperimentForModel(modelId);
    	String rewardFunction = lastExperiment != null ? lastExperiment.getRewardFunction() : defaultRewardFunction; 
    	Experiment newExperiment = ExperimentUtils.generateNewDefaultExperiment(modelId, experimentName, rewardFunction);
    	long newExperimentId = experimentDAO.setupNewExperiment(newExperiment);
    	ui.navigate(NewExperimentView.class, newExperimentId);
	}
}
