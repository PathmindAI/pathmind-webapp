package io.skymind.pathmind.services.billing;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;

@Service
public class StripeService
{

	@Value("${stripe.public.key}")
	private String publicKey;

	@Value("${stripe.secret.key}")
	private String secretKey;

	@PostConstruct
	public void init()
	{
		Stripe.apiKey = secretKey;
	}

	public PaymentIntent createPayment(Map<String, Object> paymentIntentParams) throws StripeException
	{
		return PaymentIntent.create(paymentIntentParams);
	}
}
