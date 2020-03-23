package io.skymind.pathmind.webapp.ui.views.account;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.webapp.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;

import org.springframework.beans.factory.annotation.Autowired;

import static io.skymind.pathmind.shared.security.Routes.UPGRADE_DONE;

@Route(value = UPGRADE_DONE, layout = MainLayout.class)
public class UpgradeDoneView extends PathMindDefaultView
{

	private final UpgradeDoneViewContent upgradeDoneViewContent;
	
	@Autowired
	public UpgradeDoneView(UpgradeDoneViewContent upgradeDoneViewContent) {
		this.upgradeDoneViewContent = upgradeDoneViewContent;
	}

	@Override
	protected boolean isAccessAllowedForUser() {
		return true;
	}

	@Override
	protected Component getTitlePanel() {
		return new ScreenTitlePanel("Account", "Upgrade Subscription Plans", AccountView.class);
	}

	@Override
	protected Component getMainContent() {
		return upgradeDoneViewContent;
	}

}
