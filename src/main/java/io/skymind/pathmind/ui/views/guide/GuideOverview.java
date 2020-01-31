package io.skymind.pathmind.ui.views.guide;

import io.skymind.pathmind.bus.events.UserUpdateBusEvent;
import io.skymind.pathmind.db.dao.UserDAO;
import io.skymind.pathmind.exception.InvalidDataException;
import io.skymind.pathmind.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;

import io.skymind.pathmind.constants.GuideStep;
import io.skymind.pathmind.db.dao.GuideDAO;
import io.skymind.pathmind.security.Routes;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.ui.views.PathMindDefaultView;

@Route(value = Routes.GUIDE_URL, layout = MainLayout.class)
public class GuideOverview extends PathMindDefaultView implements HasUrlParameter<Long> {
	@Autowired
	private GuideDAO guideDAO;

	private final GuideOverviewContent pageContent;

	private long projectId;

	private GuideStep guideStep;

	@Autowired
	public GuideOverview(GuideOverviewContent pageContent) {
		this.pageContent = pageContent;
	}

	@Override
	protected boolean isAccessAllowedForUser() {
		return true;
	}

	@Override
	protected Component getTitlePanel() {
		return new ScreenTitlePanel("PATHMIND GUIDE", "Overview");
	}

	@Override
	protected Component getMainContent() {
        GuideStep guideStep = guideDAO.getGuideStep(projectId);

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
		pageContent.initBtns(guideStep, projectId);
	}

	@Override
	public void setParameter(BeforeEvent event, Long projectId) {
		this.projectId = projectId;
	}
}
