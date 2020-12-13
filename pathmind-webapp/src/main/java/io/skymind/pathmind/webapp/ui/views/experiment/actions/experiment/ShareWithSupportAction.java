package io.skymind.pathmind.webapp.ui.views.experiment.actions.experiment;

import com.vaadin.flow.component.button.Button;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.ui.components.atoms.TagLabel;
import io.skymind.pathmind.webapp.ui.utils.ConfirmationUtils;

public class ShareWithSupportAction {

    public static void shareWithSupportAction(ExperimentDAO experimentDAO, Experiment experiment, TagLabel sharedWithSupportLabel, Button shareButton) {
        ConfirmationUtils.confirmationPopupDialog(
                "Share training with support",
                "This will give Pathmind a read-only mode to the experiment to help with debugging any issues.",
                "Share Training",
                () -> {
                    experimentDAO.shareExperimentWithSupport(experiment.getId());
                    experiment.setSharedWithSupport(true);
                    setSharedWithSupportComponents(sharedWithSupportLabel, shareButton, experiment);
                });

    }

    private static void setSharedWithSupportComponents(TagLabel sharedWithSupportLabel, Button shareButton, Experiment experiment) {
        sharedWithSupportLabel.setVisible(experiment.isSharedWithSupport());
        shareButton.setVisible(!experiment.isSharedWithSupport());
    }
}
