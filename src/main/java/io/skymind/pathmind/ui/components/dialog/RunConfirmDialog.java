package io.skymind.pathmind.ui.components.dialog;

import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;

public class RunConfirmDialog extends ConfirmDialog {
    public RunConfirmDialog() {
        this.setHeader("Starting the training…");
        this.setText(DialogContent());
        this.setConfirmButton("Okay", confirmEvent -> {});
    }

    private Div DialogContent() {
        Div dialogWrapper = new Div();
        dialogWrapper.add(
            new Paragraph("You’ll see the results as the training starts. This could take a couple minutes."),
            new Paragraph("We'll send you an email when training completes!")
        );
        return dialogWrapper;
    }
}
