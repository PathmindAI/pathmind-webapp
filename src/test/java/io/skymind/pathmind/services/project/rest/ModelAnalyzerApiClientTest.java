package io.skymind.pathmind.services.project.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.skymind.pathmind.utils.ObjectMapperHolder;
import org.junit.Test;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.Assert.*;

public class ModelAnalyzerApiClientTest {

    @Test
    public void testBasicModelAnlayze() {
        ObjectMapper objectMapper = ObjectMapperHolder.getJsonMapper();

        final ModelAnalyzerApiClient client = new ModelAnalyzerApiClient(
                "http://localhost:8081",
                null,
                objectMapper,
                WebClient.builder());

        client.health();
    }

}