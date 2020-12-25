package io.skymind.pathmind.webapp.security;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import io.skymind.pathmind.db.dao.RunDAO;
import io.skymind.pathmind.db.dao.UserDAO;
import io.skymind.pathmind.shared.data.PathmindUser;
import io.skymind.pathmind.shared.security.PathmindUserDetails;
import io.skymind.pathmind.webapp.exception.EmailIsNotVerifiedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implements the {@link UserDetailsService}.
 * <p>
 * This implementation searches for {@link User} entities by the e-mail address
 * supplied in the login screen.
 */
@Slf4j
@Service
@Primary
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserDAO userDAO;
    private final RunDAO runDAO;
    private final int allowedRunsNoVerified;

    @Autowired
    public UserDetailsServiceImpl(UserDAO userDAO, RunDAO runDAO, @Value("${pm.allowed_run_no_verified}") int allowedRunsNoVerified) {
        this.userDAO = userDAO;
        this.runDAO = runDAO;
        this.allowedRunsNoVerified = allowedRunsNoVerified;
    }

    /**
     * Recovers the {@link User} from the database using the e-mail address supplied
     * in the login screen. If the user is found, returns a
     * {@link org.springframework.security.core.userdetails.User}.
     *
     * @param username User's e-mail address
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws AuthenticationException {
        PathmindUser user = userDAO.findByEmailIgnoreCase(username);
        if (null == user) {
            throw new UsernameNotFoundException("No user present with email: " + username);
        } else if (user.getEmailVerifiedAt() == null) {
            long numRuns = runDAO.numberOfRunsByUser(user.getId());
            if (numRuns >= allowedRunsNoVerified) {
                throw new EmailIsNotVerifiedException(URLEncoder.encode(user.getEmail(), StandardCharsets.UTF_8));
            }
            log.warn("Unverified user {} with email {} has no runs allowed", user.getId(), user.getEmail());
        }

        Set<SimpleGrantedAuthority> permissions = user.getAccountType().getGrantedAuthorities();
        permissions.add(new SimpleGrantedAuthority("logged_in"));
        return new PathmindUserDetails(
                user.getEmail(),
                user.getPassword(),
                permissions,
                user.getId(),
                user.getFirstname(),
                user.getLastname()
        );

    }
}