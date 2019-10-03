package io.skymind.pathmind.security;

import io.skymind.pathmind.data.PathmindUser;

@FunctionalInterface
public interface CurrentUser {

	PathmindUser getUser();
}
