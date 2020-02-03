package io.skymind.pathmind.ui.views.project.components.panels;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.skymind.pathmind.ui.components.PathmindGreetingComponent;

public class NewProjectLogoWizardPanel extends VerticalLayout
{
	public NewProjectLogoWizardPanel()
	{
		setSpacing(false);
		add(new PathmindGreetingComponent());

		setDefaultHorizontalComponentAlignment(Alignment.CENTER);
		setWidthFull();
	}
}
