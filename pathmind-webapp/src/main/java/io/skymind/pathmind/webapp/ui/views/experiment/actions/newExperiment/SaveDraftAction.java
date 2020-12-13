package io.skymind.pathmind.webapp.ui.views.experiment.actions.newExperiment;

import io.skymind.pathmind.db.dao.ObservationDAO;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.bus.events.view.experiment.ExperimentSavedViewBusEvent;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.utils.NotificationUtils;

public class SaveDraftAction {
    public static void saveDraft(ObservationDAO observationDAO, Experiment experiment, SegmentIntegrator segmentIntegrator) {
//        EventBus.post(new ExperimentSavedViewBusEvent());
//        // TODO -> STEPH -> How do implement?
//        observationDAO.saveExperimentObservations(experiment.getId(), observationsPanel.getSelectedObservations());
//        segmentIntegrator.draftSaved();
//        disableSaveDraft();
//        NotificationUtils.showSuccess("Draft successfully saved");
//        isNeedsSaving = false;
//        afterClickedCallback.execute();

    }
}
