package io.skymind.pathmind.webapp.ui.views.model;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;

@Tag("upload-model-error-view")
@JsModule("./src/pages/upload-model-error-view.ts")
public class UploadModelErrorViewContent extends LitTemplate {

    public UploadModelErrorViewContent(String contactLink) {
        super();
        getElement().setProperty("contactLink", contactLink);
    }

    public void setError(String error) {
        getElement().setProperty("error", error);
    }

}
