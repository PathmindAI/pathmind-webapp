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

    public ToggleButton(String trueText, String falseText) {
        this(trueText, falseText, () -> {});
    }

    public ToggleButton(String trueText, String falseText, Command callback) {
        super();
        getElement().setProperty("trueText", trueText);
        getElement().setProperty("falseText", falseText);
        setToggleCallback(callback);
    }

    public void setToggleButtonState(boolean state) {
        getElement().setProperty("state", state);
    }

    public boolean getToggleButtonState() {
        return Boolean.parseBoolean(getElement().getProperty("state"));
    }

    public void setToggleCallback(Command callback) {
        this.toggleCallback = callback;
    }

    @ClientCallable
    private void toggleHandler() {
        toggleCallback.execute();
    }
}
