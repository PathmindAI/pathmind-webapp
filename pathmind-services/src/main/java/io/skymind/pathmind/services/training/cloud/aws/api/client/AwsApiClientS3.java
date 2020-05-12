package io.skymind.pathmind.services.training.cloud.aws.api.client;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AwsApiClientS3 {

    @Getter
    private final AmazonS3 s3Client;

    protected AwsApiClientS3(AwsApiClientBuilder clientBuilder, @Value("${pathmind.aws.mock-s3:true}") boolean mockS3) {

        AmazonS3ClientBuilder s3ClientBuilder = clientBuilder.configure(AmazonS3ClientBuilder.standard(), mockS3);
        if (clientBuilder.mocking() && mockS3) {
            s3ClientBuilder.withPathStyleAccessEnabled(true);
        }
        this.s3Client = s3ClientBuilder.build();
    }

}
