package io.skymind.pathmind.webapp.security;

import io.skymind.pathmind.db.data.PathmindUser;

@FunctionalInterface
public interface CurrentUser {

	PathmindUser getUser();
}
