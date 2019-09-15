package io.skymind.pathmind.ui.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.db.ProjectRepository;
import io.skymind.pathmind.db.UserRepository;
import io.skymind.pathmind.security.SecurityUtils;
import io.skymind.pathmind.ui.views.project.NewProjectView;
import io.skymind.pathmind.ui.views.project.ProjectView;
import io.skymind.pathmind.ui.views.project.ProjectsView;
import org.springframework.beans.factory.annotation.Autowired;

@Route("login")
public class LoginView extends LoginOverlay implements BeforeEnterObserver // , AfterNavigationObserver
{
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProjectRepository projectRepository;

	public LoginView()
	{
		LoginI18n loginForm = LoginI18n.createDefault();
		loginForm.setHeader(new LoginI18n.Header());
		loginForm.getHeader().setTitle("Pathmind");
		loginForm.setAdditionalInformation("By clicking Log In, you agree to Pathmind's Terms of Use and Privacy Policy");
		loginForm.getForm().setUsername("Email");
		setI18n(loginForm);

		addLoginListener(e -> handleLogin(e));
	}

	private void handleLogin(LoginEvent e) {
		if(SecurityUtils.isAuthenticatedUser(e.getUsername(), e.getPassword(), userRepository))
			navigateToEntryView();
		else
			setError(true);
	}

	private void navigateToEntryView() {
		UI.getCurrent().navigate(getRerouteClass());
	}

	private Class getRerouteClass() {
		if(projectRepository.getProjectsForUser().isEmpty())
			return NewProjectView.class;
		return ProjectsView.class;
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event)
	{
		if (SecurityUtils.isUserLoggedIn()) {
			event.forwardTo(getRerouteClass());
			return;
		}

		// Just a quick sanity check.
		if (!isOpened())
			setOpened(true);
	}
}
