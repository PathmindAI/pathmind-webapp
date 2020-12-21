package io.skymind.pathmind.api.conf.security;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;

public class PathmindApiAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

    public static final String HEADER_API_TOKEN_NAME = "X-PM-API-TOKEN";

    private static final AuthenticationSuccessHandler NOOP_SUCCESS_HANDLER = (request, response, authentication) -> {
    };

    public PathmindApiAuthenticationProcessingFilter(AuthenticationManager authenticationManager,
                                                     AuthenticationFailureHandler authenticationFailureHandler) {
        super(new RequestHeaderRequestMatcher(HEADER_API_TOKEN_NAME));
        super.setAuthenticationManager(authenticationManager);
        Objects.requireNonNull(authenticationManager, "AuthenticationManager should be provided");
        this.setAuthenticationSuccessHandler(NOOP_SUCCESS_HANDLER);
        this.setAuthenticationFailureHandler(authenticationFailureHandler);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String apiKey = StringUtils.trimToEmpty(request.getHeader(HEADER_API_TOKEN_NAME));

        if (StringUtils.isEmpty(apiKey)) {
            throw new AuthenticationCredentialsNotFoundException("No API key is provided on request");
        }

        Authentication authRequest = new PathmindAuthenticationToken(apiKey);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    // the implementation from the parent delegates to a successHandler, which unfortunately cannot
    // propagate the request down the filter chain, so we decorate the method
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }

}
