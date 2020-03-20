package io.skymind.pathmind.webapp.ui.components.buttons;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.webapp.ui.views.experiment.utils.ExperimentViewNavigationUtils;

public class NewExperimentButton extends Button
{
	public NewExperimentButton(ExperimentDAO experimentDAO, long modelId) {
		this(experimentDAO, modelId, "reward = after[0] - before[0];");
	}

	public NewExperimentButton(ExperimentDAO experimentDAO, long modelId, String defaultRewardFunction)
	{
		super("New Experiment");
		setIcon(new Icon(VaadinIcon.PLUS));

        addClickListener(evt -> ExperimentViewNavigationUtils.createAndNavigateToNewExperiment(UI.getCurrent(), experimentDAO, modelId, defaultRewardFunction));
			
		addThemeVariants(ButtonVariant.LUMO_PRIMARY);
	}
}
