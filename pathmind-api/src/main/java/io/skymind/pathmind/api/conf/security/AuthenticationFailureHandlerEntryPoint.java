package io.skymind.pathmind.api.conf.security;

import com.google.gson.Gson;
import liquibase.util.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

@Component
public class AuthenticationFailureHandlerEntryPoint extends SimpleUrlAuthenticationFailureHandler implements AuthenticationEntryPoint {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        process(request, response, authException);
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        process(request, response, authException);
    }

    private void process(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

        int statusCode = SC_UNAUTHORIZED;
        if (authException instanceof CredentialsExpiredException) { // thrown at PathmindApiAuthenticationProvider
            statusCode = SC_FORBIDDEN;
        }

        Gson gson = new Gson();
        Map<String, Object> responseProps = Map.of(
                "timestamp", DateTimeFormatter.ISO_ZONED_DATE_TIME.format(OffsetDateTime.now()),
                "status", statusCode,
                "error", StringUtils.trimToEmpty(authException.getMessage()),
                "path", request.getServletPath()
        );
        String exception = gson.toJson(responseProps);

        response.setStatus(statusCode);
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        PrintWriter writer = response.getWriter();
        writer.write(exception);
        writer.flush();
    }

}
