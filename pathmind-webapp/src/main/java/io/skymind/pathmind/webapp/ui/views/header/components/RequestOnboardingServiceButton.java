package io.skymind.pathmind.webapp.ui.views.header.components;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.spring.annotation.SpringComponent;
import io.skymind.pathmind.shared.data.PathmindUser;
import io.skymind.pathmind.webapp.security.CurrentUser;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import com.vaadin.flow.component.littemplate.LitTemplate;

@Tag("request-onboarding-service-button")
@JsModule("./src/components/header/request-onboarding-service-button.ts")
@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RequestOnboardingServiceButton extends LitTemplate {
    public RequestOnboardingServiceButton(CurrentUser currentUser, String stripePublicKey, String pathmindApiUrl) {
        PathmindUser user = currentUser.getUser();
        getElement().setProperty("userAPIKey", user.getApiKey());
        getElement().setProperty("key", stripePublicKey);
        getElement().setProperty("apiUrl", pathmindApiUrl);
    }
}
