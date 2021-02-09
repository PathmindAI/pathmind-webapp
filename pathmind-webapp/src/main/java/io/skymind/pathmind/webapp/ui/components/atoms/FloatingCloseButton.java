package io.skymind.pathmind.webapp.ui.components.atoms;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.server.Command;
import com.vaadin.flow.templatemodel.TemplateModel;

@Tag("floating-close-button")
@JsModule("/src/components/atoms/floating-close-button.js")
public class FloatingCloseButton extends PolymerTemplate<FloatingCloseButton.Model> {

    Command handleClick;

    public FloatingCloseButton() {
        this("Close");
    }

    public FloatingCloseButton(String text) {
        this(text, () -> {});
    }

    public FloatingCloseButton(String text, Command clickHandler) {
        getModel().setText(text);
        setClickHandler(clickHandler);
    }

    public void setClickHandler(Command clickHandler) {
        handleClick = clickHandler;
    }

    public void click() {
        getElement().callJsFunction("click");
    }

    @EventHandler
    private void onClick() {
        handleClick.execute();
    }

    public interface Model extends TemplateModel {
        void setText(String text);
    }

}
