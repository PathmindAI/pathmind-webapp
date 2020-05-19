package io.skymind.pathmind.webapp.ui.views.account;

import static io.skymind.pathmind.shared.security.Routes.PAYMENT_URL;

import io.skymind.pathmind.webapp.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;

import io.skymind.pathmind.shared.security.SecurityUtils;
import io.skymind.pathmind.services.billing.StripeService;

/**
 * Serverside part of the payment-view element which handles the integration to Stripe.
 *
 * NOTE: This element is not rendered in a shadow DOM so you cannot use the normal @Id Polymer template connector annotations
 *
 * DO NOT SEND CREDIT CARD INFORMATION FROM FRONTEND TO BACKEND. LET STRIPE HANDLE ALL THAT INSTEAD AND ONLY PASS IDS.
 */
@Route(value = PAYMENT_URL, layout = MainLayout.class)
public class PaymentView extends PathMindDefaultView {

	private final PaymentViewContent paymentViewContent;
	
	private StripeService stripeService;

	@Autowired
	public PaymentView(PaymentViewContent paymentViewContent, StripeService stripeService) {
		this.paymentViewContent = paymentViewContent;
		this.stripeService = stripeService;
	}

	@Override
	protected Component getTitlePanel() {
		return new ScreenTitlePanel("Account", "Upgrade Subscription Plans", AccountView.class);
	}

	@Override
	protected Component getMainContent() {
		return paymentViewContent;
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
	

}
