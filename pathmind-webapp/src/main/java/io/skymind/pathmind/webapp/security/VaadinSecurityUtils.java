package io.skymind.pathmind.webapp.security;

import java.util.stream.Stream;

import com.vaadin.flow.server.ServletHelper;
import com.vaadin.flow.shared.ApplicationConstants;
import io.skymind.pathmind.shared.security.SecurityUtils;
import io.skymind.pathmind.webapp.ui.views.errors.PageNotFoundView;
import io.skymind.pathmind.webapp.ui.views.login.EmailVerificationView;
import io.skymind.pathmind.webapp.ui.views.login.LoginView;
import io.skymind.pathmind.webapp.ui.views.login.ResetPasswordView;
import io.skymind.pathmind.webapp.ui.views.login.SignUpView;
import io.skymind.pathmind.webapp.ui.views.login.VerificationEmailSentView;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class VaadinSecurityUtils {

	private VaadinSecurityUtils()
	{
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
				|| SignUpView.class.equals(securedClass)
				|| ResetPasswordView.class.equals(securedClass)
				|| PageNotFoundView.class.equals(securedClass)
				|| EmailVerificationView.class.equals(securedClass)
				|| VerificationEmailSentView.class.equals(securedClass);

		// Always allow access to public views
		if (publicView) {
			return true;
		}

		Authentication userAuthentication = SecurityContextHolder.getContext().getAuthentication();

		// All other views require authentication
		if (!SecurityUtils.isUserLoggedIn(userAuthentication)) {
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
				&& Stream.of(ServletHelper.RequestType.values()).anyMatch(r -> r.getIdentifier().equals(parameterValue));
	}



}
