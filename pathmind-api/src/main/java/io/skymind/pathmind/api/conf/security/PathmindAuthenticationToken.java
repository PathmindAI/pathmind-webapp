package io.skymind.pathmind.api.conf.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class PathmindAuthenticationToken extends AbstractAuthenticationToken {

	private final String apiKey;

	public PathmindAuthenticationToken(String apiKey) {
		super(null);
		super.setAuthenticated(false);
		this.apiKey = apiKey;
	}

	@Override
	public Object getCredentials() {
		return apiKey;
	}

	@Override
	public Object getPrincipal() {
		throw new UnsupportedOperationException("no principal is created for PathmindAuthenticationToken");
	}
}
