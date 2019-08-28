package io.skymind.pathmind.ui.views.errors;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.ui.components.ActionMenu;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.views.BasicViewInterface;

/**
 * TODO -> Implement correctly, this is just a quick stub.
 */
@StyleSheet("frontend://styles/styles.css")
@Route(value="invalidLink", layout = MainLayout.class)
public class InvalidDataView extends VerticalLayout implements BasicViewInterface
{
	public InvalidDataView() {
		setWidthFull();
		add(new Label("Invalid data error"));
	}

	@Override
	public ActionMenu getActionMenu() {
		return null;
	}

	@Override
	public Component getTitlePanel() {
		return null;
	}

	@Override
	public Component getMainContent() {
		return null;
	}
}
