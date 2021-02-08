package io.skymind.pathmind.shared.services.training.environment;

import java.util.HashMap;
import java.util.Map;

import io.skymind.pathmind.shared.constants.EC2InstanceType;
import io.skymind.pathmind.shared.services.training.versions.AnyLogic;
import io.skymind.pathmind.shared.services.training.versions.Conda;
import io.skymind.pathmind.shared.services.training.versions.JDK;
import io.skymind.pathmind.shared.services.training.versions.NativeRL;
import io.skymind.pathmind.shared.services.training.versions.PathmindHelper;
import org.springframework.stereotype.Component;

@Component
public class ExecutionEnvironmentManager {

    // todo need to save this to DB
    private static Map<Long, ExecutionEnvironment> environmentMap = new HashMap();

    private ExecutionEnvironment defaultEnvironment() {
        return new ExecutionEnvironment(AnyLogic.VERSION_8_7_0,
                PathmindHelper.VERSION_1_4_0,
                NativeRL.VERSION_1_5_0,
                JDK.VERSION_8_222,
                Conda.VERSION_1_0_0,
                EC2InstanceType.IT_36CPU_72GB,
                0,
                0,
                0,
                0);
    }

    public ExecutionEnvironment getEnvironment(long userId) {
        if (!environmentMap.containsKey(userId)) {
            environmentMap.put(userId, defaultEnvironment());
        }

        return environmentMap.get(userId);
    }


}
