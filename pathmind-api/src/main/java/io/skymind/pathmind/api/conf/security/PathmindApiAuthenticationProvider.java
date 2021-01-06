package io.skymind.pathmind.api.conf.security;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

import io.skymind.pathmind.db.dao.UserDAO;
import io.skymind.pathmind.shared.data.PathmindUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PathmindApiAuthenticationProvider implements AuthenticationProvider {

    private final UserDAO userDAO;
    private final Duration keyValidityDuration;

    public PathmindApiAuthenticationProvider(UserDAO userDAO, @Value("${pm.api.key-validity-duration}") Duration keyValidityDuration) {
        this.userDAO = userDAO;
        this.keyValidityDuration = keyValidityDuration;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String apiKey = authentication.getCredentials().toString();

        return Optional.ofNullable(userDAO.findByApiKey(apiKey))
                .map(user -> {
                    if (user.getEmailVerifiedAt() == null) {
                        throw new PreAuthenticatedCredentialsNotFoundException("email was not verified " + user.getEmail());
                    }
                    LocalDateTime keyExpirationDate = user.getApiKeyCreatedAt().plus(keyValidityDuration);
                    if (keyExpirationDate.isBefore(LocalDateTime.now())) {
                        throw new CredentialsExpiredException("API Key had expired on " + DateTimeFormatter.ISO_LOCAL_DATE.format(keyExpirationDate));
                    }
                    return map(user);
                })
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