package io.skymind.pathmind.services.training.cloud.rescale.api;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import io.skymind.pathmind.services.training.cloud.aws.api.AWSApiClient;

import java.io.File;
import java.util.List;

public class AWSApiClientTest {
    private static final String KEY_ID = "AKIA4G2DQUWTMERGXAOY";
    private static final String SECRET_ACCESS_KEY = "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";
//
//    private static final AWSCredentials credentials;
//    private static AmazonS3 s3client;
//
//    static {
//        credentials = new BasicAWSCredentials(
//                KEY_ID,
//                SECRET_ACCESS_KEY
//        );
//    }
//
//    public static void main(String[] args) {
//
//        s3client = createConnectionWithCredentials(credentials);
//
////        List<Bucket> buckets = s3client.listBuckets();
////        System.out.println("Your Amazon S3 buckets are:");
////        for (Bucket b : buckets) {
////            System.out.println("b* " + b.getName());
////        }
//
//        ListObjectsV2Result result = s3client.listObjectsV2("test-training-dynamic-files.pathmind.com");
//        List<S3ObjectSummary> objects = result.getObjectSummaries();
//        for (S3ObjectSummary os : objects) {
//            System.out.println("o* " + os.getKey());
//        }
//    }
//
//    private static AmazonS3 createConnectionWithCredentials(AWSCredentials credentials) {
//        return AmazonS3ClientBuilder
//                .standard()
//                .withCredentials(new AWSStaticCredentialsProvider(credentials)) //#1 - 직접 값을 전달함
//                .withRegion(Regions.US_EAST_1)
//                .build();
//
////        return AmazonS3ClientBuilder.standard().withRegion(Regions.DEFAULT_REGION).build();
//    }

    public static void main(String[] args) {
        AWSApiClient client = new AWSApiClient(KEY_ID, SECRET_ACCESS_KEY);

        String bucketName = "test-training-dynamic-files.pathmind.com";
        File model = new File("/home/kepricon/Desktop/model.zip");

//        client.listObjects(bucketName).getObjectSummaries().stream()
//                .forEach(os -> System.out.println("o* " + os.getKey()));

//        client.fileUpload(bucketName, "id2/model.zip", model);
//
//        client.listObjects(bucketName).getObjectSummaries().stream()
//                .forEach(os -> System.out.println("o* " + os.getKey()));

//        byte[] contents = client.fileContents(bucketName, "id2/model.zip");
        byte[] contents = client.fileContents(bucketName, "id1/script.sh");
        System.out.println(new String(contents));

//        client.fileDelete(bucketName, "id2/model.zip");
//
//        client.listObjects(bucketName).getObjectSummaries().stream()
//                .forEach(os -> System.out.println("o* " + os.getKey()));
    }

}
