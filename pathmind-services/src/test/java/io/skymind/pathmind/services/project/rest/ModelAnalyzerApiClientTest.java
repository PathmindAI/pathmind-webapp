package io.skymind.pathmind.services.project.rest;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.skymind.pathmind.services.project.rest.dto.AnalyzeRequestDTO;
import io.skymind.pathmind.services.project.rest.dto.HyperparametersDTO;
import io.skymind.pathmind.shared.utils.ObjectMapperHolder;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.Assert.assertEquals;

@Ignore
public class ModelAnalyzerApiClientTest {

    @Test
    public void testBasicModelAnalyze() throws IOException {
        ObjectMapper objectMapper = ObjectMapperHolder.getJsonMapper();

        final ModelAnalyzerApiClient client = new ModelAnalyzerApiClient(
                "https://ma.dev.devpathmind.com",
                null,
                objectMapper,
                WebClient.builder());

        File model = new ClassPathResource("model/call_center.zip").getFile();
        HyperparametersDTO hyperparametersDTO = client.analyze(model, AnalyzeRequestDTO.ModelType.ANY_LOGIC);

        assertEquals(hyperparametersDTO.getObservations(), "70");
        assertEquals(hyperparametersDTO.getRewardFunction(), "new double[]{this.getReward()}");
    }

}