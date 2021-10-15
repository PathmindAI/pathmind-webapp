package io.skymind.pathmind.webapp.ui.views.login;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.shared.data.PathmindUser;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.webapp.security.UserService;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@Tag("email-verification-view")
@JsModule("./src/pages/account/email-verification-view.ts")
@Route(value = Routes.EMAIL_VERIFICATION)
public class EmailVerificationView extends LitTemplate
        implements PublicView, HasUrlParameter<String>, AfterNavigationObserver {
    @Id("backToApp")
    private Button backToApp;

    @Autowired
    private UserService userService;

    @Autowired
    private SegmentIntegrator segmentIntegrator;

    private String token;
    private PathmindUser verifiedUser;

    public EmailVerificationView(@Value("${pathmind.contact-support.address}") String contactLink) {
        getElement().setProperty("contactLink", contactLink);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        getElement().appendChild(segmentIntegrator.getElement());
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String param) {
        token = param;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        backToApp.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(LoginView.class)));

        try {
            PathmindUser user = userService.verifyEmailByToken(token);
            if (user == null) {
                getElement().setProperty("error", true);
                return;
            } else {
                verifiedUser = user;
                segmentIntegrator.emailVerified(verifiedUser);
            }

            getElement().setProperty("error", false);
        } catch (IllegalArgumentException e) {
            getElement().setProperty("error", true);
        }
    }
}
