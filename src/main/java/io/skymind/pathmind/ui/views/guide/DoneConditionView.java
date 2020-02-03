package io.skymind.pathmind.ui.views.guide;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.security.Routes;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.views.guide.template.DefaultGuideView;
import io.skymind.pathmind.ui.views.guide.template.DefaultPageContent;

@Route(value = Routes.GUIDE_DONE_URL, layout = MainLayout.class)
public class DoneConditionView extends DefaultGuideView {

	private GuideOverviewContent pageContent;

	public DoneConditionView(GuideOverviewContent pageContent) {
		super();
		this.pageContent = pageContent;
	}

	@Override
	protected DefaultPageContent initPageContent() {
		return pageContent;
	}

	@Override
	protected Component getTitlePanel() {
		return new ScreenTitlePanel("PATHMIND GUIDE", "Define \"Done\" Condition");
	}
}
