package io.skymind.pathmind.ui.layouts.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouterLink;
import io.skymind.pathmind.ui.views.AccountView;

public class AccountHeaderPanel extends HorizontalLayout
{
	public AccountHeaderPanel() {

		Label userLabel = new Label("Name");
		RouterLink accountRouterLink = new RouterLink("Account", AccountView.class);
		Button logoutButton = new Button("Logout");
		logoutButton.setThemeName("tertiary-inline");

		add(userLabel, accountRouterLink, logoutButton);

		getElement().getStyle().set("margin-left", "auto");
		getElement().getStyle().set("padding-right", "20px");
	}
}
