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

	public static String toCamelCase(String str) {
		if (org.apache.commons.lang3.StringUtils.isEmpty(str)){
			return "—";
		}
		return WordUtils.capitalizeFully(str).replaceAll(" ", "");
    }
    
    public static String toCapitalize(String str) {
		if (org.apache.commons.lang3.StringUtils.isEmpty(str)){
			return "—";
		}
        return WordUtils.capitalizeFully(str);
    }

	public static String removeInvalidChars(String str) {
		return str.replaceAll("[^a-zA-Z0-9\\-\\.]","");
    }
    
    public static String replaceHyphenWithSpace(String str) {
        return str.replaceAll("[-]"," ");
    }
}
