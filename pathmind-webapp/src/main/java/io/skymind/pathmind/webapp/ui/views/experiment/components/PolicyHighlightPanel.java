package io.skymind.pathmind.webapp.ui.views.experiment.components;

import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class PolicyHighlightPanel extends VerticalLayout
{
	public PolicyHighlightPanel()
	{
		setWidthFull();
		setPadding(false);
		setVisible(false);
		setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
	}
}