package io.skymind.pathmind.ui.views;

import com.vaadin.flow.component.Component;
import io.skymind.pathmind.ui.components.ActionMenu;

// TODO -> To remove later since we don't want these methods to be public. It's more just to help remind myself what I need to implement
// and to be consistent.
public interface BasicViewInterface
{
	public ActionMenu getActionMenu();
	public Component getTitlePanel();
	public Component getMainContent();
}
