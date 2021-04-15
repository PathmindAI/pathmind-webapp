package io.skymind.pathmind.services.analytics;

import java.util.HashMap;
import java.util.Map;

import io.skymind.pathmind.shared.data.Run;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.segment.analytics.Analytics;
import com.segment.analytics.messages.TrackMessage;

import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.constants.RunType;
import lombok.extern.slf4j.Slf4j;

import static io.skymind.pathmind.shared.segment.SegmentTrackingEvents.EVENT_TRAINING_COMPLETED;
import static io.skymind.pathmind.shared.segment.SegmentTrackingEvents.EVENT_ONBOARDING_SERVICE_PAID;

/**
 * SegmentTrackerService is server side counter part of <code>SegmentIntegrator</code>
 * This service uses a server type of source in segment, and uses segment java API
 */
@Service
@Slf4j
public class SegmentTrackerService {
	
	private final Analytics analytics;
	private final boolean enabled;
	
	public SegmentTrackerService(@Value("${pathmind.segment.server.source.key}") String key, @Value("${pathmind.segment.enabled}") Boolean enabled) {
		analytics = Analytics.builder(key).build();
		this.enabled = enabled;
	}

    public void onboardingServicePaid(long userId, Map<String, String> properties) {
        track(EVENT_ONBOARDING_SERVICE_PAID, Long.toString(userId), properties);
    }

    public void trainingCompleted(long userId, Run run) {
	    trainingCompleted(userId, run.getExperimentId(), run.getRunTypeEnum(), run.getStatusEnum());
    }

    public void trainingCompleted(long userId, long experimentId, RunType runType, RunStatus jobStatus) {
		Map<String, String> properties = new HashMap<>();
		properties.put("type", runType.toString());
		properties.put("status", jobStatus.toString());
		properties.put("experiment", Long.toString(experimentId));
		
		track(EVENT_TRAINING_COMPLETED, Long.toString(userId), properties);
	}

	private void track(String event, String userId, Map<String, String> properties) {
		if (enabled) {
			analytics.enqueue(TrackMessage.builder(event)
					.userId(userId)
					.properties(properties));
			analytics.flush();
		} else {
			log.info("Segment integration is disabled, not sending " + event + " track event");
		}
	}
	
}
