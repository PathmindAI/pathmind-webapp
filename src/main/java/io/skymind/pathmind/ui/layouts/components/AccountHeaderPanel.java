package io.skymind.pathmind.ui.layouts.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import io.skymind.pathmind.bus.PathmindBusEvent;
import io.skymind.pathmind.bus.utils.PolicyBusEventUtils;
import io.skymind.pathmind.data.PathmindUser;
import io.skymind.pathmind.ui.utils.PushUtils;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.ui.views.account.AccountView;
import reactor.core.publisher.Flux;
import org.apache.commons.lang3.StringUtils;

public class AccountHeaderPanel extends HorizontalLayout
{
	private Span usernameLabel = new Span();

	public AccountHeaderPanel(PathmindUser user, Flux<PathmindBusEvent> consumer) {
		addClassName("nav-account-links");

		MenuBar menuBar = new MenuBar();
		add(menuBar);
		menuBar.setThemeName("tertiary");
		menuBar.addClassName("account-menu");

		MenuItem account = menuBar.addItem(createItem(new Icon(VaadinIcon.USER), user));
		account.getSubMenu().addItem("Account", e -> UI.getCurrent().navigate(AccountView.class));
		account.getSubMenu().addItem("Logout", e ->
				UI.getCurrent().getPage().executeJavaScript("location.assign('/logout')"));

        subscribeToEventBus(consumer);
	}

	private HorizontalLayout createItem(Icon icon, PathmindUser user) {
        updateData(user);
		HorizontalLayout hl = WrapperUtils.wrapWidthFullHorizontal(icon, usernameLabel);
		return hl;
	}

	private void subscribeToEventBus(Flux<PathmindBusEvent> consumer) {
		PolicyBusEventUtils.consumerBusEventBasedOnUserUpdate(
				consumer, pathmindUser -> PushUtils.push(UI.getCurrent(), () -> updateData(pathmindUser)));
	}

	private void updateData(PathmindUser pathmindUser) {
		if (usernameLabel != null) {
			usernameLabel.setText(getUsername(pathmindUser));
		}
	}

	private String getUsername(PathmindUser user){
		return StringUtils.isBlank(user.getName()) ? user.getEmail() : user.getName();
	}
}
