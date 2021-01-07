package io.skymind.pathmind.services.project.demo;

import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;

@Component
public class ExperimentManifestRepository {

    public List<ExperimentManifest> getAll() {
        return List.of(
                ExperimentManifest.builder()
                        .name("Supply Chain Demo 1")
                        .imageUrl(URI.create("https://www.lokad.com/public/Upload//Support/Glossary/supply-chain-management.png"))
                        .description("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.")
                        .modelUrl(URI.create("https://files-media-images.s3-eu-west-1.amazonaws.com/SupplyChainDemoAsTuple+Exported.zip"))
                        .rewardFunction(
                                //@formatter:off
                                "reward -= after.waitTimeMean - before.waitTimeMean;\n" +
                                "reward -= after.costTotalMean - before.costTotalMean;\n"
                                //@formatter:on
                        )
                        .build(),
                ExperimentManifest.builder()
                        .name("Supply Chain Demo 2")
                        .imageUrl(URI.create("https://www.lokad.com/public/Upload//Support/Glossary/supply-chain-management.png"))
                        .description("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.")
                        .modelUrl(URI.create("https://files-media-images.s3-eu-west-1.amazonaws.com/SupplyChainDemoAsTuple+Exported.zip"))
                        .rewardFunction(
                                //@formatter:off
                                "reward -= after.waitTimeMean - before.waitTimeMean;\n" +
                                "reward -= after.costTotalMean - before.costTotalMean;\n"
                                //@formatter:on
                        )
                        .build(),
                ExperimentManifest.builder()
                        .name("Supply Chain Demo 3")
                        .imageUrl(URI.create("https://www.lokad.com/public/Upload//Support/Glossary/supply-chain-management.png"))
                        .description("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.")
                        .modelUrl(URI.create("https://files-media-images.s3-eu-west-1.amazonaws.com/SupplyChainDemoAsTuple+Exported.zip"))
                        .rewardFunction(
                                //@formatter:off
                                "reward -= after.waitTimeMean - before.waitTimeMean;\n" +
                                "reward -= after.costTotalMean - before.costTotalMean;\n"
                                //@formatter:on
                        )
                        .build()
        );
    }

}
