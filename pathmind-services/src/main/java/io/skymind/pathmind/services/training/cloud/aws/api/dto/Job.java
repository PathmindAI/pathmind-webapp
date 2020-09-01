package io.skymind.pathmind.services.training.cloud.aws.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.skymind.pathmind.shared.constants.EC2InstanceType;
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

    @JsonProperty("cycle")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String mockupCycle;

    @JsonProperty("hw_type")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private EC2InstanceType ec2InstanceType;

    public Job(String s3Bucket, String s3Path, int mockCycle) {
        this(s3Bucket, s3Path, false, mockCycle);
    }

    public Job(String s3Bucket, String s3Path, boolean destroy, int mockCycle) {
        this.S3Bucket = s3Bucket;
        this.S3Path = s3Path;
        this.destroy = destroy ? 1 : null;

        if (mockCycle > 0) {
            this.mockup = "1";
            this.mockupCycle = String.valueOf(mockCycle);
        }
    }

    public void setEc2InstanceType(EC2InstanceType instanceType) {
        this.ec2InstanceType = instanceType;
    }

}
