package io.skymind.pathmind.data.utils;

import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.utils.DateAndTimeUtils;

public class PolicyUtils
{
	private PolicyUtils() {
	}

	public static final String getDuration(Policy policy) {
		return DateAndTimeUtils.formatTime(RunUtils.getElapsedTime(policy.getRun()));
	}
}
