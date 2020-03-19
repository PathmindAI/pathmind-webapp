package io.skymind.pathmind.webapp.ui.views.guide;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.webapp.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.views.guide.template.DefaultGuideView;
import io.skymind.pathmind.webapp.ui.views.guide.template.DefaultPageContent;

@Route(value = Routes.GUIDE_URL, layout = MainLayout.class)
public class GuideOverview extends DefaultGuideView {
	
	private GuideOverviewContent pageContent;

	public GuideOverview(GuideOverviewContent pageContent) {
		super();
		this.pageContent = pageContent;
	}

	@Override
	protected DefaultPageContent initPageContent() {
		return pageContent;
	}

	@Override
	protected Component getTitlePanel() {
		return new ScreenTitlePanel("PATHMIND GUIDE", "Overview");
	}
}
