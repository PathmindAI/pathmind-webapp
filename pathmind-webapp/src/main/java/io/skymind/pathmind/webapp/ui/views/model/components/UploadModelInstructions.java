package io.skymind.pathmind.webapp.ui.views.model.components;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;

@Tag("upload-model-instructions")
@JsModule("/src/upload/upload-model-instructions.ts")
public class UploadModelInstructions extends LitTemplate {
    public UploadModelInstructions() {
        this(false);
    }

    public UploadModelInstructions(Boolean isZip) {
        super();
        setIsZip(isZip);
    }

    public void setIsZip(Boolean isZip) {
        getElement().setProperty("isZip", isZip);
    }
}
