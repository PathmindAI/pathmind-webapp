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
@Slf4j
public class UpdatePolicy_ChangeSet_20191022_2 implements CustomSqlChange, CustomSqlRollback
{
    @Override
    public SqlStatement[] generateStatements(Database database) throws CustomChangeException
    {
        // IMPORTANT -> Do NOT close the connection as it's used by liquibase for the rest of the changesets.
        Connection connection = ((JdbcConnection) database.getConnection()).getUnderlyingConnection();
        List<Changeset201910222_2_Policy> policies = getPoliciesFromDatabase(connection);
        convertJsonValues(policies);
        return updatePoliciesInDatabase(policies);
    }

    private void convertJsonValues(List<Changeset201910222_2_Policy> policies) {
        ObjectMapper objectMapper = ObjectMapperHolder.getJsonMapper();
        policies.parallelStream().forEach(policy -> processProgressJson(objectMapper, policy, policy.getProgress()));
    }

    /**
     * Copied this code from PolicyUtils.processProgressJson so that it can be changed independently without worrying about breaking
     * the database changelog.
     */
    public static void processProgressJson(ObjectMapper objectMapper, Changeset201910222_2_Policy policy, String progressString)
    {
        if(StringUtils.isEmpty(progressString))
            return;

        try {
            final Changeset201910222_2_Policy jsonPolicy = objectMapper.readValue(progressString, Changeset201910222_2_Policy.class);
            policy.setStartedAt(jsonPolicy.getStartedAt());
            policy.setStoppedAt(jsonPolicy.getStoppedAt());
            policy.setAlgorithm(jsonPolicy.getAlgorithm());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public SqlStatement[] generateRollbackStatements(Database database) throws CustomChangeException, RollbackImpossibleException {
        // IMPORTANT -> Do NOT close the connection as it's used by liquibase for the rest of the changesets.
        Connection connection = ((JdbcConnection) database.getConnection()).getUnderlyingConnection();
        List<Changeset201910222_2_Policy> policies = getPoliciesFromDatabase(connection);
        convertJsonValues(policies);
        return rollbackPoliciesInDatabase(policies);
    }

    private SqlStatement[] rollbackPoliciesInDatabase(List<Changeset201910222_2_Policy> policies)
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

    private SqlStatement[] updatePoliciesInDatabase(List<Changeset201910222_2_Policy> policies)
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

    private List<Changeset201910222_2_Policy> getPoliciesFromDatabase(Connection connection) throws CustomChangeException {
        ArrayList<Changeset201910222_2_Policy> policies = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT ID, PROGRESS FROM POLICY")) {
            ResultSet result = preparedStatement.executeQuery();
            while(result.next()) {
                Changeset201910222_2_Policy policy = new Changeset201910222_2_Policy();
                policy.setId(result.getLong("ID"));
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

// This has to be outside of the class due to the Json parsing.
@JsonIgnoreProperties(value = {"id", "rewardProgression", "hyperParameters"})
class Changeset201910222_2_Policy {
    private long id;
    private String progress;
    private LocalDateTime startedAt;
    private LocalDateTime stoppedAt;
    private String algorithm;

    public Changeset201910222_2_Policy() {
    }

    public Changeset201910222_2_Policy(String progress, LocalDateTime startedAt, LocalDateTime stoppedAt, String algorithm) {
        this.progress = progress;
        this.startedAt = startedAt;
        this.stoppedAt = stoppedAt;
        this.algorithm = algorithm;
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

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getStoppedAt() {
        return stoppedAt;
    }

    public void setStoppedAt(LocalDateTime stoppedAt) {
        this.stoppedAt = stoppedAt;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }
}