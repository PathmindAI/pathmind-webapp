package io.skymind.pathmind.services.training.cloud.rescale.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.skymind.pathmind.services.training.cloud.rescale.api.dto.*;
import io.skymind.pathmind.services.training.constant.TrainingFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RescaleRestApiClient {
    private final String rescaleBaseUrl;
    private final String apiKey;
    private final ObjectMapper objectMapper;
    private final WebClient client;

    private static final Duration TIMEOUT = Duration.ofSeconds(5);

    public RescaleRestApiClient(
            @Value("${skymind.rescale.base-url}") String rescaleBaseUrl,
            @Value("${skymind.rescale.platform.key}") String apiKey,
            ObjectMapper objectMapper,
            WebClient.Builder webClientBuilder
    ) {
        this.rescaleBaseUrl = rescaleBaseUrl;
        this.apiKey = apiKey;
        this.objectMapper = objectMapper;

        client = webClientBuilder
                .baseUrl(rescaleBaseUrl)
                .defaultHeader("Authorization", "Token "+apiKey)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public PagedResult<JobSummary> jobList() {
        return get("/jobs/?page_size=9999", new ParameterizedTypeReference<PagedResult<JobSummary>>() {});
    }

    public Job jobCreate(Job job){
        return get("/jobs/", Job.class);
    }

    public void jobSubmit(String jobId){
        post("/jobs/" + jobId + "/submit/", Void.class);
    }

    public void jobStop(String jobId){
        post("/jobs/" + jobId + "/stop/", Void.class);
    }

    public void jobDelete(String jobId){
        delete("/jobs/" + jobId + "/", Void.class);
    }

    public Job jobDetails(String jobId){
        return get("/jobs/" + jobId + "/", Job.class);
    }

    public PagedResult<JobStatus> jobStatusHistory(String jobId){
        return get("/jobs/" + jobId + "/statuses/", new ParameterizedTypeReference<PagedResult<JobStatus>>(){});
    }

    public PagedResult<JobStatus> jobClusterStatusHistory(String jobId){
        return get("/jobs/" + jobId + "/cluster_statuses/", new ParameterizedTypeReference<PagedResult<JobStatus>>(){});
    }

    public PagedResult<JobRun> jobRuns(String jobId){
        return get("/jobs/" + jobId + "/runs/", new ParameterizedTypeReference<PagedResult<JobRun>>(){});
    }

    public List<DirectoryFileReference> workingFiles(String jobId, String run){
        return  client.get().uri("/jobs/" + jobId + "/runs/" + run + "/directory-contents/?page_size=9999")
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, it -> it.bodyToMono(String.class).map(RuntimeException::new))
                .onStatus(HttpStatus::is5xxServerError, it -> it.bodyToMono(String.class).map(RuntimeException::new))
                .bodyToFlux(DirectoryFileReference.class)
                .onErrorMap(RuntimeException::new)
                .toStream()
                .collect(Collectors.toList());
    }

    public PagedResult<RescaleFile> outputFiles(String jobId, String run){
        return get("/jobs/" + jobId + "/runs/" + run + "/files/?page_size=1000", new ParameterizedTypeReference<PagedResult<RescaleFile>>(){});
    }

    public String tailConsole(String jobId, String run){
        return new String(tail(jobId, run, TrainingFile.RESCALE_LOG));
    }

    public byte[] tail(String jobId, String run, String filename){
        return get("/jobs/" + jobId + "/runs/" + run + "/tail/" + filename, byte[].class, true);
    }

    public byte[] fileContents(String fileId) {
        return get("/files/" + fileId + "/contents/", byte[].class, true);
    }

    public byte[] outputFile(String jobId, String run, String filename){
        //todo why isUploaded is false
//        final RescaleFile rescaleFile = outputFiles(jobId, run).getResults().stream().filter(it -> it.getName().equals(filename) && it.isUploaded() && !it.isDeleted()).findFirst().get();
        final RescaleFile rescaleFile = outputFiles(jobId, run).getResults().stream().filter(it -> it.getName().equals(filename)  && !it.isDeleted()).findFirst().get();
        return fileContents(rescaleFile.getId());
    }

    public String consoleOutput(String jobId, String run){
        return new String(outputFile(jobId, run, TrainingFile.RESCALE_LOG));
    }

    /**
     * Uses a different client library to work around the fact that rescale doesn't support `Transfer-Encoding: chunked`
     * for file uploads and WebClient doesn't properly support not using it for file uploads
     *
     * TODO: Test if upload with indefinite size works, or if we must create an input stream reader body type that knows about filesize
     */
    public RescaleFile fileUpload(byte[] content, String filename) throws IOException
    {
        final HttpPost post = getHttpPost();
        post.setEntity(MultipartEntityBuilder.create()
                .addBinaryBody("file", content, ContentType.APPLICATION_OCTET_STREAM, filename)
                .build());

        try(final CloseableHttpClient client = getCloseableHttpClient();
            final CloseableHttpResponse resp = client.execute(post))
        {
            return objectMapper.readValue(resp.getEntity().getContent(), RescaleFile.class);
        }
    }

    public RescaleFile fileUpload(File file, String filename) throws IOException
    {
        final HttpPost post = getHttpPost();
        post.setEntity(MultipartEntityBuilder.create()
                .addBinaryBody("file", file, ContentType.MULTIPART_FORM_DATA, filename)
                .build());

        try(final CloseableHttpClient client = getCloseableHttpClient();
            final CloseableHttpResponse resp = client.execute(post))
        {
            return objectMapper.readValue(resp.getEntity().getContent(), RescaleFile.class);
        }
    }

    private HttpPost getHttpPost() {
        return new HttpPost(rescaleBaseUrl + "/files/contents/");
    }

    private CloseableHttpClient getCloseableHttpClient() {
        return HttpClients.custom().setDefaultHeaders(
                Arrays.asList(new BasicHeader("Authorization", "Token " + apiKey)))
                .build();
    }

    public PagedResult<RescaleFile> filesList(){
        return get("/files/?page_size=9999", new ParameterizedTypeReference<PagedResult<RescaleFile>>(){});
    }

    public void deleteFile(String fileId){
        delete("/files/" + fileId + "/", Void.class);
    }

    private <T> T get(String uri, Class<T> bodyType) {
        return get(uri, bodyType, false);
    }

    private <T> T get(String uri, Class<T> bodyType, boolean isFile) {
        try {
            Mono<T> mono = client.get().uri(uri)
                    .retrieve()
                    .onStatus(HttpStatus::is4xxClientError, it -> it.bodyToMono(String.class).map(RuntimeException::new))
                    .onStatus(HttpStatus::is5xxServerError, it -> it.bodyToMono(String.class).map(RuntimeException::new))
                    .bodyToMono(bodyType)
                    .onErrorMap(RuntimeException::new);

            return isFile ? mono.block() : mono.block(TIMEOUT);
        } catch (IllegalStateException e) { // If block() exceeds TIMEOUT, it will throw IllegalStateException
            log.error(e.getMessage());
            return null;
        }
    }

    private <T> T get(String uri, ParameterizedTypeReference<T> typeReference) {
        try {
            return client.get().uri(uri)
                    .retrieve()
                    .onStatus(HttpStatus::is4xxClientError, it -> it.bodyToMono(String.class).map(RuntimeException::new))
                    .onStatus(HttpStatus::is5xxServerError, it -> it.bodyToMono(String.class).map(RuntimeException::new))
                    .bodyToMono(typeReference)
                    .onErrorMap(RuntimeException::new)
                    .block(TIMEOUT);
        } catch (IllegalStateException e) { // If block() exceeds TIMEOUT, it will throw IllegalStateException
            log.error(e.getMessage());

            return (T) new PagedResult<T>(0, new ArrayList()){};
        }
    }

    private <T> void post(String uri, Class<T> bodyType) {
        client.post().uri(uri)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, it -> it.bodyToMono(String.class).map(RuntimeException::new))
                .onStatus(HttpStatus::is5xxServerError, it -> it.bodyToMono(String.class).map(RuntimeException::new))
                .bodyToMono(bodyType)
                .onErrorMap(RuntimeException::new)
                .block();
    }

    private <T> void delete(String uri, Class<T> bodyType) {
        client.delete().uri(uri)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, it -> it.bodyToMono(String.class).map(RuntimeException::new))
                .onStatus(HttpStatus::is5xxServerError, it -> it.bodyToMono(String.class).map(RuntimeException::new))
                .bodyToMono(bodyType)
                .onErrorMap(RuntimeException::new)
                .block();
    }
}
