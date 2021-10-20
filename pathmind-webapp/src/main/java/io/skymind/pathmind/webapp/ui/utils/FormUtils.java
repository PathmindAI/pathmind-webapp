package io.skymind.pathmind.webapp.ui.utils;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

public class FormUtils {
    private FormUtils() {
    }

    public static boolean isValidForm(Binder binder, Object pojo) {
        try {
            binder.writeBean(pojo);
            return true;
        } catch (ValidationException e) {
            return false;
        }
    }

    public static Button createNextStepButton() {
        Button nextStepButton = new Button("Next", new Icon(VaadinIcon.CHEVRON_RIGHT));
        nextStepButton.setIconAfterText(true);
        nextStepButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        return nextStepButton;
    }
}
