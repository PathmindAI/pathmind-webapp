package io.skymind.pathmind.services.training.cloud.aws.api.client;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AwsApiClientSQS {

    @Getter
    private final AmazonSQS sqsClient;

    protected AwsApiClientSQS(AwsApiClientBuilder clientBuilder, @Value("${pathmind.aws.mock-sqs:true}") boolean mockSQS) {
        this.sqsClient = clientBuilder.configure(AmazonSQSClientBuilder.standard(), mockSQS).build();
    }

}
