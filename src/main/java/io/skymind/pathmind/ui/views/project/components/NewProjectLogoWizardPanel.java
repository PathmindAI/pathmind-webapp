package io.skymind.pathmind.ui.views.project.components;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.ui.components.LabelFactory;
import io.skymind.pathmind.ui.constants.CssMindPathStyles;

public class NewProjectLogoWizardPanel extends VerticalLayout
{
	public NewProjectLogoWizardPanel()
	{
		add(LabelFactory.createLabel("Welcome to", CssMindPathStyles.SMALL_LIGHT_LABEL),
				LabelFactory.createLabel("Pathmind", CssMindPathStyles.LOGO_LABEL));

		setDefaultHorizontalComponentAlignment(Alignment.CENTER);
		setWidthFull();
	}
}
