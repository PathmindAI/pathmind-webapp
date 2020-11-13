package io.skymind.pathmind.webapp.ui.components.atoms;

import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;

@Tag("tag-label")
@JsModule("./src/components/atoms/tag-label.js")
public class TagLabel extends PolymerTemplate<TagLabel.Model> implements HasStyle {
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
        setText(text);
        getModel().setOutline(outlineStyle);
        getModel().setSize(size);
    }

    public void setText(String text) {
        getModel().setText(text);
    }

    public interface Model extends TemplateModel {
        void setText(String text);

        void setOutline(Boolean outlineStyle);

        void setSize(String size);
    }
}