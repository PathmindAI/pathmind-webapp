package io.skymind.pathmind.webapp.ui.views.header.components;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.templatemodel.TemplateModel;
import io.skymind.pathmind.shared.data.PathmindUser;
import io.skymind.pathmind.webapp.security.CurrentUser;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

@Tag("request-onboarding-service-button")
@JsModule("./src/components/header/request-onboarding-service-button.js")
@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RequestOnboardingServiceButton extends PolymerTemplate<RequestOnboardingServiceButton.Model> {

    public RequestOnboardingServiceButton(CurrentUser currentUser, String stripePublicKey, String pathmindApiUrl) {
        PathmindUser user = currentUser.getUser();
        getModel().setUserAPIKey(user.getApiKey());
        getModel().setKey(stripePublicKey);
        getModel().setApiUrl(pathmindApiUrl);
    }

    public interface Model extends TemplateModel {

        void setUserAPIKey(String key);

        void setKey(String key);

        void setApiUrl(String apiUrl);

    }
}
