package io.skymind.pathmind.services.training;

import io.skymind.pathmind.services.training.versions.AnyLogic;
import io.skymind.pathmind.services.training.versions.PathmindHelper;
import io.skymind.pathmind.services.training.versions.RLLib;

public class ExecutionEnvironment {
    private final AnyLogic anylogicVersion;
    private final PathmindHelper pathmindHelperVersion;
    private final RLLib rllibVersion;

    public ExecutionEnvironment(AnyLogic anylogicVersion, PathmindHelper pathmindHelperVersion, RLLib rllibVersion) {
        this.anylogicVersion = anylogicVersion;
        this.pathmindHelperVersion = pathmindHelperVersion;
        this.rllibVersion = rllibVersion;
    }

    public AnyLogic getAnylogicVersion() {
        return anylogicVersion;
    }

    public PathmindHelper getPathmindHelperVersion() {
        return pathmindHelperVersion;
    }

    public RLLib getRllibVersion() {
        return rllibVersion;
    }
}
