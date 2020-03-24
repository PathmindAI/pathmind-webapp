package io.skymind.pathmind.webapp.ui.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.FlexLayout;

public class ViewSection extends FlexLayout {

    public ViewSection() {
        setClassName("view-section");
        getStyle().set("flex-direction", "column");
    }

    public ViewSection(boolean isFlexDirectionColumn) {
        this();
        getStyle().set("flex-direction", isFlexDirectionColumn ? "column" : "row");
    }

    public ViewSection(Component... components) {
        this();
        add(components);
    }

    public ViewSection(boolean isFlexDirectionColumn, Component... components) {
        this(isFlexDirectionColumn);
        add(components);
    }

}
