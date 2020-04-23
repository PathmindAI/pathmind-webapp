package io.skymind.pathmind.services.training.cloud.aws.api.client;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
class AwsApiClientBuilder {

    private final Regions regions;
    private final AWSCredentials credentials;
    private final AwsClientBuilder.EndpointConfiguration endpointConfiguration;

    public AwsApiClientBuilder(
            @Value("${pathmind.aws.region}") String regionStr,
            @Value("${pathmind.aws.key.id}") String keyId,
            @Value("${pathmind.aws.secret_key}") String secretAccessKey,
            @Value("${pathmind.aws.mock-endpoint-url:}") String awsEndpointUrl) {
        regions = Regions.fromName(regionStr);
        credentials = new BasicAWSCredentials(keyId, secretAccessKey);

        if (!StringUtils.isEmpty(awsEndpointUrl)) {
            endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(awsEndpointUrl, regionStr);
        } else {
            endpointConfiguration = null;
        }
    }

    public boolean mocking() {
        return endpointConfiguration != null;
    }

    protected  <S extends AwsClientBuilder<S, T>, T> S configure(S builder, boolean mockService) {
        builder.withCredentials(new AWSStaticCredentialsProvider(credentials));
        if (mocking() && mockService) {
            builder.withEndpointConfiguration(endpointConfiguration);
        } else {
            builder.withRegion(regions);
        }
        return builder;
    }

}
