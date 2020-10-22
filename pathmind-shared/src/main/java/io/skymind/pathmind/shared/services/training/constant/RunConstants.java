package io.skymind.pathmind.shared.services.training.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public final class RunConstants {
	public static int PBT_RUN_ITERATIONS;
	public static int PBT_MAX_TIME_IN_SEC;
	public static int PBT_NUM_SAMPLES;

    private RunConstants(
        @Value("${pathmind.training.pbt_max_time_in_sec}")
        int PBT_RUN_ITERATIONS,
        @Value("${pathmind.training.pbt_run_iterations}")
        int PBT_MAX_TIME_IN_SEC,
        @Value("${pathmind.training.pbt_num_samples}")
        int PBT_NUM_SAMPLES ) {
        RunConstants.PBT_RUN_ITERATIONS = PBT_RUN_ITERATIONS;
        RunConstants.PBT_MAX_TIME_IN_SEC = PBT_MAX_TIME_IN_SEC;
        RunConstants.PBT_NUM_SAMPLES = PBT_NUM_SAMPLES;
    }

}
