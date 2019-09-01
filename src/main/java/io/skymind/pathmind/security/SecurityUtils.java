package io.skymind.pathmind.security;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;
import io.skymind.pathmind.data.User;
import io.skymind.pathmind.db.UserRepository;
import io.skymind.pathmind.ui.views.LoginView;

public class SecurityUtils
{
	private SecurityUtils()
	{
	}

	public static boolean isAuthenticatedUser(String email, String password, UserRepository userRepository)
	{
		User user = userRepository.getUserByEmailAndPassword(email, password);

		if(user != null)
			VaadinSession.getCurrent().setAttribute(User.class, user);
		return user != null;
	}

	public static boolean isUserLoggedIn()
	{
		return getUser() != null;
	}

	public static void logout() {
		VaadinSession.getCurrent().setAttribute(User.class, null);
		UI.getCurrent().navigate(LoginView.class);
	}

	public static User getUser() {
		if(VaadinSession.getCurrent().getAttribute(User.class) != null)
			return VaadinSession.getCurrent().getAttribute(User.class);
		return null;
	}
}
