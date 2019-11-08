package io.skymind.pathmind.ui.plugins;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.segment.analytics.Analytics;
import com.segment.analytics.messages.PageMessage;
import com.segment.analytics.messages.TrackMessage;

import io.skymind.pathmind.security.PathmindUserDetails;
import io.skymind.pathmind.security.SecurityUtils;

@Component
public class SegmentTracker {

	private Analytics analytics;

	public SegmentTracker(@Value("${skymind.segment.key}") String key) {
		analytics = Analytics.builder(key).build();
	}
	
	public void page(String page) {
		analytics.enqueue(PageMessage.builder(page)
				.userId(getUserId()));
	}
	
	public void track(String event, Object val) {
		analytics.enqueue(TrackMessage.builder(event)
				.userId(getUserId())
				);
	}
	
	private String getUserId() {
		PathmindUserDetails user = SecurityUtils.getUser();
		if (user == null) {
			return "-1";
		} else {
			return Long.toString(user.getId());
		}
	}
	
	
}
