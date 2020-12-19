package io.skymind.pathmind.webapp.ui.views.model;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;

@Tag("upload-model-error-view")
@JsModule("./src/pages/upload-model-error-view.js")
public class UploadModelErrorViewContent extends PolymerTemplate<UploadModelErrorViewContent.Model> {

    public UploadModelErrorViewContent(String contactLink) {
        super();
        getModel().setContactLink(contactLink);
    }

    public void setError(String error) {
        getModel().setError(error);
    }

    public interface Model extends TemplateModel {

        void setContactLink(String contactLink);

        void setError(String error);

    }

}
