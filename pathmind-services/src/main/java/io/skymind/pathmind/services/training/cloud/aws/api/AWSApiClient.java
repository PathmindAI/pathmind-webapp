package io.skymind.pathmind.services.training.cloud.aws.api;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.amazonaws.util.IOUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.skymind.pathmind.services.training.cloud.aws.api.dto.Job;
import io.skymind.pathmind.shared.constants.EC2InstanceType;
import io.skymind.pathmind.shared.constants.RunType;
import lombok.Getter;
import io.skymind.pathmind.services.training.cloud.aws.api.dto.Job;
import io.skymind.pathmind.services.training.cloud.aws.api.dto.UpdateEven;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class AWSApiClient {

    private final AmazonS3 s3Client;
    private final AmazonSQS sqsClient;
    private final ObjectMapper objectMapper;

    private final String bucketName;
    private final String queueUrl;
    private final String updaterQueueUrl;
    private final int mockCycle;

    public AWSApiClient(
            @Value("${pathmind.aws.region}") String region,
            @Value("${pathmind.aws.key.id}") String keyId,
            @Value("${pathmind.aws.secret_key}") String secretAccessKey,
            @Value("${pathmind.aws.s3.bucket}") String bucketName,
            @Value("${pathmind.aws.sqs_url}") String queueUrl,
            @Value("${pathmind.aws.sqs_updater_url}") String updaterQueueUrl,
            @Value("${pathmind.aws.mock_cycle:0}") int mockCycle,
            @Value("${pathmind.aws.mock-endpoint-url:}") String awsEndpointUrl,
            @Value("${pathmind.aws.mock-s3:true}") boolean mockS3,
            @Value("${pathmind.aws.mock-sqs:true}") boolean mockSQS,
            ObjectMapper objectMapper) {

        Regions regions = Regions.fromName(region);
        AWSCredentials credentials = new BasicAWSCredentials(keyId, secretAccessKey);
        AwsClientBuilder.EndpointConfiguration endpointConfiguration = null;
        if (!StringUtils.isEmpty(awsEndpointUrl)) {
            endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(awsEndpointUrl, region);
        }

        AmazonS3ClientBuilder s3ClientBuilder =
                this.buildAwsClient(AmazonS3ClientBuilder.standard(), regions, credentials, endpointConfiguration, mockS3);
        if (endpointConfiguration != null && mockS3) {
            s3ClientBuilder.withPathStyleAccessEnabled(true);
        }
        this.s3Client = s3ClientBuilder.build();
        this.sqsClient = buildAwsClient(AmazonSQSClientBuilder.standard(), regions, credentials, endpointConfiguration, mockSQS).build();

        this.bucketName = bucketName;
        this.queueUrl = queueUrl;
        this.updaterQueueUrl = updaterQueueUrl;

        this.objectMapper = objectMapper;

        this.mockCycle = mockCycle;
        if (mockCycle != 0) {
            Assert.isTrue(mockCycle > 0, "Mock Cycle should be greater than zero");
            log.warn("Running with mock cycle {}", mockCycle);
        }
    }

    private <S extends AwsClientBuilder<S, T>, T> S buildAwsClient(
            S builder, Regions regions, AWSCredentials credentials,
            AwsClientBuilder.EndpointConfiguration endpointConfiguration, boolean mocked)
    {
        builder.withCredentials(new AWSStaticCredentialsProvider(credentials));
        if (endpointConfiguration != null && mocked) {
            builder.withEndpointConfiguration(endpointConfiguration);
        } else {
            builder.withRegion(regions);
        }
        return builder;
    }

    public List<Bucket> listBuckets() {
        return s3Client.listBuckets();
    }

    public ListObjectsV2Result listObjects() {
        return s3Client.listObjectsV2(bucketName);
    }

    public ListObjectsV2Result listObjects(String path) {
        return s3Client.listObjectsV2(bucketName, path);
    }

    public String fileUpload(String keyId, File file) {
        return s3Client.putObject(bucketName, keyId, file).getETag();
    }

    public String fileUpload(String keyId, byte[] bytes) {
        long contentLength = bytes.length;
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(contentLength);
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        return s3Client.putObject(bucketName, keyId, bis, metadata).getETag();
    }

    public byte[] fileContents(String keyId) {
        return fileContents(keyId, false);
    }

    public byte[] fileContents(String keyId, boolean checkExists) {
        if (checkExists) {
            if (!fileExists(keyId)) {
                return null;
            }
        }
        S3Object o = s3Client.getObject(bucketName, keyId);
        try {
            return IOUtils.toByteArray(o.getObjectContent());
        } catch (IOException e) {
            log.error("Failed to get content from {}/{}", bucketName, keyId, e);
            return null;
        }
    }

    public boolean fileExists(String keyId) {
        return s3Client.doesObjectExist(bucketName, keyId);
    }

    public void fileDelete(String bucketName, String keyId) {
        s3Client.deleteObject(bucketName, keyId);
    }

    public String jobSubmit(String jobId, RunType type) throws JsonProcessingException {
        final String mockType = type == null ? null : type.toString();
        Job job = new Job(bucketName, jobId, mockCycle, mockType);
        job.setEc2InstanceType(EC2InstanceType.IT_16CPU_32GB);

        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageGroupId("training")
                .withMessageBody(objectMapper.writeValueAsString(job));

        sqsClient.sendMessage(send_msg_request);
        return jobId;
    }

    public String jobStop(String jobId) throws JsonProcessingException {
        Job job = new Job(bucketName, jobId, true);

        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageGroupId("training")
                .withMessageBody(objectMapper.writeValueAsString(job));

        SendMessageResult result = sqsClient.sendMessage(send_msg_request);
        return result.getMessageId();
    }

    public String sendUpdaterMessage(UpdateEven event) throws JsonProcessingException {
        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withMessageDeduplicationId(UUID.randomUUID().toString()) // we might expect similar content based
                .withQueueUrl(updaterQueueUrl)
                .withMessageGroupId("updater")
                .withMessageBody(objectMapper.writeValueAsString(event));

        SendMessageResult result = sqsClient.sendMessage(send_msg_request);
        return result.getMessageId();
    }
}
