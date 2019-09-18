package io.skymind.pathmind.security;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;
import io.skymind.pathmind.data.PathmindUser;
import io.skymind.pathmind.db.dao.UserDAO;
import io.skymind.pathmind.db.repositories.UserRepository;
import io.skymind.pathmind.ui.views.LoginView;

public class SecurityUtils
{
	private SecurityUtils()
	{
	}

	public static boolean isAuthenticatedUser(String email, String password, UserDAO userDAO)
	{
		PathmindUser user = userDAO.getUserByEmailAndPassword(email, password);

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

	// Will return null if no user is found in the session.
	public static PathmindUser getUser() {
		return VaadinSession.getCurrent().getAttribute(PathmindUser.class);
	}

	// TODO -> Deal with errors if no user is found in the session.
	public static long getUserId() {
		return getUser().getId();
	}
}
