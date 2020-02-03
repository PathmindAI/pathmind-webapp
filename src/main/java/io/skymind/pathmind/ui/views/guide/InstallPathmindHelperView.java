package io.skymind.pathmind.ui.views.guide;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.security.Routes;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.views.guide.template.DefaultGuideView;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = Routes.GUIDE_INSTALL_URL, layout = MainLayout.class)
public class InstallPathmindHelperView extends DefaultGuideView {
	
	private final static InstallPathmindHelperViewContent pageContent = new InstallPathmindHelperViewContent();

	@Autowired
	public InstallPathmindHelperView() {
		super(pageContent);
	}

	@Override
	protected Component getTitlePanel() {
		return new ScreenTitlePanel("PATHMIND GUIDE", "Install Pathmind Helper");
	}
}
