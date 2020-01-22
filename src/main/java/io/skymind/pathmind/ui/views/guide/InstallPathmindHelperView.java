package io.skymind.pathmind.ui.views.guide;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

import io.skymind.pathmind.security.Routes;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.ui.views.PathMindDefaultView;

@Route(value = Routes.GUIDE_INSTALL_URL, layout = MainLayout.class)
public class InstallPathmindHelperView extends PathMindDefaultView {

	 private final GuideMenu guideMenu;

    @Autowired
    public InstallPathmindHelperView(GuideMenu guideMenu) {
		this.guideMenu = guideMenu;
    }

    @Override
    protected boolean isAccessAllowedForUser() {
        return true;
    }

    @Override
    protected Component getTitlePanel() {
        return new ScreenTitlePanel("PATHMIND GUIDE", "Install Pathmind Helper");
    }

    @Override
    protected Component getMainContent() {
        Div testDiv = new Div();
        testDiv.setText("dummy content page side");
        HorizontalLayout gridWrapper = WrapperUtils.wrapWidthFullBetweenHorizontal(
			guideMenu, testDiv
        );

        return gridWrapper;
    }
}
