package io.skymind.pathmind.ui.layouts.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
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

		String username = StringUtils.isBlank(user.getName()) ? user.getEmail() : user.getName();
		MenuItem account = menuBar.addItem(createItem(new Icon(VaadinIcon.USER), username));

		account.getSubMenu().addItem("Account", e -> UI.getCurrent().navigate(AccountView.class));
		account.getSubMenu().addItem("Logout", e ->
				UI.getCurrent().getPage().executeJavaScript("location.assign('/logout')"));

		addClassName("nav-account-links");
	}

	private HorizontalLayout createItem(Icon icon, String text) {
		HorizontalLayout hl = WrapperUtils.wrapWidthFullHorizontal(icon, new Span(text));
		return hl;
	}
}
