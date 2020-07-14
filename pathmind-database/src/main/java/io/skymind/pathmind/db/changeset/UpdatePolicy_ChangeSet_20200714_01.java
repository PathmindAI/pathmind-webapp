package io.skymind.pathmind.db.changeset;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import liquibase.change.custom.CustomTaskChange;
import liquibase.database.Database;
import liquibase.exception.CustomChangeException;
import liquibase.exception.SetupException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This changeset is to be able to automatically reset the S3 bucket for the developers
 */
@Slf4j
public class UpdatePolicy_ChangeSet_20200714_01 implements CustomTaskChange
{
    private static final String RESET_S3_BUCKET_ON_DATABASE_RESET = "RESET_S3_BUCKET_ON_DATABASE_RESET";

    private static final List<String> DO_NOT_EMPTY_BUCKETS = List.of("dev", "prod", "test");

    @Override
    public void execute(Database database) throws CustomChangeException {

        // Check that RESET_S3_BUCKET_ON_DATABASE_RESET is enabled otherwise skip this whole changeset
        if(!isResetS3EnabledEnvironmentVariable()) {
            log.info("S3 bucket has NOT been reset.");
            return;
        }

        // Liquibase will stop if the environment variable is missing. This is so that developers get notified to update their environment variables
        // to be able to properly delete their S3 buckets as part of a database reset.
        String region = getEnvironmentVariable("AWS_DEFAULT_REGION");
        String accessKey = getEnvironmentVariable("AWS_ACCESS_KEY_ID");
        String secretAccessKey = getEnvironmentVariable("AWS_SECRET_ACCESS_KEY");
        String bucket = getEnvironmentVariable("S3_BUCKET");

        log.info("Emptying S3 bucket : " + bucket);

        // Extra security precaution. I'm not throwing an exception because in those cases we just want to stop.
        if(DO_NOT_EMPTY_BUCKETS.stream().anyMatch(specialBuckets -> bucket.startsWith(specialBuckets))) {
            log.info("Bucket " + bucket + " will NOT be emptied");
            return;
        }

        AmazonS3 s3Client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretAccessKey)))
                .withRegion(region)
                .build();

        // Amazon S3 limits deletions to 1000 objects per call.
        ObjectListing objectListing = s3Client.listObjects(bucket);
        deleteObjects(objectListing, s3Client, bucket);
        while(objectListing.isTruncated()) {
            objectListing = s3Client.listNextBatchOfObjects(objectListing);
            deleteObjects(objectListing, s3Client, bucket);
        }
    }

    private boolean isResetS3EnabledEnvironmentVariable() throws CustomChangeException {
        return Boolean.parseBoolean(Optional.ofNullable(
                System.getenv(RESET_S3_BUCKET_ON_DATABASE_RESET)).orElse(Boolean.FALSE.toString()));
    }

    private String getEnvironmentVariable(String name) throws CustomChangeException {
        return Optional.ofNullable(System.getenv(name)).orElseThrow(
                () -> new CustomChangeException(name + " environment variable is not set"));
    }

    private void deleteObjects(ObjectListing objectListing, AmazonS3 s3Client, String bucket) {
        String[] objectKeys = objectListing.getObjectSummaries().stream()
                .map(object -> object.getKey())
                .toArray(String[]::new);

        // In case the S3 bucket is already empty otherwise it will throw an error when trying to delete the objects (malformed xml).
        if(ArrayUtils.isEmpty(objectKeys))
            return;

        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucket)
                .withKeys(objectKeys);

        s3Client.deleteObjects(deleteObjectsRequest);
    }

    @Override
    public String getConfirmationMessage() {
        return null;
    }

    @Override
    public void setUp() throws SetupException {

    }

    @Override
    public void setFileOpener(ResourceAccessor resourceAccessor) {

    }

    @Override
    public ValidationErrors validate(Database database) {
        return null;
    }
}
