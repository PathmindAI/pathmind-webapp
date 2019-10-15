package io.skymind.pathmind.ui.layouts.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.server.VaadinServlet;

import io.skymind.pathmind.security.PathmindUserDetails;
import io.skymind.pathmind.security.SecurityUtils;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.ui.views.account.AccountView;
import org.apache.commons.lang3.StringUtils;

public class AccountHeaderPanel extends HorizontalLayout
{
	public AccountHeaderPanel() {
		PathmindUserDetails user = SecurityUtils.getUser();

		MenuBar menuBar = new MenuBar();
		menuBar.setThemeName("tertiary");
		add(menuBar);

		MenuItem account = menuBar.addItem("Account");
		String username = StringUtils.isBlank(user.getName()) ? user.getEmail() : user.getName();
		account.getSubMenu().addItem(createItem(new Icon(VaadinIcon.USER), username),
				e -> UI.getCurrent().navigate(AccountView.class));

		account.getSubMenu().addItem(createLogoutLink(
				createItem(new Icon(VaadinIcon.EXIT), "Logout")));

		getElement().getStyle().set("margin-left", "auto");
		getElement().getStyle().set("padding-right", "20px");
		setId("nav-account-links");
	}

	private HorizontalLayout createItem(Icon icon, String text) {
		Span label = new Span(text);
		label.getStyle().set("padding-top", "5px");

		HorizontalLayout hl = WrapperUtils.wrapWidthFullHorizontal(icon, label);
		hl.getStyle().set("color", "var(--_lumo-button-color, var(--lumo-primary-text-color)");
		return hl;
	}

	private Anchor createLogoutLink(HorizontalLayout hl) {
		final String contextPath = VaadinServlet.getCurrent().getServletContext().getContextPath();
		Anchor logoutLink = new Anchor(contextPath + "/logout");
		logoutLink.add(hl);
		return logoutLink;
	}
}
