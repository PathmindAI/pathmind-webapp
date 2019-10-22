package io.skymind.pathmind.ui.layouts.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinServlet;

import io.skymind.pathmind.bus.PathmindBusEvent;
import io.skymind.pathmind.bus.utils.PolicyBusEventUtils;
import io.skymind.pathmind.data.PathmindUser;
import io.skymind.pathmind.security.PathmindUserDetails;
import io.skymind.pathmind.security.SecurityUtils;
import io.skymind.pathmind.ui.utils.PushUtils;
import io.skymind.pathmind.ui.views.account.AccountView;
import reactor.core.publisher.Flux;

public class AccountHeaderPanel extends HorizontalLayout
{

	private RouterLink accountRouterLink;

	public AccountHeaderPanel(Flux<PathmindBusEvent> consumer) {
		PathmindUserDetails user = SecurityUtils.getUser();
		accountRouterLink = new RouterLink(user.getName(), AccountView.class);
		Anchor logoutLink = createLogoutLink();

		final Icon userIcon = new Icon(VaadinIcon.USER);
		userIcon.getStyle().set("margin-top", "3px");
		add(userIcon, accountRouterLink, logoutLink);

		getElement().getStyle().set("margin-left", "auto");
		getElement().getStyle().set("padding-right", "20px");
		setId("nav-account-links");
		subscribeToEventBus(UI.getCurrent(), consumer);
	}

	private Anchor createLogoutLink() {
		final String contextPath = VaadinServlet.getCurrent().getServletContext().getContextPath();
		Anchor logoutLink = new Anchor(contextPath + "/logout");
		logoutLink.add("Logout");
		return logoutLink;
	}

	private void subscribeToEventBus(UI ui, Flux<PathmindBusEvent> consumer) {
		PolicyBusEventUtils.consumerBusEventBasedOnUserUpdate(
				consumer, pathmindUser -> PushUtils.push(ui, () -> updateData(pathmindUser)));
	}

//	TODO: update after fix with DEV.
	private void updateData(PathmindUser pathmindUser) {
		accountRouterLink.setText(pathmindUser.getName());
	}
}
