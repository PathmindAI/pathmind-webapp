package io.skymind.pathmind.shared.featureflag;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FeatureManager {

    private final boolean multiAgentEnabled;

    public FeatureManager(@Value("${pathmind.training.multiagent:false}") boolean multiAgentEnabled) {
        this.multiAgentEnabled = multiAgentEnabled;
    }


    public boolean isEnabled(Feature feature) {
        switch (feature) {
            case REWARD_VARIABLES_FEATURE:
                return true;
            case MULTI_AGENT_TRAINING:
                return multiAgentEnabled;
            case ACCOUNT_UPGRADE:
                return false;
            case SIMULATION_METRICS:
                return true;
            default:
                return true;
        }
    }
}
