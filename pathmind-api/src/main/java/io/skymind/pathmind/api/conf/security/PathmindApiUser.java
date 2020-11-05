package io.skymind.pathmind.api.conf.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.Collection;

public class PathmindApiUser extends PreAuthenticatedAuthenticationToken {

    private final Long userId;

	public PathmindApiUser(Object principal, Object creds, Long userId, Collection<? extends GrantedAuthority> anAuthorities) {
		super(principal, creds, anAuthorities);
		this.userId = userId;
	}

	public Long getUserId() {
		return userId;
	}
}
