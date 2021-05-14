package io.skymind.pathmind.services.project.demo;

import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;

@Component
public class ExperimentManifestRepository {

    public List<ExperimentManifest> getAll() {
        return List.of(
                ExperimentManifest.builder()
                        .name("Automated Guided Vehicles")
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
                        .name("Interconnected Call Centers")
                        .imageUrl(URI.create("frontend/images/callcenters.png"))
                        .description("Calls are made to each of five interconnected call centers simultaneously. Once a call is received, each call center will decide to either accept the call or transfer it to another call center. A call is balked when the wait time for a particular caller exceeds a randomly initialized threshold (between 20 and 25 minutes). We compare the reinforcement learning policy with three call routing heuristics (no call transferring, shortest queue, and most efficient call center). The objective is to minimize wait times and to minimize balked callers.")
                        .result("The reinforcement learning policy trained using Pathmind outperforms the heuristics by over 9.6%.")
                        .modelUrl(URI.create("https://s3.amazonaws.com/public-pathmind.com/preloaded_models/CallCenterPathmindDemo.zip"))
                        .rewardFunction(
                                //@formatter:off
                                "reward += after.aMeanWaitTimes - before.aMeanWaitTimes; // minimize aMeanWaitTimes"
                                //@formatter:on
                        )
                        .build()
        );
    }

}
