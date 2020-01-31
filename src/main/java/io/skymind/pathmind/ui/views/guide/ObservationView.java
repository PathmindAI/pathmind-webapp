package io.skymind.pathmind.ui.views.guide;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.Component;
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
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.ui.views.PathMindDefaultView;

@Route(value = Routes.GUIDE_OBSERVATION_URL, layout = MainLayout.class)
public class ObservationView extends PathMindDefaultView implements HasUrlParameter<Long> {
	@Autowired
	private GuideDAO guideDAO;
	
	private final ObservationViewContent pageContent;

	private long projectId;

	private GuideStep guideStep;

	@Autowired
	public ObservationView(ObservationViewContent pageContent) {
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
