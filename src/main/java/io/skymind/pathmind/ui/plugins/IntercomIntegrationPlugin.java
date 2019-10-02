package io.skymind.pathmind.ui.plugins;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.page.PendingJavaScriptResult;
import com.vaadin.flow.shared.ui.LoadMode;
import io.skymind.pathmind.data.PathmindUser;
import io.skymind.pathmind.data.utils.PathmindUserUtils;
import io.skymind.pathmind.security.SecurityUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

// TODO -> DH -> Initially this was setup as a system environment at the request of Paul but then he seems
// to have transferred it to application.properties. With that in mind I'm leaving in the application.properties
// file however we should at some point confirm how we want to handle the dev/staging/prod values and do so consistently.
@Component
public class IntercomIntegrationPlugin
{
	private static final String INTERCOM_INTEGRATION_JS_FILE = "/frontend/js/IntercomIntegration.js";
	private final String appId;

	private IntercomIntegrationPlugin(@Value("${skymind.intercom.appId}") String appId) {
		this.appId = appId;
	}

	public void addPluginToPage() {
		PendingJavaScriptResult result = getPathmindUserInfoForIntercom();
		result.then(jsonValue -> UI.getCurrent().getPage().addJavaScript(INTERCOM_INTEGRATION_JS_FILE, LoadMode.EAGER));
	}

	/**
	 * TIPS -> Be extremely careful here with the first parameter is String concatenation. As well please note that the values cannot be empty
	 * such as name or email on the login page otherwise the Intercom Javascript will fail with a non-obvious error.
 	 */
	private PendingJavaScriptResult getPathmindUserInfoForIntercom() {
		PathmindUser user = SecurityUtils.getUser();
		return UI.getCurrent().getPage().executeJs(
				getJavascriptVariables(),
				appId,
				getFullName(user),
				getEmail(user),
				getUserId(user));
	}

	private String getFullName(PathmindUser user) {
		return user == null || StringUtils.isEmpty(PathmindUserUtils.getFullName(user)) ? "Anonymous" : PathmindUserUtils.getFullName(user);
	}

	private String getEmail(PathmindUser user) {
		return user == null || StringUtils.isEmpty(user.getEmail()) ? "Anonymous" : user.getEmail();
	}

	private String getUserId(PathmindUser user) {
		return user == null ? "-1" : Long.toString(user.getId());
	}

	private String getJavascriptVariables() {
		return "PATHMIND_APP_ID = $0; " +
					"PATHMIND_USER_NAME = $1; " +
					"PATHMIND_USER_EMAIL = $2; "+
					"PATHMIND_USER_ID = $3;";
	}
}
