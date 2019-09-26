package io.skymind.pathmind.ui.plugins;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.shared.ui.LoadMode;

public class IntercomIntegrationPlugin
{
	private static final String DEV_JS_FILE = "/frontend/js/IntercomIntegration-dev.js";
	private static final String PROD_JS_FILE = "/frontend/js/IntercomIntegration.js";

	private IntercomIntegrationPlugin() {
	}

	public static void addPluginToPage() {
		UI.getCurrent().getPage().addJavaScript(getJSFile(), LoadMode.EAGER);
	}

	private static final String getJSFile() {
		try {
			switch (System.getenv("env")) {
				case "prod":
					return PROD_JS_FILE;
				case "dev":
				default:
					return DEV_JS_FILE;
			}
		} catch (NullPointerException e) {
			// This is for cases where the dev environment does not have the environment variable setup.
			return DEV_JS_FILE;
		}
	}
}
