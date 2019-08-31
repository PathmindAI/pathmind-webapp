package io.skymind.pathmind.ui.views.project.components;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class ProjectStatusPanel extends HorizontalLayout
{
	private TextField observationCountTextField = new TextField("Observation Count");
	private TextField possibleActionsCountTextField = new TextField("Possible Actions Count");
	private TextField simulationStepCountTextField = new TextField("Simulation Step Count");
	private TextField stepSizeTextField = new TextField("Step Size");
	private TextField modelTimeUnitsTextField = new TextField("Model Time Units");

	public ProjectStatusPanel()
	{
		add(
				observationCountTextField,
				possibleActionsCountTextField,
				simulationStepCountTextField,
				stepSizeTextField,
				modelTimeUnitsTextField);

		setAlignItems(Alignment.END);
	}
}
