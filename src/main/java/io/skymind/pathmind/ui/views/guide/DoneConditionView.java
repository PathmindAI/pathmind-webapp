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

@Route(value = Routes.GUIDE_DONE_URL, layout = MainLayout.class)
public class DoneConditionView extends PathMindDefaultView {

     private final GuideMenu guideMenu;
     private final DoneConditionViewContent pageContent;

    @Autowired
    public DoneConditionView(GuideMenu guideMenu, DoneConditionViewContent pageContent) {
        this.guideMenu = guideMenu;
        this.pageContent = pageContent;
    }

    @Override
    protected boolean isAccessAllowedForUser() {
        return true;
    }

    @Override
    protected Component getTitlePanel() {
        return new ScreenTitlePanel("PATHMIND GUIDE", "Define \"Done\" Condition");
    }

    @Override
    protected Component getMainContent() {
        Div testDiv = new Div();
        HorizontalLayout gridWrapper = WrapperUtils.wrapWidthFullBetweenHorizontal(
			guideMenu, pageContent
        );
        gridWrapper.getStyle().set("background-color", "white");
        return gridWrapper;
    }
}
