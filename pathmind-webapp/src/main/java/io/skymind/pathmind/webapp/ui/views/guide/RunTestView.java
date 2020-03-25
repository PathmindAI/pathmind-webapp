package io.skymind.pathmind.webapp.ui.views.guide;

import com.vaadin.flow.router.Route;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.views.guide.template.DefaultGuideView;
import io.skymind.pathmind.webapp.ui.views.guide.template.DefaultPageContent;

@Route(value = Routes.GUIDE_RUN_TEST_URL, layout = MainLayout.class)
public class RunTestView extends DefaultGuideView {

	private RunTestViewContent pageContent;

	public RunTestView(RunTestViewContent pageContent) {
		super();
		this.pageContent = pageContent;
	}

	@Override
	protected DefaultPageContent initPageContent() {
		return pageContent;
	}
}
