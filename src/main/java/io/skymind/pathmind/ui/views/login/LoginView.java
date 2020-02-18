package io.skymind.pathmind.ui.views.login;

import static io.skymind.pathmind.ui.constants.CssMindPathStyles.WELCOME_TEXT;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.shared.communication.PushMode;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import io.skymind.pathmind.data.PathmindUser;
import io.skymind.pathmind.db.dao.ProjectDAO;
import io.skymind.pathmind.security.Routes;
import io.skymind.pathmind.security.SecurityUtils;
import io.skymind.pathmind.services.UserService;
import io.skymind.pathmind.services.notificationservice.EmailNotificationService;
import io.skymind.pathmind.ui.components.LabelFactory;
import io.skymind.pathmind.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.ui.utils.NotificationUtils;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.ui.views.dashboard.DashboardView;
import io.skymind.pathmind.ui.views.project.NewProjectView;

@Route(Routes.LOGIN_URL)
@Theme(Lumo.class)
@CssImport(value = "./styles/components/vaadin-button.css", themeFor = "vaadin-button")
@CssImport(value = "./styles/views/pathmind-dialog-view.css")
@CssImport(value = "./styles/views/login-view-styles.css")
@CssImport(value = "./styles/components/vaadin-login-form-wrapper.css", themeFor = "vaadin-login-form-wrapper")
public class LoginView extends HorizontalLayout
		implements PublicView, AfterNavigationObserver, BeforeEnterObserver, HasUrlParameter<String>
{
	private Div badCredentials = new Div();
	private HorizontalLayout emailNotVerified = WrapperUtils.wrapWidthFullHorizontal();

	@Autowired
	private ProjectDAO projectDAO;

	@Autowired
	private EmailNotificationService emailNotificationService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private SegmentIntegrator segmentIntegrator;

	private String errorMessage;
	private String email;

	public LoginView(@Value("${pathmind.privacy-policy.url}") String privacyPolicyUrl,
					 @Value("${pathmind.terms-of-use.url}") String termsOfUseUrl)
	{
		addClassName("login-panel-cont");
		Span welcome = LabelFactory.createLabel("Welcome to", WELCOME_TEXT);
		Image img = new Image("frontend/images/pathmind-logo.png", "Pathmind logo");
		img.setClassName("logo");
		img.setWidth("200px");

		H3 title = new H3("Sign In");

		badCredentials.add(new Span("Incorrect username or password"));
		badCredentials.setClassName("error-message");
		badCredentials.setVisible(false);

		updateEmailNotVerified();

		Div innerContent = new Div();
		innerContent.setClassName("inner-content");
		// Temporarily block new signups for public beta - issue https://github.com/SkymindIO/pathmind-webapp/issues/356
		// innerContent.add(title, badCredentials, emailNotVerified, createLoginForm(), createSignUp());
		innerContent.add(title, badCredentials, emailNotVerified, createLoginForm());

		Anchor termsLink = new Anchor(termsOfUseUrl, "Terms of Use");
		termsLink.setTarget("_blank");

		Anchor privacyLink = new Anchor(privacyPolicyUrl, "Privacy Policy");
		privacyLink.setTarget("_blank");

		Div policy = new Div();
		policy.addClassName("policy");
		policy.add(new Span("By clicking Sign In, you agree to Pathmind's "),
				termsLink,
				new Span(" and "),
				privacyLink,
				new Span("."));

		Div loginPanel = new Div();
		add(loginPanel);
		loginPanel.setClassName("content");
		loginPanel.add(welcome, img, innerContent, policy);
		setSpacing(false);
	}

	private void updateEmailNotVerified() {
		Button resendVerification = new Button("Resend");
		resendVerification.getElement().setAttribute("title", "Send verification email again.");
		resendVerification.addClickListener(e -> {
			PathmindUser user = userService.findByEmailIgnoreCase(email);
			if (user != null) {
				emailNotificationService.sendVerificationEmail(user);
				NotificationUtils.showNotification("Email verification was sent to your email.",
						NotificationVariant.LUMO_SUCCESS);
			} else {
				NotificationUtils.showNotification("Email: " + email + " was not found. Please try to login again.",
						NotificationVariant.LUMO_ERROR);
			}
		});

		emailNotVerified.setSpacing(false);
		emailNotVerified.setPadding(false);
		emailNotVerified.setVisible(false);
		emailNotVerified.addClassName("email-not-verified-cont");
		emailNotVerified.addClassName("error-message");
		emailNotVerified.add(new Span("Email is not verified"), resendVerification);
	}

	private Component createSignUp() {
		Span dontHaveAccount = new Span("Don't have an account?");
		dontHaveAccount.getStyle().set("color", "var(--lumo-secondary-text-color)");
		RouterLink start = new RouterLink("Get started", SignUpView.class);

		Div signUpCont = new Div();
		signUpCont.addClassName("account-help-wrapper");
		signUpCont.add(dontHaveAccount, start);

		return signUpCont;
	}

	private Component createLoginForm() {
		LoginI18n loginI18n = LoginI18n.createDefault();
		loginI18n.setHeader(new LoginI18n.Header());
		loginI18n.getHeader().setTitle("");
		loginI18n.getForm().setUsername("Email");
		loginI18n.getForm().setSubmit("Sign in");
		loginI18n.getForm().setForgotPassword("Forgot your password?");

		LoginForm loginForm = new LoginForm();
		loginForm.setI18n(loginI18n);
		loginForm.setAction(Routes.LOGIN_URL);
		loginForm.addForgotPasswordListener(e -> UI.getCurrent().navigate(ResetPasswordView.class));
		loginForm.addLoginListener(evt -> segmentIntegrator.userLoggedIn());
		return loginForm;
	}

	private Class getRerouteClass() {
		if(projectDAO.getProjectsForUser(SecurityUtils.getUserId()).isEmpty())
			return NewProjectView.class;
		return DashboardView.class;
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event)
	{
		if (SecurityUtils.isUserLoggedIn()) {
			event.forwardTo(getRerouteClass());
			// Make sure automatic push mode is enabled. If we don't do this, automatic push
			// won't work even we have proper annotations in place.
			UI.getCurrent().getPushConfiguration().setPushMode(PushMode.AUTOMATIC);
			return;
		}
		add(segmentIntegrator);
	}

	@Override
	public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		emailNotVerified.setVisible(false);
		badCredentials.setVisible(false);

		if (errorMessage == null)
			return;

		if (Routes.BAD_CREDENTIALS.equals(errorMessage)) {
			badCredentials.setVisible(true);
		} else if (Routes.EMAIL_VERIFICATION_FAILED.equals(errorMessage)) {
			List<String> params = event.getLocation().getQueryParameters().getParameters().get("email");
			if (params != null && !params.isEmpty()) {
				email = params.get(0);
			}

			emailNotVerified.setVisible(true);
		}
	}
}
