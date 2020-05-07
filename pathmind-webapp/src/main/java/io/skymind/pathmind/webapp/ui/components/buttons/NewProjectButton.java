package io.skymind.pathmind.webapp.ui.components.buttons;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

import io.skymind.pathmind.webapp.ui.views.project.NewProjectView;

public class NewProjectButton extends Button {
    public NewProjectButton() {
        super("New Project", new Icon(VaadinIcon.PLUS));
        addClickListener(evt -> getUI().ifPresent(ui -> ui.navigate(NewProjectView.class)));
        addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    }
}
