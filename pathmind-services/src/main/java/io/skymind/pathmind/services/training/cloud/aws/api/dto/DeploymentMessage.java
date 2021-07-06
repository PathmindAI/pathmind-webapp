package io.skymind.pathmind.services.training.cloud.aws.api.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class DeploymentMessage {

    @Setter
    @JsonProperty("S3Bucket")
    private String s3Bucket;

    @JsonProperty("S3ModelPath")
    private String s3ModelPath;

    @JsonProperty("S3SchemaPath")
    private String s3SchemaPath;

    @JsonProperty("JobId")
    private String jobId;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("destroy")
    private String destroy;

}
