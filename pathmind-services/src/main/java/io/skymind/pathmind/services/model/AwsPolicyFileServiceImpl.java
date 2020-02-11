package io.skymind.pathmind.services.model;

import io.skymind.pathmind.shared.bus.EventBus;
import io.skymind.pathmind.db.bus.events.PolicyUpdateBusEvent;
import io.skymind.pathmind.db.dao.PolicyDAO;
import io.skymind.pathmind.services.PolicyFileService;
import io.skymind.pathmind.services.training.cloud.aws.api.AWSApiClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Service
class AwsPolicyFileServiceImpl implements PolicyFileService {

    public static final String POLICY_FILE = "policy_file/";
    public static final String POLICY_CHECKPOINT = "policy_checkpoint/";

    private final AWSApiClient awsApiClient;
    private final PolicyDAO policyDAO;

    AwsPolicyFileServiceImpl(AWSApiClient awsApiClient, PolicyDAO policyDAO) {
        this.awsApiClient = awsApiClient;
        this.policyDAO = policyDAO;
    }

    @Override
    public boolean hasPolicyFile(long policyId) {
        return awsApiClient.fileExists(POLICY_FILE + policyId);
    }

    @Override
    public byte[] getPolicyFile(long policyId) {
        return awsApiClient.fileContents(POLICY_FILE + policyId, true);
    }

    @Override
    public byte[] getSnapshotFile(long policyId) {
        return awsApiClient.fileContents(POLICY_CHECKPOINT + policyId, true);
    }

    @Override
    public void savePolicyFile(Long runId, String finishPolicyName, byte[] policyFile) {
        Long policyId = policyDAO.assurePolicyId(runId, finishPolicyName);
        savePolicyFile(policyId, policyFile);
    }

    @Override
    public void saveCheckpointFile(Long runId, String finishPolicyName, byte[] checkPointFile) {
        Long policyId = policyDAO.assurePolicyId(runId, finishPolicyName);
        saveCheckpointFile(policyId, checkPointFile);
    }

    @Override
    public void savePolicyFile(Long policyId, byte[] policyFile) {
        awsApiClient.fileUpload(POLICY_FILE + policyId, policyFile);
        policyDAO.setHasFile(policyId, true);

        Optional.ofNullable(policyDAO.getPolicy(policyId))
                .ifPresent(policy -> {
                    policy.setHasFile(true);
                    EventBus.post(new PolicyUpdateBusEvent(Arrays.asList(policy)));
                });
    }

    @Override
    public void saveCheckpointFile(Long policyId, byte[] policyFile) {
        awsApiClient.fileUpload(POLICY_CHECKPOINT + policyId, policyFile);
    }

}
