package io.skymind.pathmind.ui.layouts.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Span;


import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinServlet;

import io.skymind.pathmind.ui.utils.WrapperUtils;


import io.skymind.pathmind.bus.PathmindBusEvent;
import io.skymind.pathmind.bus.utils.PolicyBusEventUtils;
import io.skymind.pathmind.data.PathmindUser;
import io.skymind.pathmind.security.PathmindUserDetails;
import io.skymind.pathmind.security.SecurityUtils;
import io.skymind.pathmind.ui.utils.PushUtils;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.ui.views.account.AccountView;
import reactor.core.publisher.Flux;
import org.apache.commons.lang3.StringUtils;


public class AccountHeaderPanel extends HorizontalLayout
{

	private RouterLink accountRouterLink;

	public AccountHeaderPanel(Flux<PathmindBusEvent> consumer) {
		PathmindUserDetails user = SecurityUtils.getUser();

		MenuBar menuBar = new MenuBar();
		menuBar.setThemeName("tertiary");
		add(menuBar);

		String username = StringUtils.isBlank(user.getName()) ? user.getEmail() : user.getName();
		MenuItem account = menuBar.addItem(createItem(new Icon(VaadinIcon.USER), username));
		account.getElement().getStyle().set("color", "var(--lumo-header-text-color)");
		account.getSubMenu().addItem( new Span("Account"), e -> UI.getCurrent().navigate(AccountView.class));
		account.getSubMenu().addItem(createLogoutLink(new Span( "Logout")));

		getElement().getStyle().set("margin-left", "auto");
		getElement().getStyle().set("padding-right", "20px");
		setId("nav-account-links");
		subscribeToEventBus(UI.getCurrent(), consumer);
	}

	private HorizontalLayout createItem(Icon icon, String text) {
		Span label = new Span(text);
		label.getStyle().set("padding-top", "5px");

		HorizontalLayout hl = WrapperUtils.wrapWidthFullHorizontal(icon, label);
		hl.getStyle().set("color", "var(--lumo-header-text-color)");
		return hl;
	}

	private Anchor createLogoutLink(Component hl) {
		final String contextPath = VaadinServlet.getCurrent().getServletContext().getContextPath();
		Anchor logoutLink = new Anchor(contextPath + "/logout");
		logoutLink.getStyle().set("color", "var(--lumo-header-text-color)");
		logoutLink.add(hl);
		return logoutLink;
	}

	private void subscribeToEventBus(UI ui, Flux<PathmindBusEvent> consumer) {
		PolicyBusEventUtils.consumerBusEventBasedOnUserUpdate(
				consumer, pathmindUser -> PushUtils.push(UI.getCurrent(), () -> updateData(pathmindUser)));
	}

//	TODO: update after fix with DEV.
	private void updateData(PathmindUser pathmindUser) {
		accountRouterLink.setText(pathmindUser.getName());
	}
}
