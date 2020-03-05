package io.skymind.pathmind.ui.plugins;

import org.springframework.beans.factory.annotation.Value;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.templatemodel.TemplateModel;

import elemental.json.Json;
import elemental.json.JsonObject;
import io.skymind.pathmind.security.PathmindUserDetails;
import io.skymind.pathmind.security.SecurityUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * SegmentIntegrator component is client side counter part of <code>SegmentTrackerService</code>
 * This component runs on user browser, and tracks user event using Segment JS API 
 */
@SpringComponent
@UIScope
@Tag("segment-integrator")
@JsModule("./src/plugins/segment-integrator.js")
@Slf4j
public class SegmentIntegrator extends PolymerTemplate<SegmentIntegrator.Model> {

	private String sourceKey;
	private boolean enabled;
	private PathmindUserDetails user;

	private static final String EVENT_SIGN_UP = "Sign up";
	private static final String EVENT_LOGIN = "Login";
	private static final String EVENT_IMPORT_MODEL = "Import Model";
	private static final String EVENT_CREATE_PROJECT = "Create Project";
	private static final String EVENT_CREATE_REWARD_FUNTION = "Create Reward Function";
	private static final String EVENT_START_DISCOVERY_RUN = "Start Discovery Run";
	private static final String EVENT_START_FULL_RUN = "Start Full Run";
	private static final String EVENT_EXPORT_POLICY = "Export Policy";
	private static final String EVENT_SAVE_DRAFT = "Save Draft";
	private static final String EVENT_CHANGE_PW = "Change Password";
	private static final String EVENT_EDIT_INFO = "Edit Info";
	private static final String EVENT_ACCOUNT_UPGRADE = "Account Upgrade";
	private static final String EVENT_CANCEL_SUBSCRIPTION = "Cancel Subscription";
	private static final String EVENT_COMPLETED_GUIDE_OVERVIEW = "Completed Guide Overview";
	private static final String EVENT_COMPLETED_GUIDE_INSTALL = "Completed Guide Install Pathmind Helper";
	private static final String EVENT_COMPLETED_GUIDE_OBSERVATION = "Completed Guide Build Observation Space";
	private static final String EVENT_COMPLETED_GUIDE_ACTION_SPACE = "Completed Guide Build Action Space";
	private static final String EVENT_COMPLETED_GUIDE_TRIGGER_ACTIONS = "Completed Guide Triggering Actions";
	private static final String EVENT_COMPLETED_GUIDE_DONE = "Completed Guide Define Done Condition";
	private static final String EVENT_COMPLETED_GUIDE_REWARD = "Completed Guide Define Reward Variables";
	private static final String EVENT_COMPLETED_GUIDE_RECAP = "Completed Guide Conclusion / Recap";
	private static final String EVENT_SKIP_TO_UPLOAD_MODEL = "Skipped Guide to Upload Model";
	private static final String EVENT_UPDATED_NOTES_MODELS_VIEW = "Updated Notes on Models View";
	private static final String EVENT_UPDATED_NOTES_EXPERIMENTS_VIEW = "Updated Notes on Experiments View";
	private static final String EVENT_UPDATED_NOTES_EXPERIMENT_VIEW = "Updated Notes on Experiment View";
	private static final String EVENT_ADDED_NOTES_UPLOAD_MODEL_VIEW = "Added Notes on Upload Model View";
	private static final String EVENT_ADDED_NOTES_NEW_EXPERIMENT_VIEW = "Added Notes on New Experiment View";

	public SegmentIntegrator(@Value("${skymind.segment.website.source.key}") String key,
			@Value("${skymind.segment.enabled}") Boolean enabled) {
		this.sourceKey = key;
		this.enabled = enabled;
	}

	public void userLoggedIn() {
		track(EVENT_LOGIN);
	}

	public void userRegistered() {
		track(EVENT_SIGN_UP);
	}

	public void modelImported(boolean result) {
		JsonObject additionalInfo = Json.createObject();
		additionalInfo.put("result", result ? "success" : "failed");
		track(EVENT_IMPORT_MODEL, additionalInfo);
	}

	public void projectCreated() {
		track(EVENT_CREATE_PROJECT);
	}

	public void rewardFuntionCreated() {
		track(EVENT_CREATE_REWARD_FUNTION);
	}

	public void discoveryRunStarted() {
		track(EVENT_START_DISCOVERY_RUN);
	}

	public void fullRunStarted() {
		track(EVENT_START_FULL_RUN);
	}

	public void policyExported() {
		track(EVENT_EXPORT_POLICY);
	}

	public void draftSaved() {
		track(EVENT_SAVE_DRAFT);
	}

	public void passwordChanged() {
		track(EVENT_CHANGE_PW);
	}

	public void infoEdited() {
		track(EVENT_EDIT_INFO);
	}

	public void accountUpgraded() {
		track(EVENT_ACCOUNT_UPGRADE);
	}
	
	public void subscriptionCancelled() {
		track(EVENT_CANCEL_SUBSCRIPTION);
	}
	
	public void completedGuideOverview() {
		track(EVENT_COMPLETED_GUIDE_OVERVIEW);
	}

	public void completedGuideInstall() {
		track(EVENT_COMPLETED_GUIDE_INSTALL);
	}

	public void completedGuideObservation() {
		track(EVENT_COMPLETED_GUIDE_OBSERVATION);
	}

	public void completedGuideActionSpace() {
		track(EVENT_COMPLETED_GUIDE_ACTION_SPACE);
	}

	public void completedGuideTriggerActions() {
		track(EVENT_COMPLETED_GUIDE_TRIGGER_ACTIONS);
	}

	public void completedGuideDone() {
		track(EVENT_COMPLETED_GUIDE_DONE);
	}

	public void completedGuideReward() {
		track(EVENT_COMPLETED_GUIDE_REWARD);
	}

	public void completedGuideRecap() {
		track(EVENT_COMPLETED_GUIDE_RECAP);
	}

	public void skippedGuideToUploadModel() {
		track(EVENT_SKIP_TO_UPLOAD_MODEL);
	}

	public void updatedNotesModelsView() {
		track(EVENT_UPDATED_NOTES_MODELS_VIEW);
	}

	public void updatedNotesExperimentsView() {
		track(EVENT_UPDATED_NOTES_EXPERIMENTS_VIEW);
	}

	public void updatedNotesExperimentView() {
		track(EVENT_UPDATED_NOTES_EXPERIMENT_VIEW);
	}

	public void addedNotesUploadModelView() {
		track(EVENT_ADDED_NOTES_UPLOAD_MODEL_VIEW);
	}

	public void addedNotesNewExperimentView() {
		track(EVENT_ADDED_NOTES_NEW_EXPERIMENT_VIEW);
	}

	private void track(String event) {
		track(event, Json.createObject());
	}

	private void track(String event, JsonObject props) {
		if (enabled) {
			getElement().callJsFunction("track", event, props);
		} else {
			log.info("Segment integration is disabled, not sending " + event + " track event");
		}
	}
	private void page() {
		if (enabled) {
			getElement().callJsFunction("page");
		} else {
			log.info("Segment integration is disabled, not sending page visit");
		}
	}

	@Override
	protected void onAttach(AttachEvent attachEvent) {
		if (enabled) {
			getModel().setSourceKey(sourceKey);
			if (user == null && user != SecurityUtils.getUser()) {
				user = SecurityUtils.getUser();
				getModel().setUser(new SegmentUser(user));
			}
			page();
		} else {
			log.info("Segment integration is disabled, not sending page visit");
		}
	}

	public interface Model extends TemplateModel {
		void setSourceKey(String key);
		void setUser(SegmentUser user);
	}

}
