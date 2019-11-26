package io.skymind.pathmind.ui.views.account;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Subscription;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.templatemodel.TemplateModel;

import elemental.json.JsonObject;
import io.skymind.pathmind.data.PathmindUser;
import io.skymind.pathmind.security.CurrentUser;
import io.skymind.pathmind.services.billing.StripeService;
import io.skymind.pathmind.ui.utils.NotificationUtils;

/**
 * Serverside part of the payment-view element which handles the integration to Stripe.
 *
 * NOTE: This element is not rendered in a shadow DOM so you cannot use the normal @Id Polymer template connector annotations
 *
 * DO NOT SEND CREDIT CARD INFORMATION FROM FRONTEND TO BACKEND. LET STRIPE HANDLE ALL THAT INSTEAD AND ONLY PASS IDS.
 */
@Tag("payment-view-content")
@JsModule("./src/account/payment-view-content.js")
@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PaymentViewContent extends PolymerTemplate<PaymentViewContent.Model>{

	private static Logger log = LogManager.getLogger(PaymentViewContent.class);
	
	private StripeService stripeService;

	private PathmindUser user;

	@Autowired
	public PaymentViewContent(@Value("${pathmind.stripe.public.key}") String publicKey,
					   StripeService stripeService,
					   CurrentUser currentUser,
					   @Value("${pathmind.contact-support.address}") String contactLink)
	{
		this.stripeService = stripeService;
		user = currentUser.getUser();

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
			NotificationUtils.showNotification("Missing form fields. Please make sure to fill in all the fields", NotificationVariant.LUMO_ERROR);
			return;
		}
		getModel().setIsFormComplete(false);

		final String paymentMethodId = paymentMethod.getString("id");
		if (paymentMethodId == null) {
			final String noPaymentId = "Received a payment method call without a payment id";
			log.info(noPaymentId);
			throw new RuntimeException(noPaymentId);
		}
		try {
			final String nameOnCard = getNameOnCard(paymentMethod);
			final String addressLine1 = getAddressLine1(paymentMethod);
			final String city = getCity(paymentMethod);
			final String state = getState(paymentMethod);
			final String postalCode = getPostalCode(paymentMethod);
			Customer customer = stripeService.createCustomer(user.getEmail(), paymentMethodId, nameOnCard, addressLine1, city, state, postalCode);
			final Subscription subscription = stripeService.createSubscription(customer);
			UI.getCurrent().navigate(UpgradeDoneView.class);
		} catch (StripeException e) {
			log.warn("There was an error creating a subscription for the customer: " + user.getEmail());
			getModel().setIsFormComplete(true);
			NotificationUtils.showNotification("There was a problem creating a subsription, please try again later", NotificationVariant.LUMO_ERROR);
		}
	}

	@ClientCallable
	private void validateForm(JsonObject jsonObject)
	{
		if (!isValid(jsonObject)) {
			getModel().setIsFormComplete(false);
		} else {
			getModel().setIsFormComplete(true);
		}
	}

	private boolean isValid(JsonObject paymentMethod)
	{
		final String nameOnCard = getNameOnCard(paymentMethod);
		final String address = getAddressLine1(paymentMethod);
		final String city = getCity(paymentMethod);
		final String state = getState(paymentMethod);
		final String zip = getPostalCode(paymentMethod);
		return !StringUtils.isAnyEmpty(nameOnCard, address, city, state, zip);
	}

	private String getPostalCode(JsonObject paymentMethod)
	{
		return paymentMethod.getObject("billing_details").getObject("address").getString("postal_code");
	}

	private String getState(JsonObject paymentMethod)
	{
		return paymentMethod.getObject("billing_details").getObject("address").getString("state");
	}

	private String getCity(JsonObject paymentMethod)
	{
		return paymentMethod.getObject("billing_details").getObject("address").getString("city");
	}

	private String getAddressLine1(JsonObject paymentMethod)
	{
		return paymentMethod.getObject("billing_details").getObject("address").getString("line1");
	}

	private String getNameOnCard(JsonObject paymentMethod)
	{
		return paymentMethod.getObject("billing_details").getString("name");
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
	}
}
