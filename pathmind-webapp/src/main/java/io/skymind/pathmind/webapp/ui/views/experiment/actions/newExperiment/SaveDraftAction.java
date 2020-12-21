package io.skymind.pathmind.webapp.ui.views.experiment.actions.newExperiment;

import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.db.dao.ObservationDAO;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.utils.NotificationUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.NewExperimentView;

public class SaveDraftAction {

    public static void saveDraft(NewExperimentView newExperimentView, ExperimentDAO experimentDAO, ObservationDAO observationDAO, Experiment experiment, SegmentIntegrator segmentIntegrator) {

        synchronized (newExperimentView.getExperimentLock()) {
            // update the internal values of the experiment from the components as in some cases there may not be a binder, etc.
            newExperimentView.updateExperimentFromComponents();

            // TODO -> REFACTOR -> https://github.com/SkymindIO/pathmind-webapp/issues/2597 Combine all the individual saves into one DAO call, that
            //  is ExperimentDAO. That way we can both have less database calls AND we only need to pass one DAO layer class to this action (and view).
            observationDAO.saveExperimentObservations(experiment.getId(), experiment.getSelectedObservations());
            experimentDAO.updateUserNotes(experiment.getId(), experiment.getUserNotes());
            experimentDAO.updateRewardFunction(experiment);

            segmentIntegrator.draftSaved();

            newExperimentView.disableSaveNeeded();
            NotificationUtils.showSuccess("Draft successfully saved");
        }
    }
}
