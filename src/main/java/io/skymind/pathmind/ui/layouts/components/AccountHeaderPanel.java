package io.skymind.pathmind.ui.layouts.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinServlet;

import io.skymind.pathmind.security.PathmindUserDetails;
import io.skymind.pathmind.security.SecurityUtils;
import io.skymind.pathmind.ui.views.account.AccountView;

public class AccountHeaderPanel extends HorizontalLayout
{
	public AccountHeaderPanel() {
		PathmindUserDetails user = SecurityUtils.getUser();
		RouterLink accountRouterLink = new RouterLink(user.getEmail(), AccountView.class);

		Anchor logoutLink = createLogoutLink();

		final Icon userIcon = new Icon(VaadinIcon.USER);
		userIcon.getStyle().set("margin-top", "3px");
		add(userIcon, accountRouterLink, logoutLink);

		getElement().getStyle().set("margin-left", "auto");
		getElement().getStyle().set("padding-right", "20px");
		setId("nav-account-links");
	}

	private Anchor createLogoutLink() {
		final String contextPath = VaadinServlet.getCurrent().getServletContext().getContextPath();
		Anchor logoutLink = new Anchor(contextPath + "/logout");
		logoutLink.add("Logout");
		return logoutLink;
	}
}
