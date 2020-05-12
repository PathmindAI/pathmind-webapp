package io.skymind.pathmind.webapp.ui.layouts.components;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouterLink;

import io.skymind.pathmind.shared.featureflag.FeatureManager;
import io.skymind.pathmind.webapp.ui.views.dashboard.DashboardView;
import io.skymind.pathmind.webapp.ui.views.project.ProjectsView;

public class SectionsHeaderPanel extends HorizontalLayout
{
	public SectionsHeaderPanel(boolean hasLoginUser)
	{
		HorizontalLayout sectionsHorizontalLayout = new HorizontalLayout();
		sectionsHorizontalLayout.add(linkedLogo());
		if (hasLoginUser) {
			RouterLink dashboardLink = new RouterLink("Dashboard", DashboardView.class);
			RouterLink projectsLink = new RouterLink("Projects", ProjectsView.class);
			projectsLink.setHighlightCondition((link, event) -> {
				return event.getLocation().getPath().startsWith("projects") ||
						event.getLocation().getPath().startsWith("project") ||
						event.getLocation().getPath().startsWith("model") ||
						event.getLocation().getPath().startsWith("experiment");
				}
			);

			sectionsHorizontalLayout.add(
					dashboardLink,
					projectsLink,
					getLearnAnchor());
		}
		add(sectionsHorizontalLayout);

		sectionsHorizontalLayout.setId("nav-main-links");
	}

	private Anchor linkedLogo() {
		Image logo = new Image("frontend/images/pathmind-logo.png", "Pathmind Logo");
		Anchor anchor = new Anchor("/", logo);
		anchor.addClassName("navbar-logo");
		return anchor;
	}

	private Anchor getLearnAnchor() {
		Anchor anchor = new Anchor("https://help.pathmind.com/", "Learn");
		anchor.setTarget("_blank");
		return anchor;
	}
}
