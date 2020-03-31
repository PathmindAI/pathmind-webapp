package io.skymind.pathmind.webapp.security;

import org.springframework.stereotype.Component;

@Component
public class FeatureManager {
    public boolean isEnabled(Feature feature) {
        switch (feature) {
        case REWARD_VARIABLES_FEATURE:
        	return false;
        default:
            return false;
        }
    }
}
