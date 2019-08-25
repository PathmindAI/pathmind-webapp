package io.skymind.pathmind.security;

import com.vaadin.flow.server.VaadinSession;
import io.skymind.pathmind.data.User;
import io.skymind.pathmind.db.UserRepository;
import org.springframework.stereotype.Service;

/**
 * INFORMATION -> Temporary quick solution since I wasn't sure if Spring Authentication was used or some other
 * mechanisms. To save time I just quickly store the user in the session if they've logged in and then
 * look to see if they exist. Not very good but it works well enough for wireframing.
 */
@Service
public class SecurityService
{
	private SecurityService()
	{
	}

	public boolean isAuthenticatedUser(String email, String password, UserRepository userRepository)
	{
		User user = userRepository.findUserByEmailAndPassword(email, password);

		if(user != null)
			VaadinSession.getCurrent().setAttribute(User.class, user);
		return user != null;
	}

	public boolean isUserLoggedIn()
	{
		return VaadinSession.getCurrent().getAttribute(User.class) != null;
	}
}
