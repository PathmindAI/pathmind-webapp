package io.skymind.pathmind.webapp.utils;

import javax.servlet.http.Cookie;

import com.vaadin.flow.server.VaadinService;

public class CookieUtils {

	public static void deleteCookie(String cookieName) {
		Cookie cookie = new Cookie(cookieName, "");
		// A cookie will be deleted when maxAge is 0
		cookie.setMaxAge(0);
		VaadinService.getCurrentResponse().addCookie(cookie);
	}

	public static void deleteAWSCanCookie() {
		// Deleting the cookie on the login page load to make sure new user sessions
		// won't be using old webapp instances in the case of canary deployments.
		deleteCookie("Can");
	}
}