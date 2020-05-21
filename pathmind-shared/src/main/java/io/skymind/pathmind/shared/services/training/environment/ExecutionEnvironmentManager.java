package io.skymind.pathmind.shared.services.training.environment;

import io.skymind.pathmind.shared.constants.EC2InstanceType;
import io.skymind.pathmind.shared.featureflag.Feature;
import io.skymind.pathmind.shared.featureflag.FeatureManager;
import io.skymind.pathmind.shared.services.training.versions.AnyLogic;
import io.skymind.pathmind.shared.services.training.versions.Conda;
import io.skymind.pathmind.shared.services.training.versions.JDK;
import io.skymind.pathmind.shared.services.training.versions.NativeRL;
import io.skymind.pathmind.shared.services.training.versions.PathmindHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExecutionEnvironmentManager {
//    private CurrentUser currentUser;
    private final FeatureManager featureManager;

    @Autowired
    public ExecutionEnvironmentManager(FeatureManager featureManager) {
        this.featureManager = featureManager;
    }

    // todo need to set the env for each user
    private ExecutionEnvironment environment = null;

    private ExecutionEnvironment defaultEnvironment() {
        PathmindHelper pathmindHelperVersion = PathmindHelper.VERSION_1_0_1;
        if (featureManager.isEnabled(Feature.MULTI_AGENT_TRAINING)) {
            pathmindHelperVersion = PathmindHelper.VERSION_0_0_25_Multi;
        }

        return new ExecutionEnvironment(AnyLogic.VERSION_8_5_2,
                pathmindHelperVersion,
                NativeRL.VERSION_1_0_6,
                JDK.VERSION_8_222,
                Conda.VERSION_0_7_6,
                EC2InstanceType.IT_16CPU_32GB);
    }

    public ExecutionEnvironment getEnvironment() {
        if (environment == null) {
            environment = defaultEnvironment();
        }

        return environment;
    }


}
