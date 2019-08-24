package io.skymind.pathmind.views;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("dashboard")
public class DashboardView extends VerticalLayout
{
	public DashboardView()
	{
		add(new Label("Dashboard"));
	}
}
