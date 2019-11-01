package io.skymind.pathmind.ui.views.account;

import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Subscription;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import elemental.json.JsonObject;
import io.skymind.pathmind.data.PathmindUser;
import io.skymind.pathmind.security.CurrentUser;
import io.skymind.pathmind.services.billing.StripeService;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.layouts.MainLayout;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Objects;

import static io.skymind.pathmind.security.Routes.PAYMENT_URL;

@Tag("payment-view")
@JsModule("./src/account/payment-view.js")
@Route(value = PAYMENT_URL, layout = MainLayout.class)
public class PaymentView extends PolymerTemplate<PaymentView.Model>
{

	private static Logger log = LogManager.getLogger(PaymentView.class);
	private StripeService stripeService;

	private PathmindUser user;

	@Autowired
	public PaymentView(@Value("${pathmind.stripe.public.key}") String publicKey,
					   StripeService stripeService,
					   CurrentUser currentUser,
					   @Value("${pathmind.contact-support.address}") String contactLink)
	{
		this.stripeService = stripeService;
		user = currentUser.getUser();
		final ScreenTitlePanel screenTitlePanel = new ScreenTitlePanel("UPGRADE", "Subscription Plans");
		getElement().appendVirtualChild(screenTitlePanel.getElement());
		getElement().callJsFunction("addScreenTitlePanel", screenTitlePanel.getElement());

		getModel().setContactLink(contactLink);
		getModel().setPlan("Professional");
		getModel().setKey(publicKey);

	}

	/**
	 * This method is called from the client-side
	 *
	 * @param paymentMethod
	 */
	@ClientCallable
	private void paymentMethodCallback(JsonObject paymentMethod)
	{
		Objects.requireNonNull(paymentMethod);
		if (!isValid(paymentMethod)) {
			getModel().setShowValidationError(true);
			return;
		}
		getModel().setShowValidationError(false);

		final String paymentMethodId = paymentMethod.getString("id");
		if (paymentMethodId == null) {
			final String noPaymentId = "Received a payment method call without a payment id";
			log.info(noPaymentId);
			throw new RuntimeException(noPaymentId);
		}
		try {
			Customer customer = stripeService.createCustomer(user.getEmail(), paymentMethodId);
			final Subscription subscription = stripeService.createSubscription(customer);
			Notification.show("A new customer has been created on Stripe. id: " + customer.getId() + ", email: " + customer.getEmail() + ", with subscription id: " + subscription.getId());
		} catch (StripeException e) {
			throw new RuntimeException(e);
		}
	}

	@ClientCallable
	private void validateForm(JsonObject jsonObject)
	{
		if (!isValid(jsonObject)) {
			getModel().setShowValidationError(true);
			getModel().setIsFormComplete(false);
		} else {
			getModel().setShowValidationError(false);
			getModel().setIsFormComplete(true);
		}
	}

	private boolean isValid(JsonObject paymentMethod)
	{
		final String nameOnCard = paymentMethod.getObject("billing_details").getString("name");
		final String address = paymentMethod.getObject("billing_details").getObject("address").getString("line1");
		final String city = paymentMethod.getObject("billing_details").getObject("address").getString("city");
		final String state = paymentMethod.getObject("billing_details").getObject("address").getString("state");
		final String zip = paymentMethod.getObject("billing_details").getObject("address").getString("postal_code");
		return !StringUtils.isAnyEmpty(nameOnCard, address, city, state, zip);
	}

	/**
	 * This method is called from the client-side
	 */
	@EventHandler
	private void cancelButtonClicked()
	{
		UI.getCurrent().navigate(AccountUpgradeView.class);
	}

	public interface Model extends TemplateModel
	{
		void setContactLink(String contactLink);

		void setPlan(String plan);

		void setKey(String key);

		void setIsFormComplete(Boolean isFormComplete);

		void setShowValidationError(Boolean showValidationError);
	}

}
