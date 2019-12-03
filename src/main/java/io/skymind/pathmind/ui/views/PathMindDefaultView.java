package io.skymind.pathmind.ui.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.cookieconsent.CookieConsent;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.shared.communication.PushMode;
import io.skymind.pathmind.exception.InvalidDataException;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.ui.utils.GuiUtils;
import io.skymind.pathmind.ui.views.errors.ErrorView;
import io.skymind.pathmind.ui.views.errors.InvalidDataView;
import io.skymind.pathmind.utils.PathmindUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * Do NOT implement any default methods for this interface because a large part of it's goal is to remind
 * the developer to implement these methods in all the views to keep the layout and coding consistent.
 */
public abstract class PathMindDefaultView extends VerticalLayout implements BeforeEnterObserver, HasDynamicTitle
{

	private static Logger log = LogManager.getLogger(PathMindDefaultView.class);
	private static String COOKIE_CONSENT_LINK = "https://pathmind.com/privacy";

	private boolean isGenerated = false;

    @Value("${skymind.debug.accelerate}")
    private boolean isDebugAccelerate;
    
    @Autowired
    private SegmentIntegrator segmentIntegrator;

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

		// IMPORTANT -> Needed so that Push works consistently on every page/view.
		UI.getCurrent().getPushConfiguration().setPushMode(PushMode.AUTOMATIC);
	}

    public boolean isDebugAccelerate() {
        return isDebugAccelerate;
    }

    // TODO handle different exceptions differently.
	public void beforeEnter(BeforeEnterEvent event)
	{
		try {
			// TODO -> https://github.com/SkymindIO/pathmind-webapp/issues/217 Implement a security framework on the views.
			// Before we do anything we need to confirm the user has permission to access the data.
			// TODO -> This solution is a band-aid solution and although it does implement enough security for now
			// we absolutely have to revisit it (as well as the exception). See the method itself for more details.
			// TODO -> This throws InvalidDataException which is incorrect but it is the best we can do with the current solution
			// until we decide how we want to implement user data management.
			if(!isAccessAllowedForUser())
				throw new InvalidDataException("Item does not exist");
			// Next we initialize the data from the database in case there is an issue such as an InvalidDataException
			initLoadData();
			// If there is an exception in generating the screens we don't want to display any system related information to the user for security reasons.
			if(!isGenerated)
				addScreens();
			// Update the screen based on the parameters if need be.
			initScreen(event);
			// Segment plugin added
			add(segmentIntegrator);
			isGenerated = true;
		} catch (InvalidDataException e) {
			log.info("Invalid data attempt: " + e.getMessage());
			event.rerouteTo(InvalidDataView.class);
//		} catch (AccessDeniedException e) {
//			log.info("Access denied to data for " + SecurityUtils.getUserId() + " : " + e.getMessage());
//			event.rerouteTo(AccessDeniedView.class);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			event.rerouteTo(ErrorView.class);
		}
	}

	protected void initLoadData() throws InvalidDataException{
		// Do nothing by default.
	}

	private void addScreens()
	{
		final Component titlePanel = getTitlePanel();
		if(titlePanel != null) add(titlePanel);
		final Component mainContent = getMainContent();
		if(mainContent != null) add(mainContent);
	}

	// TODO -> https://github.com/SkymindIO/pathmind-webapp/issues/217 Implement a security framework on the views.
	// NOTE -> This is a janky solution for https://github.com/SkymindIO/pathmind-webapp/issues/217 until we decide exactly
	// what we want to implement.
	// NOTE -> I'm forcing all views to implement this method, even if it's just to return true, so that if any new
	// views are implemented before we implement a good framework then it will at least hopefully remind the developer to do a user access check.
	// TODO -> This currently cannot tell us if a user has access to an item because they item could be just none-existant. But for
	// now I'm using these method names so that we understand what needs to be done eventually.
	protected abstract boolean isAccessAllowedForUser();

	protected abstract Component getTitlePanel();

	protected abstract Component getMainContent();

	protected void initScreen(BeforeEnterEvent event) throws InvalidDataException {
	}

	@Override
	public String getPageTitle() {
		return PathmindUtils.getPageTitle();
	}
}
