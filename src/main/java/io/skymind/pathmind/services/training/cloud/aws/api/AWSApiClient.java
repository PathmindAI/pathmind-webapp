package io.skymind.pathmind.services.training.cloud.aws.api;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.amazonaws.util.IOUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.skymind.pathmind.services.training.cloud.aws.api.dto.Job;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class AWSApiClient {
    private final AWSCredentials credentials;
    private final AmazonS3 s3Client;
    private final AmazonSQS sqsClient;
    private final ObjectMapper objectMapper;

    private final String bucketName;
    private final String queueUrl;

    public AWSApiClient(
            @Value("${pathmind.aws.key.id}") String keyId,
            @Value("${pathmind.aws.secret_key}") String secretAccessKey,
            @Value("${pathmind.aws.s3.bucket}") String bucketName,
            @Value("${pathmind.aws.sqs_url}") String queueUrl,
            ObjectMapper objectMapper) {

        this.credentials = new BasicAWSCredentials(
                keyId,
                secretAccessKey
        );

        this.s3Client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.US_EAST_1)
                .build();

        this.sqsClient = AmazonSQSClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.US_EAST_1)
                .build();

        this.bucketName = bucketName;
        this.queueUrl = queueUrl;

        this.objectMapper = objectMapper;
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

    public byte[] fileContents(String keyId) {
        S3Object o = s3Client.getObject(bucketName, keyId);
        try {
            return IOUtils.toByteArray(o.getObjectContent());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void fileDelete(String bucketName, String keyId) {
        s3Client.deleteObject(bucketName, keyId);
    }

    public String jobSubmit(String jobId) throws JsonProcessingException {
        Job job = new Job(bucketName, jobId);

        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageGroupId("training")
                .withMessageBody(objectMapper.writeValueAsString(job));

        SendMessageResult result = sqsClient.sendMessage(send_msg_request);
        return result.getMessageId();
    }
}
