package io.skymind.pathmind.api.domain;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.stripe.Stripe;
import com.stripe.exception.EventDataObjectDeserializationException;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;

import io.skymind.pathmind.api.conf.security.PathmindApiUser;
import io.skymind.pathmind.db.dao.UserDAO;
import io.skymind.pathmind.services.analytics.SegmentTrackerService;
import io.skymind.pathmind.shared.data.PathmindUser;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    // TODO -> This is currently Fionna's local webhook secret key for her own stripe ac
    private String stripeWebhookSecret = "whsec_WfPcjszxi6AvNU1mzMThbJkuGbbHYHl3";

    @Autowired
    private SegmentTrackerService segmentTrackerService;

    @Autowired
    private UserDAO userDAO;

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
                response.setStatus(400);
                return "";
            }

            switch (event.getType()) {
                case "payment_intent.succeeded":
                    System.out.println("PaymentIntent was successful!");
                    break;
                case "checkout.session.completed":
                    EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
                    StripeObject stripeObject = null;
                    if (dataObjectDeserializer.getObject().isPresent()) {      
                        stripeObject = dataObjectDeserializer.getObject().get();
                    } else {
                        // Handle absent data object with `deserializeUnsafe` or `deserializeUnsafeWith`.
                        // Please see the usage details in java doc on the class `EventDataObjectDeserializer`.
                        try {
                            stripeObject = dataObjectDeserializer.deserializeUnsafe();
                        } catch (EventDataObjectDeserializationException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Checkout session completed");
                    JsonObject obj = JsonParser.parseString(stripeObject.toJson()).getAsJsonObject();
                    String customerEmail = obj.get("customer_details").getAsJsonObject().get("email").toString();
                    if (customerEmail != null) {
                        PathmindUser user = userDAO.findByEmailIgnoreCase(customerEmail);
                        String paymentStatus = obj.get("payment_status").toString();
                        System.out.println(user);
                        Map<String, String> properties = new HashMap<>();
                        properties.put("payment_status", paymentStatus);
                        if (user != null) {
                            segmentTrackerService.onboardingServicePaid(user.getId(), properties);
                        } else {
                            System.out.println("User who paid for onboarding service with email "+customerEmail+" is not found.");
                        }
                    }
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
