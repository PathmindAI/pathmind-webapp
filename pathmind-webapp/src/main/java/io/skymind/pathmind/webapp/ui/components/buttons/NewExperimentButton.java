package io.skymind.pathmind.webapp.ui.components.buttons;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;

public class NewExperimentButton extends Button
{
	public NewExperimentButton(ExperimentDAO experimentDAO, long modelId)
	{
		super("New Experiment");
		setIcon(new Icon(VaadinIcon.PLUS));

        addClickListener(evt -> getUI().ifPresent(ui -> ExperimentUtils.createAndNavigateToNewExperiment(ui, experimentDAO, modelId)));
			
		addThemeVariants(ButtonVariant.LUMO_PRIMARY);
	}
}
