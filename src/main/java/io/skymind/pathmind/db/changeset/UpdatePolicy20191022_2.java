package io.skymind.pathmind.db.changeset;

import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.utils.PolicyUtils;
import liquibase.change.custom.CustomTaskChange;
import liquibase.database.Database;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.CustomChangeException;
import liquibase.exception.SetupException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * IMPORTANT -> I'm using JDBC because it's not possible to use JOOQ because of the order in which the changeset and JOOQ files are generated.
 * The goal here is to extract 3 values from the JSON progress string into their database columns for performance improvements.
 * IMPORTANT -> If you modify this code please note that you need to run the Maven compile target before you run the liquibase targets
 * otherwise the CODE CHANGES WILL NOT BE REFLECTED IN THE LIQUIBASE TARGETS in any automatic way!!
 */
public class UpdatePolicy20191022_2 implements CustomTaskChange
{
    private static Logger log = LogManager.getLogger(UpdatePolicy20191022_2.class);

    @Override
    public void execute(final Database database) throws CustomChangeException
    {
        // IMPORTANT -> Do NOT close the connection as it's used by liquibase for the rest of the changesets.
        Connection connection = ((JdbcConnection) database.getConnection()).getUnderlyingConnection();

        List<Policy> policies = getPoliciesFromDatabase(connection);
        convertJsonValues(policies);
        updatePoliciesInDatabase(connection, policies);
    }

    private void convertJsonValues(List<Policy> policies) {
        policies.parallelStream().forEach(policy -> PolicyUtils.processProgressJson(policy));
    }

    private void updatePoliciesInDatabase(Connection connection, List<Policy> policies)
    {
        // We could do it with a single SQL command (INSERT ... ON DUPLICATE KEY UPDATE ...) but at this point I don't think we
        // have enough data to warrant the effort and so I'm just going to do a batch update.
        try(PreparedStatement preparedStatement = connection.prepareStatement("UPDATE POLICY SET STARTEDAT=?, STOPPEDAT=?, ALGORITHM=? WHERE id=?")) {
            for(Policy policy: policies) {
                preparedStatement.setObject(1, policy.getStartedAt());
                preparedStatement.setObject(2, policy.getStoppedAt());
                preparedStatement.setString(3, policy.getAlgorithm());
                preparedStatement.setLong(4, policy.getId());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    private List<Policy> getPoliciesFromDatabase(Connection connection) throws CustomChangeException {
        ArrayList<Policy> policies = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT ID, PROGRESS FROM POLICY")) {
            ResultSet result = preparedStatement.executeQuery();
            while(result.next()) {
                Policy policy = new Policy();
                policy.setId(result.getInt("ID"));
                policy.setProgress(result.getString("PROGRESS"));
                policies.add(policy);
            }
            return policies;
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new CustomChangeException(e);
        }
    }

    /**
     * I'm using JDBC because it's not possible to use JOOQ because of the order in which the changeset and JOOQ files are generated but I left it in case there
     * is an option that I've missed. As well this code does NOT batch update so that should be resolved as well.
     */
//    private void jooqVersionThatDoesNOTWork(Database database) {
//        DSLContext dslContext = DSL.using(((JdbcConnection) database.getConnection()).getUnderlyingConnection());
//        dslContext.select(POLICY.ID, POLICY.PROGRESS)
//                .from(POLICY)
//                .forEach(record -> {
//                        Policy policy = record.into(POLICY).into(Policy.class);
//                        PolicyUtils.processProgressJson(policy);
//                        dslContext.update(POLICY)
//                                .set(POLICY.STARTEDAT, policy.getStartedAt())
//                                .set(POLICY.STOPPEDAT, policy.getStoppedAt())
//                                .set(POLICY.ALGORITHM, policy.getAlgorithmEnum())
//                                .where(POLICY.ID.eq(policy.getId()))
//                                .execute();
//                });
//    }

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
