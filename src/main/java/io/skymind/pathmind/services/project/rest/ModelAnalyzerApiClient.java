package io.skymind.pathmind.services.project.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class ModelAnalyzerApiClient {
    private final String url;
    private final String token;
    private final ObjectMapper objectMapper;
    private final WebClient client;

    public ModelAnalyzerApiClient(
            @Value("${skymind.model.anlayzer.base-url}") String url,
            @Value("${skymind.model.anlayzer.token}") String token,
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

    public void health() {
        String result = client.get().uri("/actuator/health")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        System.out.println(result);
    }
}
