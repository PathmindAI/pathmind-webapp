package io.skymind.pathmind.ui.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import io.skymind.pathmind.exception.InvalidDataException;
import io.skymind.pathmind.security.SecurityUtils;
import io.skymind.pathmind.ui.components.ActionMenu;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.views.errors.ErrorView;
import io.skymind.pathmind.ui.views.errors.InvalidDataView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Do NOT implement any default methods for this interface because a large part of it's goal is to remind
 * the developer to implement these methods in all the views to keep the layout and coding consistent.
 */
public abstract class PathMindDefaultView extends VerticalLayout implements BeforeEnterObserver
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
			// If there is an exception in generating the screens we don't want to display any system related information to the user for security reasons.
			addScreens();
			// Update the screen based on the parameters if need be.
			updateScreen(event);
			// Must be after update because we generally need to filter the event based on the screen data
			subscribeToEventBus();
		} catch (InvalidDataException e) {
			log.info("Invalid data attempt: " + e.getMessage());
			event.rerouteTo(InvalidDataView.class);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			event.rerouteTo(ErrorView.class);
		}
	}

	private void addScreens()
	{
		if(isGenerated)
			return;

		final ActionMenu actionMenu = getActionMenu();
		if(actionMenu != null) add(actionMenu);
		final Component titlePanel = getTitlePanel();
		if(titlePanel != null) add(titlePanel);
		final Component mainContent = getMainContent();
		if(mainContent != null) add(mainContent);

		isGenerated = true;
	}

	protected void subscribeToEventBus() {
		// Do nothing by default.
	}

	protected ActionMenu getActionMenu() {
		return new ActionMenu();
	}

	protected Component getTitlePanel() {
		return new ScreenTitlePanel("Please contact Skymind for assistance.");
	}

	protected Component getMainContent() {
		return new Label("Please contact Skymind for assistance.");
	}

	protected void updateScreen(BeforeEnterEvent event) throws InvalidDataException {
	}
}
