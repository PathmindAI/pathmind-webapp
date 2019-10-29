package io.skymind.pathmind.services.billing;

import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import io.skymind.pathmind.PathmindApplicationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class StripeServiceTest extends PathmindApplicationTests
{

	@Autowired
	private StripeService stripeService;

	@Test
	public void test() throws StripeException
	{
		// Set your secret key: remember to change this to your live secret key in production
		// See your keys here: https://dashboard.stripe.com/account/apikeys

		Map<String, Object> paymentIntentParams = new HashMap<String, Object>();
		paymentIntentParams.put("amount", 1000);
		paymentIntentParams.put("currency", "eur");
		ArrayList payment_method_types = new ArrayList();
		payment_method_types.add("card");
		paymentIntentParams.put("payment_method_types", payment_method_types);
		paymentIntentParams.put("receipt_email", "jenny.rosen@example.com");

		PaymentIntent paymentIntent = stripeService.createPayment(paymentIntentParams);
		//paymentIntent.setPaymentMethod();
		//assertEquals(I//PaymentIntent.t, paymentIntent.getStatus());
	}

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
		int i = 0;
	}


	@Test
	public void testPaymentIntent() throws StripeException
	{
		PaymentIntentCreateParams createParams = new PaymentIntentCreateParams.Builder()
				.setCurrency("usd").setAmount(1099L)
				.build();

		PaymentIntent intent = PaymentIntent.create(createParams);

	}

}

