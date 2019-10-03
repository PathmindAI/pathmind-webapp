package io.skymind.pathmind.data.utils;

import io.skymind.pathmind.data.PathmindUser;
import io.skymind.pathmind.security.PathmindUserDetails;

public class PathmindUserUtils
{
	private PathmindUserUtils() {
	}

	public static String getFullName(PathmindUserDetails user) {
		return user.getFirstname() + " " + user.getLastname();
	}
}
