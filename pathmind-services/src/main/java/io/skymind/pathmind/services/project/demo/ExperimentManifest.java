package io.skymind.pathmind.services.project.demo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URI;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExperimentManifest {

    public URI modelUrl;
    public String rewardFunction;

    public ExperimentManifest(URI modelUrl) {
        this.modelUrl = modelUrl;
    }
}
