package io.skymind.pathmind.webapp.ui.views.account;

import io.skymind.pathmind.webapp.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.Route;

import io.skymind.pathmind.shared.security.Routes;

@Route(value = Routes.ACCOUNT_CHANGE_PASS_URL, layout = MainLayout.class)
public class ChangePasswordView extends PathMindDefaultView {

	private final ChangePasswordViewContent changePasswordViewContent;

	@Autowired
	public ChangePasswordView(ChangePasswordViewContent changePasswordViewContent) {
		this.changePasswordViewContent = changePasswordViewContent;
	}

	@Override
	protected boolean isAccessAllowedForUser() {
		return true;
	}

	@Override
	protected Component getTitlePanel() {
		return new ScreenTitlePanel("Account", "Change Password", AccountView.class);
	}

	@Override
	protected Component getMainContent() {
		return changePasswordViewContent;
	}
}
