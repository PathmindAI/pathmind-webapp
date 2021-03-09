package io.skymind.pathmind.services.training.cloud.aws.api.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class DeploymentMessage {

    @JsonProperty("S3Bucket")
    private String s3Bucket;

    @JsonProperty("S3ModelPath")
    private String s3ModelPath;

    @JsonProperty("S3SchemaPath")
    private String s3SchemaPath;

    @JsonProperty("JobId")
    private String jobId;

}
