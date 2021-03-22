package io.skymind.pathmind.bddtests;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.SystemEnvironmentVariables;

public class AmazonUtils {

    private static EnvironmentVariables variables = SystemEnvironmentVariables.createEnvironmentVariables();
    private static String SQS_POLICY_SERVER_URL = variables.getProperty("SQS_POLICY_SERVER_URL");
    private static String AWS_ACCESS_KEY_ID = variables.getProperty("AWS_ACCESS_KEY_ID");
    private static String AWS_SECRET_ACCESS_KEY = variables.getProperty("AWS_SECRET_ACCESS_KEY");

    private static void sendSqsMessage(String body) {
        body = "{\"S3Bucket\": \"dev-training-dynamic-files.pathmind.com\",\"S3ModelPath\":\"id6911/saved_model.zip\",\"S3SchemaPath\":\"id6911/schema.yaml\",\"JobId\":\"id6911\",\"id\":\"2\"}";

        AWSCredentials credentials = new AWSCredentials() {
            @Override
            public String getAWSAccessKeyId() {
                return AWS_ACCESS_KEY_ID;
            }

            @Override
            public String getAWSSecretKey() {
                return AWS_SECRET_ACCESS_KEY;
            }
        };

        AmazonSQS sqs = AmazonSQSClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withRegion(Regions.US_EAST_1)
            .build();
        SendMessageRequest sendMessageFifoQueue = new SendMessageRequest()
            .withQueueUrl(SQS_POLICY_SERVER_URL)
            .withMessageBody(body)
            .withMessageGroupId("example-1");
        sqs.sendMessage(sendMessageFifoQueue);
    }

    public static void main(String[] args) {
        sendSqsMessage("");
    }
}
