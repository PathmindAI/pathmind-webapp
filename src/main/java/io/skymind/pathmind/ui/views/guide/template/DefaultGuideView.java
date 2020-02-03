package io.skymind.pathmind.ui.views.guide.template;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.constants.GuideStep;
import io.skymind.pathmind.db.dao.GuideDAO;
import io.skymind.pathmind.exception.InvalidDataException;
import io.skymind.pathmind.security.Routes;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.ui.views.PathMindDefaultView;
import io.skymind.pathmind.ui.views.guide.GuideMenu;

import org.springframework.beans.factory.annotation.Autowired;

@Route(value = Routes.GUIDE_URL, layout = MainLayout.class)
public class DefaultGuideView extends PathMindDefaultView implements HasUrlParameter<Long> {
	@Autowired
	private GuideDAO guideDAO;
	
	@Autowired
	protected SegmentIntegrator segmentIntegrator;

	private DefaultPageContent pageContent;

	protected long projectId;

	protected GuideStep guideStep;

	public DefaultGuideView(DefaultPageContent pageContent) {
		this.pageContent = pageContent;
	}

	@Override
	protected boolean isAccessAllowedForUser() {
		return true;
	}

	@Override
	protected Component getTitlePanel() {
		return new ScreenTitlePanel("PATHMIND GUIDE", "");
	}

	@Override
	protected Component getMainContent() {
		HorizontalLayout gridWrapper = WrapperUtils.wrapWidthFullBetweenHorizontal(
			new GuideMenu(guideStep, projectId), pageContent
        );
		gridWrapper.getStyle().set("background-color", "white");
		gridWrapper.getStyle().set("flex-grow", "1");
        return gridWrapper;
	}

	@Override
	protected void initLoadData() throws InvalidDataException {
		guideStep = guideDAO.getGuideStep(projectId);
	}

	@Override
	protected void initScreen(BeforeEnterEvent event) throws InvalidDataException {
		pageContent.initBtns(guideDAO, guideStep, projectId, segmentIntegrator);
	}

	@Override
	public void setParameter(BeforeEvent event, Long projectId) {
		this.projectId = projectId;
	}
}
