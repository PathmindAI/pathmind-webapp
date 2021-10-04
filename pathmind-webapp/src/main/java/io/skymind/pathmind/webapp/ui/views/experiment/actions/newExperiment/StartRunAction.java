package io.skymind.pathmind.webapp.ui.views.experiment.actions.newExperiment;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.bus.events.main.ExperimentStartTrainingBusEvent;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.NewExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.utils.ExperimentCapLimitVerifier;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public class StartRunAction {

    public static void startRun(NewExperimentView newExperimentView) {
        if (!ExperimentCapLimitVerifier.isUserWithinCapLimits(newExperimentView.getRunDAO(), newExperimentView.getUserCaps(), newExperimentView.getSegmentIntegrator())) {
            return;
        }

        newExperimentView.updateExperimentFromComponents();
        newExperimentView.saveAdvancedSettings();
        Experiment newExperiment = newExperimentView.getExperiment();
        final String settingsText = newExperimentView.getSettingsText();
        if (StringUtils.isNotEmpty(settingsText)) {
            String userNotes = newExperiment.getUserNotes();
            userNotes += "\n---Advanced Settings---\n" + settingsText;
            newExperiment.setUserNotes(userNotes);
        }
        newExperimentView.getExperimentDAO().saveExperiment(newExperiment);
        newExperimentView.getTrainingService().startRun(newExperiment);
        newExperimentView.getSegmentIntegrator().startTraining();
        if (!CollectionUtils.isEqualCollection(
                newExperiment.getModelObservations(),
                newExperiment.getSelectedObservations())) {
            newExperimentView.getSegmentIntegrator().observationsSelected();
        }
        EventBus.post(new ExperimentStartTrainingBusEvent(newExperiment));

        // Remove the isNeedsSaving toggle in the NewExperimentView so that the automatic saving mechanism from beforeLeave is not triggered.
        newExperimentView.removeNeedsSaving();
        newExperimentView.getUISupplier().get().ifPresent(ui -> ui.navigate(ExperimentView.class, ""+newExperiment.getId()));
    }
}
