package io.skymind.pathmind.ui.layouts.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouterLink;
import io.skymind.pathmind.security.SecurityUtils;
import io.skymind.pathmind.ui.views.AccountView;

public class AccountHeaderPanel extends HorizontalLayout
{
	public AccountHeaderPanel() {

		Label userLabel = new Label("Name");
		RouterLink accountRouterLink = new RouterLink("Account", AccountView.class);

		Button logoutButton = new Button("Logout",
				click -> SecurityUtils.logout());

		logoutButton.setThemeName("tertiary-inline");
		logoutButton.setClassName("nav-logout-button");

		final Icon userIcon = new Icon(VaadinIcon.USER);
		userIcon.getStyle().set("margin-top", "3px");
		add(userIcon, userLabel, accountRouterLink, logoutButton);

		getElement().getStyle().set("margin-left", "auto");
		getElement().getStyle().set("padding-right", "20px");
		setId("nav-account-links");
	}
}
