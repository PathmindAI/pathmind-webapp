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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This has to be done this way because of how the values are parsed from the name in the policy table.
 *
 * IMPORTANT -> This changeset is complete self contained so that any refactoring in the code base does NOT affect this code
 * change. This INCLUDES constants, etc.
 * NOTE -> I'm using JDBC because it's not possible to use JOOQ because of the order in which the changeset and JOOQ files are generated.
 * The goal here is to extract 3 values from the JSON progress string into their database columns for performance improvements.
 * IMPORTANT TIP -> If you modify this code please note that you need to run the Maven compile target before you run the liquibase targets
 * otherwise the CODE CHANGES WILL NOT BE REFLECTED IN THE LIQUIBASE TARGETS in any automatic way!!
 */
@Slf4j
public class UpdatePolicy_ChangeSet_20200128_04 implements CustomSqlChange, CustomSqlRollback
{
    // IMPORTANT -> These are copied from their various sources in case they change over time so that the database changeset is NOT affected.
    private static final int TRIAL_ID_LEN = 8;
    private static final int DATE_LEN = 19;
    private static final String TEMPORARY_POSTFIX = "TEMP";
    private static final String LEARNING_RATE = "lr";
    private static final String GAMMA = "gamma";
    private static final String BATCH_SIZE = "sgd_minibatch_size";

    @Override
    public SqlStatement[] generateStatements(Database database) throws CustomChangeException
    {
        // IMPORTANT -> Do NOT close the connection as it's used by liquibase for the rest of the changesets.
        Connection connection = ((JdbcConnection) database.getConnection()).getUnderlyingConnection();
        Map<Long, String> policies = getPoliciesFromDatabase(connection);
        return policies.keySet().parallelStream().map(policyId -> {
            ChangeSetHyperParameter hyperParameter = interpretKey(policies.get(policyId));
            return new RawSqlStatement(
                    "UPDATE POLICY SET " +
                            "LEARNING_RATE=" + hyperParameter.learningRate + ", " +
                            "GAMMA=" + hyperParameter.gamma + ", " +
                            "BATCH_SIZE=" + hyperParameter.batchSize + " " +
                            "WHERE " +
                            "id=" + policyId);
        }).toArray(SqlStatement[]::new);
    }

    // Rollback is not required as we would just delete the new hyperparameter columns but for completeness it's added anyways.
    @Override
    public SqlStatement[] generateRollbackStatements(Database database) throws CustomChangeException, RollbackImpossibleException {
        return new SqlStatement[] {
                new RawSqlStatement(
                        "UPDATE POLICY SET " +
                                "LEARNING_RATE=NULL, " +
                                "GAMMA==NULL, " +
                                "BATCH_SIZE==NULL")
        };
    }

    private Map<Long, String> getPoliciesFromDatabase(Connection connection) throws CustomChangeException {
        HashMap<Long, String> policies = new HashMap<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT ID, EXTERNAL_ID FROM POLICY")) {
            ResultSet result = preparedStatement.executeQuery();
            while(result.next())
                policies.put(result.getLong("ID"), result.getString("EXTERNAL_ID"));
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

    // Stripped down version of ProgressInterpreter.interpretKey for what we need. Also this way should we change the algorithm
    // this changeset will NOT be affected by it.
    private ChangeSetHyperParameter interpretKey(String keyString)
    {
        ChangeSetHyperParameter changeSetHyperParameter = new ChangeSetHyperParameter();

        int keyLength = keyString.length();

        if (keyString.endsWith(TEMPORARY_POSTFIX)) {
            // looks something like this:
            // PPO_PathmindEnvironment_0_gamma=0.99,lr=1.0E-5,sgd_minibatch_size=128_1TEMP
            keyString = keyString.substring(0, keyLength - TEMPORARY_POSTFIX.length() - 2);
        } else {
            // looks something like this:
            // PPO_CoffeeEnvironment_0_gamma=0.99,lr=5e-05,sgd_minibatch_size=128_2019-08-05_13-56-455cdir_3f
            keyString = keyString.substring(0, keyLength - TRIAL_ID_LEN - DATE_LEN - 1);
        }

        // keyString now looks like :
        // PPO_CoffeeEnvironment_0_gamma=0.99,lr=5e-05,sgd_minibatch_size=128
        List<String> list = Arrays.asList(keyString.split("_", 4));

        Arrays.stream(list.get(3).split(",")).forEach(it -> {
            final String[] split = it.split("=");
            setHyperParameter(changeSetHyperParameter, split[0], split[1]);
        });

        return changeSetHyperParameter;
    }

    private void setHyperParameter(ChangeSetHyperParameter changeSetHyperParameter, String name, String value) {
        switch (name) {
            case LEARNING_RATE:
                changeSetHyperParameter.learningRate = Double.valueOf(value);
                break;
            case GAMMA:
                changeSetHyperParameter.gamma = Double.valueOf(value);
                break;
            case BATCH_SIZE:
                changeSetHyperParameter.batchSize = Integer.valueOf(value);
                break;
        }
    }

    class ChangeSetHyperParameter {
        private double learningRate;
        private double gamma;
        private int batchSize;
    }
}
