package io.skymind.pathmind.ui.components.buttons;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.ui.utils.ExceptionWrapperUtils;
import io.skymind.pathmind.ui.views.experiment.utils.ExperimentViewNavigationUtils;

public class NewExperimentButton extends Button
{
	public NewExperimentButton(ExperimentDAO experimentDAO, long modelId) {
		super("New Experiment");
		setIcon(new Icon(VaadinIcon.PLUS));
		
		addClickListener(click ->
		ExceptionWrapperUtils.handleButtonClicked(() -> ExperimentViewNavigationUtils.createAndNavigateToNewExperiment(experimentDAO, modelId)));
		addThemeVariants(ButtonVariant.LUMO_PRIMARY);
	}

}
