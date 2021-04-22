package io.skymind.pathmind.webapp.ui.views;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.templatemodel.TemplateModel;

import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.BeanDefinition;

@Tag("onboarding-payment-success-view")
@JsModule("./src/pages/onboarding-payment-success-view.js")
@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class OnboardingPaymentSuccessViewContent extends PolymerTemplate<TemplateModel> {
    public OnboardingPaymentSuccessViewContent() {
    }
}
