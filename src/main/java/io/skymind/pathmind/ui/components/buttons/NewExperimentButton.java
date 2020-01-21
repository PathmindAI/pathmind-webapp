package io.skymind.pathmind.ui.components.buttons;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.utils.ExperimentUtils;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.ui.views.experiment.NewExperimentView;

public class NewExperimentButton extends Button
{
	public NewExperimentButton(ExperimentDAO experimentDAO, long modelId) {
		this(experimentDAO, modelId, "reward = after[0] - before[0];");
	}

	public NewExperimentButton(ExperimentDAO experimentDAO, long modelId, String defaultRewardFunction)
	{
		super("New Experiment");
		setIcon(new Icon(VaadinIcon.PLUS));

        addClickListener(evt -> {
        	String experimentName = Integer.toString (experimentDAO.getExperimentCount(modelId) + 1);
        	Experiment lastExperiment = experimentDAO.getLastExperimentForModel(modelId);
        	String rewardFunction = lastExperiment != null ? lastExperiment.getRewardFunction() : defaultRewardFunction; 
        	Experiment newExperiment = ExperimentUtils.generateNewDefaultExperiment(modelId, experimentName, rewardFunction);
        	long newExperimentId = experimentDAO.setupNewExperiment(newExperiment);
        	UI.getCurrent().navigate(NewExperimentView.class, newExperimentId);
        });
			
		addThemeVariants(ButtonVariant.LUMO_PRIMARY);
	}
}
