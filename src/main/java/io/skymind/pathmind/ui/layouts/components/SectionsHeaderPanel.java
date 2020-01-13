package io.skymind.pathmind.ui.layouts.components;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouterLink;

import io.skymind.pathmind.ui.views.dashboard.DashboardView;
import io.skymind.pathmind.ui.views.project.ProjectsView;

public class SectionsHeaderPanel extends HorizontalLayout
{
	public SectionsHeaderPanel(boolean hasLoginUser)
	{
		HorizontalLayout sectionsHorizontalLayout = new HorizontalLayout();
		final Image logo = new Image("frontend/images/pathmind-logo.png", "Skymind Logo");
		logo.addClassName("navbar-logo");
		sectionsHorizontalLayout.add(logo);
		if (hasLoginUser) {
			sectionsHorizontalLayout.add(
					new RouterLink("Dashboard", DashboardView.class),
					new RouterLink("Projects", ProjectsView.class),
					getLearnAnchor());
		}
		add(sectionsHorizontalLayout);

		sectionsHorizontalLayout.setId("nav-main-links");
	}

	private Anchor getLearnAnchor() {
		Anchor anchor = new Anchor("https://help.pathmind.com/", "Learn");
		anchor.setTarget("_blank");
		return anchor;
	}
}
