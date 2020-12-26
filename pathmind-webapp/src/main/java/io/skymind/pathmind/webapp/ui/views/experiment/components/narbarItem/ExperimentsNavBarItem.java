package io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem;

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
import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.data.utils.ExperimentGuiUtils;
import io.skymind.pathmind.webapp.ui.components.atoms.DatetimeDisplay;
import io.skymind.pathmind.webapp.ui.views.experiment.DefaultExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.action.NavBarItemSelectExperimentAction;
import io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.action.NavBarItemArchiveExperimentAction;
import io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.action.NavBarItemCompareExperimentAction;
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
    private ExperimentDAO experimentDAO;

    private Experiment experiment;

    private DefaultExperimentView defaultExperimentView;

    public ExperimentsNavBarItem(ExperimentsNavBar experimentsNavbar, DefaultExperimentView defaultExperimentView, ExperimentDAO experimentDAO, Experiment experiment) {
        this.experimentsNavbar = experimentsNavbar;
        this.experimentDAO = experimentDAO;
        this.experiment = experiment;
        this.defaultExperimentView = defaultExperimentView;

        // TODO -> STEPH -> I haven't checked but is the compare button only for the ExperimentView? If not then we can just do a
        // simple if(defaultExperimentView instanceof ExperimentView) then add compare option.

        if (experiment.isDraft()) {
            experimentLink.setHref(Routes.NEW_EXPERIMENT + "/" + experiment.getId());
        } else {
            experimentLink.setHref(Routes.EXPERIMENT_URL + "/" + experiment.getId());
        }

        setExperimentDetails(experiment);
    }

    @EventHandler
    private void onFavoriteToggled() {
        ExperimentGuiUtils.favoriteExperiment(experimentDAO, experiment, !experiment.isFavorite());
    }

    @EventHandler
    private void handleRowClicked() {
        Experiment selectedExperiment = experimentDAO.getFullExperiment(experiment.getId()).orElseThrow(() -> new RuntimeException("I can't happen"));
        experimentsNavbar.setCurrentExperiment(selectedExperiment);
        NavBarItemSelectExperimentAction.selectExperiment(selectedExperiment, defaultExperimentView);
    }

    @EventHandler
    private void onArchiveButtonClicked() {
        NavBarItemArchiveExperimentAction.archiveExperiment(experiment, experimentDAO, experimentsNavbar, defaultExperimentView);
    }

    @EventHandler
    private void onCompareButtonClicked() {
        // TODO -> STEPH -> When clicked it changes the experiment rather than just change the comparison components.
        NavBarItemCompareExperimentAction.compare(experiment, (ExperimentView)defaultExperimentView, experimentDAO);
    }

    private void setExperimentDetails(Experiment experiment) {
        getModel().setIsDraft(experiment.isDraft());
        getModel().setExperimentName(experiment.getName());
        getModel().setShowGoals(!experiment.isDraft()
                && experiment.isHasGoals()
                && experiment.isGoalsReached());
        getElement().appendChild(new DatetimeDisplay(experiment.getDateCreated()).getElement());
        updateVariableComponentValues();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        if (experiment.isArchived()) {
            return;
        }
        EventBus.subscribe(this, defaultExperimentView.getUISupplier(),
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
        return ExperimentGuiUtils.getIconStatus(experiment, status);
    }

    public void setAsCurrent() {
        getModel().setIsCurrent(true);
    }

    public void removeAsCurrent() {
        getModel().setIsCurrent(false);
    }

    public void setIsOnDraftExperimentView(boolean isOnDraftExperimentView) {
        getModel().setIsOnDraftExperimentView(isOnDraftExperimentView);
    }

    public Experiment getExperiment() {
        return experiment;
    }

    public void updateExperiment(Experiment experiment) {
        this.experiment = experiment;
        updateVariableComponentValues();
    }

    public void updateVariableComponentValues() {
        getModel().setStatus(getIconStatus(experiment.getTrainingStatusEnum()));
        getModel().setStatusText(experiment.getTrainingStatusEnum().toString());
        // REFACTOR -> https://github.com/SkymindIO/pathmind-webapp/issues/2277
        getModel().setGoalsReached(experiment.isGoalsReached());
        getModel().setIsFavorite(experiment.isFavorite());
    }

    public interface Model extends TemplateModel {
        void setExperimentStatus(String experimentStatus);

        void setExperimentName(String experimentName);

        void setIsCurrent(boolean isCurrent);

        void setIsDraft(boolean isDraft);

        void setIsFavorite(boolean isFavorite);

        void setIsOnDraftExperimentView(boolean isOnDraftExperimentView);

        void setStatus(String iconStatus);

        void setStatusText(String statusText);

        void setShowGoals(boolean showGoals);

        void setGoalsReached(boolean goalsReached);
    }
}