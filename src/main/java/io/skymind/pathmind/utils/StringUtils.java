package io.skymind.pathmind.utils;

import org.apache.commons.text.WordUtils;

public class StringUtils {

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
