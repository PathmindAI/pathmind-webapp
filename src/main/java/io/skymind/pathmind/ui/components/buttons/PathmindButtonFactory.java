package io.skymind.pathmind.ui.components.buttons;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import io.skymind.pathmind.ui.views.project.NewProjectView;

public class PathmindButtonFactory {

    private PathmindButtonFactory() {
    }

    public static Button getNewProjectButton() {
        return new Button("Create new project", new Icon(VaadinIcon.PLUS), click ->
            UI.getCurrent().navigate(NewProjectView.class));
    }
}
