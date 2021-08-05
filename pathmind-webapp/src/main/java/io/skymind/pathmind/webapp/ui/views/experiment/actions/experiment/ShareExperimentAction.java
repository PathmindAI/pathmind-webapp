package io.skymind.pathmind.webapp.ui.views.experiment.actions.experiment;

import java.util.function.Supplier;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.ui.components.molecules.CopyField;
import io.skymind.pathmind.webapp.ui.utils.ConfirmationUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;

public class ShareExperimentAction {

    // Locking is not really required here because there's no way to turn it off, and all it does is enable some items.
    public static void shareExperiment(ExperimentView experimentView, Supplier<Experiment> getExperimentSupplier, boolean share, Runnable callback) {

        final Experiment experiment = getExperimentSupplier.get();

        if (share) {
            ConfirmationUtils.confirmationPopupDialog(
                    "Share training publicly",
                    "This will give everyone a read-only access to the experiment.",
                    "Share Training",
                    () -> {
                        experimentView.getExperimentDAO().shareExperiment(experiment.getId(), true);
                        experiment.setShared(true);
                        createInstructionDialog(experimentView);
                        callback.run();
                    });
        } else {
            ConfirmationUtils.confirmationPopupDialog(
                    "Stop sharing training publicly",
                    "This will stop giving everyone a read-only access to the experiment.",
                    "Unshare Training",
                    () -> {
                        experimentView.getExperimentDAO().shareExperiment(experiment.getId(), false);
                        experiment.setShared(false);
                        callback.run();
                    });
        }
    }

    public static void createInstructionDialog(ExperimentView experimentView) {
        Dialog dialog = new Dialog();
        experimentView.getElement().executeJs("return window.location.href;").then(String.class, url -> {
            Div dialogContent = new Div();
            Button closeButton = new Button(VaadinIcon.CLOSE_SMALL.create());
            closeButton.addClickListener(event -> dialog.close());
            dialogContent.add(
                    new H3("Shared"),
                    new Paragraph("Share this URL with other Pathmind users."),
                    new CopyField(url.replace("experiment", "sharedExperiment"))
            );
            dialogContent.addClassName("share-with-support-instructions");
            dialog.add(dialogContent, closeButton);
            dialog.open();
        });
    }
}
