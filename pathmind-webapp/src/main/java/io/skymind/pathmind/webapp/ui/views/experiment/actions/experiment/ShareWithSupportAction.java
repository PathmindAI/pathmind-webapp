package io.skymind.pathmind.webapp.ui.views.experiment.actions.experiment;

import java.util.function.Supplier;

import com.vaadin.flow.component.button.Button;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.ui.components.atoms.TagLabel;
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
                });
    }

    private static void setSharedWithSupportComponents(TagLabel sharedWithSupportLabel, Button shareButton, Experiment experiment) {
        sharedWithSupportLabel.setVisible(experiment.isSharedWithSupport());
        shareButton.setVisible(!experiment.isSharedWithSupport());
    }
}
