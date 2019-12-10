package io.skymind.pathmind.ui.views.account;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import com.stripe.model.Subscription;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.templatemodel.TemplateModel;

import io.skymind.pathmind.data.PathmindUser;
import io.skymind.pathmind.security.CurrentUser;
import io.skymind.pathmind.services.billing.StripeService;
import io.skymind.pathmind.ui.components.dialog.SubscriptionCancelDialog;
import io.skymind.pathmind.utils.DateAndTimeUtils;

@Tag("account-view-content")
@JsModule("./src/account/account-view-content.js")
@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AccountViewContent extends PolymerTemplate<AccountViewContent.Model> {

	@Id("editInfoBtn")
	private Button editInfoBtn;

	@Id("changePasswordBtn")
	private Button changePasswordBtn;

	@Id("upgradeBtn")
	private Button upgradeBtn;

	@Id("cancelSubscriptionBtn")
	private Button cancelSubscriptionBtn;

	@Id("editPaymentBtn")
	private Button editPaymentBtn;
	
	private StripeService stripeService;

	private PathmindUser user;

	@Autowired
	public AccountViewContent(CurrentUser currentUser, @Value("${pathmind.contact-support.address}") String contactLink, StripeService stripeService) {
        this.stripeService = stripeService;
		getModel().setContactLink(contactLink);
		user = currentUser.getUser();
	}

	@PostConstruct
	private void init() {
		Subscription subscription = stripeService.getActiveSubscriptionOfUser(user.getEmail());
		initContent(subscription);
		initBtns(subscription);
	}

	private void initBtns(Subscription subscription) {
		editInfoBtn.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(AccountEditView.class)));
		changePasswordBtn.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(ChangePasswordView.class)));
		editPaymentBtn.setEnabled(false);
		upgradeBtn.setVisible(subscription == null);
		cancelSubscriptionBtn.setVisible(subscription != null);
		cancelSubscriptionBtn.setEnabled(subscription != null && !subscription.getCancelAtPeriodEnd());
		
		upgradeBtn.addClickListener(e -> UI.getCurrent().navigate(AccountUpgradeView.class));
		cancelSubscriptionBtn.addClickListener(evt -> cancelSubscription(subscription));
	}
	
	// This part will probably move to a separate view, but for now implementing it as a confirmation dialog
	private void cancelSubscription(Subscription subscription) {
		SubscriptionCancelDialog subscriptionCancelDialog = new SubscriptionCancelDialog(subscription.getCurrentPeriodEnd(), () -> {
			Subscription updatedSubscription = stripeService.cancelSubscription(user.getEmail(), true);
			initContent(updatedSubscription);
			initBtns(updatedSubscription);
		});
		subscriptionCancelDialog.open();
		
	}

	private void initContent(Subscription subscription) {
		getModel().setEmail(user.getEmail());
		getModel().setFirstName(user.getFirstname());
		getModel().setLastName(user.getLastname());
		getModel().setSubscription(subscription != null ? "Professional": "Early Access");
		if (subscription != null && subscription.getCancelAtPeriodEnd()) {
			DateAndTimeUtils.withUserTimeZoneId(userTimeZoneId -> {
				getModel().setSubscriptionCancellationNote("Subscription will be cancelled on " +
						DateAndTimeUtils.formatDateAndTimeShortFormatter(DateAndTimeUtils.fromEpoch(subscription.getCurrentPeriodEnd()), userTimeZoneId));
			});
		}
		getModel().setBillingInfo("Billing Information");
	}

	public interface Model extends TemplateModel {
		void setEmail(String email);
		void setFirstName(String firstName);
		void setLastName(String lastName);
		void setSubscription(String subscription);
		void setSubscriptionCancellationNote(String cancellationNote);
		void setBillingInfo(String billingInfo);
        void setContactLink(String contactLink);
	}
}
