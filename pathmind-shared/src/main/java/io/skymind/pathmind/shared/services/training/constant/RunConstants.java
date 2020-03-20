package io.skymind.pathmind.shared.services.training.constant;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class RunConstants {
	private static final int MINUTE = 60;
	private static final int HOUR = 60 * 60;
	
	private RunConstants() {
	}

	/**
	 * Number of possible iterations for PBT run
	 */
	public static final int PBT_RUN_ITERATIONS = 250;
	public static final int PBT_MAX_TIME_IN_SEC = 24 * HOUR; // Setting this for GHD March 14, 2020 - Slin
	public static final int PBT_NUM_SAMPLES = 10;

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
