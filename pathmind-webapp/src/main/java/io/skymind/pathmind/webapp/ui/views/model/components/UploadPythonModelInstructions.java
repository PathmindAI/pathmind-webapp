package io.skymind.pathmind.webapp.ui.views.model.components;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;

@Tag("upload-python-model-instructions")
@JsModule("/src/upload/upload-python-model-instructions.ts")
public class UploadPythonModelInstructions extends LitTemplate {

    public UploadPythonModelInstructions(String apiUrl, String apiToken, long projectId) {
        setApiUrl(apiUrl);
        setApiToken(apiToken);
        setProjectId(projectId);
    }

    private void setApiUrl(String apiUrl) {
        getElement().setProperty("apiUrl", apiUrl);
    }

    private void setApiToken(String apiToken) {
        getElement().setProperty("apiToken", apiToken);
    }

    private void setProjectId(long projectId) {
        getElement().setProperty("projectId", projectId);
    }

    @ClientCallable
    private void uploadSuccessHandler() {
        // not used right now as everything is handled in .ts
        System.out.println("UPLOAD SUCCESS");
    }

    @ClientCallable
    private void uploadErrorHandler() {
        // not used right now as everything is handled in .ts
        System.out.println("UPLOAD ERROR");
    }
}
