package io.skymind.pathmind.webapp.ui.views.experiment.actions.newExperiment;

import io.skymind.pathmind.shared.constants.UserRole;
import io.skymind.pathmind.shared.data.PathmindUser;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.bus.events.main.ExperimentStartTrainingBusEvent;
import io.skymind.pathmind.webapp.ui.utils.NotificationUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.NewExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.components.rewardFunction.RewardFunctionEditor;
import io.skymind.pathmind.webapp.ui.views.experiment.utils.ExperimentCapLimitVerifier;

public class StartRunAction {

    public static void startRun(NewExperimentView newExperimentView, RewardFunctionEditor rewardFunctionEditor) {
        //todo refactor below DH
        PathmindUser pathmindUser = newExperimentView.getUserService().getCurrentUser();
        boolean actionMaskModel = newExperimentView.getExperiment().getModel().isActionmask();
        boolean authorized = pathmindUser.getAccountType() == UserRole.Paid;
        if (actionMaskModel && !authorized) {
            NotificationUtils.showError("you need to upgrade bro.");
            return;
        }

        if (!rewardFunctionEditor.validateBinder()) {
            return;
        }
        if (!ExperimentCapLimitVerifier.isUserWithinCapLimits(newExperimentView.getRunDAO(), newExperimentView.getUserCaps(), newExperimentView.getSegmentIntegrator())) {
            return;
        }

        newExperimentView.updateExperimentFromComponents();
        newExperimentView.getExperimentDAO().saveExperiment(newExperimentView.getExperiment());
        newExperimentView.getTrainingService().startRun(newExperimentView.getExperiment());
        newExperimentView.getSegmentIntegrator().startTraining();
        if (!org.apache.commons.collections4.CollectionUtils.isEqualCollection(
                newExperimentView.getExperiment().getModelObservations(),
                newExperimentView.getExperiment().getSelectedObservations())) {
            newExperimentView.getSegmentIntegrator().observationsSelected();
        }
        EventBus.post(new ExperimentStartTrainingBusEvent(newExperimentView.getExperiment()));

        // Remove the isNeedsSaving toggle in the NewExperimentView so that the automatic saving mechanism from beforeLeave is not triggered.
        newExperimentView.removeNeedsSaving();
        newExperimentView.getUISupplier().get().ifPresent(ui -> ui.navigate(ExperimentView.class, ""+newExperimentView.getExperiment().getId()));
    }
}
