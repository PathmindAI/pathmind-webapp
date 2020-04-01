package io.skymind.pathmind.db.changeset;

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
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * This has to be done this way because of a bug in liquibase: https://stackoverflow.com/questions/59525568/liquibase-sql-command-giving-different-result-than-running-it-directly-in-sql
 *
 * IMPORTANT -> This changeset is complete self contained so that any refactoring in the code base does NOT affect this code
 * change. This INCLUDES constants, etc.
 * NOTE -> I'm using JDBC because it's not possible to use JOOQ because of the order in which the changeset and JOOQ files are generated.
 * The goal here is to extract 3 values from the JSON progress string into their database columns for performance improvements.
 * IMPORTANT TIP -> If you modify this code please note that you need to run the Maven compile target before you run the liquibase targets
 * otherwise the CODE CHANGES WILL NOT BE REFLECTED IN THE LIQUIBASE TARGETS in any automatic way!!
 */
@Slf4j
public class UpdatePolicy_ChangeSet_20200128_05 implements CustomSqlChange, CustomSqlRollback
{
    @Override
    public SqlStatement[] generateStatements(Database database) throws CustomChangeException
    {
        // IMPORTANT -> Do NOT close the connection as it's used by liquibase for the rest of the changesets.
        Connection connection = ((JdbcConnection) database.getConnection()).getUnderlyingConnection();
        List<Changeset20191229Policy> policies = getPoliciesFromDatabase(connection);
        // I'm NOT using parallel streams since it's only done once and it's much easier to track any issues if it's done in order.
        return policies.stream().map(policy -> {
            return new RawSqlStatement(
                    "UPDATE POLICY SET NOTES='sgd_minibatch_size=" + policy.batchSize + ", lr=" + policy.learningRate + ", gamma=" + policy.gamma + "' WHERE ID=" + policy.id);
        }).toArray(SqlStatement[]::new);
    }

    // Rollback is not required as we just delete the new notes columns but for completeness it's added anyways.
    @Override
    public SqlStatement[] generateRollbackStatements(Database database) throws CustomChangeException, RollbackImpossibleException {
        return new SqlStatement[] {
                new RawSqlStatement("UPDATE POLICY SET NOTES=NULL")
        };
    }

    private List<Changeset20191229Policy> getPoliciesFromDatabase(Connection connection) throws CustomChangeException {
        List<Changeset20191229Policy> policies = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT ID, LEARNING_RATE, GAMMA, BATCH_SIZE FROM POLICY")) {
            ResultSet result = preparedStatement.executeQuery();
            while(result.next()) {
                policies.add(new Changeset20191229Policy(
                            result.getLong("ID"),
                            result.getDouble("LEARNING_RATE"),
                            result.getDouble("GAMMA"),
                            result.getInt("BATCH_SIZE")));
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

    // I'm prepending the changeset to make it easier when using IDE searches and class navigations.
    class Changeset20191229Policy {
        public Changeset20191229Policy(long id, double learningRate, double gamma, int batchSize) {
            this.id = id;
            this.learningRate = learningRate;
            this.gamma = gamma;
            this.batchSize = batchSize;
        }

        private long id;
        private double learningRate;
        private double gamma;
        private int batchSize;
    }
}
