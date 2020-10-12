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
import io.skymind.pathmind.db.dao.PolicyDAO;
import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Run;
import io.skymind.pathmind.shared.utils.DateAndTimeUtils;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.bus.events.view.ExperimentChangedViewBusEvent;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
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
    private PolicyDAO policyDAO;

    private Experiment experiment;
	private Consumer<Experiment> selectExperimentConsumer;

    private SegmentIntegrator segmentIntegrator;

    public ExperimentsNavBarItem(ExperimentsNavBar experimentsNavbar, Supplier<Optional<UI>> getUISupplier, ExperimentDAO experimentDAO, PolicyDAO policyDAO, Experiment experiment, Consumer<Experiment> selectExperimentConsumer, SegmentIntegrator segmentIntegrator) {
        this.getUISupplier = getUISupplier;
        this.experimentsNavbar = experimentsNavbar;
        this.experimentDAO = experimentDAO;
        this.policyDAO = policyDAO;
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
        // REFACTOR -> STEPH -> load policies and other data for experiment. Should be a fully loaded experiment. This is a big part of the reason
        // why the data model objects need to be more complete and that the policies, reward variables, etc. all need to be loaded as part of the experiment.
        // REFACTOR -> STEPH -> Need some way to load everything for now because we can't just do it with experimentDAO. PolicyDAO also has multiple
        // repository calls as well as some logic.
        Experiment selectedExperiment = experimentDAO.getFullExperiment(experiment.getId()).orElseThrow(() -> new RuntimeException("I can't happen"));
        selectedExperiment.setPolicies(policyDAO.getPoliciesForExperiment(experiment.getId()));

        EventBus.post(new ExperimentChangedViewBusEvent(selectedExperiment));
        selectExperimentConsumer.accept(selectedExperiment);
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
        RunStatus overallExperimentStatus = experiment.getTrainingStatusEnum();
        getModel().setStatus(getIconStatus(overallExperimentStatus));
        getModel().setIsDraft(ExperimentUtils.isDraftRunType(experiment));
        getModel().setIsFavorite(experiment.isFavorite());
        getModel().setExperimentName(experiment.getName());
        getModel().setShowGoals(!ExperimentUtils.isDraftRunType(experiment)
                && experiment.isHasGoals()
                && experiment.isGoalsReached());
        updateGoalStatus(experiment.isGoalsReached());
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

    private String getIconStatus(RunStatus status) {
        if(ExperimentUtils.isDraftRunType(experiment))
            return "pencil";
        if (RunStatus.isRunning(status)) {
            return "loading";
        } else if (status == RunStatus.Completed) {
            return "check";
        } else if (status == RunStatus.Killed || status == RunStatus.Stopping) {
            return "stopped";
        }
        return "exclamation";
    }

    private void updateGoalStatus(Boolean goalStatus) {
        getModel().setGoalsReached(goalStatus);
    }

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
        getModel().setStatus(getIconStatus(runStatus));
    }

    public void updateExperiment(Experiment experiment) {
        this.experiment = experiment;
        updateStatus(experiment.getTrainingStatusEnum());
        updateGoalStatus(experiment.isGoalsReached());
        getModel().setIsFavorite(experiment.isFavorite());
    }

    public void updateRun(Run run) {
        experiment.updateRun(run);
        updateStatus(experiment.getTrainingStatusEnum());
        updateGoalStatus(run.getExperiment().isGoalsReached());
    }

	public interface Model extends TemplateModel {
        void setExperimentStatus(String experimentStatus);
        void setExperimentName(String experimentName);
        void setCreatedDate(String createdDate);
        void setIsCurrent(boolean isCurrent);
        void setIsDraft(boolean isDraft);
        void setIsFavorite(boolean isFavorite);
        void setStatus(String IconStatus);
        void setShowGoals(boolean showGoals);
        void setGoalsReached(boolean goalsReached);
    }
}