package io.skymind.pathmind.security;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;
import io.skymind.pathmind.data.PathmindUser;
import io.skymind.pathmind.db.UserRepository;
import io.skymind.pathmind.ui.views.LoginView;

public class SecurityUtils
{
	private SecurityUtils()
	{
	}

	public static boolean isAuthenticatedUser(String email, String password, UserRepository userRepository)
	{
		PathmindUser user = userRepository.getUserByEmailAndPassword(email, password);

		if(user != null)
			VaadinSession.getCurrent().setAttribute(PathmindUser.class, user);
		return user != null;
	}

	public static boolean isUserLoggedIn()
	{
		return getUser() != null;
	}

	public static void logout() {
		VaadinSession.getCurrent().setAttribute(PathmindUser.class, null);
		UI.getCurrent().navigate(LoginView.class);
	}

	public static PathmindUser getUser() {
		if(VaadinSession.getCurrent().getAttribute(PathmindUser.class) != null)
			return VaadinSession.getCurrent().getAttribute(PathmindUser.class);
		return null;
	}
}
