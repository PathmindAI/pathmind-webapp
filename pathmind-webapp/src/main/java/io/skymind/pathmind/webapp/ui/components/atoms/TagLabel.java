package io.skymind.pathmind.webapp.ui.components.atoms;

import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;

@Tag("tag-label")
@JsModule("./src/components/atoms/tag-label.ts")
public class TagLabel extends LitTemplate implements HasStyle {
    public TagLabel() {
        this("", false, "");
    }

    public TagLabel(String text) {
        this(text, false, "");
    }

    public TagLabel(String text, Boolean outlineStyle) {
        this(text, outlineStyle, "");
    }

    public TagLabel(String text, Boolean outlineStyle, String size) {
        super();
        getElement().setProperty("text", text);
        getElement().setProperty("outline", outlineStyle);
        getElement().setProperty("size", size);
    }

    public void setText(String text) {
        getElement().setProperty("text", text);
    }

}