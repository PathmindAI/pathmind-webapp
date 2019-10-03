package io.skymind.pathmind.ui.layouts.components;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouterLink;
import io.skymind.pathmind.ui.views.dashboard.DashboardView;
import io.skymind.pathmind.ui.views.project.ProjectsView;

public class SectionsHeaderPanel extends HorizontalLayout
{
	public SectionsHeaderPanel()
	{
		HorizontalLayout sectionsHorizontalLayout = new HorizontalLayout();
		final Image logo = new Image("frontend/images/pathmind-logo.png", "Skymind Logo");
		logo.addClassName("navbar-logo");
		sectionsHorizontalLayout.add(
				logo,
				new RouterLink("Dashboard", DashboardView.class),
				new RouterLink("Projects", ProjectsView.class),
				new Anchor("https://help.pathmind.com/", "Learn"));
		add(sectionsHorizontalLayout);

		sectionsHorizontalLayout.setId("nav-main-links");
	}
}
