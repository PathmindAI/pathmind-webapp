package io.skymind.pathmind.ui.components.dialog;

import com.vaadin.flow.component.confirmdialog.ConfirmDialog;

public class RunConfirmDialog extends ConfirmDialog {
    public RunConfirmDialog() {
        this.setHeader("Starting the training…");
        this.setText("You’ll see the results as the training starts. This could take a couple minutes! (We’re working on fixing this.)");
        this.setConfirmButton("Okay", confirmEvent -> {});
    }

}
