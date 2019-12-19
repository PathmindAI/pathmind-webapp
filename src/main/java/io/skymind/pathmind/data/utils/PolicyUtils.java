package io.skymind.pathmind.data.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.constants.RunType;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.Run;
import io.skymind.pathmind.data.policy.HyperParameters;
import io.skymind.pathmind.services.training.progress.ProgressInterpreter;
import io.skymind.pathmind.utils.DateAndTimeUtils;
import io.skymind.pathmind.utils.ObjectMapperHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public class PolicyUtils
{
    private static final String lrPatternStr = "lr=.*,";
    private static final Pattern lrPattern = Pattern.compile(lrPatternStr);

    private static final ObjectMapper OBJECT_MAPPER = ObjectMapperHolder.getJsonMapper();

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

    public static String getParsedPolicyName(Policy policy) {
        return policy.getParsedName();
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

    public static void processProgressJson(Policy policy, String progressString)
    {
        if(StringUtils.isEmpty(progressString))
            return;

        try {
            // STEPH -> Is this needed any more other than the setScores? Don't we already have all of this in the database? Once
            // the scores are in the database all this parsing can also be deleted.
            final Policy jsonPolicy = OBJECT_MAPPER.readValue(progressString, Policy.class);
            policy.setScores(jsonPolicy.getScores());
            policy.setStartedAt(jsonPolicy.getStartedAt());
            policy.setStoppedAt(jsonPolicy.getStoppedAt());
            policy.setAlgorithm(jsonPolicy.getAlgorithm());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    // STEPH -> This is very expensive for what it does but before it was masked under a different stack of code. Once
    // the HyperParameters are moved into the database we can delete this code.
    public static HyperParameters getHyperParametersFromName(Policy policy) {
        return ProgressInterpreter.interpretKey(policy.getName()).getHyperParameters();
    }

    public static List<Number> getMeanScores(Policy policy) {
        return policy.getScores().stream()
            .map(rewardScore -> rewardScore.getMean())
            .collect(Collectors.toList());
    }

    public static String getFormatHyperParameters(Policy policy) {
        return  HyperParameters.BATCH_SIZE + "=" + policy.getHyperParameters().getBatchSize() + ", " +
                HyperParameters.LEARNING_RATE + "=" + policy.getHyperParameters().getLearningRate() + ", " +
                HyperParameters.GAMMA + "=" + policy.getHyperParameters().getGamma();
    }

    // original name ex: PPO_PathmindEnvironment_0_gamma=0.99,lr=1e-05,sgd_minibatch_size=128_2019-10-11_21-16-2858waz_89
    // get rid of time and extra info
    // add run type and "TEMP"
    public static String generatePolicyTempName(String policyExtId, int runType)
    {
        String policyTempName = policyExtId.substring(0, policyExtId.length() - 27) + runType + RunUtils.TEMPORARY_POSTFIX;

        Matcher matcher = lrPattern.matcher(policyTempName);

        if (matcher.find()) {
            String lr = matcher.group();

            lr = lr.replace("lr=", "").replace(",", "");
            lr = "lr=" + Double.valueOf(lr).toString() + ",";

            policyTempName = policyTempName.replaceFirst(lrPatternStr, lr);
        }

        //PPO_PathmindEnvironment_0_gamma=0.99,lr=1e-05,sgd_minibatch_size=128_1TEMP
        return policyTempName;
    }

    public static void loadPolicyDataModel(Policy policy, long policyId, Run run) {
        policy.setId(policyId);
        policy.setName(policy.getExternalId());
        policy.setRun(run);
        policy.setExperiment(run.getExperiment());
        policy.setModel(run.getModel());
        policy.setProject(run.getProject());
        // For performance reasons.
        policy.setParsedName(parsePolicyName(policy.getName()));
        // STEPH -> This is very expensive for what it does but before it was masked under a different stack of code. Once
        // the HyperParameters are moved into the database we can delete this code.
        policy.setHyperParameters(getHyperParametersFromName(policy));
    }
}
