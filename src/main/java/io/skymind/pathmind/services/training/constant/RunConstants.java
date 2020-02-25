package io.skymind.pathmind.services.training.constant;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class RunConstants {
	private RunConstants() {
	}

	/**
	 * Number of possible iterations for a single discovery run
	 */
	public static final int DISCOVERY_RUN_ITERATIONS = 50;

	/**
	 * Number of possible iterations for a single full run
	 */
	public static final int FULL_RUN_ITERATIONS = 500;

	/**
	 * Number of possible iterations for PBT run
	 */
	public static final int PBT_RUN_ITERATIONS = 500;

	public static final String DISCOVERY_RUN_LEARNING_RATES = "DISCOVERY_RUN_LEARNING_RATES";
	public static final String DISCOVERY_RUN_GAMMAS = "DISCOVERY_RUN_GAMMAS";
	public static final String DISCOVERY_RUN_BATCH_SIZES = "DISCOVERY_RUN_BATCH_SIZES";

	/**
	 * Map contains all hyperparameters for discovery runs
	 */
	public static final Map<String, List<? extends Number>> TRAINING_HYPERPARAMETERS = Map.of(
			DISCOVERY_RUN_LEARNING_RATES, Arrays.asList(1e-3, 1e-4, 1e-5),
			DISCOVERY_RUN_GAMMAS, Arrays.asList(0.9, 0.99),
			DISCOVERY_RUN_BATCH_SIZES, Arrays.asList(64, 128)
	);
}
