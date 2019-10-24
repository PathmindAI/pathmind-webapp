package io.skymind.pathmind.security;

import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.ExceptionMappingAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.Assert;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

//Based on ExceptionMappingAuthenticationFailureHandler
public class PathmithAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private final Map<String, String> failureUrlMap = new HashMap();

    public PathmithAuthenticationFailureHandler() {
    }

    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String url = (String) this.failureUrlMap.get(exception.getClass().getName());
        if (url != null) {
            if (exception instanceof InternalAuthenticationServiceException && exception.getMessage() != null) {
                url += "?email=" + exception.getMessage();
            }

            this.getRedirectStrategy().sendRedirect(request, response, url);
        } else {
            super.onAuthenticationFailure(request, response, exception);
        }

    }

    public void setExceptionMappings(Map<?, ?> failureUrlMap) {
        this.failureUrlMap.clear();
        Iterator var2 = failureUrlMap.entrySet().iterator();

        while(var2.hasNext()) {
            Map.Entry<?, ?> entry = (Map.Entry)var2.next();
            Object exception = entry.getKey();
            Object url = entry.getValue();
            Assert.isInstanceOf(String.class, exception, "Exception key must be a String (the exception classname).");
            Assert.isInstanceOf(String.class, url, "URL must be a String");
            Assert.isTrue(UrlUtils.isValidRedirectUrl((String)url), () -> {
                return "Not a valid redirect URL: " + url;
            });
            this.failureUrlMap.put((String)exception, (String)url);
        }

    }
}
