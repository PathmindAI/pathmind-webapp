package io.skymind.pathmind.ui.components.buttons;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;

public class StartRunButton extends Button {

    public StartRunButton(String text, ComponentEventListener<ClickEvent<Button>> clickListener) {
        this (text, null, clickListener);
    }
    public StartRunButton(String text, Component icon, ComponentEventListener<ClickEvent<Button>> clickListener) {
        super(text);

        if (icon != null) {
            this.setIcon(icon);
        }


        ConfirmDialog confirmDialog = new ConfirmDialog(
                "Starting the training…",
                "You’ll see the results as the training starts. This could take a couple minutes! (We’re working on fixing this.)",
                "Okay",
                confirmEvent -> {}
                );

        addClickListener(clickListener);
        addClickListener(click -> confirmDialog.open());
    }
}
