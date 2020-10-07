package io.skymind.pathmind.shared.services.training.environment;

import io.skymind.pathmind.shared.constants.EC2InstanceType;
import io.skymind.pathmind.shared.services.training.versions.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ExecutionEnvironment {
    private AnyLogic anylogicVersion;
    private PathmindHelper pathmindHelperVersion;
    private NativeRL nativerlVersion;
    private final JDK jdkVersion;
    private Conda condaVersion;
    private EC2InstanceType ec2InstanceType;

    public ExecutionEnvironment(AnyLogic anylogicVersion, PathmindHelper pathmindHelperVersion, NativeRL nativerlVersion, JDK jdkVersion, Conda condaVersion, EC2InstanceType ec2InstanceType) {
        this.anylogicVersion = anylogicVersion;
        this.pathmindHelperVersion = pathmindHelperVersion;
        this.nativerlVersion = nativerlVersion;
        this.jdkVersion = jdkVersion;
        this.condaVersion = condaVersion;
        this.ec2InstanceType = ec2InstanceType;
    }
}
