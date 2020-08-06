package io.skymind.pathmind.services.training.cloud.aws.api;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.amazonaws.util.IOUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.skymind.pathmind.services.training.cloud.aws.api.client.AwsApiClientS3;
import io.skymind.pathmind.services.training.cloud.aws.api.client.AwsApiClientSQS;
import io.skymind.pathmind.services.training.cloud.aws.api.dto.Job;
import io.skymind.pathmind.shared.constants.EC2InstanceType;
import io.skymind.pathmind.shared.constants.RunType;
import io.skymind.pathmind.shared.exception.PathMindException;
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
@Getter
public class AWSApiClient {

    private final AmazonS3 s3Client;
    private final String bucketName;

    private final AmazonSQS sqsClient;
    private final String queueUrl;
    private final String updaterQueueUrl;

    private final ObjectMapper objectMapper;

    private final int mockCycle;

    public AWSApiClient(
            AwsApiClientS3 s3,
            @Value("${pathmind.aws.s3.bucket}") String bucketName,
            AwsApiClientSQS sqs,
            @Value("${pathmind.aws.sqs.url}") String queueUrl,
            @Value("${pathmind.aws.sqs.updater_url}") String updaterQueueUrl,
            ObjectMapper objectMapper,
            @Value("${pathmind.aws.mock_cycle:0}") int mockCycle) {

        this.s3Client = s3.getS3Client();
        this.bucketName = bucketName;

        this.sqsClient = sqs.getSqsClient();
        this.queueUrl = queueUrl;
        this.updaterQueueUrl = updaterQueueUrl;

        this.objectMapper = objectMapper;

        this.mockCycle = mockCycle;
        if (isUsingMockBackend()) {
            Assert.isTrue(mockCycle > 0, "Mock Cycle should be greater than zero");
            log.warn("Running with mock cycle {}", mockCycle);
        }

    }

    public boolean isUsingMockBackend() {
        return this.mockCycle != 0;
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
        String targetBucketName = bucketName;
        String originalPath = s3Client.getObjectMetadata(bucketName, keyId).getUserMetaDataOf("path");
        if (originalPath != null) {
            // an example of originalPath : test-training-static-files.pathmind.com/mockup/2/trial_list
            String[] split = originalPath.split("/", 2);
            targetBucketName = split[0];
            keyId = split[1];
        }

        S3Object o = s3Client.getObject(targetBucketName, keyId);
        try {
            return IOUtils.toByteArray(o.getObjectContent());
        } catch (IOException e) {
            log.error("Failed to get content from {}/{}", targetBucketName, keyId, e);
            throw new PathMindException("Failed to get model content");
        }
    }

    public boolean fileExists(String keyId) {
        return s3Client.doesObjectExist(bucketName, keyId);
    }

    public void fileDelete(String bucketName, String keyId) {
        s3Client.deleteObject(bucketName, keyId);
    }

    public String jobSubmit(String jobId, RunType type, EC2InstanceType ec2InstanceType) throws JsonProcessingException {
        final String mockType = type == null ? null : type.toString();
        Job job = new Job(bucketName, jobId, mockCycle, mockType);
        job.setEc2InstanceType(ec2InstanceType);

        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageGroupId("training")
                .withMessageBody(objectMapper.writeValueAsString(job));

        int retryCount = 3;

        SendMessageResult sendMessageResult = sqsClient.sendMessage(send_msg_request);
        while(!isValidResponse(sendMessageResult) && retryCount-- > 0) {
            log.info("retry submit job for " + jobId);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {}
            sendMessageResult = sqsClient.sendMessage(send_msg_request);
        }

        if (!isValidResponse(sendMessageResult)) {
            log.info("failure on submitting the job for " + jobId + ", " + sendMessageResult);
            throw new RuntimeException("failure on submitting the job for " + jobId);
        }

        return jobId;
    }

    private boolean isValidResponse(SendMessageResult sendMessageResult) {
        return sendMessageResult.getMessageId() != null && !sendMessageResult.getMessageId().isEmpty() && sendMessageResult.getSdkHttpMetadata().getHttpStatusCode() == 200;
    }


    public String jobStop(String jobId) throws JsonProcessingException {
        Job job = new Job(bucketName, jobId, true, mockCycle);

        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageGroupId("training")
                .withMessageBody(objectMapper.writeValueAsString(job));

        SendMessageResult result = sqsClient.sendMessage(send_msg_request);
        return result.getMessageId();
    }

}
