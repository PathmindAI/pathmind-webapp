package io.skymind.pathmind.webapp.ui.components.atoms;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.dom.DomEventListener;

@Tag("floating-close-button")
@JsModule("/src/components/atoms/floating-close-button.ts")
public class FloatingCloseButton extends LitTemplate {
    public FloatingCloseButton() {
        this("Close");
    }

    public FloatingCloseButton(String text) {
        this(text, click -> {});
    }

    public FloatingCloseButton(String text, DomEventListener clickHandler) {
        getElement().setProperty("text", text);
        getElement().addEventListener("click", clickHandler);
    }
}
