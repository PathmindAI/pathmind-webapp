package io.skymind.pathmind.webapp.ui.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.FlexLayout;

public class ViewSection extends FlexLayout {

    public ViewSection() {
        setClassName("view-section");
        getStyle().set("flex-direction", "column");
    }

    public ViewSection(Component... components) {
        this();
        add(components);
    }

}
