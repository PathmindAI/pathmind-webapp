package io.skymind.pathmind.ui.layouts.components;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouterLink;
import io.skymind.pathmind.ui.views.DashboardView;
import io.skymind.pathmind.ui.views.LearnView;
import io.skymind.pathmind.ui.views.project.ProjectsView;

public class SectionsHeaderPanel extends HorizontalLayout
{
	public SectionsHeaderPanel()
	{
		HorizontalLayout sectionsHorizontalLayout = new HorizontalLayout();
		sectionsHorizontalLayout.add(
				new Label("Logo"),
				new RouterLink("Dashboard", DashboardView.class),
				new RouterLink("Projects", ProjectsView.class),
				new RouterLink("Learn", LearnView.class));
		add(sectionsHorizontalLayout);

		getElement().getStyle().set("padding-left", "20px");
	}
}
