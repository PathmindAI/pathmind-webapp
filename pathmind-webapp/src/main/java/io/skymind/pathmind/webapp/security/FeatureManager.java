package io.skymind.pathmind.webapp.security;

import org.springframework.stereotype.Component;

@Component
public class FeatureManager {
    public boolean isEnabled(Feature feature) {
        switch (feature) {
        case TEST_FEATURE:
            return false;
        default:
            return true;
        }
    }
}
