package io.skymind.pathmind.webapp.ui.components.atoms;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.server.Command;
import com.vaadin.flow.templatemodel.TemplateModel;

@Tag("button-with-loading")
@JsModule("./src/components/atoms/button-with-loading.js")
public class ButtonWithLoading extends PolymerTemplate<ButtonWithLoading.Model> {

    private Command handleClick;

    public ButtonWithLoading(String text, Command handleClick, Boolean rectangularStyle) {
        super();
        this.handleClick = handleClick;
        getModel().setText(text);
        getModel().setRectangularStyle(rectangularStyle);
    }

    public void setLoading(Boolean loading) {
        // for some reason using getModel would not work when
        // the user closes the dialog at the second time
        // but the javascript way works
        getElement().executeJs("this.loading = $0", loading);
    }

    public void setDisabled(Boolean disabled) {
        // for some reason using getModel would not work when
        // the user closes the dialog at the second time
        // but the javascript way works
        getElement().executeJs("this.disabled = $0", disabled);
    }

    @EventHandler
    private void onClick() {
        handleClick.execute();
    }

    public interface Model extends TemplateModel {
        void setText(String text);

        void setRectangularStyle(Boolean rectangularStyle);
    }

}
