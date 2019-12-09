package io.skymind.pathmind.services.billing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Plan;
import com.stripe.model.Product;

import io.skymind.pathmind.PathmindApplicationTests;

public class StripeServiceTest extends PathmindApplicationTests
{

	@Autowired
	private StripeService stripeService;
	@Value("${pathmind.stripe.professional-plan-id}")
	private String professionalPlanId;

	private String customerEmail = "test_pathmind@skymind.io";

	@Test
	public void testProfessionalProductExists() throws StripeException
	{
		final Product pathmindProfessionalProduct = Product.retrieve("prod_G2AGAGjNJIkUbv");
		assertTrue(pathmindProfessionalProduct.getActive());
		assertEquals("Pathmind Professional", pathmindProfessionalProduct.getName());
	}

	@Test
	public void testProfessionalPlanExists() throws StripeException
	{
		final Plan pathmindProfessionalPlan = Plan.retrieve(professionalPlanId);
		assertTrue(pathmindProfessionalPlan.getActive());
		assertTrue(50000L == pathmindProfessionalPlan.getAmount());
		assertEquals("month", pathmindProfessionalPlan.getInterval());
	}

	@Test
	public void testCustomerAlreadyExists()
	{
		assertTrue(stripeService.customerAlreadyExists(customerEmail));
	}
	
	@Test
	public void testGetCustomer() throws StripeException
	{
		final Customer customer = stripeService.getCustomer(customerEmail);
		assertEquals("Pathmind Test", customer.getName());
	}


	@Test
	public void testUserHasActiveProfessionalSubscription()
	{
		assertFalse(stripeService.userHasActiveProfessionalSubscription(customerEmail));
	}

	@Test
	public void testSubscribeAndCancellation() throws StripeException
	{
		final Customer customer = stripeService.getCustomer(customerEmail);
		assertNotNull(stripeService.createSubscription(customer));
		assertTrue(stripeService.userHasActiveProfessionalSubscription(customerEmail));
		stripeService.cancelSubscription(customerEmail, false);
		assertFalse(stripeService.userHasActiveProfessionalSubscription(customerEmail));
	}

}

