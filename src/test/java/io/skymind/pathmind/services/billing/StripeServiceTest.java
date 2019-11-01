package io.skymind.pathmind.services.billing;

import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.param.PaymentIntentCreateParams;
import io.skymind.pathmind.PathmindApplicationTests;
import io.skymind.pathmind.data.PathmindUser;
import io.skymind.pathmind.db.dao.UserDAO;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Ignore
public class StripeServiceTest extends PathmindApplicationTests
{

	@Autowired
	private StripeService stripeService;
	@Autowired
	private UserDAO userDAO;
	@Value("${pathmind.stripe.professional-plan-id}")
	private String professionalPlanId;

	@Test
	public void products() throws StripeException
	{
		Map<String, Object> paymentIntentParams = new HashMap<String, Object>();
		paymentIntentParams.put("amount", 1000);
		paymentIntentParams.put("currency", "eur");
		ArrayList payment_method_types = new ArrayList();
		payment_method_types.add("card");
		paymentIntentParams.put("payment_method_types", payment_method_types);
		paymentIntentParams.put("receipt_email", "jenny.rosen@example.com");

		final Product prod_g2AGAGjNJIkUbv = Product.retrieve("prod_G2AGAGjNJIkUbv");
	}

	@Test
	public void plans() throws StripeException
	{
		Map<String, Object> planParams = new HashMap<String, Object>();
		planParams.put("limit", 3);

		final PlanCollection list = Plan.list(planParams);
	}


	@Test
	public void testPaymentIntent() throws StripeException
	{
		PaymentIntentCreateParams createParams = new PaymentIntentCreateParams.Builder()
				.setCurrency("usd").setAmount(1099L)
				.build();

		PaymentIntent intent = PaymentIntent.create(createParams);

	}

	@Test
	public void getCustomer() throws StripeException
	{
		final PathmindUser pathmindUser = userDAO.findByEmailIgnoreCase("vesa@vaadin.com");
		final Customer customer = Customer.retrieve(pathmindUser.getStripeCustomerId());
		final Plan plan = Plan.retrieve(professionalPlanId);
	}

}

