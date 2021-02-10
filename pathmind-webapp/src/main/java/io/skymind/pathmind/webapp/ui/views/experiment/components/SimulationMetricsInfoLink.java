package io.skymind.pathmind.webapp.ui.views.experiment.components;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.VaadinIcon;

public class SimulationMetricsInfoLink extends Anchor {
    public SimulationMetricsInfoLink() {
        add(VaadinIcon.INFO_CIRCLE_O.create());
        setHref("https://help.pathmind.com/en/articles/4305404-simulation-metrics");
        setTitle("Learn more about Simulation Metrics");
        setTarget("_blank");
        addClassName("helper-icon");
    }
}