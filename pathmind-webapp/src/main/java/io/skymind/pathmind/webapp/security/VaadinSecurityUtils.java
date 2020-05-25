package io.skymind.pathmind.webapp.security;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.vaadin.flow.server.ServletHelper;
import com.vaadin.flow.shared.ApplicationConstants;
import io.skymind.pathmind.shared.constants.ViewPermission;
import io.skymind.pathmind.shared.security.SecurityUtils;
import io.skymind.pathmind.webapp.security.annotation.Permission;
import io.skymind.pathmind.webapp.ui.views.errors.PageNotFoundView;
import io.skymind.pathmind.webapp.ui.views.login.EmailVerificationView;
import io.skymind.pathmind.webapp.ui.views.login.LoginView;
import io.skymind.pathmind.webapp.ui.views.login.ResetPasswordView;
import io.skymind.pathmind.webapp.ui.views.login.SignUpView;
import io.skymind.pathmind.webapp.ui.views.login.VerificationEmailSentView;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
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
		Optional<Annotation> optionalAnnotation = Arrays.stream(securedClass.getAnnotations())
                .filter(a -> a.annotationType().equals(Permission.class))
                .findFirst();

		// All other views require authentication
		if (!SecurityUtils.isUserLoggedIn(userAuthentication)) {
			return false;
		}

		if (optionalAnnotation.isPresent()) {
			Permission permission = (Permission)optionalAnnotation.get();
			List<String> neededPermissions = Arrays.asList(permission.permissions()).stream()
                    .map(ViewPermission::getPermission)
                    .collect(Collectors.toList());
            log.info("kepricondebug need : " + neededPermissions);
			List have = userAuthentication.getAuthorities().stream()
                    .map(authority -> authority.getAuthority())
                    .collect(Collectors.toList());
			log.info("kepricondebug have : " + have);
            neededPermissions.removeAll(have);
			if (neededPermissions.size() > 0) {
				log.info("kepricondebug : need more authority : " + neededPermissions);
				return false;
			}
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
