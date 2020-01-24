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

@Route(value = Routes.GUIDE_OBSERVATION_URL, layout = MainLayout.class)
public class ObservationView extends PathMindDefaultView {

     private final GuideMenu guideMenu;
     private final ObservationViewContent pageContent;

    @Autowired
    public ObservationView(GuideMenu guideMenu, ObservationViewContent pageContent) {
        this.guideMenu = guideMenu;
        this.pageContent = pageContent;
    }

    @Override
    protected boolean isAccessAllowedForUser() {
        return true;
    }

    @Override
    protected Component getTitlePanel() {
        return new ScreenTitlePanel("PATHMIND GUIDE", "Build Observation Space");
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
