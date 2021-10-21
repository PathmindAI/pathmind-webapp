package io.skymind.pathmind.webapp.ui.views.model.components;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;

@Tag("upload-python-model-instructions")
@JsModule("/src/upload/upload-python-model-instructions.ts")
public class UploadPythonModelInstructions extends LitTemplate {

    public UploadPythonModelInstructions(String apiUrl, String apiToken) {
        setApiUrl(apiUrl);
        setApiToken(apiToken);
    }

    public void setApiUrl(String apiUrl) {
        getElement().setProperty("apiUrl", apiUrl);
    }

    public void setApiToken(String apiToken) {
        getElement().setProperty("apiToken", apiToken);
    }

    @ClientCallable
    private void uploadSuccessHandler() {
        System.out.println("UPLOAD SUCCESS");
    }

    @ClientCallable
    private void uploadErrorHandler() {
        System.out.println("UPLOAD ERROR");
    }

    @ClientCallable
    private void fileRejectHandler() {
        System.out.println("FILE REJECT");
    }
}
