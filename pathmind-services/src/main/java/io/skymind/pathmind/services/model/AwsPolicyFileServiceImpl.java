package io.skymind.pathmind.services.model;

import io.skymind.pathmind.db.dao.PolicyDAO;
import io.skymind.pathmind.services.PolicyFileService;
import io.skymind.pathmind.services.training.cloud.aws.api.AWSApiClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
        return awsApiClient.fileExists(getPolicyFileLocation(policyId));
    }

    @Override
    public String getPolicyFileLocation(long policyId) {
        return POLICY_FILE + policyId;
    }

    @Override
    public byte[] getPolicyFile(long policyId) {
        return awsApiClient.fileContents(getPolicyFileLocation(policyId), true);
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
        awsApiClient.fileUpload(getPolicyFileLocation(policyId), policyFile);
    }

    @Override
    public void saveCheckpointFile(Long policyId, byte[] policyFile) {
        awsApiClient.fileUpload(POLICY_CHECKPOINT + policyId, policyFile);
    }

}
