package io.skymind.pathmind.shared.security;


import javax.naming.AuthenticationException;

import io.skymind.pathmind.shared.data.user.UserMetrics;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

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
		if (context != null && context.getAuthentication() != null) {
			Object principal = context.getAuthentication().getPrincipal();
			if(principal instanceof UserDetails) {
				UserDetails userDetails = (UserDetails) context.getAuthentication().getPrincipal();
				return userDetails.getUsername();
			}
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

	public static boolean isUserLoggedIn(Authentication authentication) {
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

    public static boolean isWithinCap(UserMetrics userMetrics) {
        return userMetrics.getExperimentsCreatedToday() <= UserMetrics.MAX_EXPERIMENTS_ALLOWED_PER_DAY &&
                userMetrics.getExperimentsCreatedThisMonth() <= UserMetrics.MAX_EXPERIMENTS_ALLOWED_PER_MONTH;
    }

    public static UserMetrics.UserCap getWhichCapIsExceed(UserMetrics userMetrics) {
        if(userMetrics.getExperimentsCreatedToday() > UserMetrics.MAX_EXPERIMENTS_ALLOWED_PER_DAY)
            return UserMetrics.UserCap.Daily;
        if(userMetrics.getExperimentsCreatedThisMonth() > UserMetrics.MAX_EXPERIMENTS_ALLOWED_PER_MONTH)
            return UserMetrics.UserCap.Monthly;
        throw new RuntimeException("Invalid exceeded cap");
    }
}
