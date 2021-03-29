package io.skymind.pathmind.api.domain;

import java.io.IOException;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;

import io.skymind.pathmind.api.conf.security.PathmindApiUser;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class StripeAPIService {

    private final String webappDomainUrl;

    @Value("${pathmind.stripe.secret.key}")
    private String secretKey;

    private String stripeWebhookSecret = "whsec_WfPcjszxi6AvNU1mzMThbJkuGbbHYHl3";

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

    public StripeAPIService(@Value("${pm.api.webapp.url}") String webappDomainUrl) {
        this.webappDomainUrl = webappDomainUrl;
    }

    @PostMapping(path = "/create-checkout-session", produces = MediaType.APPLICATION_JSON_VALUE)
    public HashMap<String, String> CreateCheckoutSession(@AuthenticationPrincipal PathmindApiUser pmUser) {
        SessionCreateParams params = 
            SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(webappDomainUrl + "/onboarding-payment-success")
                .setCancelUrl(webappDomainUrl)
                .addLineItem(
                SessionCreateParams.LineItem.builder()
                    .setQuantity(1L)
                    .setPriceData(
                    SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency("usd")
                        .setUnitAmount(50000L)
                        .setProductData(
                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                            .setName("Exclusive Onboarding Service")
                            .setDescription("We will assign a Customer Success Specialist to help with configuring your simulation model for Pathmind. If you would like an in-depth walkthrough, sign up now!")
                            .build())
                        .build())
                    .build())
                .build();
        Session session;
        HashMap<String, String> responseData = new HashMap<String, String>();
        try {
            session = Session.create(params);
            responseData.put("id", session.getId());
        } catch (StripeException e) {
            e.printStackTrace();
        }
        return responseData;
    }

    @PostMapping(path = "/stripe-webhook")
    public String StripeWebhook(HttpServletRequest request, HttpServletResponse response) {
        try {
            String payload = IOUtils.toString(request.getInputStream(), "UTF-8");
            String sigHeader = request.getHeader("Stripe-Signature");
            Event event = null;
            
            try {
                event = Webhook.constructEvent(payload, sigHeader, stripeWebhookSecret);
            } catch (JsonSyntaxException e) {
                System.out.println("Invalid payload");
                response.setStatus(400);
                return "";
            } catch (SignatureVerificationException e) {
                System.out.println("Invalid signature");
                System.out.println(e);
                response.setStatus(400);
                return "";
            }

            EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
            StripeObject stripeObject = null;
            if (dataObjectDeserializer.getObject().isPresent()) {      
                stripeObject = dataObjectDeserializer.getObject().get();
                System.out.println("Checkout session completed");
                System.out.println(stripeObject);
            } else {
            // Handle absent data object with `deserializeUnsafe` or `deserializeUnsafeWith`.
            // Please see the usage details in java doc on the class `EventDataObjectDeserializer`.
            }

            switch (event.getType()) {
                case "payment_intent.succeeded":
                    PaymentIntent paymentIntent = (PaymentIntent) stripeObject;
                    System.out.println("PaymentIntent was successful!");
                    break;
                case "charge.refunded":
                    Gson gson = new Gson();
                    Charge charge = gson.fromJson(event.getData().getObject().toJson(), Charge.class);
                    // Call your service here with your Charge object.
                    break;
                case "checkout.session.completed":
                    System.out.println("checkout.session.completed");
                    break;
                default:
                    System.out.println("Unhandled event type: " + event.getType());
            }

            response.setStatus(200);
        } catch (IOException e) {
            System.out.println("IOException");
            response.setStatus(400);
        }
        return "";
    }
}
