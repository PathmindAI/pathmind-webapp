package io.skymind.pathmind.ui.views.project.components.panels;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.ui.components.LabelFactory;
import io.skymind.pathmind.ui.constants.CssMindPathStyles;

public class NewProjectLogoWizardPanel extends VerticalLayout
{
	public NewProjectLogoWizardPanel()
	{
		setSpacing(false);
		Image pathMindLogo = new Image("frontend/images/pathmind-logo.png", "Pathmind Logo");
		pathMindLogo.addClassName("navbar-logo");
		add(LabelFactory.createLabel("Welcome to", CssMindPathStyles.SMALL_LIGHT_LABEL),
				pathMindLogo);

		setDefaultHorizontalComponentAlignment(Alignment.CENTER);
		setWidthFull();
	}
}
