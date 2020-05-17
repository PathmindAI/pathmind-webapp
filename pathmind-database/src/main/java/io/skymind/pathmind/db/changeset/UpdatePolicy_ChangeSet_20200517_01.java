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
public class UpdatePolicy_ChangeSet_20200517_01 implements CustomTaskChange
{
    @Override
    public void execute(Database database) throws CustomChangeException {
        // DH -> I would recommend using the environment variable to automatically select the bucket. That being said
        // I also recommend adding code so that if the environment variable is production we throw an exception
        // and everything is stopped immediately as a safety measure.
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
