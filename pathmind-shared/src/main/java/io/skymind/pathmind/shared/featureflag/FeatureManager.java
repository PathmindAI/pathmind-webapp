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
            case ACTIONS_AND_OBSERVATION_FEATURE:
                return false;
            case MULTI_AGENT_TRAINING:
                return multiAgentEnabled;
            case ACCOUNT_UPGRADE:
                return false;
            default:
                return true;
        }
    }
}
