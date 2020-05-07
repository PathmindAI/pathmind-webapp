package io.skymind.pathmind.services.training.cloud.aws.api;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class AWSApiClient {
    private final AWSCredentials credentials;
    private final AmazonS3 s3Client;
    private final AmazonSQS sqsClient;
    private final ObjectMapper objectMapper;

    private final Regions regions;
    @Getter
    private final String bucketName;
    private final String queueUrl;
    private final int mockCycle;

    public AWSApiClient(
            @Value("${pathmind.aws.region}") String region,
            @Value("${pathmind.aws.key.id}") String keyId,
            @Value("${pathmind.aws.secret_key}") String secretAccessKey,
            @Value("${pathmind.aws.s3.bucket}") String bucketName,
            @Value("${pathmind.aws.sqs_url}") String queueUrl,
            @Value("${pathmind.aws.mock_cycle:0}") int mockCycle,
            ObjectMapper objectMapper) {

        this.regions = Regions.fromName(region);
        this.credentials = new BasicAWSCredentials(
                keyId,
                secretAccessKey
        );

        this.s3Client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(regions)
                .build();

        this.sqsClient = AmazonSQSClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(regions)
                .build();

        this.bucketName = bucketName;
        this.queueUrl = queueUrl;

        this.objectMapper = objectMapper;

        this.mockCycle = mockCycle;
        if (mockCycle != 0) {
            Assert.isTrue(mockCycle > 0, "Mock Cycle should be greater than zero");
            log.warn("Running with mock cycle {}", mockCycle);
        }
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
}
