package io.skymind.pathmind.db.changeset;

import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.utils.PolicyUtils;
import liquibase.change.custom.CustomSqlChange;
import liquibase.change.custom.CustomSqlRollback;
import liquibase.database.Database;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.CustomChangeException;
import liquibase.exception.RollbackImpossibleException;
import liquibase.exception.SetupException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;
import liquibase.statement.SqlStatement;
import liquibase.statement.core.RawSqlStatement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * INFO -> I decided to use CustomTaskChange rather than CustomSQLChange because the only option I could see
 * to run SQL statements was with RaqSQLStatement which should be ok for SQL injection but didn't really give us
 * much of a benefit. And the Rollback feature was not really useful.
 * NOTE -> I'm using JDBC because it's not possible to use JOOQ because of the order in which the changeset and JOOQ files are generated.
 * The goal here is to extract 3 values from the JSON progress string into their database columns for performance improvements.
 * IMPORTANT TIP -> If you modify this code please note that you need to run the Maven compile target before you run the liquibase targets
 * otherwise the CODE CHANGES WILL NOT BE REFLECTED IN THE LIQUIBASE TARGETS in any automatic way!!
 */
public class UpdatePolicy_ChangeSet_20191022_2 implements CustomSqlChange, CustomSqlRollback
{
    private static Logger log = LogManager.getLogger(UpdatePolicy_ChangeSet_20191022_2.class);

    @Override
    public SqlStatement[] generateStatements(Database database) throws CustomChangeException
    {
        // IMPORTANT -> Do NOT close the connection as it's used by liquibase for the rest of the changesets.
        Connection connection = ((JdbcConnection) database.getConnection()).getUnderlyingConnection();
        List<Policy> policies = getPoliciesFromDatabase(connection);
        convertJsonValues(policies);
        return updatePoliciesInDatabase(policies);
    }

    private void convertJsonValues(List<Policy> policies) {
        policies.parallelStream().forEach(policy -> PolicyUtils.processProgressJson(policy, policy.getProgress()));
    }

    @Override
    public SqlStatement[] generateRollbackStatements(Database database) throws CustomChangeException, RollbackImpossibleException {
        // IMPORTANT -> Do NOT close the connection as it's used by liquibase for the rest of the changesets.
        Connection connection = ((JdbcConnection) database.getConnection()).getUnderlyingConnection();
        List<Policy> policies = getPoliciesFromDatabase(connection);
        convertJsonValues(policies);
        return rollbackPoliciesInDatabase(policies);
    }

    private SqlStatement[] rollbackPoliciesInDatabase(List<Policy> policies)
    {
        return policies.stream().map(policy ->
                new RawSqlStatement(
                        "UPDATE POLICY SET " +
                                "STARTEDAT=null, " +
                                "STOPPEDAT=null, " +
                                "ALGORITHM=null, " +
                        "WHERE " +
                                "id=" + policy.getId())
        ).toArray(SqlStatement[]::new);
    }

    private SqlStatement[] updatePoliciesInDatabase(List<Policy> policies)
    {
        return policies.stream().map(policy ->
            new RawSqlStatement(
                    "UPDATE POLICY SET " +
                            "STARTEDAT=" + getLocalDateTime(policy.getStartedAt()) + ", " +
                            "STOPPEDAT=" + getLocalDateTime(policy.getStoppedAt()) + ", " +
                            "ALGORITHM='" + policy.getAlgorithm() + "' " +
                    "WHERE " +
                            "id=" + policy.getId())
        ).toArray(SqlStatement[]::new);
    }

    private String getLocalDateTime(LocalDateTime localDateTime) {
        if(localDateTime == null)
            return "null";
        return "'" + localDateTime.withNano(0) + "'";
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
