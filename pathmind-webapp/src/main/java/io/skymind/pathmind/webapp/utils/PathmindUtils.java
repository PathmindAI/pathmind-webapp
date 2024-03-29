package io.skymind.pathmind.webapp.utils;

import io.skymind.pathmind.shared.utils.PathmindStringUtils;
import io.skymind.pathmind.webapp.ui.utils.VaadinUtils;
import io.skymind.pathmind.webapp.ui.views.model.UploadMode;

public class PathmindUtils {
    private PathmindUtils() {
    }

    /**
     * This is in a method here because it's used in both the default path as well as the Login page.
     * I've also created a helper method so that if we need to manually create the page title
     * we can keep the same standard and just add a String to the same method signature.
     */
    public static final String getPageTitle() {
        String viewName = VaadinUtils.getViewName();
        if (viewName.contains("-")) {
            viewName = PathmindStringUtils.replaceHyphenWithSpace(viewName);
            return getPageTitle(PathmindStringUtils.toCapitalize(viewName));
        }
        return getPageTitle(PathmindStringUtils.camelCaseToWords(viewName));
    }

    public static final String getPageTitle(String title) {
        return "Pathmind | " + title;
    }

    public static final String getResumeUploadModelPath(long projectId, long modelId) {
        return String.format("%s/%s/%s", projectId, UploadMode.RESUME, modelId);
    }

    public static final String getProjectModelPath(long projectId, long modelId) {
        return String.format("/project/%s/model/%s", projectId, modelId);
    }

    public static final String getProjectModelParameter(long projectId, long modelId) {
        return String.format("%s/model/%s", projectId, modelId);
    }
}
