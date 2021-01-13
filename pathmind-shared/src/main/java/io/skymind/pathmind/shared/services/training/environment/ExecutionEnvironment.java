package io.skymind.pathmind.shared.services.training.environment;

import io.skymind.pathmind.shared.constants.EC2InstanceType;
import io.skymind.pathmind.shared.services.training.constant.RunConstants;
import io.skymind.pathmind.shared.services.training.versions.AnyLogic;
import io.skymind.pathmind.shared.services.training.versions.Conda;
import io.skymind.pathmind.shared.services.training.versions.JDK;
import io.skymind.pathmind.shared.services.training.versions.NativeRL;
import io.skymind.pathmind.shared.services.training.versions.PathmindHelper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class ExecutionEnvironment {
    private AnyLogic anylogicVersion;
    private PathmindHelper pathmindHelperVersion;
    private NativeRL nativerlVersion;
    private final JDK jdkVersion;
    private Conda condaVersion;
    private EC2InstanceType ec2InstanceType;
    private int PBT_RUN_ITERATIONS;
    private int PBT_MAX_TIME_IN_SEC;
    private int PBT_NUM_SAMPLES;
    private int maxMemory;

    public int getPBT_RUN_ITERATIONS() {
        return PBT_RUN_ITERATIONS == 0 ? RunConstants.PBT_RUN_ITERATIONS : PBT_RUN_ITERATIONS;
    }

    public int getPBT_MAX_TIME_IN_SEC() {
        return PBT_MAX_TIME_IN_SEC == 0 ? RunConstants.PBT_MAX_TIME_IN_SEC : PBT_MAX_TIME_IN_SEC;
    }

    public int getPBT_NUM_SAMPLES() {
        return PBT_NUM_SAMPLES == 0 ? RunConstants.PBT_NUM_SAMPLES : PBT_NUM_SAMPLES;
    }

    public int getMaxMemory() {
        return maxMemory == 0 ? 4096 : maxMemory;
    }
}
