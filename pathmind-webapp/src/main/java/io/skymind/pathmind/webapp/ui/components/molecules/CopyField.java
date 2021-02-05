package io.skymind.pathmind.webapp.ui.components.molecules;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;

@Tag("copy-field")
@JsModule("./src/components/molecules/copy-field.js")
public class CopyField extends PolymerTemplate<CopyField.Model> {

    public CopyField(String text) {
        getModel().setText(text);
    }

    public interface Model extends TemplateModel {
        void setText(String text);
    }
}
