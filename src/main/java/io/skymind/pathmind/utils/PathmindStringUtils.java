package io.skymind.pathmind.utils;

public class PathmindStringUtils
{
	private PathmindStringUtils() {
	}

	public final static String camelCaseToWords(String camelCase) {
		return org.apache.commons.lang3.StringUtils.capitalize(
				org.apache.commons.lang3.StringUtils.join(
						org.apache.commons.lang3.StringUtils.splitByCharacterTypeCamelCase(camelCase), org.apache.commons.lang3.StringUtils.SPACE));
	}
}
