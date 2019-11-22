package io.skymind.pathmind.ui.views.account;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.security.Routes;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.views.PathMindDefaultView;
import org.springframework.beans.factory.annotation.Autowired;

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
		return new ScreenTitlePanel("CHANGE PASSWORD");
	}

	@Override
	protected Component getMainContent() {
		return changePasswordViewContent;
	}
}
