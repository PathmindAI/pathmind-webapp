package io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem;

import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.db.dao.PolicyDAO;
import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.bus.events.view.experiment.ExperimentCompareViewBusEvent;
import io.skymind.pathmind.webapp.bus.events.view.experiment.ExperimentSwitchedViewBusEvent;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.components.atoms.DatetimeDisplay;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.utils.ConfirmationUtils;
import io.skymind.pathmind.webapp.ui.utils.NotificationUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.subscribers.main.NavBarItemExperimentFavoriteSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.subscribers.main.NavBarItemExperimentUpdatedSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.subscribers.main.NavBarItemNotificationExperimentStartTrainingSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.subscribers.main.NavBarItemRunUpdateSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.navbar.ExperimentsNavBar;

@Tag("experiment-navbar-item")
@JsModule("./src/experiment/experiment-navbar-item.js")
public class ExperimentsNavBarItem extends PolymerTemplate<ExperimentsNavBarItem.Model> {

    @Id("experimentLink")
    private Anchor experimentLink;

    private ExperimentsNavBar experimentsNavbar;
    private Supplier<Optional<UI>> getUISupplier;
    private ExperimentDAO experimentDAO;
    private PolicyDAO policyDAO;

    private Experiment experiment;
    private SegmentIntegrator segmentIntegrator;

    public ExperimentsNavBarItem(ExperimentsNavBar experimentsNavbar, Supplier<Optional<UI>> getUISupplier, ExperimentDAO experimentDAO, PolicyDAO policyDAO, Experiment experiment, SegmentIntegrator segmentIntegrator) {
        this.getUISupplier = getUISupplier;
        this.experimentsNavbar = experimentsNavbar;
        this.experimentDAO = experimentDAO;
        this.policyDAO = policyDAO;
        this.experiment = experiment;
        this.segmentIntegrator = segmentIntegrator;

        if (ExperimentUtils.isDraftRunType(experiment)) {
            experimentLink.setHref(Routes.NEW_EXPERIMENT + "/" + experiment.getId());
        } else {
            experimentLink.setHref(Routes.EXPERIMENT_URL + "/" + experiment.getId());
        }

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

        if (experiment.isDraft() == selectedExperiment.isDraft()) {
            // this is to prevent emission when the view has to be changed
            // e.g. from Experiment View to New Experiment View
            EventBus.post(new ExperimentSwitchedViewBusEvent(selectedExperiment));
        }
    }

    @EventHandler
    private void onArchiveButtonClicked() {
        // TODO -> STEPH -> Fix this code.
        // TODO -> STEPH -> False if we want to disable compare.
        // TODO -> STEPH -> Eventhandler will be on navbar rather than item because that would be too many events for nothing.
       boolean isVisible = new Random().nextBoolean();
       System.out.println("Visible: " + isVisible);
       ConfirmationUtils.archive("Experiment #" + experiment.getName(), () -> {
           ExperimentUtils.archiveExperiment(experimentDAO, experiment, true);
           segmentIntegrator.archived(Experiment.class, true);
           ExperimentUtils.navigateToFirstUnarchivedOrModel(getUISupplier, experimentsNavbar.getExperiments());
       });
    }

    @EventHandler
    private void onCompareButtonClicked() {
        if(experiment.isDraft()) {
            NotificationUtils.showError("Cannot compare draft experiment<br>Option shouldn't be available rather than error message");
        }
        EventBus.post(new ExperimentCompareViewBusEvent(experiment, true));
    }

    private void setExperimentDetails(UI ui, Experiment experiment) {
        RunStatus overallExperimentStatus = experiment.getTrainingStatusEnum();
        getModel().setStatus(getIconStatus(overallExperimentStatus));
        getModel().setStatusText(overallExperimentStatus.toString());
        getModel().setIsDraft(ExperimentUtils.isDraftRunType(experiment));
        getModel().setIsFavorite(experiment.isFavorite());
        getModel().setExperimentName(experiment.getName());
        getModel().setShowGoals(!ExperimentUtils.isDraftRunType(experiment)
                && experiment.isHasGoals()
                && experiment.isGoalsReached());
        updateGoalStatus(experiment.isGoalsReached());
        getElement().appendChild(new DatetimeDisplay(experiment.getDateCreated()).getElement());
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        if (experiment.isArchived()) {
            return;
        }
        EventBus.subscribe(this, getUISupplier,
                new NavBarItemExperimentFavoriteSubscriber(this),
                new NavBarItemExperimentUpdatedSubscriber(this),
                new NavBarItemRunUpdateSubscriber(this),
                new NavBarItemNotificationExperimentStartTrainingSubscriber(this));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        EventBus.unsubscribe(this);
    }

    private String getIconStatus(RunStatus status) {
        return ExperimentUtils.getIconStatus(experiment, status);
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
        getModel().setStatusText(runStatus.toString());
    }

    public void updateExperiment(Experiment experiment) {
        this.experiment = experiment;
        update();
    }

    public void update() {
        experiment.updateTrainingStatus();
        updateStatus(experiment.getTrainingStatusEnum());
        // REFACTOR -> https://github.com/SkymindIO/pathmind-webapp/issues/2277
        updateGoalStatus(experiment.isGoalsReached());
        getModel().setIsFavorite(experiment.isFavorite());
    }

    public interface Model extends TemplateModel {
        void setExperimentStatus(String experimentStatus);

        void setExperimentName(String experimentName);

        void setIsCurrent(boolean isCurrent);

        void setIsDraft(boolean isDraft);

        void setIsFavorite(boolean isFavorite);

        void setStatus(String iconStatus);

        void setStatusText(String statusText);

        void setShowGoals(boolean showGoals);

        void setGoalsReached(boolean goalsReached);
    }
}