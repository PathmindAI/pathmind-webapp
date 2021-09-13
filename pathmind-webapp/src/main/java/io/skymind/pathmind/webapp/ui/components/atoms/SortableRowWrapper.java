package io.skymind.pathmind.webapp.ui.components.atoms;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.server.Command;

@Tag("sortable-row-wrapper")
@JsModule("./src/components/atoms/sortable-row-wrapper.ts")
public class SortableRowWrapper extends Component implements HasComponents {

    private Command removeRowCallback = () -> {};

    public SortableRowWrapper(Component component, boolean sortable) {
        add(component);
        getElement().setProperty("sortable", sortable);
    }

    public void setRemoveRowCallback(Command callback) {
        removeRowCallback = callback;
    }

    @ClientCallable
    public void removeRow() {
        getElement().removeFromParent();
        removeRowCallback.execute();
    }
}
