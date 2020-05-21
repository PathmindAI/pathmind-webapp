package io.skymind.pathmind.shared.services.training.environment;

import io.skymind.pathmind.shared.constants.EC2InstanceType;
import io.skymind.pathmind.shared.services.training.versions.*;

public class ExecutionEnvironment {
    private final AnyLogic anylogicVersion;
    private final PathmindHelper pathmindHelperVersion;
    private final NativeRL rllibVersion;
    private final JDK jdkVersion;
    private final Conda condaVersion;
    private EC2InstanceType ec2InstanceType;

    public ExecutionEnvironment(AnyLogic anylogicVersion, PathmindHelper pathmindHelperVersion, NativeRL rllibVersion, JDK jdkVersion, Conda condaVersion, EC2InstanceType ec2InstanceType) {
        this.anylogicVersion = anylogicVersion;
        this.pathmindHelperVersion = pathmindHelperVersion;
        this.rllibVersion = rllibVersion;
        this.jdkVersion = jdkVersion;
        this.condaVersion = condaVersion;
        this.ec2InstanceType = ec2InstanceType;
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

    public EC2InstanceType getEc2InstanceType() {
        return ec2InstanceType;
    }

    public void setEc2InstanceType(EC2InstanceType ec2InstanceType) {
        this.ec2InstanceType = ec2InstanceType;
    }
}
