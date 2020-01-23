package io.skymind.pathmind.services.training.constant;

import java.util.Arrays;
import java.util.List;

public final class RunConstants {
	public static final int DISCOVERY_RUN_ITERATIONS = 100;
	public static final int FULL_RUN_ITERATIONS = 500;
	public static final List<Double> DISCOVERY_RUN_LEARNING_RATES = Arrays.asList(1e-3, 1e-5);
	public static final List<Double> DISCOVERY_RUN_GAMMAS = Arrays.asList(0.9, 0.99);
	public static final List<Integer> DISCOVERY_RUN_BATCH_SIZES = Arrays.asList(64, 128);

	public static int getNumberOfDiscoveryRuns() {
		return DISCOVERY_RUN_LEARNING_RATES.size() + DISCOVERY_RUN_GAMMAS.size() + DISCOVERY_RUN_BATCH_SIZES.size();
	}
}
