package io.skymind.pathmind.api.domain;

import java.util.HashMap;

import javax.annotation.PostConstruct;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import io.skymind.pathmind.api.conf.security.PathmindApiUser;
import lombok.extern.slf4j.Slf4j;
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
                .setSuccessUrl(webappDomainUrl + "/success.html")
                .setCancelUrl(webappDomainUrl + "/cancel.html")
                .addLineItem(
                SessionCreateParams.LineItem.builder()
                    .setQuantity(1L)
                    .setPriceData(
                    SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency("usd")
                        .setUnitAmount(2000L)
                        .setProductData(
                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                            .setName("Stubborn Attachments")
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


}
