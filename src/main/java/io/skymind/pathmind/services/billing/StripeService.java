package io.skymind.pathmind.services.billing;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Subscription;
import io.skymind.pathmind.data.PathmindUser;
import io.skymind.pathmind.db.dao.UserDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * This class is used to manage credit card subscription of the Pathmind application.
 * Stripe is used as the service. For more information please refer to the documentation: https://stripe.com/docs
 *
 * The following documentation was followed to initially create this class and allow the Customer to subscribe to a
 * recurring plan:
 * https://stripe.com/docs/billing/subscriptions/set-up-subscription
 *
 * PLEASE NOTE: NO CREDIT CARD INFORMATION IS TO BE STORED ON PATHMIND APPLICATION. STRIPE WILL BE USED FOR THIS.
 *
 * Stripe's customer id is saved in {@link io.skymind.pathmind.data.PathmindUser#stripeCustomerId} which serves
 * as the link between Pathmind and Stripe data.
 *
 */
@Service
public class StripeService
{

	private static Logger log = LogManager.getLogger(StripeService.class);

	@Value("${pathmind.stripe.public.key}")
	private String publicKey;

	@Value("${pathmind.stripe.secret.key}")
	private String secretKey;

	@Value("${pathmind.stripe.professional-plan-id}")
	private String professionalPlanId;

	private final UserDAO userDAO;

	public StripeService(@Autowired UserDAO userDAO)
	{
		this.userDAO = userDAO;
	}

	@PostConstruct
	public void init()
	{
		Stripe.apiKey = secretKey;
	}

	/**
	 * Creates a new Customer object on Stripe with the given parameters.
	 * @param email user email address
	 * @param paymentMethod payment method id that Stripe Elements returns on the frontend. This is used to create
	 *                      the subscription
	 *      				See <a href="https://stripe.com/docs/api/payment_methods">https://stripe.com/docs/api/payment_methods</a>
	 *        				for more information
	 * @param nameOnCard
	 * @param addressLine1
	 * @param city
	 * @param state
	 * @param postalCode
	 * @return new Stripe customer object.
	 * @throws StripeException
	 */
	public Customer createCustomer(String email, String paymentMethod, String nameOnCard, String addressLine1, String city, String state, String postalCode) throws StripeException
	{
		if (customerAlreadyExists(email)) {
			throw new RuntimeException("Cannot create the same customer a second time");
		}

		Map<String, Object> customerParams = new HashMap<>();
		customerParams.put("email", email);
		customerParams.put("name", nameOnCard);
		Map<String, String> address = new HashMap<>();
		address.put("line1", addressLine1);
		address.put("city", city);
		address.put("state", state);
		address.put("postal_code", postalCode);
		customerParams.put("address", address);
		customerParams.put("payment_method", paymentMethod);
		Map<String, String> invoiceSettings = new HashMap<>();
		invoiceSettings.put("default_payment_method", paymentMethod);
		customerParams.put("invoice_settings", invoiceSettings);
		final Customer customer = Customer.create(customerParams);

		PathmindUser pathmindUser = userDAO.findByEmailIgnoreCase(email);
		pathmindUser.setStripeCustomerId(customer.getId());
		userDAO.update(pathmindUser);

		return customer;
	}

	/**
	 * Checks whether a customer already exists on Stripe based on the Stripe customer id that is stored in
	 * {@link io.skymind.pathmind.data.PathmindUser#stripeCustomerId}
	 * @param email email of the Pathmind user
	 * @return true if the customer already exists, false otherwise
	 */
	public boolean customerAlreadyExists(String email)
	{
		try {
			final PathmindUser pathmindUser = userDAO.findByEmailIgnoreCase(email);
			if (pathmindUser.getStripeCustomerId() == null) {
				return false;
			}
			return null != Customer.retrieve(pathmindUser.getStripeCustomerId());
		} catch (StripeException e) {
			log.info("Could not retrieve Customer from Stripe with email: " + email);
			return false;
		}
	}

	/**
	 * Creates a new recurring Professional subscription for the given Stripe customer.
	 * @param customer Stripe customer to create the subscription for.
	 * @return The newly created Subscription
	 */
	public Subscription createSubscription(Customer customer)
	{
		Objects.requireNonNull(customer);
		Objects.requireNonNull(customer.getId());
		Map<String, Object> item = new HashMap<>();
		item.put("plan", professionalPlanId);
		Map<String, Object> items = new HashMap<>();
		items.put("0", item);
		Map<String, Object> expand = new HashMap<>();
		expand.put("0", "latest_invoice.payment_intent");
		Map<String, Object> params = new HashMap<>();
		params.put("customer", customer.getId());
		params.put("items", items);
		params.put("expand", expand);
		try {
			return Subscription.create(params);
		} catch (StripeException e) {
			log.info("Could not create a Stripe subscription for: " + customer.getId());
			throw new RuntimeException(e);
		}
	}

	/**
	 * Get a Stripe customer by Pathmind user email.
	 * @param email
	 * @return
	 * @throws StripeException
	 */
	public Customer getCustomer(String email) throws StripeException
	{
		final PathmindUser pathmindUser = userDAO.findByEmailIgnoreCase(email);
		return Customer.retrieve(pathmindUser.getStripeCustomerId());
	}

	/**
	 * Checks whether the user has an active recurring Professional subscription.
	 * @param email Pathmind user email address
	 * @return true if the user has an ongoing subscription, false if not.
	 */
	public boolean userHasActiveProfessionalSubscription(String email)
	{
		Customer customer = null;
		try {
			customer = getCustomer(email);
		} catch (StripeException e) {
			log.info("Could not retrieve customer from Stripe: " + email);
			return false;
		}
		if (customer.getSubscriptions() == null ||
				customer.getSubscriptions().getData() == null ||
				customer.getSubscriptions().getData().isEmpty()
		) {
			return false;
		}
		return "active".equalsIgnoreCase(customer.getSubscriptions().getData().get(0).getStatus());
	}

}
