package io.skymind.pathmind.webapp.ui.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.Route;

import org.springframework.beans.factory.annotation.Autowired;

import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;

@Route(value = Routes.ONBOARDING_PAYMENT_SUCCESS, layout = MainLayout.class)
public class OnboardingPaymentSuccessView extends PathMindDefaultView {

    private final OnboardingPaymentSuccessViewContent onboardingPaymentSuccessViewContent;

    @Autowired
    public OnboardingPaymentSuccessView(OnboardingPaymentSuccessViewContent onboardingPaymentSuccessViewContent) {
        this.onboardingPaymentSuccessViewContent = onboardingPaymentSuccessViewContent;
    }

    @Override
    protected Component getTitlePanel() {
        return null;
    }

    @Override
    protected Component getMainContent() {
        return onboardingPaymentSuccessViewContent;
    }

}
