package io.skymind.pathmind.webapp.ui.components;

import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;

@Tag("chart-placeholder")
@JsModule("/src/experiment/chart-placeholder.js")
public class ChartPlaceholder extends PolymerTemplate<TemplateModel> implements HasStyle {
}