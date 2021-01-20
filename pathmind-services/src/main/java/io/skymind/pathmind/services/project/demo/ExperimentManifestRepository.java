package io.skymind.pathmind.services.project.demo;

import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;

@Component
public class ExperimentManifestRepository {

    public List<ExperimentManifest> getAll() {
        return List.of(
                ExperimentManifest.builder()
                        .name("Automated Guided Vehicles (AGV)")
                        .imageUrl(URI.create("https://downloads.intercomcdn.com/i/o/273937963/e33cea1e9b5ac12c5d6de951/image.png"))
                        .description("A fleet of automated guided vehicles (AGVs) optimizes its dispatching routes to maximize product throughput in a manufacturing center. When component parts arrive to be processed, they must be brought to the appropriate machine according to a specific processing sequence.")
                        .result("The reinforcement learning policy outperforms the optimizer by over 20%.")
                        .modelUrl(URI.create("https://s3.amazonaws.com/public-pathmind.com/preloaded_models/AGVPathmindDemoExport.zip"))
                        .rewardFunction(
                                //@formatter:off
                                "reward += after.totalThroughput * 0.01;\n" +
                                "reward += ( after.machineUtil - before.machineUtil ) * 100;\n" +
                                "reward += after.essentialDelivery - before.essentialDelivery;\n" +
                                "reward -= after.fullConveyor - before.fullConveyor;\n" +
                                "reward += ( after.trips - before.trips ) * 0.01;\n"
                                //@formatter:on
                        )
                        .build(),
                ExperimentManifest.builder()
                        .name("Product Delivery")
                        .imageUrl(URI.create("https://downloads.intercomcdn.com/i/o/233456333/5221ec61b7e207c74823733d/image.png"))
                        .description("This model simulates product delivery in Europe. The supply chain includes three manufacturing centers and fifteen distributors that order random amounts of the product every 1-2 days. There is a fleet of trucks in each manufacturing facility. When a manufacturing facility receives an order from a distributor, it checks the number of products in storage. If the required amount is available, it sends a loaded truck to the distributor. Otherwise, the order waits until the factory produces sufficient inventory.")
                        .result("Reinforcement learning outperforms nearest manufacturing center heuristic by over 80%.")
                        .modelUrl(URI.create("https://s3.amazonaws.com/public-pathmind.com/preloaded_models/ProductDeliveryPathmindDemoExport.zip"))
                        .rewardFunction(
                                //@formatter:off
                                "reward -= after.avgWaitTime - before.avgWaitTime; // Minimize wait times\n"
                                //@formatter:on
                        )
                        .build(),
                ExperimentManifest.builder()
                        .name("Autonomous Moon Landing")
                        .imageUrl(URI.create("https://downloads.intercomcdn.com/i/o/222782983/ffe1bbf2a4c5766698a7dbca/Overview.png"))
                        .description("This model simulates a lunar module as it attempts to make a safe landing on the moon. Several key factors are considered as the module approaches the designated landing area and each must have values within a safe zone to avoid crashing or drifting into space.")
                        .result("The AI learns to land safely on the moon without human intervention.")
                        .modelUrl(URI.create("https://s3.amazonaws.com/public-pathmind.com/preloaded_models/MoonLandingPathmindDemoExport.zip"))
                        .rewardFunction(
                                //@formatter:off
                                "reward += after.fuelRemaining - before.fuelRemaining;\n" +
                                "reward += Math.abs(before.distanceToX) - Math.abs(after.distanceToX);\n" +
                                "reward += Math.abs(before.distanceToY) - Math.abs(after.distanceToY);\n" +
                                "reward += before.distanceToZ - after.distanceToZ;\n\n" +
                                "reward += after.landed == 1 ? 3 : 0;\n" +
                                "reward -= after.crashed == 1 ? 0.3 : 0;\n" +
                                "reward -= after.gotAway == 1 ? 1 : 0;\n\n" +
                                "reward -= before.distanceToZ <= 100. / 1500. && Math.abs(after.speedX) > 200 ? 0.01 : 0;\n" + 
                                "reward -= before.distanceToZ <= 100. / 1500. && Math.abs(after.speedY) > 200 ? 0.01 : 0;\n" +
                                "reward -= before.distanceToZ <= 100. / 1500. && Math.abs(after.speedZ) > 200 ? 0.01 : 0;\n"
                                //@formatter:on
                        )
                        .build()
        );
    }

}
