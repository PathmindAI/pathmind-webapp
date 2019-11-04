package io.skymind.pathmind.data.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.constants.RunType;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.services.training.progress.Progress;
import io.skymind.pathmind.services.training.progress.ProgressInterpreter;
import io.skymind.pathmind.utils.DateAndTimeUtils;
import io.skymind.pathmind.utils.ObjectMapperHolder;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class PolicyUtils
{
    private static Logger log = LogManager.getLogger(PolicyUtils.class);
    private static ObjectMapper OBJECT_MAPPER = ObjectMapperHolder.getJsonMapper();

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

    public static String getLastScoreString(Policy policy) {
        Number number = getLastScore(policy);
        return number == null ? "" : number.toString();
    }

    public static Number getLastScore(Policy policy) {
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

    public static void processProgressJson(Policy policy)
    {
        if(StringUtils.isEmpty(policy.getProgress()))
            return;

        try {
            final Progress progress = OBJECT_MAPPER.readValue(policy.getProgress(), Progress.class);
            policy.setScores(progress.getRewardProgression());
            policy.setStartedAt(progress.getStartedAt());
            policy.setStoppedAt(progress.getStoppedAt());
            policy.setAlgorithm(progress.getAlgorithm());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public static String getNotesFromName(Policy policy) {
        return ProgressInterpreter.interpretKey(policy.getName()).getHyperParameters().toString().replaceAll("(\\{|\\})", "");
    }

    public static List<Number> getMeanScores(Policy policy) {
        return policy.getScores().stream()
            .map(rewardScore -> rewardScore.getMean())
            .collect(Collectors.toList());
    }
}
