package io.skymind.pathmind.webapp.ui.components.atoms;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;

@Tag("sortable-row-wrapper")
@JsModule("/src/components/atoms/sortable-row-wrapper.js")
public class SortableRowWrapper extends Component implements HasComponents {
    public SortableRowWrapper(Component component) {
        add(component);
    }
}
