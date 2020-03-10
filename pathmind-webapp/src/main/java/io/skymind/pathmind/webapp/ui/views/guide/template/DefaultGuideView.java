package io.skymind.pathmind.webapp.ui.views.guide.template;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.shared.constants.GuideStep;
import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.db.dao.GuideDAO;
import io.skymind.pathmind.db.dao.ProjectDAO;
import io.skymind.pathmind.webapp.exception.InvalidDataException;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.webapp.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.webapp.ui.components.navigation.Breadcrumbs;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import io.skymind.pathmind.webapp.ui.views.guide.GuideMenu;

import org.springframework.beans.factory.annotation.Autowired;

@Route(value = Routes.GUIDE_URL, layout = MainLayout.class)
public abstract class DefaultGuideView extends PathMindDefaultView
		implements HasUrlParameter<Long>, AfterNavigationObserver {

	protected abstract DefaultPageContent initPageContent();

	@Autowired
	private ProjectDAO projectDAO;
	@Autowired
	private GuideDAO guideDAO;

	@Autowired
	protected SegmentIntegrator segmentIntegrator;

	protected long projectId;

	protected GuideStep guideStep;

	private Project project;

	public DefaultGuideView() {
		super();
		addClassName("guide");
	}

	@Override
	protected boolean isAccessAllowedForUser() {
		return true;
	}

	@Override
	protected Component getTitlePanel() {
		return null;
	}

	@Override
	protected Component getMainContent() {
		DefaultPageContent pageContent = initPageContent();
		pageContent.initBtns(guideDAO, guideStep, projectId, segmentIntegrator);

		HorizontalLayout contentWrapper = WrapperUtils
				.wrapWidthFullBetweenHorizontal(new GuideMenu(guideStep, projectId, segmentIntegrator), 
				pageContent);
		contentWrapper.addClassName("guide-view-wrapper");

		return WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(
			WrapperUtils.wrapWidthFullCenterHorizontal(new Breadcrumbs(project)),
			contentWrapper
		);
	}

	@Override
	protected void initLoadData() {
		project = projectDAO.getProject(projectId)
				.orElseThrow(() -> new InvalidDataException("Attempted to access Pathmind Guide for Project: " + projectId));
		guideStep = guideDAO.getGuideStep(projectId);
	}

	@Override
	public void setParameter(BeforeEvent event, Long projectId) {
		this.projectId = projectId;
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		Page page = UI.getCurrent().getPage();

		page.executeJs(
				"document.querySelector('vaadin-app-layout').shadowRoot.querySelector('div[content]').scrollTo(0,0)");
    }
}
