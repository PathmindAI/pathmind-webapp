package io.skymind.pathmind.services.training.constant;

import io.skymind.pathmind.constants.RunType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

// TODO (KW): 25.01.2020 add javadoc
public final class RunConstants {
	private RunConstants() {
	}

	public static final int DISCOVERY_RUN_ITERATIONS = 100;
	public static final int FULL_RUN_ITERATIONS = 500;
	public static final String DISCOVERY_RUN_LEARNING_RATES = "DISCOVERY_RUN_LEARNING_RATES";
	public static final String DISCOVERY_RUN_GAMMAS = "DISCOVERY_RUN_GAMMAS";
	public static final String DISCOVERY_RUN_BATCH_SIZES = "DISCOVERY_RUN_BATCH_SIZES";

	public static final Map<String, List<Number>> RUN_HYPERPARAMETERS = Map.of(
			DISCOVERY_RUN_LEARNING_RATES, Arrays.asList(1e-3, 1e-5),
			DISCOVERY_RUN_GAMMAS, Arrays.asList(0.9, 0.99),
			DISCOVERY_RUN_BATCH_SIZES, Arrays.asList(64, 128)
	);
}
