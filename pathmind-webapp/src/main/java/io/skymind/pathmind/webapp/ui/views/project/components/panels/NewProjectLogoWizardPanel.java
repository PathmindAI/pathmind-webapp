package io.skymind.pathmind.webapp.ui.views.project.components.panels;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.skymind.pathmind.webapp.ui.components.PathmindGreetingComponent;

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
