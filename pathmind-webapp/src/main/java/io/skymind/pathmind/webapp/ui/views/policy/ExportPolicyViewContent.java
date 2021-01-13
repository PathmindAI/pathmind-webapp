package io.skymind.pathmind.webapp.ui.views.policy;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.templatemodel.TemplateModel;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

@Tag("export-policy-view-content")
@JsModule("./src/pages/export-policy-view-content.js")
@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ExportPolicyViewContent extends PolymerTemplate<ExportPolicyViewContent.Model> {

    public ExportPolicyViewContent() {
    }

    public void setFilename(String filename) {
        getModel().setFilename(filename);
    }

    public interface Model extends TemplateModel {
        void setFilename(String filename);
    }

}