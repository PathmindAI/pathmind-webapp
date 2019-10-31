package io.skymind.pathmind.services.billing;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import io.skymind.pathmind.data.PathmindUser;
import io.skymind.pathmind.db.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class StripeService
{

	@Value("${pathmind.stripe.public.key}")
	private String publicKey;

	@Value("${pathmind.stripe.secret.key}")
	private String secretKey;

	private final UserDAO userDAO;

	public StripeService(@Autowired UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	@PostConstruct
	public void init()
	{
		Stripe.apiKey = secretKey;
	}

	public PaymentIntent createPayment(Map<String, Object> paymentIntentParams) throws StripeException
	{
		return PaymentIntent.create(paymentIntentParams);
	}

	public Customer createCustomer(String email, String paymentMethod) throws StripeException
	{
		Map<String, Object> customerParams = new HashMap<>();
		customerParams.put("email", email);
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

	//public boolean doesCustomerExist(String email) {
	//
	//}

}
