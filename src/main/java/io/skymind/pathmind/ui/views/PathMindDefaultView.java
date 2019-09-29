package io.skymind.pathmind.ui.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.HasDynamicTitle;
import io.skymind.pathmind.exception.InvalidDataException;
import io.skymind.pathmind.security.SecurityUtils;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.views.errors.ErrorView;
import io.skymind.pathmind.ui.views.errors.InvalidDataView;
import io.skymind.pathmind.utils.PathmindUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Do NOT implement any default methods for this interface because a large part of it's goal is to remind
 * the developer to implement these methods in all the views to keep the layout and coding consistent.
 */
public class PathMindDefaultView extends VerticalLayout implements BeforeEnterObserver, HasDynamicTitle
{
	private static Logger log = LogManager.getLogger(PathMindDefaultView.class);

	private boolean isGenerated = false;

	public PathMindDefaultView()
	{
		setSizeFull();
		setDefaultHorizontalComponentAlignment(Alignment.CENTER);
	}

	// TODO handle different exceptions differently.
	public void beforeEnter(BeforeEnterEvent event)
	{
		if(!SecurityUtils.isUserLoggedIn()) {
			event.forwardTo(LoginView.class);
			return;
		}

		try {
			// Before we do anything we should first load the data from the database in case there is an issue such as an InvalidDataException
			loadData();
			// If there is an exception in generating the screens we don't want to display any system related information to the user for security reasons.
			if(!isGenerated)
				addScreens();
			// Update the screen based on the parameters if need be.
			updateScreen(event);
			// Must be after update because we generally need to filter the event based on the screen data
			if(!isGenerated)
				subscribeToEventBus();
			isGenerated = true;
		} catch (InvalidDataException e) {
			log.info("Invalid data attempt: " + e.getMessage());
			event.rerouteTo(InvalidDataView.class);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			event.rerouteTo(ErrorView.class);
		}
	}

	protected void loadData() throws InvalidDataException{
		// Do nothing by default.
	}

	private void addScreens()
	{
		final Component titlePanel = getTitlePanel();
		if(titlePanel != null) add(titlePanel);
		final Component mainContent = getMainContent();
		if(mainContent != null) add(mainContent);
	}

	protected void subscribeToEventBus() {
		// Do nothing by default.
	}

	protected Component getTitlePanel() {
		return new ScreenTitlePanel("Please contact Skymind for assistance.");
	}

	protected Component getMainContent() {
		return new Label("Please contact Skymind for assistance.");
	}

	protected void updateScreen(BeforeEnterEvent event) throws InvalidDataException {
	}

	@Override
	public String getPageTitle() {
		return PathmindUtils.getPageTitle();
	}
}
