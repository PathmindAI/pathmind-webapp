package io.skymind.pathmind.data.utils;

import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.constants.RunType;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.Run;
import io.skymind.pathmind.utils.DateAndTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static io.skymind.pathmind.utils.StringUtils.toCamelCase;
import static io.skymind.pathmind.utils.StringUtils.removeInvalidChars;

@Slf4j
public class PolicyUtils
{
    public static final String LEARNING_RATE = "lr";
    public static final String GAMMA = "gamma";
    public static final String BATCH_SIZE = "sgd_minibatch_size";

    private PolicyUtils() {
    }

    public static RunStatus getRunStatus(Policy policy) {
        if (policy.getRun().getRunTypeEnum() == RunType.DiscoveryRun && policy.getRun().getStatusEnum() == RunStatus.Running) {
            return policy.getStoppedAt() == null ? RunStatus.Running : RunStatus.Completed;
        } else {
            return policy.getRun().getStatusEnum();
        }
    }

    public static LocalDateTime getRunCompletedTime(Policy policy)
    {
        if (RunStatus.Completed == getRunStatus(policy))
            return policy.getStoppedAt();
        return null;
    }
    
    /**
     * If abs(score) > 1, show one decimal point
     * else show 6 decimal points
     */
    private static DecimalFormat getLastScoreFormatter(Double score) {
 		DecimalFormat decimalFormat = new DecimalFormat();
 		if (Math.abs(score) > 1) {
 			decimalFormat.setMaximumFractionDigits(1);
 		} else {
 			decimalFormat.setMaximumFractionDigits(6);
 		}
 		return decimalFormat;
 	}

 	public static String getFormattedLastScore(Policy policy) {
 		Double score = getLastScore(policy);
 		if(score == null || score.isNaN()) {
 			return "-";
 		}
 		return getLastScoreFormatter(score).format(score);
 	}

    public static Double getLastScore(Policy policy) {
        if(policy == null || policy.getScores() == null || policy.getScores().isEmpty())
            return null;
        return policy.getScores().get(policy.getScores().size() - 1).getMean();
    }

    public static final String getElapsedTime(Policy policy) {
        return DateAndTimeUtils.formatDurationTime(RunUtils.getElapsedTime(policy.getRun()));
    }

    public static String parsePolicyName(String name) {
        try {
            return name.split("_")[2];
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
            return null;
        }
    }

    public static List<Number> getMeanScores(Policy policy) {
        return policy.getScores().stream()
            .map(rewardScore -> rewardScore.getMean())
            .collect(Collectors.toList());
    }

    public static String generateDefaultNotes(Policy policy) {
        return  BATCH_SIZE + "=" + policy.getBatchSize() + ", " +
                LEARNING_RATE + "=" + policy.getLearningRate() + ", " +
                GAMMA + "=" + policy.getGamma();
    }

    public static void loadPolicyDataModel(Policy policy, long policyId, Run run) {
        policy.setId(policyId);
        policy.setRun(run);
        policy.setExperiment(run.getExperiment());
        policy.setModel(run.getModel());
        policy.setProject(run.getProject());
    }

    public static List<Long> convertToPolicyIds(List<Policy> policies) {
        return policies.stream().map(policy -> policy.getId()).collect(Collectors.toList());
    }

    public static String generatePolicyFileName(Policy policy) {
        if(!ObjectUtils.allNotNull(policy, policy.getProject(), policy.getModel(), policy.getExperiment())) {
            return "-";
        }
        return removeInvalidChars(String.format("%s-M%sE%s-Policy.zip", toCamelCase(policy.getProject().getName()), policy.getModel().getName(), policy.getExperiment().getName()));
    }
}
