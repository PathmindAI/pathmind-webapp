package io.skymind.pathmind.ui.views;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.ui.layouts.MainLayout;

@Route(value="dashboard", layout = MainLayout.class)
public class DashboardView extends VerticalLayout
{
	public DashboardView()
	{
		add(new Label("Dashboard"));
	}
}
