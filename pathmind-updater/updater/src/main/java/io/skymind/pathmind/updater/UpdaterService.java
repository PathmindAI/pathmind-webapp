package io.skymind.pathmind.updater;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.skymind.pathmind.db.dao.RunDAO;
import io.skymind.pathmind.db.dao.TrainingErrorDAO;
import io.skymind.pathmind.services.PolicyFileService;
import io.skymind.pathmind.services.training.cloud.aws.AWSExecutionProvider;
import io.skymind.pathmind.services.training.cloud.aws.api.client.AwsApiClientSNS;
import io.skymind.pathmind.services.training.cloud.aws.api.dto.UpdateEvent;
import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.data.Data;
import io.skymind.pathmind.shared.data.Metrics;
import io.skymind.pathmind.shared.data.MetricsRaw;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.data.PolicyUpdateInfo;
import io.skymind.pathmind.shared.data.ProviderJobStatus;
import io.skymind.pathmind.shared.data.RewardScore;
import io.skymind.pathmind.shared.data.Run;
import io.skymind.pathmind.shared.data.rllib.CheckPoint;
import io.skymind.pathmind.shared.data.rllib.ExperimentState;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static io.skymind.pathmind.services.training.cloud.aws.AWSExecutionProvider.RLLIB_ERROR_PREFIX;
import static io.skymind.pathmind.services.training.cloud.aws.AWSExecutionProvider.RLLIB_MAX_LEN;
import static io.skymind.pathmind.services.training.cloud.aws.AWSExecutionProvider.SUCCESS_MAX_LEN;
import static io.skymind.pathmind.services.training.cloud.aws.AWSExecutionProvider.SUCCESS_MESSAGE_PREFIX;
import static io.skymind.pathmind.services.training.cloud.aws.AWSExecutionProvider.WARNING_MAX_LEN;
import static io.skymind.pathmind.services.training.cloud.aws.AWSExecutionProvider.WARNING_MESSAGE_PREFIX;
import static io.skymind.pathmind.services.training.cloud.aws.api.dto.UpdateEvent.CARGO_ATTRIBUTE;
import static io.skymind.pathmind.services.training.cloud.aws.api.dto.UpdateEvent.FILTER_ATTRIBUTE;
import static io.skymind.pathmind.services.training.cloud.aws.api.dto.UpdateEvent.TYPE_POLICY;
import static io.skymind.pathmind.services.training.cloud.aws.api.dto.UpdateEvent.TYPE_RUN;
import static io.skymind.pathmind.shared.constants.RunStatus.Completed;
import static io.skymind.pathmind.shared.services.training.constant.ErrorConstants.KILLED_TRAINING_KEYWORD;
import static io.skymind.pathmind.shared.services.training.constant.ErrorConstants.UNKNOWN_ERROR_KEYWORD;

@Service
@Slf4j
public class UpdaterService {
    private final AWSExecutionProvider provider;

    private final RunDAO runDAO;
    private final PolicyFileService policyFileService;
    private final TrainingErrorDAO trainingErrorDAO;
    private final ObjectMapper objectMapper;
    private final String updaterTopicArn;
    private final AwsApiClientSNS snsClient;
    private final String sqsFilter;

    @Autowired
    public UpdaterService(AWSExecutionProvider provider,
                          RunDAO runDAO,
                          PolicyFileService policyFileService, TrainingErrorDAO trainingErrorDAO,
                          AwsApiClientSNS snsClient,
                          @Value("${pathmind.aws.sns.updater_topic_arn}") String topicArn,
                          @Value("${pathmind.aws.sns.updater_sqs_filter}") String sqsFilter,
                          ObjectMapper objectMapper) {
        this.provider = provider;
        this.runDAO = runDAO;
        this.policyFileService = policyFileService;
        this.trainingErrorDAO = trainingErrorDAO;
        this.snsClient = snsClient;
        this.objectMapper = objectMapper;
        this.updaterTopicArn = topicArn;
        this.sqsFilter = sqsFilter;
    }

    public ProviderJobStatus updateRunInformation(Run run, int updateCompletingAttemptsLimit, List<String> stoppedPoliciesNames) {
        final String jobHandle = run.getJobId();

        ProviderJobStatus providerJobStatus = provider.status(jobHandle);
        if (providerJobStatus.getRunStatus().equals(RunStatus.Completing)) {
            run.setCompletingUpdatesAttempts(run.getCompletingUpdatesAttempts() + 1);
            List<String> unfinishedIds = runDAO.unfinishedPolicyIds(run.getId());
            boolean enforceComplete = run.getCompletingUpdatesAttempts() >= updateCompletingAttemptsLimit;
            if (unfinishedIds.size() == 0 || enforceComplete) {
                if (enforceComplete) {
                    log.info("Marking Completing run [{}] as Completed since limit of update attempts had been reached", run.getId());
                }
                providerJobStatus = new ProviderJobStatus(Completed, providerJobStatus.getDescription(), providerJobStatus.getExperimentState());
            }
        }
        ExperimentState experimentState = providerJobStatus.getExperimentState();

        final List<Policy> policies = getPoliciesFromProgressProvider(stoppedPoliciesNames, run.getId(), jobHandle, experimentState, false);

        setStoppedAtForFinishedPolicies(policies, experimentState);
        setEventualInformationAboutWhyTheRunEnded(run, providerJobStatus);

        List<PolicyUpdateInfo> policiesUpdateInfo = getPoliciesUpdateInfo(stoppedPoliciesNames, run.getId(), jobHandle,
                providerJobStatus);

        updateInfoInAWS(run, policiesUpdateInfo);

        updateInfoInDB(run, providerJobStatus, policies, policiesUpdateInfo);

        return providerJobStatus;
    }

    private void updateInfoInDB(Run run, ProviderJobStatus providerJobStatus, List<Policy> policies, List<PolicyUpdateInfo> policiesUpdateInfo) {
        // dont' need to update database for freezing policy
        policiesUpdateInfo = policiesUpdateInfo.stream().filter(p -> !p.getName().equals("freezing")).collect(Collectors.toList());
        List<Policy> policiesToRaiseUpdateEvent = runDAO.updateRun(run, providerJobStatus, policies, policiesUpdateInfo, getValidExternalIdsIfCompleted(providerJobStatus));
        policiesToRaiseUpdateEvent.addAll(ensurePolicyDataIfRunIsCompleted(run, providerJobStatus));

        // The EventBus updates have to be done AFTER the transaction is completed and NOT during in case the transaction fails.
        fireEventUpdates(run, policies);
        policiesToRaiseUpdateEvent
                .forEach(policy -> fireEventUpdates(null, Collections.singletonList(policy)));
    }

    // When the Run is completed, update policy data one more time just to ensure all data is saved to DB
    // See https://github.com/SkymindIO/pathmind-webapp/issues/1866 for details.
    private List<Policy> ensurePolicyDataIfRunIsCompleted(Run run, ProviderJobStatus providerJobStatus) {
        if (run.getStatusEnum() == Completed) {
            log.debug("final DB updates for " + run.getJobId());
            List<Policy> policies = getPoliciesFromProgressProvider(Collections.emptyList(), run.getId(),
                    run.getJobId(), providerJobStatus.getExperimentState(), true);
            setStoppedAtForFinishedPolicies(policies, providerJobStatus.getExperimentState());
            runDAO.updatePolicyData(run, policies);
            Map<String, Boolean> dbPolicies = runDAO.getPolicies(run.getId()).stream()
                    .collect(Collectors.toMap(Policy::getExternalId, Policy::hasFile));
            policies.stream().forEach(p -> p.setHasFile(dbPolicies.getOrDefault(p.getExternalId(), false)));
            return policies;
        }
        return Collections.emptyList();
    }

    private List<String> getValidExternalIdsIfCompleted(ProviderJobStatus providerJobStatus) {
        if (RunStatus.isCompleting(providerJobStatus.getRunStatus())) {
            return providerJobStatus.getExperimentState().getCheckpoints().stream()
                    .map(CheckPoint::getId)
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    private void updateInfoInAWS(Run run, List<PolicyUpdateInfo> policiesUpdateInfo) {
        policiesUpdateInfo.forEach(policyInfo -> {
            policyFileService.savePolicyFile(run.getId(), policyInfo.getName(), policyInfo.getPolicyFile());
            if (policyInfo.getCheckpointFile() != null) {
                policyFileService.saveCheckpointFile(run.getId(), policyInfo.getName(),
                        policyInfo.getCheckpointFile()); // only update aws
                log.debug("checkpoint saved for " + policyInfo.getName());
            }
        });
    }

    private void setStoppedAtForFinishedPolicies(List<Policy> policies, ExperimentState experimentState) {
        Map<String, LocalDateTime> terminatedTrials = provider.getTerminatedTrials(experimentState);

        policies.stream()
                .filter(policy -> policy.getStoppedAt() == null)
                .filter(policy -> terminatedTrials.containsKey(policy.getExternalId()))
                .forEach(policy -> policy.setStoppedAt(terminatedTrials.get(policy.getExternalId())));
    }

    private List<Policy> getPoliciesFromProgressProvider(List<String> stoppedPoliciesNamesForRuns, Long runId,
                                                         String jobHandle, ExperimentState experimentState, boolean isFinalUpdate) {
        if (experimentState == null) {
            return Collections.emptyList();
        }

        List<String> validExternalIds = experimentState.getCheckpoints().stream()
                .map(CheckPoint::getId)
                .filter(id -> !stoppedPoliciesNamesForRuns.contains(id))
                .collect(Collectors.toList());

        final Map<String, String> rawProgress = provider.progress(jobHandle, validExternalIds);

        return rawProgress.entrySet().stream()
                .map(e -> {
                    List<RewardScore> previousScores = runDAO.getScores(runId, e.getKey());
                    List<Metrics> previousMetrics = runDAO.getMetrics(runId, e.getKey());
                    int numReward = runDAO.getRewardNumForRun(runId);
                    int numAgents = runDAO.getAgentsNumForRun(runId);
                    Policy policy = ProgressInterpreter.interpret(e, previousScores, previousMetrics, numReward, numAgents);
                    if (isFinalUpdate && policy.getMetrics().size() > 0) {
                        List<MetricsRaw> previousMetricsRaw = runDAO.getMetricsRaw(runId, e.getKey());
                        int lastIteration = policy.getMetrics().get(policy.getMetrics().size() - 1).getIteration();
                        ProgressInterpreter.interpretMetricsRaw(e, policy, previousMetricsRaw, lastIteration - 10, numReward, numAgents);
                    }

                    return policy;
                })
                .collect(Collectors.toList());
    }

    private void setEventualInformationAboutWhyTheRunEnded(Run run, ProviderJobStatus jobStatus) {
        final var status = jobStatus.getRunStatus();
        Collection<String> descriptions = CollectionUtils.emptyIfNull(jobStatus.getDescription());
        if (status == RunStatus.Error && !CollectionUtils.isEmpty(descriptions)) {
            // TODO (KW): 05.02.2020 gets only first error, refactor if multiple errors scenario is possible
            final var errorMessage = descriptions.iterator().next();
            final var allErrorsKeywords = trainingErrorDAO.getAllErrorsKeywords();
            final var knownErrorMessage = allErrorsKeywords.stream()
                    .filter(errorMessage::contains)
                    .findAny()
                    .orElseGet(() -> {
                        log.warn("Unrecognized error: {}", errorMessage);
                        return UNKNOWN_ERROR_KEYWORD;
                    });

            final var foundError = trainingErrorDAO.getErrorByKeyword(knownErrorMessage);
            foundError.ifPresent(e -> run.setTrainingErrorId(e.getId()));
            getDescriptionStartingWithPrefix(descriptions, RLLIB_ERROR_PREFIX, RLLIB_MAX_LEN).ifPresent(run::setRllibError);
        } else if (status == RunStatus.Killed && run.getStatusEnum() != RunStatus.Stopping) {
            // Stopping status is set, when user wants to stop training. So, don't assign an error in this case
            trainingErrorDAO.getErrorByKeyword(KILLED_TRAINING_KEYWORD).ifPresent(error -> {
                run.setTrainingErrorId(error.getId());
            });
            getDescriptionStartingWithPrefix(descriptions, RLLIB_ERROR_PREFIX, RLLIB_MAX_LEN).ifPresent(run::setRllibError);
        } else if (status == Completed) {
            getDescriptionStartingWithPrefix(descriptions, SUCCESS_MESSAGE_PREFIX, SUCCESS_MAX_LEN).ifPresent(run::setSuccessMessage);
            getDescriptionStartingWithPrefix(descriptions, WARNING_MESSAGE_PREFIX, WARNING_MAX_LEN).ifPresent(run::setWarningMessage);
        }
    }

    private Optional<String> getDescriptionStartingWithPrefix(Collection<String> descriptions, String prefix, int maxLength) {
        return descriptions.stream()
                .filter(e -> e.startsWith(prefix)).findAny().map(e -> {
                    e = e.replace(prefix, "");
                    if (e.length() > maxLength) {
                        e = e.substring(0, maxLength);
                    }
                    return e;
                });
    }

    private void fireEventUpdates(Run run, List<Policy> policies) {
        for (Policy policy : CollectionUtils.emptyIfNull(policies)) {
            serializeAndFireEvent(policy, TYPE_POLICY);
        }
        serializeAndFireEvent(run, TYPE_RUN);
    }

    private <T extends Data> void serializeAndFireEvent(T data, String type) {
        if (data == null) {
            return;
        }
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream out = new ObjectOutputStream(bos)) {
            out.writeObject(data);
            out.flush();
            byte[] bytes = bos.toByteArray();
            fireEventUpdate(type, data.getId(), bytes);
        } catch (IOException e) {
            log.error("Failed to file event: {} {}", type, data.getId(), e);
        }
    }

    private void fireEventUpdate(String type, Long id, byte[] cargo) {
        try {
            final UpdateEvent event = new UpdateEvent(id, type, cargo.length);
            sendUpdaterMessage(event, cargo);
            log.debug("fired update event [{}]", event);
        } catch (Exception e) {
            log.error("Failed to submit update request to SNS", e);
        }
    }

    public String sendUpdaterMessage(UpdateEvent event, byte[] cargo) throws JsonProcessingException {

        PublishRequest publishRequest = new PublishRequest()
                .withTopicArn(updaterTopicArn)
                .withMessage(objectMapper.writeValueAsString(event));

        if (StringUtils.isNotEmpty(sqsFilter)) {
            publishRequest.addMessageAttributesEntry(
                    FILTER_ATTRIBUTE, new MessageAttributeValue().withStringValue(sqsFilter).withDataType("String")
            );
        }

        publishRequest.addMessageAttributesEntry(CARGO_ATTRIBUTE,
                new MessageAttributeValue().withBinaryValue(ByteBuffer.wrap(cargo)).withDataType("Binary")
        );

        PublishResult result = snsClient.getSnsClient().publish(publishRequest);
        return result.getMessageId();
    }

    private List<PolicyUpdateInfo> getPoliciesUpdateInfo(List<String> stoppedPoliciesNames, Long runId,
                                                         String jobHandle, ProviderJobStatus providerJobStatus) {
        List<PolicyUpdateInfo> policiesInfo = new ArrayList<>();
        if (RunStatus.isCompleting(providerJobStatus.getRunStatus())) {
            List<String> unfinishedPolicyIds = runDAO.unfinishedPolicyIds(runId);
            policiesInfo.addAll(
                    stoppedPoliciesNames
                            .stream()
                            .filter(unfinishedPolicyIds::contains)
                            .map(finishPolicyName -> {
                                final byte[] policyFile = provider
                                        .policy(jobHandle, finishPolicyName); // don't update db nor aws
                                if (policyFile == null) {
                                    return Optional.<PolicyUpdateInfo>empty();
                                }
                                PolicyUpdateInfo policyUpdateInfo = new PolicyUpdateInfo();
                                policyUpdateInfo.setName(finishPolicyName);
                                policyUpdateInfo.setPolicyFile(policyFile);
                                Map.Entry<String, byte[]> checkpointInfo = provider
                                        .snapshot(jobHandle, finishPolicyName);
                                if (checkpointInfo != null) {
                                    policyUpdateInfo.setCheckpointFileKey(checkpointInfo.getKey());
                                    policyUpdateInfo.setCheckpointFile(checkpointInfo.getValue());
                                }
                                return Optional.of(policyUpdateInfo);
                            })
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .collect(Collectors.toList())
            );

            if (policiesInfo.size() > 0) {
                final byte[] policyFile = provider.policy(jobHandle, "freezing");
                if (policiesInfo != null) {
                    PolicyUpdateInfo policyUpdateInfo = new PolicyUpdateInfo();
                    policyUpdateInfo.setName("freezing");
                    policyUpdateInfo.setPolicyFile(policyFile);
                    policiesInfo.add(policyUpdateInfo);
                }
            }
        }
        return policiesInfo;
    }
}
