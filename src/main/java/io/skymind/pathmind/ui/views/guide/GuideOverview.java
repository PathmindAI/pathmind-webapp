package io.skymind.pathmind.ui.views.guide;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.security.Routes;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.views.guide.template.DefaultGuideView;

@Route(value = Routes.GUIDE_URL, layout = MainLayout.class)
public class GuideOverview extends DefaultGuideView {
	
	private final static GuideOverviewContent pageContent = new GuideOverviewContent();

	public GuideOverview() {
		super(pageContent);
	}

	@Override
	protected Component getTitlePanel() {
		return new ScreenTitlePanel("PATHMIND GUIDE", "Overview");
	}
}
