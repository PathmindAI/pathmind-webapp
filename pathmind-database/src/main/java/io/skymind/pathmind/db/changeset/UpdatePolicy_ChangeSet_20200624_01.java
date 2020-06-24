package io.skymind.pathmind.db.changeset;

import liquibase.change.custom.CustomTaskChange;
import liquibase.database.Database;
import liquibase.exception.CustomChangeException;
import liquibase.exception.SetupException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;
import lombok.extern.slf4j.Slf4j;

/**
 * IMPORTANT -> This changeset is to be able to automatically reset the S3 bucket for the developer
 */
@Slf4j
public class UpdatePolicy_ChangeSet_20200624_01 implements CustomTaskChange
{
    @Override
    public void execute(Database database) throws CustomChangeException {
        // @DH -> I'm not sure how it's all wired but would it be possible to call the new method I created called AWSApiClient.emptyBucket() from here?
        throw new CustomChangeException("Stop if production");
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
