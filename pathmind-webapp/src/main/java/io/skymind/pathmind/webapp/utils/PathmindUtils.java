package io.skymind.pathmind.webapp.utils;

import io.skymind.pathmind.shared.utils.PathmindStringUtils;
import io.skymind.pathmind.webapp.ui.utils.VaadinUtils;

public class PathmindUtils
{
	private PathmindUtils() {
	}

	/**
	 * This is in a method here because it's used in both the default path as well as the Login page.
	 * I've also created a helper method so that if we need to manually create the page title
	 * we can keep the same standard and just add a String to the same method signature.
	 */
	public static final String getPageTitle() {
		return getPageTitle(PathmindStringUtils.camelCaseToWords(VaadinUtils.getViewName()));
	}

	public static final String getPageTitle(String title) {
		return "Pathmind | " + title;
	}
}
