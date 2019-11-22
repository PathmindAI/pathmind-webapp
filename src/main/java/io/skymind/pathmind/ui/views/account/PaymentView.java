package io.skymind.pathmind.ui.views.account;

import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Subscription;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import elemental.json.JsonObject;
import io.skymind.pathmind.data.PathmindUser;
import io.skymind.pathmind.security.CurrentUser;
import io.skymind.pathmind.services.billing.StripeService;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.utils.NotificationUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Objects;

import static io.skymind.pathmind.security.Routes.PAYMENT_URL;

/**
 * Serverside part of the payment-view element which handles the integration to Stripe.
 *
 * NOTE: This element is not rendered in a shadow DOM so you cannot use the normal @Id Polymer template connector annotations
 *
 * DO NOT SEND CREDIT CARD INFORMATION FROM FRONTEND TO BACKEND. LET STRIPE HANDLE ALL THAT INSTEAD AND ONLY PASS IDS.
 */
@Tag("payment-view")
@JsModule("./src/account/payment-view.js")
@Route(value = PAYMENT_URL, layout = MainLayout.class)
public class PaymentView extends PolymerTemplate<PaymentView.Model> implements BeforeEnterObserver
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

		// because payment-view is not in the shadow DOM we cannot use the @Id PolymerTemplate connector annotation
		// and we have to add the ScreenTitlePanel element via this custom method
		final ScreenTitlePanel screenTitlePanel = new ScreenTitlePanel("UPGRADE", "Subscription Plans");
		getElement().appendVirtualChild(screenTitlePanel.getElement());
		getElement().callJsFunction("addScreenTitlePanel", screenTitlePanel.getElement());

		getModel().setContactLink(contactLink);
		getModel().setPlan("Professional");
		getModel().setKey(publicKey);

	}

	@Override
	public void beforeEnter(BeforeEnterEvent event)
	{
		// if user has an ongoing subscription this view shouldn't be shown
		if (stripeService.userHasActiveProfessionalSubscription(user.getEmail())) {
			event.rerouteTo(AccountView.class);
		}
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
