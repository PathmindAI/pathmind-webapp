package io.skymind.pathmind.services.project.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.skymind.pathmind.services.project.rest.dto.AnalyzeRequestDTO;
import io.skymind.pathmind.services.project.rest.dto.HyperparametersDTO;
import io.skymind.pathmind.shared.utils.ObjectMapperHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.UUID;
import java.util.function.Predicate;

@Slf4j
@Service
public class ModelAnalyzerApiClient {
    private final String url;
    private final String token;
    private final ObjectMapper objectMapper;
    private final WebClient client;

    public ModelAnalyzerApiClient(
            @Value("${skymind.model.analyzer.base-url}") String url,
            @Value("${skymind.model.analyzer.token}") String token,
            ObjectMapper objectMapper,
            WebClient.Builder webClientBuilder
    ) {
        this.url = url;
        this.token = token;
        this.objectMapper = objectMapper;

        client = webClientBuilder
                .baseUrl(this.url)
//                .defaultHeader("Authorization", "Token " + this.token)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .filter(ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
                            log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
                            clientRequest.headers().forEach((name, values) -> values.forEach(value -> log.info("{}={}", name, value)));
                            return Mono.just(clientRequest);
                        })
                )
                .build();
    }

    public String health() {
        // todo when we need to use this endpoint, need to change return type
        String result = client.get().uri("/actuator/health")
                .retrieve()
                .onStatus(Predicate.isEqual(HttpStatus.BAD_REQUEST), it -> it.bodyToMono(String.class).map(RuntimeException::new))
                .bodyToMono(String.class)
                .onErrorMap(RuntimeException::new)
                .block();
        return result;
    }

    public HyperparametersDTO analyze(File file) {
        return analyze(file);
    }

    public HyperparametersDTO analyze(File file, String message, String mainAgentName, String experimentClass, String experimentType, String pmHelperName) {
        final HttpPost post = new HttpPost(this.url + "/api/v1/extract-hyperparameters");

        String messageId = message + "_" + UUID.randomUUID().toString();
        AnalyzeRequestDTO req = new AnalyzeRequestDTO(messageId, mainAgentName, experimentClass, experimentType, pmHelperName);
        StringBody requestBody = null;
        try {
            requestBody = new StringBody(ObjectMapperHolder.getJsonMapper().writeValueAsString(req), ContentType.MULTIPART_FORM_DATA);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }

        post.setEntity(MultipartEntityBuilder.create()
            .addPart("id", requestBody)
            .addBinaryBody("file", file, ContentType.MULTIPART_FORM_DATA, file.getName())
            .build());


        try (final CloseableHttpClient client = getCloseableHttpClient();
             final CloseableHttpResponse resp = client.execute(post)) {
            log.info(String.format("Analyze Request %s is sent", messageId));
            return objectMapper.readValue(resp.getEntity().getContent(), HyperparametersDTO.class);
        } catch (Exception e) {
            log.warn(e.getMessage());
            return null;
        }
    }


    private CloseableHttpClient getCloseableHttpClient() {
        return HttpClients.custom().setDefaultHeaders(
                Arrays.asList(new BasicHeader("Authorization", "Token "+ this.token)))
                .build();
    }
}
