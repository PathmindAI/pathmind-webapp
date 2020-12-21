package io.skymind.pathmind.webapp.ui.views.model.components;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;

@Tag("upload-model-instructions")
@JsModule("/src/upload/upload-model-instructions.js")
public class UploadModelInstructions extends PolymerTemplate<UploadModelInstructions.Model> {

    public UploadModelInstructions() {
        this(false);
    }

    public UploadModelInstructions(Boolean isZip) {
        super();
        setIsZip(isZip);
    }

    public void setIsZip(Boolean isZip) {
        getModel().setIsZip(isZip);
    }

    public interface Model extends TemplateModel {
        void setIsZip(Boolean isZip);
    }
}
