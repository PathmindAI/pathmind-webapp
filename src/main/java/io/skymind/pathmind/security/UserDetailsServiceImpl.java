package io.skymind.pathmind.security;

import java.util.Collections;

import io.skymind.pathmind.data.PathmindUser;
import io.skymind.pathmind.db.repositories.UserRepository;
import io.skymind.pathmind.exception.EmailIsNotVerifiedException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implements the {@link UserDetailsService}.
 *
 * This implementation searches for {@link User} entities by the e-mail address
 * supplied in the login screen.
 */
@Service
@Primary
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;
	
	@Autowired
	public UserDetailsServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	/**
	 *
	 * Recovers the {@link User} from the database using the e-mail address supplied
	 * in the login screen. If the user is found, returns a
	 * {@link org.springframework.security.core.userdetails.User}.
	 *
	 * @param username User's e-mail address
	 *
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws AuthenticationException {
		PathmindUser user = userRepository.findByEmailIgnoreCase(username);
		if (null == user) {
			throw new UsernameNotFoundException("No user present with email: " + username);
		} else if (user.getEmailVerifiedAt() == null) {
			throw new EmailIsNotVerifiedException(user.getEmail());
		} else {
			return new PathmindUserDetails(
					user.getEmail(),
					user.getPassword(),
					Collections.singletonList(new SimpleGrantedAuthority("logged_in")),
					user.getId(),
					user.getFirstname(),
					user.getLastname(),
					user.getName()
					);
		}
	}
}