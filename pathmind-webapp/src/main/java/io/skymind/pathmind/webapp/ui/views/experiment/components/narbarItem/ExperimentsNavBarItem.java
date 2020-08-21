package io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Run;
import io.skymind.pathmind.shared.utils.DateAndTimeUtils;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.components.atoms.GoalsReachedStatus;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.utils.ConfirmationUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.subscribers.NavBarItemExperimentUpdatedSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.subscribers.NavBarItemRunUpdateSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.navbar.ExperimentsNavBar;
import io.skymind.pathmind.webapp.utils.VaadinDateAndTimeUtils;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Tag("experiment-navbar-item")
@JsModule("./src/experiment/experiment-navbar-item.js")
public class ExperimentsNavBarItem extends PolymerTemplate<ExperimentsNavBarItem.Model> {

    private ExperimentsNavBar experimentsNavbar;
    private Supplier<Optional<UI>> getUISupplier;
    private ExperimentDAO experimentDAO;

    private Experiment experiment;
    private GoalsReachedStatus goalStatusComponent;
	private Consumer<Experiment> selectExperimentConsumer;

    private SegmentIntegrator segmentIntegrator;

    public ExperimentsNavBarItem(Supplier<Optional<UI>> getUISupplier, ExperimentDAO experimentDAO, Experiment experiment, Consumer<Experiment> selectExperimentConsumer, SegmentIntegrator segmentIntegrator) {
        this.experimentsNavbar = experimentsNavbar;
        this.getUISupplier = getUISupplier;
        this.experimentDAO = experimentDAO;
        this.experiment = experiment;
        this.segmentIntegrator = segmentIntegrator;
        this.selectExperimentConsumer = selectExperimentConsumer;

        UI.getCurrent().getUI().ifPresent(ui -> setExperimentDetails(ui, experiment));
    }

    @EventHandler
    private void onFavoriteToggled() {
        ExperimentUtils.favoriteExperiment(experimentDAO, experiment, !experiment.isFavorite());
    }

    @EventHandler
    private void handleRowClicked() {
        selectExperimentConsumer.accept(experiment);
    }

    @EventHandler
    private void onArchiveButtonClicked() {
        ConfirmationUtils.archive("Experiment #"+experiment.getName(), () -> {
            ExperimentUtils.archiveExperiment(experimentDAO, experiment, true);
            segmentIntegrator.archived(Experiment.class, true);
            ExperimentUtils.navigateToFirstUnarchivedOrModel(getUISupplier, experimentsNavbar.getExperiments());
        });
    }

    private void setExperimentDetails(UI ui, Experiment experiment) {
        RunStatus overallExperimentStatus = ExperimentUtils.getTrainingStatus(experiment);
        getModel().setStatusIcon(getStatusIcon(overallExperimentStatus));
        getModel().setIsDraft(ExperimentUtils.isDraftRunType(experiment));
        getModel().setIsFavorite(experiment.isFavorite());
        getModel().setExperimentName(experiment.getName());
        VaadinDateAndTimeUtils.withUserTimeZoneId(ui, timeZoneId -> {
            getModel().setCreatedDate(DateAndTimeUtils.formatDateAndTimeShortFormatter(experiment.getDateCreated(), timeZoneId));
        });
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        if(experiment.isArchived())
            return;
        EventBus.subscribe(this,
                new NavBarItemExperimentUpdatedSubscriber(getUISupplier, this),
                new NavBarItemRunUpdateSubscriber(getUISupplier, this));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        EventBus.unsubscribe(this);
    }

    private String getStatusIcon(RunStatus status) {
        if(ExperimentUtils.isDraftRunType(experiment))
            return "pencil";
        if (RunStatus.isRunning(status)) {
            return("icon-loading-spinner");
        } else if (status == RunStatus.Completed) {
            return "check";
        } else if (status == RunStatus.Killed || status == RunStatus.Stopping) {
            return "icon-stopped";
        }
        return "exclamation";
    }

    // Part of this will need to be moved to the javascript part of the refactored Nav Bar Item
    // Part of this will need to be moved to the javascript part of the refactored Nav Bar Item
    // private void updateGoalStatus(Boolean goalStatus) {
    //     if (experiment.isHasGoals()) {
    //         goalStatusComponent.setValue(goalStatus);
    //         goalStatusComponent.setVisible(!ExperimentUtils.isDraftRunType(experiment));
    //     }
    // }

    public void setAsCurrent() {
        getModel().setIsCurrent(true);
    }

    public void removeAsCurrent() {
        getModel().setIsCurrent(false);
    }

    public Experiment getExperiment() {
        return experiment;
    }

    private void updateStatus(RunStatus runStatus) {
        getModel().setStatusIcon(getStatusIcon(runStatus));
    }

    public void updateExperiment(Experiment experiment) {
        this.experiment = experiment;
        updateStatus(ExperimentUtils.getTrainingStatus(experiment));
        // updateGoalStatus(experiment.isGoalsReached());
        getModel().setIsFavorite(experiment.isFavorite());
    }

    public void updateRun(Run run) {
        experiment.updateRun(run);
        updateStatus(ExperimentUtils.getTrainingStatus(experiment));
        // updateGoalStatus(run.getExperiment().isGoalsReached());
    }

	public interface Model extends TemplateModel {
        void setExperimentStatus(String experimentStatus);
        void setExperimentName(String experimentName);
        void setCreatedDate(String createdDate);
        void setIsCurrent(boolean isCurrent);
        void setIsDraft(boolean isDraft);
        void setIsFavorite(boolean isFavorite);
        void setStatusIcon(String statusIcon);
    }
}
