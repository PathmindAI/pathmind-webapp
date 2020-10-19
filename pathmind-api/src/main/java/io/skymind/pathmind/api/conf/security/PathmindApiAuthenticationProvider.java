package io.skymind.pathmind.api.conf.security;

import io.skymind.pathmind.db.dao.UserDAO;
import io.skymind.pathmind.shared.data.PathmindUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PathmindApiAuthenticationProvider implements AuthenticationProvider {

    private final UserDAO userDAO;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String apiKey = authentication.getCredentials().toString();

        return Optional.ofNullable(userDAO.findByApiKey(apiKey))
                .map(PathmindApiAuthenticationProvider::map)
                .orElseThrow(() -> new PreAuthenticatedCredentialsNotFoundException("no pathmind api user for api key " + apiKey))
                ;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(PathmindAuthenticationToken.class);
    }

    private static Authentication map(PathmindUser userDetails) {
        return new PathmindApiUser(userDetails.getName(), null, userDetails.getId(), new ArrayList<>());
    }

}