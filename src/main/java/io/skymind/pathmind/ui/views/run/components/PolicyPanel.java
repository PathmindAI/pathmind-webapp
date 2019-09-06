package io.skymind.pathmind.ui.views.run.components;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class PolicyPanel extends VerticalLayout
{
	public PolicyPanel()
	{
		setSizeFull();
		add(new Label("Are these Panels, tables, etc.?"));
	}
}
