package io.skymind.pathmind.services.training.cloud.aws.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Job {
    @JsonProperty("S3Bucket")
    private String S3Bucket;
    @JsonProperty("S3Path")
    private String S3Path;
    @JsonProperty("destroy")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer destroy = null;

    @JsonProperty("mockup")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String mockup;

    @JsonProperty("mockup_type")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String mockupType;

    @JsonProperty("cycle")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String mockupCycle;

    public Job(String s3Bucket, String s3Path) {
        this(s3Bucket, s3Path, false);
    }

    public Job(String s3Bucket, String s3Path, boolean destroy) {
        S3Bucket = s3Bucket;
        S3Path = s3Path;
        this.destroy = destroy ? 1 : null;
    }

    public Job(String s3Bucket, String s3Path, int mockCycle, String mockType) {
        this(s3Bucket, s3Path);
        if (mockCycle > 0) {
            mockup = "1";
            this.mockupCycle = String.valueOf(mockCycle);
            this.mockupType = mockType;
        }
    }

}
