package io.skymind.pathmind.webapp.ui.views.dashboard.components;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;

@Tag("empty-dashboard-placeholder")
@JsModule("./src/dashboard/empty-dashboard-placeholder.js")
public class EmptyDashboardPlaceholder extends PolymerTemplate<TemplateModel> {
    @Id("newProjectButton")
    private Anchor newProjectButton;

    public EmptyDashboardPlaceholder(SegmentIntegrator segmentIntegrator) {
        newProjectButton.getElement().addEventListener("click", click -> {
            segmentIntegrator.createFirstProject();
        });
    }
}
