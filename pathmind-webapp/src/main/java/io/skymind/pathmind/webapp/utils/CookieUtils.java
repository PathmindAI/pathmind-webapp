package io.skymind.pathmind.webapp.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import javax.servlet.http.Cookie;

import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

public class CookieUtils {

    public static void setNotFirstTimeVisitCookie() {
        setCookie("isFirstTimeVisit", "false", 365*24*60*60); // expires in a year
    }

    public static void setCookie(String cookieName, String value, int age) {
        Cookie cookie = new Cookie(cookieName, value);
        cookie.setMaxAge(age);
        cookie.setPath("/");
        if (VaadinService.getCurrentResponse() != null) {
            VaadinService.getCurrentResponse().addCookie(cookie);
        }
    }

    public static Cookie getCookie(String cookieName) {
        Cookie[] cookies = VaadinRequest.getCurrent().getCookies();
        if (ArrayUtils.isEmpty(cookies)) {
            return null;
        }
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findFirst()
                .orElse(null);
    }

    public static void deleteCookie(String cookieName) {
        Cookie cookie = new Cookie(cookieName, "");
        // A cookie will be deleted when maxAge is 0
        cookie.setMaxAge(0);
        if (VaadinService.getCurrentResponse() != null) {
            VaadinService.getCurrentResponse().addCookie(cookie);
        }
    }

    public static void deleteAWSCanCookie() {
        // Deleting the cookie on the login page load to make sure new user sessions
        // won't be using old webapp instances in the case of canary deployments.
        deleteCookie("Can");
    }
}