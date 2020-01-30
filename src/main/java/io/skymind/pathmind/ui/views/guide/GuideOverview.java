package io.skymind.pathmind.ui.views.guide;

import io.skymind.pathmind.bus.events.UserUpdateBusEvent;
import io.skymind.pathmind.db.dao.UserDAO;
import io.skymind.pathmind.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.Route;

import io.skymind.pathmind.security.Routes;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.ui.views.PathMindDefaultView;

@Route(value = Routes.GUIDE_URL, layout = MainLayout.class)
public class GuideOverview extends PathMindDefaultView {
	// may need to take projectId as parameter?

	private final GuideMenu guideMenu;
	private final GuideOverviewContent pageContent;

	@Autowired
	public GuideOverview(GuideMenu guideMenu, GuideOverviewContent pageContent) {
		this.guideMenu = guideMenu;
		this.pageContent = pageContent;
	}

	@Override
	protected boolean isAccessAllowedForUser() {
		return true; // need to check for project
	}

	@Override
	protected Component getTitlePanel() {
		return new ScreenTitlePanel("PATHMIND GUIDE", "Overview");
	}

	@Override
	protected Component getMainContent() {
        HorizontalLayout gridWrapper = WrapperUtils.wrapWidthFullBetweenHorizontal(
			guideMenu, pageContent
        );
		gridWrapper.getStyle().set("background-color", "white");
		gridWrapper.getStyle().set("flex-grow", "1");
        return gridWrapper;
	}
}
