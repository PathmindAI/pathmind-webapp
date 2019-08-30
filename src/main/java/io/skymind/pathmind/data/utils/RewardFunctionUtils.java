package io.skymind.pathmind.data.utils;

import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.RewardFunction;

public class RewardFunctionUtils
{
	private RewardFunctionUtils() {
	}

	public static RewardFunction generateNewRewardFunction(Experiment experiment) {
		return new RewardFunction(
				experiment.getProject().getName() + " 1",
				"",
				experiment);
	}
}
