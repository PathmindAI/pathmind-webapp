package io.skymind.pathmind.updater;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.data.PolicyUpdateInfo;
import io.skymind.pathmind.shared.data.ProviderJobStatus;
import io.skymind.pathmind.shared.data.RewardScore;
import io.skymind.pathmind.shared.data.Run;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static io.skymind.pathmind.services.training.cloud.aws.api.dto.UpdateEvent.CARGO_ATTRIBUTE;
import static io.skymind.pathmind.services.training.cloud.aws.api.dto.UpdateEvent.TYPE_POLICY;
import static io.skymind.pathmind.services.training.cloud.aws.api.dto.UpdateEvent.TYPE_RUN;
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

    @Autowired
    public UpdaterService(AWSExecutionProvider provider,
            RunDAO runDAO,
            PolicyFileService policyFileService, TrainingErrorDAO trainingErrorDAO,
            ObjectMapper objectMapper, @Value("${pathmind.aws.sns.updater_topic_arn}") String updaterTopicArn,
            AwsApiClientSNS snsClient) {
        this.provider = provider;
        this.runDAO = runDAO;
        this.policyFileService = policyFileService;
        this.trainingErrorDAO = trainingErrorDAO;
        this.objectMapper = objectMapper;
        this.updaterTopicArn = updaterTopicArn;
        this.snsClient = snsClient;
    }

    public ProviderJobStatus updateRunInformation(
            Run run,
            Map<Long, List<String>> stoppedPoliciesNamesForRuns,
            Map<Long, String> awsJobIds
    ) {
        List<String> stoppedPoliciesNames = stoppedPoliciesNamesForRuns
                .getOrDefault(run.getId(), Collections.emptyList());

        String jobHandle = awsJobIds.get(run.getId());
        ProviderJobStatus providerJobStatus = provider.status(jobHandle);

        final List<Policy> policies = getPoliciesFromProgressProvider(stoppedPoliciesNamesForRuns, run.getId(),
                jobHandle);

        setStoppedAtForFinishedPolicies(policies, jobHandle);
        setRunError(run, providerJobStatus);

        List<PolicyUpdateInfo> policiesUpdateInfo = getPoliciesUpdateInfo(stoppedPoliciesNames, jobHandle,
                providerJobStatus);

        updateInfoInAWS(run, policiesUpdateInfo);

        updateInfoInDB(run, providerJobStatus, policies, policiesUpdateInfo);

        return providerJobStatus;
    }

    private void updateInfoInDB(Run run, ProviderJobStatus providerJobStatus, List<Policy> policies,
            List<PolicyUpdateInfo> policiesUpdateInfo) {
        List<Policy> policiesToRaiseUpdateEvent =  runDAO.updateRun(run, providerJobStatus, policies, policiesUpdateInfo);
        // The EventBus updates have to be done AFTER the transaction is completed and NOT during in case the transaction fails.
        fireEventUpdates(run, policies);
        policiesToRaiseUpdateEvent
                .forEach(policy -> fireEventUpdates(null, Collections.singletonList(policy)));
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

    private void setStoppedAtForFinishedPolicies(List<Policy> policies, String jobHandle) {
        Map<String, LocalDateTime> terminatedTrials = provider.getTerminatedTrials(jobHandle);

        policies.stream()
                .filter(policy -> policy.getStoppedAt() == null)
                .filter(policy -> terminatedTrials.containsKey(policy.getExternalId()))
                .forEach(policy -> policy.setStoppedAt(terminatedTrials.get(policy.getExternalId())));
    }

    private List<Policy> getPoliciesFromProgressProvider(Map<Long, List<String>> stoppedPoliciesNamesForRuns, Long runId, String jobHandle) {
        final Map<String, String> rawProgress = provider.progress(jobHandle);
        return rawProgress.entrySet().stream()
                .filter(e -> !stoppedPoliciesNamesForRuns.getOrDefault(runId, Collections.emptyList()).contains(e.getKey()))
                .map(e -> {
                    List<RewardScore> previousScores = runDAO.getScores(runId, e.getKey());
                    return ProgressInterpreter.interpret(e, previousScores);
                })
                .collect(Collectors.toList());
    }

    private void setRunError(Run run, ProviderJobStatus jobStatus) {
        final var status = jobStatus.getRunStatus();
        if (status == RunStatus.Error && !org.apache.commons.collections4.CollectionUtils.isEmpty(jobStatus.getDescription())) {
            // TODO (KW): 05.02.2020 gets only first error, refactor if multiple errors scenario is possible
            final var errorMessage = jobStatus.getDescription().get(0);
            final var allErrorsKeywords = trainingErrorDAO.getAllErrorsKeywords();
            final var knownErrorMessage = allErrorsKeywords.stream()
                    .filter(errorMessage::contains)
                    .findAny()
                    .orElseGet(() -> {
                        log.warn("Unrecognized error: {}", errorMessage);
                        return UNKNOWN_ERROR_KEYWORD;
                    });

            final var foundError = trainingErrorDAO.getErrorByKeyword(knownErrorMessage);
            foundError.ifPresent(
                    e -> run.setTrainingErrorId(e.getId())
            );
        } else if (status == RunStatus.Killed && run.getStatusEnum() != RunStatus.Stopping) {
            // Stopping status is set, when user wants to stop training. So, don't assign an error in this case
            trainingErrorDAO.getErrorByKeyword(KILLED_TRAINING_KEYWORD).ifPresent(error -> {
                run.setTrainingErrorId(error.getId());
            });
        }
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
                .withMessage(objectMapper.writeValueAsString(event))
                .withMessageAttributes(Map.of(
                        CARGO_ATTRIBUTE, new MessageAttributeValue()
                                .withBinaryValue(ByteBuffer.wrap(cargo)).withDataType("Binary")
                ));

        PublishResult result = snsClient.getSnsClient().publish(publishRequest);
        return result.getMessageId();
    }

    private List<PolicyUpdateInfo> getPoliciesUpdateInfo(List<String> stoppedPoliciesNames,
            String jobHandle, ProviderJobStatus providerJobStatus) {
        List<PolicyUpdateInfo> policiesInfo = new ArrayList<>();
        if (providerJobStatus.getRunStatus() == RunStatus.Completed) {
            policiesInfo.addAll(
                    stoppedPoliciesNames
                            .stream()
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
        }
        return policiesInfo;
    }
}
