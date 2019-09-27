package io.skymind.pathmind.ui.components.buttons;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.utils.ExperimentUtils;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.ui.views.experiment.NewExperimentView;

public class NewExperimentButton extends Button
{
	// TODO -> ExperimentDAO should be injected.
	public NewExperimentButton(ExperimentDAO experimentDAO, long modelId) {
		this(experimentDAO, modelId, "todo rewardFunction");
	}

	// TODO -> Case #79 -> How do we get the name? Id number?
	//  https://github.com/SkymindIO/pathmind-webapp/issues/79
	// TODO -> Case #80 -> Do we use the same reward function from the experiment we're on as a default value?
	//  https://github.com/SkymindIO/pathmind-webapp/issues/80
	// TODO -> Case #71 -> Define exactly what last activity represents
	//  https://github.com/SkymindIO/pathmind-webapp/issues/71
	public NewExperimentButton(ExperimentDAO experimentDAO, long modelId, String rewardFunction)
	{
		super("New Experiment");
		setIcon(new Icon(VaadinIcon.PLUS));

		addClickListener(click -> {
			Experiment newExperiment = ExperimentUtils.generateNewDefaultExperiment(modelId, "Todo Experiment", rewardFunction);
			long newExperimentId = experimentDAO.setupNewExperiment(newExperiment);
			UI.getCurrent().navigate(NewExperimentView.class, newExperimentId);
		});
	}
}
