package io.skymind.pathmind.shared.utils;

import org.apache.commons.text.WordUtils;

public class PathmindStringUtils
{
	private PathmindStringUtils() {
	}

	public final static String camelCaseToWords(String camelCase) {
		return org.apache.commons.lang3.StringUtils.capitalize(
				org.apache.commons.lang3.StringUtils.join(
						org.apache.commons.lang3.StringUtils.splitByCharacterTypeCamelCase(camelCase), org.apache.commons.lang3.StringUtils.SPACE));
	}

	public static String toCamelCase(String val) {
		if (val == null){
			return "-";
		}
		return WordUtils.capitalizeFully(val).replaceAll(" ", "");
	}

	public static String removeInvalidChars(String str) {
		return str.replaceAll("[^a-zA-Z0-9\\-\\.]","");
	}
}
