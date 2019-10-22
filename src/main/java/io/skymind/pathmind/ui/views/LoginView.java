package io.skymind.pathmind.ui.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.InitialPageSettings;
import com.vaadin.flow.server.PageConfigurator;
import com.vaadin.flow.shared.communication.PushMode;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import io.skymind.pathmind.db.dao.ProjectDAO;
import io.skymind.pathmind.security.SecurityUtils;
import io.skymind.pathmind.ui.plugins.IntercomIntegrationPlugin;
import io.skymind.pathmind.ui.utils.VaadinUtils;
import io.skymind.pathmind.ui.views.account.ResetPasswordView;
import io.skymind.pathmind.ui.views.dashboard.DashboardView;
import io.skymind.pathmind.ui.views.project.NewProjectView;
import io.skymind.pathmind.utils.PathmindUtils;
import org.springframework.beans.factory.annotation.Autowired;

@Route("login")
@Theme(Lumo.class)
@CssImport(value = "./styles/components/vaadin-button.css", themeFor = "vaadin-button")
@CssImport(value = "./styles/views/login-view-styles.css")
@CssImport(value = "./styles/components/vaadin-login-form-wrapper.css", themeFor = "vaadin-login-form-wrapper")
public class LoginView extends HorizontalLayout
		implements AfterNavigationObserver, BeforeEnterObserver, HasDynamicTitle, PageConfigurator
{
	private Div error = new Div();

	@Autowired
	private ProjectDAO projectDAO;

	public LoginView(IntercomIntegrationPlugin intercomIntegrationPlugin)
	{
		addClassName("login-panel-cont");
		Label welcome = new Label("Welcome to");
		welcome.setClassName("welcome-text");
		Image img = new Image("frontend/images/pathmind-logo.png", "Pathmind logo");
		img.setClassName("logo");
		img.setWidth("200px");

		Div title = new Div();
		title.add(new Label("Sign in to your new account!"));
		title.setClassName("title");

		error.add(new H5("Incorrect username or password"));
		error.setClassName("error-message");
		error.setVisible(false);


		Div loginPanel = new Div();
		add(loginPanel);
		loginPanel.setClassName("login-panel");
		loginPanel.add(welcome, img, title, error, createLoginForm(), createSignUp());

		intercomIntegrationPlugin.addPluginToPage();
	}

	private Component createSignUp() {
		Label dontHaveAccount = new Label("Don't have an account?");
		dontHaveAccount.getStyle().set("color", "var(--pm-secondary-text-color)");
		Button start = new Button("Get started");
		start.setThemeName("tertiary");
		Anchor signUp = new Anchor("/sign-up");
		signUp.add(start);

		Div signUpCont = new Div();
		signUpCont.getStyle().set("flex-shrink", "0");
		signUpCont.add(dontHaveAccount, signUp);

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
		loginForm.setAction("login");
		loginForm.addForgotPasswordListener(e -> UI.getCurrent().navigate(ResetPasswordView.class));
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
	}

	@Override
	public String getPageTitle() {
		return PathmindUtils.getPageTitle();
	}

	@Override
	public void configurePage(InitialPageSettings settings) {
		VaadinUtils.setupFavIcon(settings);
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		error.setVisible(event.getLocation().getQueryParameters().getParameters().containsKey("error"));
	}
}
