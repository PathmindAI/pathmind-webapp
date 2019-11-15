package io.skymind.pathmind.ui.plugins;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;

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

@SpringComponent
@UIScope
@Tag("segment-integrator")
@JsModule("./src/plugins/segment-integrator.js")
public class SegmentIntegrator extends PolymerTemplate<SegmentIntegrator.Model>{
	
	private String sourceKey;

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
	
	
	public SegmentIntegrator(@Value("${skymind.segment.key}") String key) {
		sourceKey = key;
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

	public void testRunStarted() {
		track(EVENT_START_TEST_RUN);
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
	
	private void track(String event) {
		getElement().callJsFunction("track", event);
	}
	
	private void track(String event, JsonObject props) {
		getElement().callJsFunction("track", event, props);
	}
	private void page() {
		getElement().callJsFunction("page");
	}

	@Override
	protected void onAttach(AttachEvent attachEvent) {
		getModel().setSourceKey(sourceKey);
		PathmindUserDetails user = SecurityUtils.getUser();
		if (user != null) {
			getModel().setUser(new SegmentUser(user));
		}
		page();
	}
	
	public interface Model extends TemplateModel {
		void setSourceKey(String key);
		void setUser(SegmentUser user);
	}
	
	public static class SegmentUser {
		private String id;
		private String name;
		private String email;
		
		public SegmentUser(PathmindUserDetails userDetails) {
			id = Long.toString(userDetails.getId());
			name = userDetails.getName();
			email = userDetails.getEmail();
		}
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj == null || !SegmentUser.class.isInstance(obj)) {
				return false;
			}
			SegmentUser user = SegmentUser.class.cast(obj);
			if (id == null || user.getId() == null) {
				return false;
			}
			return id.equals(user.getId());
		}
	}
}
