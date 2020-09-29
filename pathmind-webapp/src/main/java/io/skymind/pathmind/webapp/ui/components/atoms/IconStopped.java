package io.skymind.pathmind.webapp.ui.components.atoms;

import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;

@Tag("icon-stopped")
@JsModule("/src/components/atoms/icon-stopped.js")
public class IconStopped extends PolymerTemplate<TemplateModel> implements HasStyle {
    public IconStopped() {
    }
}