package io.skymind.pathmind.ui.views.guide;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.Route;

import io.skymind.pathmind.security.Routes;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.views.PathMindDefaultView;

@Route(value = Routes.GUIDE_URL, layout = MainLayout.class)
public class GuideOverview extends PathMindDefaultView {

	private final GuideOverviewContent guideOverviewContent;

	@Autowired
	public GuideOverview(GuideOverviewContent guideOverviewContent) {
		this.guideOverviewContent = guideOverviewContent;
	}

	@Override
	protected boolean isAccessAllowedForUser() {
		return true;
	}

	@Override
	protected Component getTitlePanel() {
		return new ScreenTitlePanel("PATHMIND GUIDE", "Overview");
	}

	@Override
	protected Component getMainContent() {
		return guideOverviewContent;
	}
}
