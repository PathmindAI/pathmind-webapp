package io.skymind.pathmind.ui.views.account;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.security.Routes;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.views.PathMindDefaultView;
import org.springframework.beans.factory.annotation.Autowired;

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
