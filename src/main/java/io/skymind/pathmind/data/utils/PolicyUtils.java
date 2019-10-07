package io.skymind.pathmind.data.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.constants.RunType;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.services.training.progress.Progress;
import io.skymind.pathmind.utils.DateAndTimeUtils;
import io.skymind.pathmind.utils.ObjectMapperHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.jni.Local;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class PolicyUtils
{
	private PolicyUtils() {
	}

	public static final String getDuration(Policy policy) {
		return DateAndTimeUtils.formatTime(RunUtils.getElapsedTime(policy.getRun()));
	}

	private static Logger log = LogManager.getLogger(PolicyUtils.class);

	private static ObjectMapper objectMapper = ObjectMapperHolder.getJsonMapper();

	public static String getRunStatus(Policy policy) {
		if (policy.getRun().getRunTypeEnum().equals(RunType.DiscoveryRun) && policy.getRun().getStatusEnum().equals(RunStatus.Running)) {
			try {
				return objectMapper.readValue(policy.getProgress(), Progress.class).getStoppedAt() != null ? RunStatus.Completed.name() : RunStatus.Running.name();
			} catch (Exception e) {
				log.debug(e.getMessage(), e);
				return null;
			}
		} else {
			return policy.getRun().getStatusEnum().name();
		}
	}

	public static LocalDateTime getRunCompletedTime(Policy policy) {
		{
			if (!RunStatus.Completed.name().equalsIgnoreCase(getRunStatus(policy))) {
				return null;
			}

			try {

				return objectMapper.readValue(policy.getProgress(), Progress.class).getStoppedAt();
			} catch (Exception e) {
				log.debug(e.getMessage(), e);
				return null;
			}
		}
	}

	public static String getLastScore(Policy policy) {
		try {
			return policy.getScores().get(policy.getScores().size() - 1).toString();
		} catch (Exception e) {
			log.debug(e.getMessage(), e);
			return null;
		}
	}

	public static String getParsedPolicyName(Policy policy) {
		try {
			return policy.getName().split("_")[2];
		} catch (Exception e) {
			log.debug(e.getMessage(), e);
			return null;
		}
	}

	public static String getElaspedTime(Policy policy) {
		try {
			Progress progress = objectMapper.readValue(policy.getProgress(), Progress.class);
			if (progress.getStoppedAt() != null) {
				return Duration.between(progress.getStartedAt(), progress.getStoppedAt()).toString();
			} else {
				return null;
			}
		} catch (IOException e) {
			log.debug(e.getMessage(), e);
			return null;
		}
	}
}
