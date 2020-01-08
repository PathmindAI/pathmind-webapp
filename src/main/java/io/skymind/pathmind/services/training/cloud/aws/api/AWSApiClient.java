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
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class AWSApiClient {
    private final AWSCredentials credentials;
    private final AmazonS3 s3client;

    public AWSApiClient(
            @Value("${pathmind.aws.key.id}") String keyId,
            @Value("${pathmind.aws.secret_key}") String secretAccessKey) {

        credentials = new BasicAWSCredentials(
                keyId,
                secretAccessKey
        );

        s3client = createConnectionWithCredentials(credentials);
    }

    public List<Bucket> listBuckets() {
        return s3client.listBuckets();
    }

    public ListObjectsV2Result listObjects(String bucketName) {
        return s3client.listObjectsV2(bucketName);
    }

    public void fileUpload(String bucketName, String keyId, File file) {
        s3client.putObject(bucketName, keyId, file);
    }

    public byte[] fileContents(String bucketName, String keyId) {
        S3Object o = s3client.getObject(bucketName, keyId);
        try {
            return IOUtils.toByteArray(o.getObjectContent());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void fileDelete(String bucketName, String keyId) {
        s3client.deleteObject(bucketName, keyId);
    }


    private static AmazonS3 createConnectionWithCredentials(AWSCredentials credentials) {
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.US_EAST_1)
                .build();
    }

}
