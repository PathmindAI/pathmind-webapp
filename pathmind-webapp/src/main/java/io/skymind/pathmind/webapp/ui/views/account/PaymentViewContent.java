package io.skymind.pathmind.webapp.ui.views.account;

import java.util.Objects;

import io.skymind.pathmind.webapp.security.CurrentUser;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.utils.NotificationUtils;
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
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.templatemodel.TemplateModel;

import elemental.json.JsonObject;
import io.skymind.pathmind.shared.data.PathmindUser;
import io.skymind.pathmind.services.billing.StripeService;

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
	
	private SegmentIntegrator segmentIntegrator;

	private PathmindUser user;

	@Autowired
	public PaymentViewContent(@Value("${pathmind.stripe.public.key}") String publicKey,
					   StripeService stripeService,
					   SegmentIntegrator segmentIntegrator,
					   CurrentUser currentUser,
					   @Value("${pathmind.contact-support.address}") String contactLink)
	{
		this.stripeService = stripeService;
		this.segmentIntegrator = segmentIntegrator;
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
			NotificationUtils.showError("Missing form fields. Please make sure to fill in all the fields");
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
			Customer customer = createOrUpdateCustomer(paymentMethod);
			final Subscription subscription = stripeService.createSubscription(customer);
			segmentIntegrator.accountUpgraded();
			getUI().ifPresent(ui -> ui.navigate(UpgradeDoneView.class));
		} catch (StripeException e) {
			log.warn("There was an error creating a subscription for the customer: " + user.getEmail());
			getModel().setIsFormComplete(true);
			NotificationUtils.showError("There was a problem creating a subsription, please try again later");
		}
	}
	
	/**
	 * If customer is new, creates a customer on Stripe,
	 * If customer already exists on Stripe, attaches the new payment method to customer, and updates customer info with provided information
	 * @param paymentMethod
	 * @return
	 * @throws StripeException
	 */
	private Customer createOrUpdateCustomer(JsonObject paymentMethod) throws StripeException {
		final String paymentMethodId = paymentMethod.getString("id");
		final String nameOnCard = getNameOnCard(paymentMethod);
		final String addressLine1 = getAddressLine1(paymentMethod);
		final String city = getCity(paymentMethod);
		final String state = getState(paymentMethod);
		final String postalCode = getPostalCode(paymentMethod);
		Customer customer = stripeService.getCustomer(user.getEmail());
		if (customer == null) {
			customer = stripeService.createCustomer(user.getEmail(), paymentMethodId, nameOnCard, addressLine1, city, state, postalCode); 
		} else {
			stripeService.attachPaymentMethodToCustomer(paymentMethodId, customer);
			customer = stripeService.updateCustomer(customer, nameOnCard, addressLine1, city, state, postalCode, paymentMethodId);
		}
		return customer;
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
		getUI().ifPresent(ui -> ui.navigate(AccountUpgradeView.class));
	}

	public interface Model extends TemplateModel
	{
		void setContactLink(String contactLink);

		void setPlan(String plan);

		void setKey(String key);

		void setIsFormComplete(Boolean isFormComplete);
	}
}
