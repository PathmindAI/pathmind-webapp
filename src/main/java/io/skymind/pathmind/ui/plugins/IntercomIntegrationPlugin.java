package io.skymind.pathmind.ui.plugins;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.page.PendingJavaScriptResult;
import com.vaadin.flow.shared.ui.LoadMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IntercomIntegrationPlugin
{
	private static final String PROD_JS_FILE = "/frontend/js/IntercomIntegration.js";
	private final String appId;

	private IntercomIntegrationPlugin(@Value("${skymind.intercom.appId}") String appId) {
		this.appId = appId;
	}

	public void addPluginToPage() {
		final PendingJavaScriptResult result = UI.getCurrent().getPage().executeJs("window.INTERCOM_APP_ID = $0;", appId);
		result.then(jsonValue -> UI.getCurrent().getPage().addJavaScript(PROD_JS_FILE, LoadMode.EAGER));
	}
}
