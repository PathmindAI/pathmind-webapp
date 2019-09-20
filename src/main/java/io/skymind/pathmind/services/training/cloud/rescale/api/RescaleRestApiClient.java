package io.skymind.pathmind.services.training.cloud.rescale.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.skymind.pathmind.services.training.cloud.rescale.api.dto.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class RescaleRestApiClient {
    private static final Logger log = LoggerFactory.getLogger(RescaleRestApiClient.class);
    private final String platformRegion;
    private final String apiKey;
    private final ObjectMapper objectMapper;
    private final WebClient client;

    public RescaleRestApiClient(
            @Value("${skymind.rescale.platform.region}") String platformRegion,
            @Value("${skymind.rescale.platform.key}") String apiKey,
            ObjectMapper objectMapper,
            WebClient.Builder webClientBuilder
    ) {
        this.platformRegion = platformRegion;
        this.apiKey = apiKey;
        this.objectMapper = objectMapper;

        client = webClientBuilder
                .baseUrl("https://" + platformRegion + "/api/v2")
                //.baseUrl("http://127.0.0.1:8000/api/v2")
                .defaultHeader("Authorization", "Token "+apiKey)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .filter(ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
                            log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
                            clientRequest.headers().forEach((name, values) -> values.forEach(value -> log.info("{}={}", name, value)));
                            return Mono.just(clientRequest);
                        })
                )
                .build();
    }

    public PagedResult<JobSummary> jobList() {
        return client.get().uri("/jobs/?page_size=9999")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PagedResult<JobSummary>>() {})
                .block();
    }

    public Job jobCreate(Job job){
        return client
                .post().uri("/jobs/")
                .contentType(MediaType.APPLICATION_JSON).body(Mono.just(job), Job.class)
                .retrieve()
                .onStatus(Predicate.isEqual(HttpStatus.BAD_REQUEST), it -> it.bodyToMono(String.class).map(RuntimeException::new))
                .bodyToMono(Job.class).block();
    }

    public void jobSubmit(String jobId){
        client.post().uri("/jobs/"+jobId+"/submit/")
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public void jobStop(String jobId){
        client.post().uri("/jobs/"+jobId+"/stop/")
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public void jobDelete(String jobId){
        client.delete().uri("/jobs/"+jobId+"/")
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public Job jobDetails(String jobId){
        return client.get().uri("/jobs/"+jobId+"/")
                .retrieve()
                .bodyToMono(Job.class).block();
    }

    public PagedResult<JobStatus> jobStatusHistory(String jobId){
        return client.get().uri("/jobs/"+jobId+"/statuses/")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PagedResult<JobStatus>>(){}).block();
    }

    public PagedResult<JobStatus> jobClusterStatusHistory(String jobId){
        return client.get().uri("/jobs/"+jobId+"/cluster_statuses/")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PagedResult<JobStatus>>(){}).block();
    }

    public PagedResult<JobRun> jobRuns(String jobId){
        return client.get().uri("/jobs/"+jobId+"/runs/")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PagedResult<JobRun>>(){}).block();
    }

    public List<DirectoryFileReference> tailFiles(String jobId, String run){
        return  client.get().uri("/jobs/"+jobId+"/runs/"+run+"/directory-contents/?page_size=9999")
                .retrieve()
                .bodyToFlux(DirectoryFileReference.class)
                .toStream().collect(Collectors.toList());
    }

    public PagedResult<RescaleFile> outputFiles(String jobId, String run){
        return client
                .get().uri("/jobs/"+jobId+"/runs/"+run+"/files/?page_size=1000")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PagedResult<RescaleFile>>(){}).block();
    }

    public String tailConsole(String jobId, String run){
        return new String(tail(jobId, run, "process_output.log"));
    }

    public byte[] tail(String jobId, String run, String filename){
        return client
                .get().uri("/jobs/"+jobId+"/runs/"+run+"/tail/"+filename)
                .retrieve()
                .bodyToMono(byte[].class)
                .block();
    }

    public byte[] fileContents(String fileId) {
        return client.get().uri("/files/"+fileId+"/contents/").retrieve().bodyToMono(byte[].class).block();
    }

    public byte[] outputFile(String jobId, String run, String filename){
        final RescaleFile rescaleFile = outputFiles(jobId, run).getResults().stream().filter(it -> it.getName().equals(filename) && it.isUploaded() && !it.isDeleted()).findFirst().get();
        return fileContents(rescaleFile.getId());
    }

    public String consoleOutput(String jobId, String run){
        return new String(outputFile(jobId, run, "process_output.log"));
    }



    /**
     * Uses a different client library to work around the fact that rescale doesn't support `Transfer-Encoding: chunked`
     * for file uploads and WebClient doesn't properly support not using it for file uploads
     *
     * TODO: Test if upload with indefinite size works, or if we must create an input stream reader body type that knows about filesize
     */
    public RescaleFile fileUpload(InputStream content, String filename) throws IOException {
        final CloseableHttpClient client = HttpClients.custom().setDefaultHeaders(Arrays.asList(
                new BasicHeader("Authorization", "Token "+apiKey)
        )).build();
        final HttpPost post = new HttpPost("https://" + platformRegion + "/api/v2/files/contents/");
        post.setEntity(MultipartEntityBuilder.create()
                .addBinaryBody("file", content, ContentType.APPLICATION_OCTET_STREAM, filename)
                .build());

        final CloseableHttpResponse resp = client.execute(post);

        return objectMapper.readValue(resp.getEntity().getContent(), RescaleFile.class);
    }

    public PagedResult<RescaleFile> filesList(){
        return client
                .get().uri("/files/?page_size=9999")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PagedResult<RescaleFile>>(){}).block();
    }

    public void deleteFile(String fileId){
        client.delete().uri("/files/"+fileId+"/")
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public InputStream policyFile(String jobId){
        return new ByteArrayInputStream(outputFile(jobId, "1", "policy.zip"));
    }
}
