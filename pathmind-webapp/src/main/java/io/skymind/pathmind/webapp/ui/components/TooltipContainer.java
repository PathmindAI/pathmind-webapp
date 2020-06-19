package io.skymind.pathmind.webapp.ui.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class TooltipContainer extends HorizontalLayout {
    public TooltipContainer(String text, String tooltipText, Component... components) {
        add(new Span(text));
        add(components);
        getElement().setAttribute("tooltip-content", tooltipText);
        addAttachListener(evt -> {
            evt.getSource().getElement().executeJs("setTimeout(function(){if ($0.hasAttribute('tooltip-content') && $0.querySelector('span').scrollWidth === $0.querySelector('span').clientWidth) { $0.removeAttribute('tooltip-content'); }}, 0);");
        });
    }
}