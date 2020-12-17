package io.skymind.pathmind.webapp.data.utils;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import com.vaadin.flow.component.UI;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.bus.events.main.ExperimentArchivedBusEvent;
import io.skymind.pathmind.webapp.bus.events.main.ExperimentFavoriteBusEvent;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.NewExperimentView;
import io.skymind.pathmind.webapp.ui.views.project.ProjectView;
import io.skymind.pathmind.webapp.utils.PathmindUtils;

public class ExperimentGuiUtils {

    private ExperimentGuiUtils() {
    }

    public static void archiveExperiment(ExperimentDAO experimentDAO, Experiment experiment, boolean isArchive) {
        experimentDAO.archive(experiment.getId(), isArchive);
        experiment.setArchived(isArchive);
        EventBus.post(new ExperimentArchivedBusEvent(experiment));
    }

    public static void favoriteExperiment(ExperimentDAO experimentDAO, Experiment experiment, boolean newIsFavorite) {
        experiment.setFavorite(newIsFavorite);
        experimentDAO.markAsFavorite(experiment.getId(), newIsFavorite);
        EventBus.post(new ExperimentFavoriteBusEvent(experiment));
    }

    public static String getIconStatus(Experiment experiment, RunStatus status) {
        if (experiment.isDraft()) {
            return "pencil";
        }
        if (RunStatus.isRunning(status)) {
            return "loading";
        } else if (status == RunStatus.Completed) {
            return "check";
        } else if (status == RunStatus.Killed || status == RunStatus.Stopping) {
            return "stopped";
        }
        return "exclamation";
    }

    // REFACTOR -> These two methods should not be in ExperimentalUtils since it has no GUI/UI code at all but I've just temporarily put them for now and will refactor
    // them as part of my bigger refactoring.
    public static void navigateToExperiment(Optional<UI> optionalUI, Experiment experiment) {
        optionalUI.ifPresent(ui -> navigateToExperiment(ui, experiment));
    }

    private static void navigateToExperiment(UI ui, Experiment experiment) {
        ui.navigate(experiment.isDraft() ? NewExperimentView.class : ExperimentView.class, experiment.getId());
    }

    public static void navigateToFirstUnarchivedOrModel(Supplier<Optional<UI>> getUISupplier, List<Experiment> experiments) {
        Optional<Experiment> firstUnarchivedExperiment = ExperimentUtils.getFirstUnarchivedExperiment(experiments);
        if (firstUnarchivedExperiment.isEmpty()) {
            getUISupplier.get().ifPresent(ui -> ui.navigate(ProjectView.class, PathmindUtils.getProjectModelParameter(experiments.get(0).getProject().getId(), experiments.get(0).getModelId())));
        } else {
            getUISupplier.get().ifPresent(ui -> navigateToExperiment(ui, firstUnarchivedExperiment.get()));
        }
    }

//    public static void updateTrainingErrorAndMessage(TrainingErrorDAO trainingErrorDAO, Experiment experiment) {
//        experiment.getRuns().stream()
//                .filter(r -> RunStatus.isError(r.getStatusEnum()))
//                .findAny()
//                .ifPresent(run ->
//                        trainingErrorDAO.getErrorById(run.getTrainingErrorId()).ifPresent(trainingError -> {
//                            experiment.setAllowRestartTraining(trainingError.isRestartable());
//                            experiment.setTrainingError(run.getRllibError() != null ? run.getRllibError() : trainingError.getDescription());
//                        }));
//    }

    // TODO -> STEPH -> This should really at the database DAO layer. That is when we load a full experiment we should just have everything and not have to remember
    //  to load extra data. However for now I'm loading it all here so that at least it's one location and we can then just move this code to the experiment
    // DAO layer.
//    public static void updateExperimentInternalValues(Experiment experiment, PolicyDAO policyDAO, TrainingErrorDAO trainingErrorDAO) {
//        experiment.setPolicies(policyDAO.getPoliciesForExperiment(experiment.getId()));
//        // TODO -> STEPH -> Not sure if updateTrainingStatus() should be in the experiment class since as it needs to be done all over the code after loading the Experiment data. So many references
//        // to updateTrainingStatus() in the code.
//        experiment.updateTrainingStatus();
//        updateBestPolicy(experiment);
//        // TODO -> STEPH -> This one just tricked me up a lot tonight and so needs to be a but more obvious or setup somewhere else. Not sure if switching experiment, update, etc. will work without it.
//        PolicyUtils.updateSimulationMetricsData(experiment.getBestPolicy());
//        PolicyUtils.updateCompareMetricsChartData(experiment.getBestPolicy());
//        // There are no extra costs if the experiment is in draft because all the values will be empty.
//        updateTrainingErrorAndMessage(trainingErrorDAO, experiment);
//        updateEarlyStopReason(experiment);
//    }
}
