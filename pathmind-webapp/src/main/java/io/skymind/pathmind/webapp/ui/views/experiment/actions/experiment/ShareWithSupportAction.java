package io.skymind.pathmind.webapp.ui.views.experiment.actions.experiment;

import java.util.function.Supplier;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.ui.components.atoms.TagLabel;
import io.skymind.pathmind.webapp.ui.components.molecules.CopyField;
import io.skymind.pathmind.webapp.ui.utils.ConfirmationUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;

public class ShareWithSupportAction {

    // Locking is not really required here because there's no way to turn it off, and all it does is enable some items.
    public static void shareWithSupport(ExperimentView experimentView, Supplier<Experiment> getExperimentSupplier, TagLabel sharedWithSupportLabel, Button shareButton) {
        ConfirmationUtils.confirmationPopupDialog(
                "Share training with support",
                "This will give Pathmind a read-only mode to the experiment to help with debugging any issues.",
                "Share Training",
                () -> {
                    Experiment experiment = getExperimentSupplier.get();
                    experimentView.getExperimentDAO().shareExperimentWithSupport(experiment.getId());
                    experiment.setSharedWithSupport(true);
                    setSharedWithSupportComponents(sharedWithSupportLabel, shareButton, experiment);
                    createInstructionDialog(experimentView);
                });
    }

    private static void setSharedWithSupportComponents(TagLabel sharedWithSupportLabel, Button shareButton, Experiment experiment) {
        sharedWithSupportLabel.setVisible(experiment.isSharedWithSupport());
        shareButton.setVisible(!experiment.isSharedWithSupport());
    }

    private static void createInstructionDialog(ExperimentView experimentView) {
        Dialog dialog = new Dialog();
        experimentView.getElement().executeJs("return window.location.href;").then(String.class, url -> {
            Div dialogContent = new Div();
            Button closeButton = new Button(VaadinIcon.CLOSE_SMALL.create());
            closeButton.addClickListener(event -> dialog.close());
            dialogContent.add(
                new H3("Shared"),
                new Paragraph("Include this URL to contact us with support."),
                new CopyField(url.replace("experiment", "sharedExperiment"))
            );
            dialogContent.addClassName("share-with-support-instructions");
            dialog.add(dialogContent, closeButton);
            dialog.open();
        });
    }
}
