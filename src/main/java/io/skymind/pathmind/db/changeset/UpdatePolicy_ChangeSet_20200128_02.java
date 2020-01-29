package io.skymind.pathmind.db.changeset;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.skymind.pathmind.utils.ObjectMapperHolder;
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
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This is to separate out the RewardScore from the Policy Json String progress into it's own RewardScore table.
 *
 * IMPORTANT -> This changeset is complete self contained so that any refactoring in the code base does NOT affect this code
 * change. This INCLUDES constants, etc.
 * NOTE -> I'm using JDBC because it's not possible to use JOOQ because of the order in which the changeset and JOOQ files are generated.
 * The goal here is to extract 3 values from the JSON progress string into their database columns for performance improvements.
 * IMPORTANT TIP -> If you modify this code please note that you need to run the Maven compile target before you run the liquibase targets
 * otherwise the CODE CHANGES WILL NOT BE REFLECTED IN THE LIQUIBASE TARGETS in any automatic way!!
 */
@Slf4j
public class UpdatePolicy_ChangeSet_20200128_02 implements CustomSqlChange, CustomSqlRollback
{
    @Override
    public SqlStatement[] generateStatements(Database database) throws CustomChangeException
    {
        // IMPORTANT -> Do NOT close the connection as it's used by liquibase for the rest of the changesets.
        Connection connection = ((JdbcConnection) database.getConnection()).getUnderlyingConnection();
        List<Changeset20200110Policy> policies = getPoliciesFromDatabase(connection);
        ObjectMapper OBJECT_MAPPER = ObjectMapperHolder.getJsonMapper();
        policies.parallelStream()
                .filter(policy -> StringUtils.isNotEmpty(policy.getProgress()))
                .map(policy -> {
                        try {
                            Changeset20200110Policy jsonPolicy = OBJECT_MAPPER.readValue(policy.getProgress(), Changeset20200110Policy.class);
                            String values = jsonPolicy.getRewardProgression().stream()
                                    .map(rewardScore ->
                                            policy.getId() + ", " +
                                                    getDoubleValue(rewardScore.getMin()) + ", " +
                                                    getDoubleValue(rewardScore.getMean()) + ", " +
                                                    getDoubleValue(rewardScore.getMax()) + ", " +
                                                    rewardScore.getIteration())
                                    .collect(Collectors.joining(",", "(", ")"));
                            return new RawSqlStatement("INSERT INTO REWARD_SCORE (POLICY_ID, MIN, MEAN, MAX, ITERATION) VALUES " + values);
                        } catch (IOException e) {
                            log.error(e.getMessage(), e);
                            // Throwing a RuntimeException to stop all the database processing because if there is an exception then we have a bigger issue.
                            throw new RuntimeException(e.getMessage(), e);
                        }
                        // we need to collect first because each map is a List<RawSqlStatement> that then needs to be combined through addAll.
                }).toArray(SqlStatement[]::new);
        return new SqlStatement[]{new RawSqlStatement("")};
    }

    private String getDoubleValue(double value) {
        return Double.isNaN(value) ? "NULL" : Double.toString(value);
    }

    // Rollback is not required as we just delete the new RewardScore table in the liquibase xml but for completeness it's added anyways.
    @Override
    public SqlStatement[] generateRollbackStatements(Database database) throws CustomChangeException, RollbackImpossibleException {
        return new SqlStatement[] {};
    }

    private List<Changeset20200110Policy> getPoliciesFromDatabase(Connection connection) throws CustomChangeException {
        List<Changeset20200110Policy> policies = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT ID, PROGRESS FROM POLICY")) {
            ResultSet result = preparedStatement.executeQuery();
            while(result.next()) {
                policies.add(new Changeset20200110Policy(
                            result.getLong("ID"),
                            result.getString("PROGRESS")));
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

// I'm prepending the changeset to make it easier when using IDE searches and class navigations.
@JsonIgnoreProperties(value = {"id", "algorithm", "startedAt", "stoppedAt", "hyperParameters"})
class Changeset20200110Policy {
    private long id;
    private String progress;
    private List<Changeset20200110RewardProgression> rewardProgression;

    // Need the empty constructor for Json parsing.
    public Changeset20200110Policy() {
    }

    public Changeset20200110Policy(long id, String progress) {
        this.id = id;
        this.progress = progress;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public List<Changeset20200110RewardProgression> getRewardProgression() {
        return rewardProgression;
    }

    public void setRewardProgression(List<Changeset20200110RewardProgression> rewardProgression) {
        this.rewardProgression = rewardProgression;
    }
}

class Changeset20200110RewardProgression {
    private Double max;
    private Double min;
    private Double mean;
    private Integer iteration;

    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }

    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    public Double getMean() {
        return mean;
    }

    public void setMean(Double mean) {
        this.mean = mean;
    }

    public Integer getIteration() {
        return iteration;
    }

    public void setIteration(Integer iteration) {
        this.iteration = iteration;
    }
}

