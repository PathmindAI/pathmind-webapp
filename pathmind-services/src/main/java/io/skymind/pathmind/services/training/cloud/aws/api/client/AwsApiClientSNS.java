package io.skymind.pathmind.services.training.cloud.aws.api.client;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AwsApiClientSNS {

    @Getter
    private final AmazonSNS snsClient;

    protected AwsApiClientSNS(AwsApiClientBuilder clientBuilder, @Value("${pathmind.aws.mock-sns:true}") boolean mockSNS) {
        this.snsClient = clientBuilder.configure(AmazonSNSClientBuilder.standard(), mockSNS).build();
    }

}
