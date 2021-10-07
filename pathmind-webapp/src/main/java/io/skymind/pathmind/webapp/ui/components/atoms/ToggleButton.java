package io.skymind.pathmind.webapp.ui.components.atoms;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.server.Command;

@Tag("toggle-button")
@JsModule("./src/components/atoms/toggle-button.ts")
public class ToggleButton extends LitTemplate {

    private Command toggleCallback = () -> {};

    public ToggleButton(String trueText, String falseText, Command callback) {
        super();
        this.toggleCallback = callback;
        getElement().setProperty("trueText", trueText);
        getElement().setProperty("falseText", falseText);
    }

    public void setToggleButtonState(boolean state) {
        getElement().setProperty("state", state);
    }

    @ClientCallable
    private void toggleHandler() {
        toggleCallback.execute();
    }
}
