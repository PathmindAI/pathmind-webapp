package io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.polymertemplate.Id;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.shared.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.data.utils.ExperimentGuiUtils;
import io.skymind.pathmind.webapp.ui.components.atoms.DatetimeDisplay;
import io.skymind.pathmind.webapp.ui.views.experiment.AbstractExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.action.NavBarItemCompareExperimentAction;
import io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.action.NavBarItemSelectExperimentAction;
import io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.subscribers.main.NavBarItemExperimentFavoriteSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.subscribers.main.NavBarItemExperimentStopTrainingSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.subscribers.main.NavBarItemNotificationExperimentStartTrainingSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.subscribers.main.NavBarItemPolicyUpdateSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.subscribers.main.NavBarItemRunUpdateSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.navbar.ExperimentsNavBar;

@Tag("experiments-navbar-item")
@JsModule("./src/experiment/experiments-navbar-item.ts")
public class ExperimentsNavBarItem extends LitTemplate {

    @Id("experimentLink")
    private Anchor experimentLink;

    private ExperimentsNavBar experimentsNavbar;
    private ExperimentDAO experimentDAO;

    private Experiment experiment;
    private Object experimentLock = new Object();

    private AbstractExperimentView abstractExperimentView;

    public ExperimentsNavBarItem(ExperimentsNavBar experimentsNavbar, AbstractExperimentView abstractExperimentView, ExperimentDAO experimentDAO, Experiment experiment) {
        this.experimentsNavbar = experimentsNavbar;
        this.experimentDAO = experimentDAO;
        this.experiment = experiment;
        this.abstractExperimentView = abstractExperimentView;

        if (experiment.isDraft()) {
            experimentLink.setHref(Routes.NEW_EXPERIMENT + "/" + experiment.getId());
        } else {
            experimentLink.setHref(Routes.EXPERIMENT + "/" + experiment.getId());
        }

        setExperimentDetails(experiment);
    }

    public Object getExperimentLock() {
        return experimentLock;
    }

    @ClientCallable
    private void onFavoriteToggled() {
        ExperimentGuiUtils.favoriteExperiment(experimentDAO, experiment, !experiment.isFavorite());
    }

    @ClientCallable
    private void handleRowClicked() {
        Experiment selectedExperiment = experimentDAO.getFullExperiment(experiment.getId()).orElseThrow(() -> new RuntimeException("I can't happen"));
        experimentsNavbar.setCurrentExperiment(selectedExperiment);
        NavBarItemSelectExperimentAction.selectExperiment(selectedExperiment, abstractExperimentView);
    }

    @ClientCallable
    private void onCompareButtonClicked() {
        ExperimentView experimentView = (ExperimentView) abstractExperimentView;
        if (experimentView.isComparisonMode() && ExperimentUtils.isSameExperiment(experiment, experimentView.getComparisonExperiment())) {
            experimentView.leaveComparisonMode();
            setIsCurrentComparison(false);
        } else {
            NavBarItemCompareExperimentAction.compare(experiment, experimentView);
            experimentsNavbar.getExperimentsNavBarItems()
                    .stream()
                    .filter(navbarItem -> !ExperimentUtils.isSameExperiment(navbarItem.getExperiment(), experiment))
                    .forEach(navbarItem -> navbarItem.setIsCurrentComparison(false));
            setIsCurrentComparison(true);
        }
    }

    private void setExperimentDetails(Experiment experiment) {
        getElement().setProperty("isDraft", experiment.isDraft());
        getElement().setProperty("experimentName", experiment.getName());
        getElement().appendChild(new DatetimeDisplay(experiment.getDateCreated()).getElement());
        updateVariableComponentValues();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        if (experiment.isArchived()) {
            return;
        }
        EventBus.subscribe(this, abstractExperimentView.getUISupplier(),
                new NavBarItemExperimentFavoriteSubscriber(this),
                new NavBarItemExperimentStopTrainingSubscriber(this),
                new NavBarItemRunUpdateSubscriber(this),
                new NavBarItemPolicyUpdateSubscriber(this, experimentDAO),
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
        getElement().setProperty("isCurrent", true);
    }

    public void removeAsCurrent() {
        getElement().setProperty("isCurrent", false);
    }

    public Boolean isCurrentComparison() {
        return Boolean.parseBoolean(getElement().getProperty("isCurrentComparisonExperiment"));
    }

    public void setIsCurrentComparison(boolean isCurrentComparisonExperiment) {
        getElement().setProperty("isCurrentComparisonExperiment", isCurrentComparisonExperiment);
    }

    public void setIsOnDraftExperimentView(boolean isOnDraftExperimentView) {
        getElement().setProperty("isOnDraftExperimentView", isOnDraftExperimentView);
    }

    public Experiment getExperiment() {
        return experiment;
    }

    public void setIsFavorite(boolean isFavorite) {
        experiment.setFavorite(isFavorite);
        updateVariableComponentValues();
    }

    public void updateExperiment(Experiment experiment) {
        this.experiment = experiment;
        updateVariableComponentValues();
    }

    public void updateVariableComponentValues() {
        getElement().setProperty("isDraft", experiment.isDraft());
        getElement().setProperty("status", getIconStatus(experiment.getTrainingStatusEnum()));
        getElement().setProperty("statusText", experiment.getTrainingStatusEnum().toString());
        getElement().setProperty("isFavorite", experiment.isFavorite());
    }
}