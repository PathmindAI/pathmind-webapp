package io.skymind.pathmind.ui.plugins;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.segment.analytics.Analytics;
import com.segment.analytics.messages.IdentifyMessage;
import com.segment.analytics.messages.PageMessage;
import com.segment.analytics.messages.TrackMessage;
import com.vaadin.flow.router.Location;

import io.skymind.pathmind.data.PathmindUser;
import io.skymind.pathmind.security.PathmindUserDetails;
import io.skymind.pathmind.security.SecurityUtils;
import io.skymind.pathmind.utils.SegmentDataMapper;

@Component
public class SegmentTracker {

	private static final String EVENT_SIGN_UP = "Sign up";
	private static final String EVENT_LOGIN = "Login";
	private static final String EVENT_IMPORT_MODEL = "Import Model";
	private static final String EVENT_CREATE_PROJECT = "Create Project";
	private static final String EVENT_CREATE_REWARD_FUNTION = "Create Reward Function";
	private static final String EVENT_START_TEST_RUN = "Start Test Run";
	private static final String EVENT_START_DISCOVERY_RUN = "Start Discovery Run";
	private static final String EVENT_START_FULL_RUN = "Start Full Run";
	private static final String EVENT_EXPORT_POLICY = "Export Policy";
	private static final String EVENT_SAVE_DRAFT = "Save Draft";
	private static final String EVENT_CHANGE_PW = "Change Password";
	private static final String EVENT_EDIT_INFO = "Edit Info";
	
	private Analytics analytics;

	public SegmentTracker(@Value("${skymind.segment.key}") String key) {
		analytics = Analytics.builder(key).build();
	}
	
	public void trackPageVisit(Location location) {
		page(location);
	}
	
	public void userRegistered(PathmindUser user) {
		identify(user);
		track(EVENT_SIGN_UP, user.getId());
	}
	
	public void userLoggedIn(PathmindUserDetails user) {
		track(EVENT_LOGIN, user.getId());
	}
	
	public void modelImported(boolean result) {
		Map<String, String> additionalInfo = new HashMap<>();
		additionalInfo.put("result", result ? "success" : "failed");
		track(EVENT_IMPORT_MODEL, SecurityUtils.getUserId(), additionalInfo);
	}
	
	public void projectCreated() {
		track(EVENT_CREATE_PROJECT, SecurityUtils.getUserId());
	}
	
	public void rewardFuntionCreated() {
		track(EVENT_CREATE_REWARD_FUNTION, SecurityUtils.getUserId());
	}

	public void testRunStarted() {
		track(EVENT_START_TEST_RUN, SecurityUtils.getUserId());
	}

	public void discoveryRunStarted() {
		track(EVENT_START_DISCOVERY_RUN, SecurityUtils.getUserId());
	}

	public void fullRunStarted() {
		track(EVENT_START_FULL_RUN, SecurityUtils.getUserId());
	}

	public void policyExported() {
		track(EVENT_EXPORT_POLICY, SecurityUtils.getUserId());
	}

	public void draftSaved() {
		track(EVENT_SAVE_DRAFT, SecurityUtils.getUserId());
	}

	public void passwordChanged() {
		track(EVENT_CHANGE_PW, SecurityUtils.getUserId());
	}

	public void infoEdited() {
		track(EVENT_EDIT_INFO, SecurityUtils.getUserId());
	}
	
	/**
	 * From Segment documentation (https://segment.com/docs/sources/server/java/quickstart/)
	 * 
	 * The page method lets you record whenever a user sees a page of your website, 
	 * along with optional extra information about the page being viewed.
	 */
	private void page(Location location) {
		analytics.enqueue(PageMessage.builder(location.getFirstSegment())
				.properties(SegmentDataMapper.getAdditionalVisitParameters(location))
				.userId(getUserId()));
	}
	
	/**
	 * From Segment documentation (https://segment.com/docs/sources/server/java/quickstart/)
	 * 
	 * The track method is how you tell Segment about which actions your users are performing on your site. 
	 * Every action triggers what we call an “event”, which can also have associated properties.
	 */
	private void track(String event, long userId) {
		track(event, userId, new HashMap<String, String>());
	}
	
	/**
	 * From Segment documentation (https://segment.com/docs/sources/server/java/quickstart/)
	 * 
	 * The track method is how you tell Segment about which actions your users are performing on your site. 
	 * Every action triggers what we call an “event”, which can also have associated properties.
	 */
	private void track(String event, long userId, Map<String, ?> additionalInfo) {
		analytics.enqueue(TrackMessage.builder(event)
				.userId(Long.toString(userId))
				.properties(additionalInfo)
				);
	}
	
	/**
	 * From Segment documentation (https://segment.com/docs/sources/server/java/quickstart/)
	 * 
	 * The identify message is how you tell Segment who the current user is. 
	 * It includes a unique User ID and any optional traits you know about them
	 */
	private void identify(PathmindUser user) {
		analytics.enqueue(IdentifyMessage.builder()
				.userId(Long.toString(user.getId()))
				.traits(SegmentDataMapper.getUserInfoAsMap(user))
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
