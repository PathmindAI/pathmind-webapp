package io.skymind.pathmind.ui.views;

import com.vaadin.flow.component.Component;
import io.skymind.pathmind.security.framework.LoginCheckBeforeEnterEvent;
import io.skymind.pathmind.ui.components.ActionMenu;

/**
 * Do NOT implement any default methods for this interface because a large part of it's goal is to remind
 * the developer to implement these methods in all the views to keep the layout and coding consistent.
 */
public interface BasicViewInterface extends LoginCheckBeforeEnterEvent
{
	public ActionMenu getActionMenu();
	public Component getTitlePanel();
	public Component getMainContent();
}
