package io.skymind.pathmind.ui.views.guide;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.Route;

import io.skymind.pathmind.security.Routes;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.ui.views.PathMindDefaultView;

@Route(value = Routes.GUIDE_RECAP_URL, layout = MainLayout.class)
public class RecapView extends PathMindDefaultView {

     private final GuideMenu guideMenu;
     private final RecapViewContent pageContent;

    @Autowired
    public RecapView(GuideMenu guideMenu, RecapViewContent pageContent) {
        this.guideMenu = guideMenu;
        this.pageContent = pageContent;
    }

    @Override
    protected boolean isAccessAllowedForUser() {
        return true;
    }

    @Override
    protected Component getTitlePanel() {
        return new ScreenTitlePanel("PATHMIND GUIDE", "Conclusion");
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
