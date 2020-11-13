package io.skymind.pathmind.db.changeset;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Run;
import liquibase.change.custom.CustomSqlChange;
import liquibase.database.Database;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.CustomChangeException;
import liquibase.exception.SetupException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;
import liquibase.statement.SqlStatement;
import liquibase.statement.core.RawSqlStatement;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UpdateExperiment_ChangeSet_20200923_02 implements CustomSqlChange {
    @Override
    public SqlStatement[] generateStatements(Database database) throws CustomChangeException {
        // IMPORTANT -> Do NOT close the connection as it's used by liquibase for the rest of the changesets.
        Connection connection = ((JdbcConnection) database.getConnection()).getUnderlyingConnection();
        List<DataNeededForMigration> data = getDataNeededForMigration(connection);
        Map<Integer, List<DataNeededForMigration>> experimentsData = data.stream().collect(Collectors.groupingBy(d -> d.experimentId, Collectors.toList()));
        List<UpdateInformation> allUpdates = experimentsData.entrySet().stream()
                .map(entry -> {
                    List<DataNeededForMigration> experimentData = entry.getValue();
                    List<Run> fakeRuns = experimentData.stream().map(e -> {
                        Run run = new Run();
                        run.setStatus(e.runStatus);
                        run.setTrainingErrorId(e.trainingErrorId);
                        return run;
                    }).collect(Collectors.toList());
                    Experiment experiment = new Experiment();
                    experiment.setRuns(fakeRuns);
                    experiment.updateTrainingStatus();
                    return new UpdateInformation(entry.getKey(), experiment.getTrainingStatusEnum().getValue());
                }).collect(Collectors.toList());
        return allUpdates.stream().map(update -> new RawSqlStatement(
                String.format("UPDATE experiment SET training_status=%s where id=%s", update.experimentStatus, update.experimentId))).toArray(SqlStatement[]::new);
    }

    private List<DataNeededForMigration> getDataNeededForMigration(Connection connection) throws CustomChangeException {
        List<DataNeededForMigration> result = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("select e.id, COALESCE(r.status, 0) as status, r.training_error_id\n" +
                "from experiment e\n" +
                "left join run r on e.id=r.experiment_id\n" +
                "order by e.id")) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                result.add(new DataNeededForMigration(
                        rs.getInt("ID"),
                        rs.getInt("STATUS"),
                        rs.getInt("training_error_id")
                ));
            }
            return result;
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

    private static class DataNeededForMigration {
        private final int experimentId;
        private final int runStatus;
        private final int trainingErrorId;

        private DataNeededForMigration(int experimentId, int runStatus, int trainingErrorId) {
            this.experimentId = experimentId;
            this.runStatus = runStatus;
            this.trainingErrorId = trainingErrorId;
        }
    }

    private static class UpdateInformation {
        private final int experimentId;
        private final int experimentStatus;

        public UpdateInformation(int experimentId, int experimentStatus) {
            this.experimentId = experimentId;
            this.experimentStatus = experimentStatus;
        }
    }
}
