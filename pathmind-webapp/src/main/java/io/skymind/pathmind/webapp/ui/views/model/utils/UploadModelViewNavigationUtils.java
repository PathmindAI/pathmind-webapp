package io.skymind.pathmind.webapp.ui.views.model.utils;

import io.skymind.pathmind.webapp.ui.views.model.UploadMode;

public class UploadModelViewNavigationUtils {
	/**
	 * Temporary solution until Vaadin adds the ability to parse multiple parameters
	 * into the view: https://github.com/vaadin/flow/issues/4213
	 */
	public static String getUploadModelParameters(Long projectId, UploadMode uploadMode) {
		return projectId + "/" + uploadMode.name().toLowerCase();
	}
	public static String getUploadModelParameters(Long projectId) {
		return Long.toString(projectId);
	}
}
