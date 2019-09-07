package io.skymind.pathmind.ui.views.project.components;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.ui.components.LabelFactory;
import io.skymind.pathmind.ui.constants.CssMindPathStyles;
import io.skymind.pathmind.ui.utils.UIConstants;
import io.skymind.pathmind.ui.utils.WrapperUtils;

public class NewProjectLogoPanel extends VerticalLayout
{
	public NewProjectLogoPanel()
	{
		add(WrapperUtils.wrapCenteredFormVertical(
				LabelFactory.createLabel("Welcome to", CssMindPathStyles.SMALL_LIGHT_LABEL),
				LabelFactory.createLabel("Pathmind", CssMindPathStyles.LOGO_LABEL)));

		setWidth(UIConstants.CENTERED_FORM_WIDTH);
	}
}
