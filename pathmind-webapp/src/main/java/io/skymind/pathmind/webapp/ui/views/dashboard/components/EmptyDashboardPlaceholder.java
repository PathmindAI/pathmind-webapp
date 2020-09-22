package io.skymind.pathmind.webapp.ui.views.dashboard.components;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;

@Tag("empty-dashboard-placeholder")
@JsModule("./src/dashboard/empty-dashboard-placeholder.js")
public class EmptyDashboardPlaceholder extends PolymerTemplate<TemplateModel> {
    public EmptyDashboardPlaceholder() {
    }
}
