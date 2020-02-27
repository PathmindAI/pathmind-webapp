package io.skymind.pathmind.utils;

import org.apache.commons.text.WordUtils;

public class StringUtils {

	public static String toCamelCase(String val) {
		if (val == null){
			return "-";
		}
		return WordUtils.capitalizeFully(val).replaceAll(" ", "");
	}
}
