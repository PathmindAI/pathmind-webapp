package io.skymind.pathmind.security;

import com.vaadin.flow.server.ServletHelper.RequestType;
import com.vaadin.flow.shared.ApplicationConstants;
import io.skymind.pathmind.ui.views.CustomRouteNotFoundError;
import io.skymind.pathmind.ui.views.LoginView;
import io.skymind.pathmind.ui.views.account.EmailVerificationView;
import io.skymind.pathmind.ui.views.account.ResetPasswordView;
import io.skymind.pathmind.ui.views.account.SignUpView;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import java.util.stream.Stream;

public class SecurityUtils
{
	private SecurityUtils()
	{
	}

	/**
	 * Gets the user name of the currently signed in user.
	 *
	 * @return the user name of the current user or <code>null</code> if the user
	 *         has not signed in
	 */
	public static String getUsername() {
		SecurityContext context = SecurityContextHolder.getContext();
		Object principal = context.getAuthentication().getPrincipal();
		if(principal instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) context.getAuthentication().getPrincipal();
			return userDetails.getUsername();
		}
		// Anonymous or no authentication.
		return null;
	}

	/**
	 * Checks if the user is logged in.
	 *
	 * @return true if the user is logged in. False otherwise.
	 */
	public static boolean isUserLoggedIn() {
		return isUserLoggedIn(SecurityContextHolder.getContext().getAuthentication());
	}

	private static boolean isUserLoggedIn(Authentication authentication) {
		return authentication != null
				&& !(authentication instanceof AnonymousAuthenticationToken);
	}

	// Will return null if no user is found
	public static PathmindUserDetails getUser() {
		SecurityContext context = SecurityContextHolder.getContext();
		if (context != null && context.getAuthentication() != null) {
			Object principal = context.getAuthentication().getPrincipal();
			if (principal instanceof UserDetails) {
				PathmindUserDetails pathmindUserDetails = (PathmindUserDetails) context.getAuthentication().getPrincipal();
				return pathmindUserDetails;
			}
		}
		return null;
	}

	// TODO -> Deal with errors if no user is found in the session.
	public static long getUserId() {
		final PathmindUserDetails user = getUser();
		if(user == null){
			throw new RuntimeException(new AuthenticationException("User not authenticated!"));
		}else{
			return user.getId();
		}
	}

	/**
	 * Checks if access is granted for the current user for the given secured view,
	 * defined by the view class.
	 *
	 * @param securedClass View class
	 * @return true if access is granted, false otherwise.
	 */
	public static boolean isAccessGranted(Class<?> securedClass) {
		final boolean publicView = LoginView.class.equals(securedClass)
				//|| AccessDeniedView.class.equals(securedClass)
				|| SignUpView.class.equals(securedClass)
				|| ResetPasswordView.class.equals(securedClass)
				|| EmailVerificationView.class.equals(securedClass)
				|| CustomRouteNotFoundError.class.equals(securedClass);

		// Always allow access to public views
		if (publicView) {
			return true;
		}

		Authentication userAuthentication = SecurityContextHolder.getContext().getAuthentication();

		// All other views require authentication
		if (!isUserLoggedIn(userAuthentication)) {
			return false;
		}
		return true;

		//// Allow if no roles are required.
		//Secured secured = AnnotationUtils.findAnnotation(securedClass, Secured.class);
		//if (secured == null) {
		//	return true;
		//}

		//List<String> allowedRoles = Arrays.asList(secured.value());
		//return userAuthentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
		//		.anyMatch(allowedRoles::contains);
	}


	/**
	 * Tests if the request is an internal framework request. The test consists of
	 * checking if the request parameter is present and if its value is consistent
	 * with any of the request types know.
	 *
	 * @param request
	 *            {@link HttpServletRequest}
	 * @return true if is an internal framework request. False otherwise.
	 */
	static boolean isFrameworkInternalRequest(HttpServletRequest request) {
		final String parameterValue = request.getParameter(ApplicationConstants.REQUEST_TYPE_PARAMETER);
		return parameterValue != null
				&& Stream.of(RequestType.values()).anyMatch(r -> r.getIdentifier().equals(parameterValue));
	}

}
