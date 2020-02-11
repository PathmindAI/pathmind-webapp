package io.skymind.pathmind.services.analytics;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.segment.analytics.Analytics;
import com.segment.analytics.messages.TrackMessage;

import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.constants.RunType;
import io.skymind.pathmind.db.data.PathmindUser;
import lombok.extern.slf4j.Slf4j;

/**
 * SegmentTrackerService is server side counter part of <code>SegmentIntegrator</code>
 * This service uses a server type of source in segment, and uses segment java API
 */
@Service
@Slf4j
public class SegmentTrackerService {
	
	private static final String EVENT_TRAINING_COMPLETED = "Training Completed";

	private Analytics analytics;
	private boolean enabled;
	
	public SegmentTrackerService(@Value("${skymind.segment.server.source.key}") String key, @Value("${skymind.segment.enabled}") Boolean enabled) {
		analytics = Analytics.builder(key).build();
		this.enabled = enabled;
	}
	
	public void trainingCompleted(PathmindUser user, long experimentId, RunType runType, RunStatus jobStatus) {
		Map<String, String> properties = new HashMap<>();
		properties.put("type", runType.toString());
		properties.put("status", jobStatus.toString());
		properties.put("experiment", Long.toString(experimentId));
		
		track(EVENT_TRAINING_COMPLETED, Long.toString(user.getId()), properties);
	}
	
	private void track(String event, String userId, Map<String, String> properties) {
		if (enabled) {
			analytics.enqueue(TrackMessage.builder(EVENT_TRAINING_COMPLETED)
					.userId(userId)
					.properties(properties));
			analytics.flush();
		} else {
			log.info("Segment integration is disabled, not sending " + event + " track event");
		}
	}
	
}
