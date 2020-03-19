package io.skymind.pathmind.webapp.ui.views.guide;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.webapp.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.views.guide.template.DefaultGuideView;
import io.skymind.pathmind.webapp.ui.views.guide.template.DefaultPageContent;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = Routes.GUIDE_INSTALL_URL, layout = MainLayout.class)
public class InstallPathmindHelperView extends DefaultGuideView {

	private InstallPathmindHelperViewContent pageContent;

	@Autowired
	public InstallPathmindHelperView(InstallPathmindHelperViewContent pageContent) {
		super();
		this.pageContent = pageContent;
	}

	@Override
	protected DefaultPageContent initPageContent() {
		return pageContent;
	}

	@Override
	protected Component getTitlePanel() {
		return new ScreenTitlePanel("PATHMIND GUIDE", "Install Pathmind Helper");
	}
}
