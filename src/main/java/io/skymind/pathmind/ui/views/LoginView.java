package io.skymind.pathmind.ui.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.router.*;
import io.skymind.pathmind.db.ProjectRepository;
import io.skymind.pathmind.db.UserRepository;
import io.skymind.pathmind.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;

@Route("login")
public class LoginView extends LoginOverlay implements BeforeEnterObserver // , AfterNavigationObserver
{
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private SecurityService securityService;

	public LoginView()
	{
		LoginI18n loginForm = LoginI18n.createDefault();
		loginForm.setHeader(new LoginI18n.Header());
		loginForm.getHeader().setTitle("Pathmind");
		loginForm.setForm(new LoginI18n.Form());
		loginForm.getForm().setSubmit("Sign in >");
		setI18n(loginForm);
		setForgotPasswordButtonVisible(false);

		addLoginListener(e ->
		{
		 	if(securityService.isAuthenticatedUser(e.getUsername(), e.getPassword(), userRepository))
				UI.getCurrent().navigate(DashboardView.class);
			else
			 	setError(true);
		});
	}

//	@Override
//	public void afterNavigation(AfterNavigationEvent event)
//	{
////		setError(event.getLocation().getQueryParameters().getParameters().containsKey("error"));
//	}

	@Override
	public void beforeEnter(BeforeEnterEvent event)
	{
		// TODO -> Password needs to be encrypted with a one-way encryption algorithm.
		if (securityService.isUserLoggedIn()) {
			event.forwardTo(DashboardView.class);
		} else {
			if(!isOpened())
				setOpened(true);
		}
	}
}
