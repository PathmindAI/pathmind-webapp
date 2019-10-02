package io.skymind.pathmind.data.utils;

import io.skymind.pathmind.data.PathmindUser;

public class PathmindUserUtils
{
	private PathmindUserUtils() {
	}

	public static String getFullName(PathmindUser user) {
		return user.getFirstname() + " " + user.getLastname();
	}
}
