package io.skymind.pathmind.webapp.ui.views.experiment.actions.newExperiment;

import io.skymind.pathmind.webapp.ui.utils.NotificationUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.NewExperimentView;

public class SaveDraftAction {
    public static void saveDraft(NewExperimentView newExperimentView) {
        synchronized (newExperimentView.getExperimentLock()) {
            // update the internal values of the experiment from the components as in some cases there may not be a binder, etc.
            newExperimentView.updateExperimentFromComponents();
            newExperimentView.saveAdvancedSettings();
            newExperimentView.getExperimentDAO().saveExperiment(newExperimentView.getExperiment());
            newExperimentView.getSegmentIntegrator().draftSaved();
            newExperimentView.disableSaveNeeded();
            NotificationUtils.showNotification("Draft successfully saved");
        }
    }
}
