package io.skymind.pathmind.services.training.cloud.aws;

import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.Run;
import io.skymind.pathmind.data.policy.RewardScore;
import io.skymind.pathmind.db.dao.ExecutionProviderMetaDataDAO;
import io.skymind.pathmind.db.dao.RunDAO;
import io.skymind.pathmind.services.notificationservice.EmailNotificationService;
import io.skymind.pathmind.services.training.ExecutionProgressUpdater;
import io.skymind.pathmind.services.training.constant.TrainingFile;
import io.skymind.pathmind.services.training.progress.ProgressInterpreter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AWSExecutionProgressUpdater implements ExecutionProgressUpdater {

    private final AWSExecutionProvider provider;
    private final ExecutionProviderMetaDataDAO executionProviderMetaDataDAO;
    private final RunDAO runDAO;
    private EmailNotificationService emailNotificationService;


    public AWSExecutionProgressUpdater(AWSExecutionProvider provider, ExecutionProviderMetaDataDAO executionProviderMetaDataDAO, RunDAO runDAO, EmailNotificationService emailNotificationService){
        this.provider = provider;
        this.executionProviderMetaDataDAO = executionProviderMetaDataDAO;
        this.runDAO = runDAO;
        this.emailNotificationService = emailNotificationService;
    }

    @Override
    public void update() {
        // Getting all these values beforehand in single database calls rather than in loops of database calls.
        final List<Long> runIds = runDAO.getExecutingRuns();
        final Map<Long, List<String>> stoppedPoliciesNamesForRuns = runDAO.getStoppedPolicyNamesForRuns(runIds);
//        final Map<Long, String> rescaleJobIds = executionProviderMetaDataDAO.getRescaleRunJobIds(runIds);
        final List<Run>  runs = runDAO.getRuns(runIds);

        log.info("kepricondebug0 : " + runIds);

        runs.parallelStream().forEach(run -> {

            String jobHandle = "id" + run.getId();
            RunStatus runStatus = provider.status(jobHandle);

            final List<Policy> policies = getPoliciesFromProgressProvider(stoppedPoliciesNamesForRuns, run.getId());
            final List<String> finishedPolicyNamesFromAWS = getTerminatedPolicesFromProvider(jobHandle);


            log.info("kepricondebug1 : " + runStatus);
            log.info("kepricondebug2 : " + policies.stream().map(Policy::getExternalId).collect(Collectors.toList()));
            log.info("kepricondebug3 : " + finishedPolicyNamesFromAWS);

            runDAO.updateRun(run, runStatus, policies);
        });



//        log.info("kepricondebug2 : " + rescaleJobIds);
    }

    private List<Policy> getPoliciesFromProgressProvider(Map<Long, List<String>> stoppedPoliciesNamesForRuns, Long runId) {
        final Map<String, String> rawProgress = provider.progress("id" + runId);
        return rawProgress.entrySet().stream()
                .filter(e -> !stoppedPoliciesNamesForRuns.getOrDefault(runId, Collections.emptyList()).contains(e.getKey()))
                .map(e -> {
                    List<RewardScore> previousScores = runDAO.getScores(runId, e.getKey());
                    return ProgressInterpreter.interpret(e, previousScores);
                })
                .collect(Collectors.toList());
    }

    private List<String> getTerminatedPolicesFromProvider(String jobHandle) {
        return provider.getTrialStatus(jobHandle, TrainingFile.RAY_TRIAL_COMPLETE);
    }
}
