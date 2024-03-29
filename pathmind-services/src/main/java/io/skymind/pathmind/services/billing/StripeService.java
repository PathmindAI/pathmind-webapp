package io.skymind.pathmind.services.billing;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.PostConstruct;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentMethod;
import com.stripe.model.Subscription;
import io.skymind.pathmind.db.dao.UserDAO;
import io.skymind.pathmind.shared.data.PathmindUser;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * This class is used to manage credit card subscription of the Pathmind application.
 * Stripe is used as the service. For more information please refer to the documentation: https://stripe.com/docs
 * <p>
 * The following documentation was followed to initially create this class and allow the Customer to subscribe to a
 * recurring plan:
 * https://stripe.com/docs/billing/subscriptions/set-up-subscription
 * <p>
 * PLEASE NOTE: NO CREDIT CARD INFORMATION IS TO BE STORED ON PATHMIND APPLICATION. STRIPE WILL BE USED FOR THIS.
 * <p>
 * Stripe's customer id is saved in {@link PathmindUser#stripeCustomerId} which serves
 * as the link between Pathmind and Stripe data.
 */
@Service
public class StripeService {

    private static Logger log = LogManager.getLogger(StripeService.class);

    @Value("${pathmind.stripe.public.key}")
    private String publicKey;

    @Value("${pathmind.stripe.secret.key}")
    private String secretKey;

    @Value("${pathmind.stripe.professional-price-id}")
    private String professionalPlanId;

    private final UserDAO userDAO;

    public StripeService(@Autowired UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

    /**
     * Creates a new Customer object on Stripe with the given parameters.
     *
     * @param email         user email address
     * @param paymentMethod payment method id that Stripe Elements returns on the frontend. This is used to create
     *                      the subscription
     *                      See <a href="https://stripe.com/docs/api/payment_methods">https://stripe.com/docs/api/payment_methods</a>
     *                      for more information
     * @param nameOnCard
     * @param addressLine1
     * @param city
     * @param state
     * @param postalCode
     * @return new Stripe customer object.
     * @throws StripeException
     */
    public Customer createCustomer(String email, String paymentMethod, String nameOnCard, String addressLine1, String city, String state, String postalCode) throws StripeException {
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
     * Updates existing customer on Stripe with given parameters
     *
     * @param customer
     * @param nameOnCard
     * @param addressLine1
     * @param city
     * @param state
     * @param postalCode
     * @param paymentMethodId
     * @return
     * @throws StripeException
     */
    public Customer updateCustomer(Customer customer, String nameOnCard, String addressLine1, String city, String state, String postalCode, String paymentMethodId) throws StripeException {
        Map<String, Object> customerParams = new HashMap<>();
        customerParams.put("name", nameOnCard);
        Map<String, String> address = new HashMap<>();
        address.put("line1", addressLine1);
        address.put("city", city);
        address.put("state", state);
        address.put("postal_code", postalCode);
        customerParams.put("address", address);
        Map<String, String> invoiceSettings = new HashMap<>();
        invoiceSettings.put("default_payment_method", paymentMethodId);
        customerParams.put("invoice_settings", invoiceSettings);

        return customer.update(customerParams);
    }

    /**
     * Checks whether a customer already exists on Stripe based on the Stripe customer id that is stored in
     * {@link PathmindUser#stripeCustomerId}
     *
     * @param email email of the Pathmind user
     * @return true if the customer already exists, false otherwise
     */
    public boolean customerAlreadyExists(String email) {
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
     * Attaches a new payment method to an existing customer
     *
     * @param paymentMethodId
     * @param customer
     * @throws StripeException
     */
    public void attachPaymentMethodToCustomer(String paymentMethodId, Customer customer) throws StripeException {
        PaymentMethod paymentMethod = PaymentMethod.retrieve(paymentMethodId);
        Map<String, Object> params = new HashMap<>();
        params.put("customer", customer.getId());
        paymentMethod.attach(params);
    }

    /**
     * Creates a new recurring Professional subscription for the given Stripe customer.
     *
     * @param customer Stripe customer to create the subscription for.
     * @return The newly created Subscription
     */
    public Subscription createSubscription(Customer customer) {
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
     * Takes a cancel request from user, and the subscription is cancelled at period end
     *
     * @return updated subscription instance
     */
    public Subscription cancelSubscription(String email, boolean cancelAtPeriodEnd) {
        try {
            Customer customer = getCustomer(email);
            Subscription subscription = customer.getSubscriptions().getData().get(0);
            if (cancelAtPeriodEnd) {
                Map<String, Object> params = new HashMap<>();
                params.put("cancel_at_period_end", true);
                return subscription.update(params);
            } else {
                return subscription.cancel();
            }
        } catch (StripeException e) {
            log.info("Could not cancel Stripe subscription for: " + email);
            throw new RuntimeException(e);
        }
    }

    /**
     * Get a Stripe customer by Pathmind user email.
     *
     * @param email
     * @return
     * @throws StripeException
     */
    public Customer getCustomer(String email) throws StripeException {
        final PathmindUser pathmindUser = userDAO.findByEmailIgnoreCase(email);
        if (pathmindUser.getStripeCustomerId() != null) {
            return Customer.retrieve(pathmindUser.getStripeCustomerId());
        } else {
            return null;
        }
    }

    /**
     * Checks whether the user has an active recurring Professional subscription.
     *
     * @param email Pathmind user email address
     * @return true if the user has an ongoing subscription, false if not.
     */
    public Result<Boolean, StripeError> userHasActiveProfessionalSubscription(String email) {
        Result<Subscription, StripeError> result = getActiveSubscriptionOfUser(email);
        if (result.error != null) {
            return new Result<>(false, result.error);
        }
        return new Result<>("active".equalsIgnoreCase(result.getResult().getStatus()), null);
    }

    /**
     * Retrieve user's active subscription.
     *
     * @param email Pathmind user email address
     * @return ongoing subscription if available, otherwise null.
     */
    public Result<Subscription, StripeError> getActiveSubscriptionOfUser(String email) {
        Customer customer = null;
        try {
            customer = getCustomer(email);
        } catch (StripeException e) {
            log.error("Failed to retrieve customer from Stripe: {}", email);
        }
        if (customer == null) {
            return new Result<>(null, StripeError.NoUserFound);
        }
        if (customer.getSubscriptions() == null || CollectionUtils.isEmpty(customer.getSubscriptions().getData())) {
            return new Result<>(null, StripeError.NoSubscriptionExist);
        }
        return new Result<>(customer.getSubscriptions().getData().get(0), null);
    }


    public enum StripeError {
        NoUserFound,
        NoSubscriptionExist
        ;
    };

    @lombok.Value
    public static class Result<T, Error> {
        T result;
        Error error;
    }

}
