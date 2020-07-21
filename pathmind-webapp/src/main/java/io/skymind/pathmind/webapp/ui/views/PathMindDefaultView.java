package io.skymind.pathmind.webapp.ui.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.cookieconsent.CookieConsent;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.shared.communication.PushMode;
import io.skymind.pathmind.services.training.cloud.aws.api.AWSApiClient;
import io.skymind.pathmind.webapp.exception.InvalidDataException;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.utils.GuiUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.utils.PathmindUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Do NOT implement any default methods for this interface because a large part of it's goal is to remind
 * the developer to implement these methods in all the views to keep the layout and coding consistent.
 */
@Slf4j
public abstract class PathMindDefaultView extends VerticalLayout implements BeforeEnterObserver, HasDynamicTitle
{
	private static String COOKIE_CONSENT_LINK = "https://pathmind.com/privacy";

    @Autowired
    private SegmentIntegrator segmentIntegrator;

    @Autowired
    private AWSApiClient awsApiClient;

	private int previousWindowWidth = 0;

	public PathMindDefaultView()
	{
		setWidth("100%");
		setDefaultHorizontalComponentAlignment(Alignment.CENTER);
		GuiUtils.removeMarginsPaddingAndSpacing(this);
		setClassName("default-view");

		CookieConsent cookieConsent = new CookieConsent();
		cookieConsent.setPosition(CookieConsent.Position.BOTTOM_LEFT);
		cookieConsent.setLearnMoreLink(COOKIE_CONSENT_LINK);
		add(cookieConsent);
	}

	public void beforeEnter(BeforeEnterEvent event)
	{
		// IMPORTANT -> Needed so that Push works consistently on every page/view.
		event.getUI().getPushConfiguration().setPushMode(PushMode.AUTOMATIC);
		
		// TODO -> https://github.com/SkymindIO/pathmind-webapp/issues/217 Implement a security framework on the views.
		// Before we do anything we need to confirm the user has permission to access the data.
		// TODO -> This solution is a band-aid solution and although it does implement enough security for now
		// we absolutely have to revisit it (as well as the exception). See the method itself for more details.
		// TODO -> This throws InvalidDataException which is incorrect but it is the best we can do with the current solution
		// until we decide how we want to implement user data management.
		if(!isAccessAllowedForUser()) {
			throw new InvalidDataException("Item does not exist");
		}
		initLoadData();
		// If there is an exception in generating the screens we don't want to display any system related information to the user for security reasons.
		addScreens();
		// Update the screen based on the parameters if need be.
		initScreen(event);
		// Segment plugin added
		add(segmentIntegrator);
	}

	public void recalculateGridColumnWidth(Page page, Grid grid) {
		page.addBrowserWindowResizeListener(resizeEvent -> {
			int windowWidth = resizeEvent.getWidth();
			if ((windowWidth > 1024 && previousWindowWidth <= 1024) ||
				(windowWidth > 1280 && previousWindowWidth <= 1280)) {
				grid.recalculateColumnWidths();
			}
			previousWindowWidth = windowWidth;
		});
	}

	protected void initLoadData() throws InvalidDataException{
		// Do nothing by default.
	}

	private void addScreens(){
		removeAll();
		if (awsApiClient.isUsingMockBackend()) {
            add(getWarningMessage());
        }
		final Component titlePanel = getTitlePanel();
		if(titlePanel != null) add(titlePanel);
		final Component mainContent = getMainContent();
		if(mainContent != null) add(mainContent);
	}

    private  Component getWarningMessage() {
        Div message = new Div();
        message.add(LabelFactory.createLabel("Using Mock Backend"));
        HorizontalLayout result = WrapperUtils.wrapWidthFullCenterHorizontal(message);
        result.getStyle().set("color", "var(--lumo-body-text-color)");
        result.getStyle().set("background-color", "#FFC038");
        return result;
    }

    // TODO -> https://github.com/SkymindIO/pathmind-webapp/issues/217 Implement a security framework on the views.
	// NOTE -> This is a janky solution for https://github.com/SkymindIO/pathmind-webapp/issues/217 until we decide exactly
	// what we want to implement.
	protected boolean isAccessAllowedForUser() {
		return true;
	}

	protected abstract Component getTitlePanel();

	protected abstract Component getMainContent();

	protected void initScreen(BeforeEnterEvent event) throws InvalidDataException {
	}

	@Override
	public String getPageTitle() {
		return PathmindUtils.getPageTitle();
	}
}
