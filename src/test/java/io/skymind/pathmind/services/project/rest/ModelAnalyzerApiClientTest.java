package io.skymind.pathmind.services.project.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.skymind.pathmind.services.project.rest.dto.HyperparametersDTO;
import io.skymind.pathmind.utils.ObjectMapperHolder;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

@Ignore
public class ModelAnalyzerApiClientTest {

    @Test
    public void testBasicModelAnalyze() throws IOException {
        ObjectMapper objectMapper = ObjectMapperHolder.getJsonMapper();

        final ModelAnalyzerApiClient client = new ModelAnalyzerApiClient(
                "http://pathmindmodelanalyzer-env-1.zmjtstudaf.us-east-1.elasticbeanstalk.com",
                null,
                objectMapper,
                WebClient.builder());

        File model = new ClassPathResource("model/call_center.zip").getFile();
        HyperparametersDTO hyperparametersDTO = client.analyze(model);

        assertEquals(hyperparametersDTO.getActions(), "70");
        assertEquals(hyperparametersDTO.getObservations(), "125");
        assertEquals(hyperparametersDTO.getRewardFunction(), "new double[]{this.getReward()}");
    }

}