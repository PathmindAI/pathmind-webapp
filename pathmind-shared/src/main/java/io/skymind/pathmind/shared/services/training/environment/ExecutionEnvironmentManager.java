package io.skymind.pathmind.shared.services.training.environment;

import io.skymind.pathmind.shared.constants.EC2InstanceType;
import io.skymind.pathmind.shared.services.training.versions.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ExecutionEnvironmentManager {

    // todo need to save this to DB
    private static Map<Long, ExecutionEnvironment> environmentMap= new HashMap();

    private ExecutionEnvironment defaultEnvironment() {
        return new ExecutionEnvironment(AnyLogic.VERSION_8_6_1,
                PathmindHelper.VERSION_1_3_0,
                NativeRL.VERSION_1_3_0,
                JDK.VERSION_8_222,
                Conda.VERSION_0_8_7,
                EC2InstanceType.IT_36CPU_72GB);
    }

    public ExecutionEnvironment getEnvironment(long userId) {
        if (!environmentMap.containsKey(userId)) {
            environmentMap.put(userId, defaultEnvironment());
        }

        return environmentMap.get(userId);
    }


}
