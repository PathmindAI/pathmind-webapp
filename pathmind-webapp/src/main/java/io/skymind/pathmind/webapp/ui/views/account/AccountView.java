package io.skymind.pathmind.webapp.ui.views.account;

import io.skymind.pathmind.webapp.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.Route;

import io.skymind.pathmind.shared.security.Routes;

@Route(value = Routes.ACCOUNT_URL, layout = MainLayout.class)
public class AccountView extends PathMindDefaultView {

	private final AccountViewContent accountViewContent;

	@Autowired
	public AccountView(AccountViewContent accountViewContent) {
		this.accountViewContent = accountViewContent;
	}

	@Override
	protected boolean isAccessAllowedForUser() {
		return true;
	}

	@Override
	protected Component getTitlePanel() {
		return new ScreenTitlePanel("ACCOUNT");
	}

	@Override
	protected Component getMainContent() {
		return accountViewContent;
	}
}
