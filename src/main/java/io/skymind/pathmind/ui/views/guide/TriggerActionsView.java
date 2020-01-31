package io.skymind.pathmind.ui.views.guide;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.Route;

import io.skymind.pathmind.constants.GuideStep;
import io.skymind.pathmind.db.dao.GuideDAO;
import io.skymind.pathmind.security.Routes;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.ui.views.PathMindDefaultView;

@Route(value = Routes.GUIDE_TRIGGER_ACTIONS_URL, layout = MainLayout.class)
public class TriggerActionsView extends PathMindDefaultView {
	// may need to take projectId as parameter?

	@Autowired
	private GuideDAO guideDAO;

     private final TriggerActionsViewContent pageContent;

    @Autowired
    public TriggerActionsView(TriggerActionsViewContent pageContent) {
        this.pageContent = pageContent;
    }

    @Override
    protected boolean isAccessAllowedForUser() {
        return true;
    }

    @Override
    protected Component getTitlePanel() {
        return new ScreenTitlePanel("PATHMIND GUIDE", "Triggering Actions");
    }

    @Override
    protected Component getMainContent() {
		// Fake project
        long projectId = 3;
        GuideStep guideStep = guideDAO.getGuideStep(projectId);
        
        HorizontalLayout gridWrapper = WrapperUtils.wrapWidthFullBetweenHorizontal(
			new GuideMenu(guideStep), pageContent
        );
        gridWrapper.getStyle().set("background-color", "white");
		gridWrapper.getStyle().set("flex-grow", "1");
        return gridWrapper;
    }
}
