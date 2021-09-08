package io.skymind.pathmind.webapp.ui.views.login;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.services.notificationservice.EmailNotificationService;
import io.skymind.pathmind.shared.data.PathmindUser;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.webapp.security.UserService;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.utils.NotificationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@Tag("reset-password-view")
@JsModule("./src/pages/account/reset-password-view.ts")
@Route(value = Routes.RESET_PASSWORD)
@Slf4j
public class ResetPasswordView extends LitTemplate
        implements PublicView, HasUrlParameter<String>, AfterNavigationObserver {

    private static final String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
    private static final String SEND_CONFIRMATION = "Reset password email was sent.";
    private static final String LINK_IS_NOT_VALID = "Link is no longer valid. Please try to recover password again.";
    private static final String CHANGED_CONFIRMATION = "Password was successfully changed";

    @Id("email")
    private EmailField emailField;

    @Id("sendBtn")
    private Button sendBtn;

    @Id("changePassword")
    private Button changePassword;

    @Id("cancelBtn")
    private Button cancelBtn;

    @Id("newPassword")
    private PasswordField newPassword;

    @Id("confirmNewPassword")
    private PasswordField confirmNewPassword;

    @Id("newPassNotes")
    private VerticalLayout passwordValidationNotes;

    @Id("prePart")
    private VerticalLayout prePart;

    @Id("postPart")
    private VerticalLayout postPart;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailNotificationService emailNotificationService;

    @Autowired
    private SegmentIntegrator segmentIntegrator;

    @Value("${pathmind.reset.password.link.valid}")
    private int resetTokenValidHours;

    private String token = null;


    public ResetPasswordView(@Value("${pathmind.contact-support.address}") String contactLink) {
        getElement().setProperty("contactLink", contactLink);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        resetLayouts();

        if (token == null) {
            initPreStep();
        } else {
            initPostStep();
        }
    }

    private void resetLayouts() {
        prePart.setSpacing(false);
        prePart.setPadding(false);

        postPart.setSpacing(false);
        postPart.setPadding(false);

        passwordValidationNotes.setSpacing(false);
        passwordValidationNotes.setPadding(false);
    }

    private void initPreStep() {
        prePart.setVisible(true);
        postPart.setVisible(false);

        emailField.setInvalid(false);
        emailField.setValueChangeMode(ValueChangeMode.ON_CHANGE);

        cancelBtn.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(LoginView.class)));
        sendBtn.addClickListener(e -> {
            if (isValid(emailField.getValue())) {
                startResetProcess(emailField.getValue());
            } else {
                emailField.setInvalid(true);
            }
        });
    }

    private void initPostStep() {
        prePart.setVisible(false);
        postPart.setVisible(true);

        try {
            PathmindUser user = userService.findByToken(token);
            if (user == null || user.getPasswordResetSendAt() == null ||
                    LocalDateTime.now().minus(resetTokenValidHours, ChronoUnit.HOURS).isAfter(user.getPasswordResetSendAt())) {
                linkIsNotValid();
                return;
            }

            changePassword.addClickListener(e -> {
                UserService.PasswordValidationResults validationResults = userService
                        .validatePassword(newPassword.getValue(), confirmNewPassword.getValue());

                if (validationResults.isOk()) {
                    userService.changePassword(user, newPassword.getValue());
                    user.setPasswordResetSendAt(null);
                    userService.update(user);
                    NotificationUtils.showSuccess(CHANGED_CONFIRMATION);
                    getUI().ifPresent(ui -> ui.navigate(LoginView.class));
                } else {
                    newPassword.setInvalid(true);
                    passwordValidationNotes.removeAll();
                    validationResults.getPasswordValidationErrors().forEach(message -> passwordValidationNotes.add(new Span(message)));
                    confirmNewPassword.setInvalid(!validationResults.getConfirmPasswordValidationError().isEmpty());
                    confirmNewPassword.setErrorMessage(validationResults.getConfirmPasswordValidationError());
                }
            });
        } catch (IllegalArgumentException e) {
            linkIsNotValid();
        }
    }

    private void linkIsNotValid() {
        getElement().setProperty("message", LINK_IS_NOT_VALID);
        token = null;
        initPreStep();
    }

    private void startResetProcess(String email) {
        PathmindUser user = userService.findByEmailIgnoreCase(email);
        NotificationUtils.showSuccess(SEND_CONFIRMATION);
        getElement().setProperty("message", "");

        if (user == null) {
            log.info("Attempt to restore password of not existing user, with email: " + email);
            return;
        }

        emailNotificationService.sendResetPasswordEmail(user);
    }

    static boolean isValid(String email) {
        return email.matches(EMAIL_REGEX);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        getElement().appendChild(segmentIntegrator.getElement());
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String param) {
        token = param;
    }
}
