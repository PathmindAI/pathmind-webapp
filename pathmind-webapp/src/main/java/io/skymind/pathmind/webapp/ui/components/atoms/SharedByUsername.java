package io.skymind.pathmind.webapp.ui.components.atoms;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;

@Tag("shared-by-username")
@JsModule("./src/components/atoms/shared-by-username.ts")
public class SharedByUsername extends LitTemplate {
    public SharedByUsername(String username) {
        super();
        setUsername(username);
    }

    public void setUsername(String username) {
        getElement().setProperty("username", username);
    }
}