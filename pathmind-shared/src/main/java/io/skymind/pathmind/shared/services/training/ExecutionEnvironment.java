package io.skymind.pathmind.shared.services.training;

import io.skymind.pathmind.shared.services.training.versions.*;

public class ExecutionEnvironment {
    private final AnyLogic anylogicVersion;
    private final PathmindHelper pathmindHelperVersion;
    private final NativeRL rllibVersion;
    private final JDK jdkVersion;
    private final Conda condaVersion;

    public ExecutionEnvironment(AnyLogic anylogicVersion, PathmindHelper pathmindHelperVersion, NativeRL rllibVersion, JDK jdkVersion, Conda condaVersion) {
        this.anylogicVersion = anylogicVersion;
        this.pathmindHelperVersion = pathmindHelperVersion;
        this.rllibVersion = rllibVersion;
        this.jdkVersion = jdkVersion;
        this.condaVersion = condaVersion;
    }

    public AnyLogic getAnylogicVersion() {
        return anylogicVersion;
    }

    public PathmindHelper getPathmindHelperVersion() {
        return pathmindHelperVersion;
    }

    public NativeRL getRllibVersion() {
        return rllibVersion;
    }

    public JDK getJdkVersion() {
        return jdkVersion;
    }

    public Conda getCondaVersion() {
        return condaVersion;
    }
}
