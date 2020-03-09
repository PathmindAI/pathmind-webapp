package io.skymind.pathmind.webapp.ui.views.account;

import static io.skymind.pathmind.shared.security.Routes.ACCOUNT_UPGRADE_URL;

import io.skymind.pathmind.webapp.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;

import io.skymind.pathmind.shared.security.SecurityUtils;
import io.skymind.pathmind.services.billing.StripeService;

@Route(value=ACCOUNT_UPGRADE_URL, layout = MainLayout.class)
public class AccountUpgradeView extends PathMindDefaultView
{
	private final AccountUpgradeViewContent accountUpgradeViewContent;
	
	private StripeService stripeService;
	
	@Autowired
	public AccountUpgradeView(AccountUpgradeViewContent accountUpgradeViewContent, StripeService stripeService)
	{
		this.accountUpgradeViewContent = accountUpgradeViewContent;
		this.stripeService = stripeService;
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event)
	{
		// if user has an ongoing subscription this view shouldn't be shown
		if (stripeService.userHasActiveProfessionalSubscription(SecurityUtils.getUser().getEmail())) {
			event.rerouteTo(AccountView.class);
		}
		super.beforeEnter(event);
	}

	@Override
	protected boolean isAccessAllowedForUser() {
		return true;
	}

	@Override
	protected Component getTitlePanel() {
		return new ScreenTitlePanel("Upgrade Subscription Plans");
	}

	@Override
	protected Component getMainContent() {
		return accountUpgradeViewContent;
	}
}
