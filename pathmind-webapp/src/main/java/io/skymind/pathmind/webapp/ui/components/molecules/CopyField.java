package io.skymind.pathmind.webapp.ui.components.molecules;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;

@Tag("copy-field")
@JsModule("./src/components/molecules/copy-field.ts")
public class CopyField extends LitTemplate {
    public CopyField(String text) {
        this(text, false);
    }

    public CopyField(String text, Boolean checkUrlReady) {
        getElement().setProperty("text", text);
        getElement().setProperty("checkUrlReady", checkUrlReady);
    }
}
