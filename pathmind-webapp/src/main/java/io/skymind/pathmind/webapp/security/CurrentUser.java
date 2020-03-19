package io.skymind.pathmind.webapp.security;

import io.skymind.pathmind.shared.data.PathmindUser;

@FunctionalInterface
public interface CurrentUser {

	PathmindUser getUser();
}
