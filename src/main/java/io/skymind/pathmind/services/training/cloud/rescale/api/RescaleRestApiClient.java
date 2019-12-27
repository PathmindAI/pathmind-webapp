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
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RescaleRestApiClient {
    private final String rescaleBaseUrl;
    private final String apiKey;
    private final ObjectMapper objectMapper;
    private final WebClient client;

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
        return client.get().uri("/jobs/?page_size=9999")
                .retrieve()
                .onStatus(Predicate.isEqual(HttpStatus.BAD_REQUEST), it -> it.bodyToMono(String.class).map(RuntimeException::new))
                .bodyToMono(new ParameterizedTypeReference<PagedResult<JobSummary>>() {})
                .onErrorMap(RuntimeException::new)
                .block();
    }

    public Job jobCreate(Job job){
        return client
                .post().uri("/jobs/")
                .contentType(MediaType.APPLICATION_JSON).body(Mono.just(job), Job.class)
                .retrieve()
                .onStatus(Predicate.isEqual(HttpStatus.BAD_REQUEST), it -> it.bodyToMono(String.class).map(RuntimeException::new))
                .bodyToMono(Job.class)
                .onErrorMap(RuntimeException::new)
                .block();
    }

    public void jobSubmit(String jobId){
        client.post().uri("/jobs/"+jobId+"/submit/")
                .retrieve()
                .onStatus(Predicate.isEqual(HttpStatus.BAD_REQUEST), it -> it.bodyToMono(String.class).map(RuntimeException::new))
                .bodyToMono(Void.class)
                .onErrorMap(RuntimeException::new)
                .block();
    }

    public void jobStop(String jobId){
        client.post().uri("/jobs/"+jobId+"/stop/")
                .retrieve()
                .onStatus(Predicate.isEqual(HttpStatus.BAD_REQUEST), it -> it.bodyToMono(String.class).map(RuntimeException::new))
                .bodyToMono(Void.class)
                .onErrorMap(RuntimeException::new)
                .block();
    }

    public void jobDelete(String jobId){
        client.delete().uri("/jobs/"+jobId+"/")
                .retrieve()
                .onStatus(Predicate.isEqual(HttpStatus.BAD_REQUEST), it -> it.bodyToMono(String.class).map(RuntimeException::new))
                .bodyToMono(Void.class)
                .onErrorMap(RuntimeException::new)
                .block();
    }

    public Job jobDetails(String jobId){
        return client.get().uri("/jobs/"+jobId+"/")
                .retrieve()
                .onStatus(Predicate.isEqual(HttpStatus.BAD_REQUEST), it -> it.bodyToMono(String.class).map(RuntimeException::new))
                .bodyToMono(Job.class)
                .onErrorMap(RuntimeException::new)
                .block();
    }

    public PagedResult<JobStatus> jobStatusHistory(String jobId){
        return client.get().uri("/jobs/"+jobId+"/statuses/")
                .retrieve()
                .onStatus(Predicate.isEqual(HttpStatus.BAD_REQUEST), it -> it.bodyToMono(String.class).map(RuntimeException::new))
                .bodyToMono(new ParameterizedTypeReference<PagedResult<JobStatus>>(){})
                .onErrorMap(RuntimeException::new)
                .block();
    }

    public PagedResult<JobStatus> jobClusterStatusHistory(String jobId){
        return client.get().uri("/jobs/"+jobId+"/cluster_statuses/")
                .retrieve()
                .onStatus(Predicate.isEqual(HttpStatus.BAD_REQUEST), it -> it.bodyToMono(String.class).map(RuntimeException::new))
                .bodyToMono(new ParameterizedTypeReference<PagedResult<JobStatus>>(){})
                .onErrorMap(RuntimeException::new)
                .block();
    }

    public PagedResult<JobRun> jobRuns(String jobId){
        return client.get().uri("/jobs/"+jobId+"/runs/")
                .retrieve()
                .onStatus(Predicate.isEqual(HttpStatus.BAD_REQUEST), it -> it.bodyToMono(String.class).map(RuntimeException::new))
                .bodyToMono(new ParameterizedTypeReference<PagedResult<JobRun>>(){})
                .onErrorMap(RuntimeException::new)
                .block();
    }

    // Github issue #569 -> Can you please add the error handling on this DH as I'm not familiar enough with it to do it on a List.
    public List<DirectoryFileReference> workingFiles(String jobId, String run){
        return  client.get().uri("/jobs/"+jobId+"/runs/"+run+"/directory-contents/?page_size=9999")
                .retrieve()
                .bodyToFlux(DirectoryFileReference.class)
                .toStream().collect(Collectors.toList());
    }

    public PagedResult<RescaleFile> outputFiles(String jobId, String run){
        return client.get().uri("/jobs/"+jobId+"/runs/"+run+"/files/?page_size=1000")
                .retrieve()
                .onStatus(Predicate.isEqual(HttpStatus.BAD_REQUEST), it -> it.bodyToMono(String.class).map(RuntimeException::new))
                .bodyToMono(new ParameterizedTypeReference<PagedResult<RescaleFile>>(){})
                .onErrorMap(RuntimeException::new)
                .block();
    }

    public String tailConsole(String jobId, String run){
        return new String(tail(jobId, run, TrainingFile.RESCALE_LOG));
    }

    public byte[] tail(String jobId, String run, String filename){
        return client.get().uri("/jobs/"+jobId+"/runs/"+run+"/tail/"+filename)
                .retrieve()
                .onStatus(Predicate.isEqual(HttpStatus.BAD_REQUEST), it -> it.bodyToMono(String.class).map(RuntimeException::new))
                .bodyToMono(byte[].class)
                .onErrorMap(RuntimeException::new)
                .block();
    }

    public byte[] fileContents(String fileId) {
        return client.get().uri("/files/"+fileId+"/contents/")
                .retrieve()
                .onStatus(Predicate.isEqual(HttpStatus.BAD_REQUEST), it -> it.bodyToMono(String.class).map(RuntimeException::new))
                .bodyToMono(byte[].class)
                .onErrorMap(RuntimeException::new)
                .block();
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
                Arrays.asList(new BasicHeader("Authorization", "Token "+apiKey)))
                .build();
    }

    public PagedResult<RescaleFile> filesList(){
        return client
                .get().uri("/files/?page_size=9999")
                .retrieve()
                .onStatus(Predicate.isEqual(HttpStatus.BAD_REQUEST), it -> it.bodyToMono(String.class).map(RuntimeException::new))
                .bodyToMono(new ParameterizedTypeReference<PagedResult<RescaleFile>>(){})
                .onErrorMap(RuntimeException::new)
                .block();
    }

    public void deleteFile(String fileId){
        client.delete().uri("/files/"+fileId+"/")
                .retrieve()
                .onStatus(Predicate.isEqual(HttpStatus.BAD_REQUEST), it -> it.bodyToMono(String.class).map(RuntimeException::new))
                .bodyToMono(Void.class)
                .onErrorMap(RuntimeException::new)
                .block();
    }
}
