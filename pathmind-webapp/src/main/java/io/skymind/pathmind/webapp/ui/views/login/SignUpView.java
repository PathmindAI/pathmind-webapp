package io.skymind.pathmind.webapp.ui.views.login;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.templatemodel.TemplateModel;
import io.skymind.pathmind.services.notificationservice.EmailNotificationService;
import io.skymind.pathmind.shared.data.PathmindUser;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.webapp.security.UserService;
import io.skymind.pathmind.webapp.ui.binders.PathmindUserBinders;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@Tag("sign-up-view")
@CssImport(value = "./styles/views/sign-up-view.css", id = "sign-up-view-styles")
@JsModule("./src/pages/account/sign-up-view.js")
@Route(value = Routes.SIGN_UP)
public class SignUpView extends PolymerTemplate<SignUpView.Model> implements PublicView, HasUrlParameter<String> {
    @Id("lastName")
    private TextField lastName;

    @Id("firstName")
    private TextField firstName;

    @Id("email")
    private TextField email;

    @Id("signIn")
    private Button signIn;

    @Id("newPassword")
    private PasswordField newPassword;

    @Id("confirmNewPassword")
    private PasswordField confirmNewPassword;

    @Id("newPassNotes")
    private VerticalLayout passwordValidationNotes;

    private final UserService userService;
    private final EmailNotificationService emailNotificationService;
    private final SegmentIntegrator segmentIntegrator;

    private final AuthenticationManager authenticationManager;

    private PathmindUser user;
    private Binder<PathmindUser> binder;

    public SignUpView(UserService userService, AuthenticationManager authenticationManager,
                      EmailNotificationService emailNotificationService, SegmentIntegrator segmentIntegrator,
                      @Value("${pathmind.contact-support.address}") String contactLink) {
        this.userService = userService;
        this.emailNotificationService = emailNotificationService;
        this.segmentIntegrator = segmentIntegrator;
        this.authenticationManager = authenticationManager;
        getModel().setContactLink(contactLink);
        user = new PathmindUser();
        initView();
        initBinder();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        getElement().appendChild(segmentIntegrator.getElement());
    }

    private void initView() {
        passwordValidationNotes.setSpacing(false);
        passwordValidationNotes.setPadding(false);
        newPassword.setRequired(true);
        confirmNewPassword.setRequired(true);

        signIn.addClickListener(e -> {
            final String passwordValue = newPassword.getValue();
            UserService.PasswordValidationResults validationResults = userService
                    .validatePassword(passwordValue, confirmNewPassword.getValue());

            if (binder.validate().isOk() && validationResults.isOk()) {
                user.setPassword(passwordValue);
                user = userService.signup(user);
                emailNotificationService.sendVerificationEmail(user, user.getEmail(), true);
                segmentIntegrator.userRegistered(user);

                loginUser(user.getEmail(), passwordValue);

                getUI().ifPresent(ui -> ui.navigate(VerificationEmailSentView.class));
            } else {
                newPassword.setInvalid(true);
                passwordValidationNotes.removeAll();
                validationResults.getPasswordValidationErrors().forEach(message -> passwordValidationNotes.add(new Span(message)));
                confirmNewPassword.setInvalid(!validationResults.getConfirmPasswordValidationError().isEmpty());
                confirmNewPassword.setErrorMessage(validationResults.getConfirmPasswordValidationError());
            }
        });
    }

    private void loginUser(String emailValue, String passwordValue) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(emailValue, passwordValue);
        VaadinRequest vaadinRequest = VaadinService.getCurrentRequest();
        HttpServletRequest request = ((VaadinServletRequest)vaadinRequest).getHttpServletRequest();
        token.setDetails(new WebAuthenticationDetails(request));
        Authentication authenticatedUser = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
    }

    private void initBinder() {
        binder = new Binder<>(PathmindUser.class);
        binder.addStatusChangeListener(evt -> processValidationStatusChange(evt.hasValidationErrors()));
        PathmindUserBinders.bindEmail(userService, binder, email);
        PathmindUserBinders.bindFirstName(binder, firstName);
        PathmindUserBinders.bindLastName(binder, lastName);

        binder.setBean(user);
    }

    private void processValidationStatusChange(boolean hasValidationErrors) {
        getModel().setIsEmailUsed(hasValidationErrors && userService.findByEmailIgnoreCase(email.getValue()) != null);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        Location location = event.getLocation();
        QueryParameters queryParameters = location.getQueryParameters();

        Map<String, List<String>> parametersMap = queryParameters.getParameters();
        if (parametersMap != null) {
            List<String> planParamList = parametersMap.get("plan");
            if (planParamList != null && planParamList.size() > 0) {
                segmentIntegrator.marketingSiteLead(planParamList.get(0));
            }
        }
    }

    public interface Model extends TemplateModel {
        void setIsEmailUsed(Boolean isEmailUsed);

        void setContactLink(String contactLink);
    }
}
