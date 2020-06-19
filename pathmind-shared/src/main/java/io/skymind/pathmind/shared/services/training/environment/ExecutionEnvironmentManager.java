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

import java.util.HashMap;
import java.util.Map;

@Component
public class ExecutionEnvironmentManager {
    private final FeatureManager featureManager;

    @Autowired
    public ExecutionEnvironmentManager(FeatureManager featureManager) {
        this.featureManager = featureManager;
    }

    // todo need to save this to DB
    private static Map<Long, ExecutionEnvironment> environmentMap= new HashMap();

    private ExecutionEnvironment defaultEnvironment() {
        PathmindHelper pathmindHelperVersion = PathmindHelper.VERSION_1_0_2;
        if (featureManager.isEnabled(Feature.MULTI_AGENT_TRAINING)) {
            pathmindHelperVersion = PathmindHelper.VERSION_0_0_25_Multi;
        }

        return new ExecutionEnvironment(AnyLogic.VERSION_8_5_2,
                pathmindHelperVersion,
                NativeRL.VERSION_1_1_0,
                JDK.VERSION_8_222,
                Conda.VERSION_0_8_5,
                EC2InstanceType.IT_36CPU_72GB);
    }

    public ExecutionEnvironment getEnvironment(long userId) {
        if (!environmentMap.containsKey(userId)) {
            environmentMap.put(userId, defaultEnvironment());
        }

        return environmentMap.get(userId);
    }


}
