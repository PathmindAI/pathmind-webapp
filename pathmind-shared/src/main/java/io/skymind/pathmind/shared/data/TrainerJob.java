package io.skymind.pathmind.shared.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class TrainerJob extends Data {
    private static final long serialVersionUID = -7545940987287812131L;
    private String jobId;
    private String sqsUrl;
    private String s3Path;
    private String s3Bucket;
    private String receiptHandle;
    private String ec2InstanceType;
    private String ec2MaxPrice;
    private LocalDateTime createDate;
    private LocalDateTime ec2CreateDate;
    private LocalDateTime ec2EndDate;
    private int status;
    private String description;
}
