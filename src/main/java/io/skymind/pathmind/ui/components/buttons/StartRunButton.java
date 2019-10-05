package io.skymind.pathmind.ui.components.buttons;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;

public class StartRunButton extends Button {

    public StartRunButton(String text, ComponentEventListener<ConfirmDialog.ConfirmEvent> confirmListener) {
        this (text, null, confirmListener);
    }
    public StartRunButton(String text, Component icon, ComponentEventListener<ConfirmDialog.ConfirmEvent> confirmListener) {
        super(text);

        if (icon != null) {
            this.setIcon(icon);
        }


        ConfirmDialog confirmDialog = new ConfirmDialog(
                "Confirm Run",
                "Are you sure you want to run? If you click \"Run\" It will automatically appear training progress in the training list and policy chart within 10 mins",
                "Run",
                confirmListener,
                "Cancel",
                cancelEvent -> {}
                );

        addClickListener(click -> confirmDialog.open());
    }
}
