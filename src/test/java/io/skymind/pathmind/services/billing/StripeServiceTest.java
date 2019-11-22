package io.skymind.pathmind.services.billing;

import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Plan;
import com.stripe.model.Product;
import io.skymind.pathmind.PathmindApplicationTests;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Ignore
public class StripeServiceTest extends PathmindApplicationTests
{

	@Autowired
	private StripeService stripeService;
	@Value("${pathmind.stripe.professional-plan-id}")
	private String professionalPlanId;

	private String customerEmail = "vesa@vaadin.com";

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
	public void testGetCustomer() throws StripeException
	{
		final Customer customer = stripeService.getCustomer(customerEmail);
		assertEquals("Vesa Nieminen", customer.getName());
	}

	@Test
	public void testCustomerAlreadyExists()
	{
		assertTrue(stripeService.customerAlreadyExists(customerEmail));
	}

	@Test
	public void testUserHasActiveProfessionalSubscription()
	{
		assertTrue(stripeService.userHasActiveProfessionalSubscription(customerEmail));
	}

}

