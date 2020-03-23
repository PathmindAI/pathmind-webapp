package io.skymind.pathmind.webapp.ui.layouts.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouterLink;

import io.skymind.pathmind.webapp.security.Feature;
import io.skymind.pathmind.webapp.security.FeatureManager;
import io.skymind.pathmind.webapp.ui.views.dashboard.DashboardView;
import io.skymind.pathmind.webapp.ui.views.project.ProjectsView;
import io.skymind.pathmind.webapp.ui.views.testfeature.TestFeatureView;

public class SectionsHeaderPanel extends HorizontalLayout
{
	public SectionsHeaderPanel(boolean hasLoginUser, FeatureManager featureManager)
	{
		HorizontalLayout sectionsHorizontalLayout = new HorizontalLayout();
		sectionsHorizontalLayout.add(linkedLogo());
		if (hasLoginUser) {
			List<Component> components = new ArrayList<>(Arrays.asList(
					new RouterLink("Dashboard", DashboardView.class),
					new RouterLink("Projects", ProjectsView.class)
			));
			if (featureManager.isEnabled(Feature.TEST_FEATURE)) {
				components.add(new RouterLink("Test feature", TestFeatureView.class));
			}
			components.add(getLearnAnchor());
			sectionsHorizontalLayout.add(components.toArray(new Component[0]));
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
