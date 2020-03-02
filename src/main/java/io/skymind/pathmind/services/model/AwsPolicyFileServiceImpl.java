package io.skymind.pathmind.services.model;

import io.skymind.pathmind.bus.EventBus;
import io.skymind.pathmind.bus.events.PolicyUpdateBusEvent;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.db.dao.PolicyDAO;
import io.skymind.pathmind.services.PolicyFileService;
import io.skymind.pathmind.services.training.cloud.aws.api.AWSApiClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;

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
        boolean exists = awsApiClient.fileExists(POLICY_FILE + policyId);
        if (!exists) {
            log.warn("Policy File was not found in S3, fallback lookup in DB");
            exists = policyDAO.hasPolicyFile(policyId);
        }
        return exists;
    }

    @Override
    public byte[] getPolicyFile(long policyId) {
        byte[] content = awsApiClient.fileContents(POLICY_FILE + policyId, true);
        if (ArrayUtils.isEmpty(content)) {
            log.warn("Policy File was not found in S3, fallback lookup in DB");
            content = policyDAO.getPolicyFile(policyId);
        }
        return content;
    }

    @Override
    public byte[] getSnapshotFile(long policyId) {
        byte[] content = awsApiClient.fileContents(POLICY_CHECKPOINT + policyId, true);
        if (ArrayUtils.isEmpty(content)) {
            log.warn("Policy File was not found in S3, fallback lookup in DB");
            content = policyDAO.getSnapshotFile(policyId);
        }
        return content;
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
                    EventBus.post(new PolicyUpdateBusEvent(policy));
                });
    }

    @Override
    public void saveCheckpointFile(Long policyId, byte[] policyFile) {
        awsApiClient.fileUpload(POLICY_CHECKPOINT + policyId, policyFile);
    }

}
