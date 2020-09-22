package io.skymind.pathmind.shared.featureflag;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@ToString
@Component
public class FeatureManager {

    private final boolean simulationMetrics;

    public FeatureManager(
            @Value("${pathmind.toggle.simulation-metrics:true}") boolean simulationMetrics
            ) {
        this.simulationMetrics = simulationMetrics;

        log.info("Toggles: {}", this);
    }

    public boolean isEnabled(Feature feature) {
        switch (feature) {
            case SEARCH:
                return true;
            case ACCOUNT_UPGRADE:
                return false;
            case SIMULATION_METRICS:
                return simulationMetrics;
            default:
                return true;
        }
    }
}
