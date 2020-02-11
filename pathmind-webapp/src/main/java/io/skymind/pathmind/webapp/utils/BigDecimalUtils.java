package io.skymind.pathmind.webapp.utils;

import java.math.BigDecimal;

public class BigDecimalUtils
{
	private BigDecimalUtils() {
	}

	public static boolean isLessThan(BigDecimal first, BigDecimal second) {
		return first.compareTo(second) < 0;
	}

	public static boolean isGreaterThan(BigDecimal first, BigDecimal second) {
		return first.compareTo(second) > 0;
	}

	public static boolean isGreaterThanOrEqual(BigDecimal first, BigDecimal second) {
		return first.compareTo(second) >= 0;
	}

	public static boolean isLessThanOrEqual(BigDecimal first, BigDecimal second) {
		return first.compareTo(second) <= 0;
	}

	public static boolean isWithin(BigDecimal min, BigDecimal max, BigDecimal value) {
		return isGreaterThanOrEqual(value, min) && isLessThanOrEqual(value, max);
	}
}
