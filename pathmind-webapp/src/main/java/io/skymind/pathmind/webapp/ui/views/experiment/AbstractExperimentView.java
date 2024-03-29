package io.skymind.pathmind.webapp.ui.views.experiment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Location;

import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.db.dao.PolicyDAO;
import io.skymind.pathmind.db.dao.RunDAO;
import io.skymind.pathmind.db.dao.SimulationParameterDAO;
import io.skymind.pathmind.services.ModelService;
import io.skymind.pathmind.services.PolicyFileService;
import io.skymind.pathmind.services.TrainingService;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.SimulationParameter;
import io.skymind.pathmind.shared.data.user.UserCaps;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.shared.security.SecurityUtils;
import io.skymind.pathmind.webapp.data.utils.ExperimentGuiUtils;
import io.skymind.pathmind.webapp.exception.InvalidDataException;
import io.skymind.pathmind.webapp.security.UserService;
import io.skymind.pathmind.webapp.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import io.skymind.pathmind.webapp.ui.views.experiment.components.ExperimentComponent;
import io.skymind.pathmind.webapp.ui.views.experiment.components.experimentNotes.ExperimentNotesField;
import io.skymind.pathmind.webapp.ui.views.experiment.components.navbar.ExperimentsNavBar;
import io.skymind.pathmind.webapp.ui.views.experiment.components.simple.shared.ExperimentBreadcrumbs;
import io.skymind.pathmind.webapp.ui.views.experiment.components.simple.shared.ExperimentPanelTitle;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractExperimentView extends PathMindDefaultView implements HasUrlParameter<String> {

    protected abstract void createExperimentComponents();
    protected abstract boolean isValidViewForExperiment(BeforeEnterEvent event);

    // We have to use a lock object rather than the experiment because we are changing it's reference which makes it not thread safe. As well we cannot lock
    // on this because part of the synchronization is in the eventbus listener in a subclass (which is also why we can't use synchronize on the method).
    private final Object experimentLock = new Object();

    @Autowired
    protected ModelService modelService;
    @Autowired
    protected UserService userService;
    @Autowired
    protected ExperimentDAO experimentDAO;
    @Autowired
    protected PolicyDAO policyDAO;
    @Autowired
    protected PolicyFileService policyFileService;
    @Autowired
    protected RunDAO runDAO;
    @Autowired
    protected SimulationParameterDAO simulationParameterDAO;
    @Autowired
    protected TrainingService trainingService;
    @Autowired
    protected SegmentIntegrator segmentIntegrator;

    protected ExperimentBreadcrumbs experimentBreadcrumbs;
    protected ExperimentPanelTitle experimentPanelTitle;
    protected ExperimentsNavBar experimentsNavbar;

    protected List<ExperimentComponent> experimentComponentList = new ArrayList<>();

    // ExperimentID is also added as the experiment can be null whereas the experimentID always has to have a value. Used by the Subscribers for the view.
    protected long experimentId;
    protected Experiment experiment;

    private UserCaps userCaps;

    @Override
    protected Component getTitlePanel() {
        return new ScreenTitlePanel(experimentBreadcrumbs);
    }

    @Override
    protected void createComponents() {
        createSharedComponents();
        createExperimentComponents();
    }

    private void createSharedComponents() {
        experimentBreadcrumbs = new ExperimentBreadcrumbs(experiment);
        experimentPanelTitle = new ExperimentPanelTitle();

        experimentComponentList.add(experimentPanelTitle);
        experimentComponentList.add(experimentBreadcrumbs);

        experimentsNavbar = new ExperimentsNavBar(this, experimentDAO);
    }

    @Override
    final protected void initLoadData(BeforeEnterEvent event) {
        // We still need to lock here on load in case there is an event part way through the page's initial load.
        synchronized (experimentLock) {
            loadFullExperimentData(event);
        }
    }

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        Location location = event.getLocation();
        Long experimentIdFromParam = Long.parseLong(parameter.replaceAll(location.getQueryParameters().toString(), ""));
        this.experimentId = experimentIdFromParam;
    }

    public long getExperimentId() {
        return experimentId;
    }

    public Experiment getExperiment() {
        return experiment;
    }

    public void setExperiment(Experiment experiment) {
        setExperiment(experiment, true);
    }

    public ExperimentsNavBar getExperimentsNavbar() {
        return experimentsNavbar;
    }

    /**
     * In cases such as NarBarItemSelectExperimentAction where we want to delay updating the components as there
     * may be additional actions and logic going on. The default is set to true, that is render when experiment is set.
     */
    public void setExperiment(Experiment experiment, boolean isUpdateComponent) {
        synchronized (experimentLock) {
            this.experiment = experiment;
            this.experimentId = experiment.getId();
            loadFullExperimentData();
            if (isUpdateComponent) {
                updateComponents();
            }
        }
    }

    public void onFavoriteToggled(Boolean newIsFavorite, Experiment experiment) {
        ExperimentGuiUtils.favoriteExperiment(experimentDAO, experiment, newIsFavorite);
    }

    /**
     * This is needed by both the NewExperimentView and the ExperimentView as we can have updates from other browsers and so
     * on while setting up the experiment data.
     */
    public Object getExperimentLock() {
        return experimentLock;
    }

     /**
     * EXTREMELY IMPORTANT -> Any code that calls this method should get the experimentLock beforehand otherwise it can lead to
     * racing conditions. The lock can NOT be set in this method because the code setting up the experiment object most likely will
     * be done OUTSIDE of this method. For example the subscriber may need to update the experiment instance's runs while this method
     * is being called which would lead to a conflict.
     *
     * Updates the internal values of the experiment from the components without changing the experiment instance. This is
      * most often called when say the runs have been updated by a back end event and instead of reloading everything
      * from the database we just update the experiment instance with the latest data and then re-render the components.
     */
    public void updateExperimentFromComponents() {
        experimentComponentList.forEach(ExperimentComponent::updateExperiment);
    }

    public void updateComponents() {
        experimentComponentList.forEach(experimentComponent -> experimentComponent.setExperiment(this.experiment));
        experimentsNavbar.setVisible(!experiment.isArchived());
    }

    private void loadFullExperimentData() {
        experiment = getExperimentForUser(experimentId)
                .orElseThrow(() -> dataExceptionMessage());
    }

    private InvalidDataException dataExceptionMessage() {
        return new InvalidDataException("Attempted to access Experiment: " + experimentId);
    }

    /**
     * This is separated from initLoadData() simply because it can also be called from within our code and it's not proper to call
     * initLoadData outside of the initial page/view load.
     */
    private void loadFullExperimentData(BeforeEnterEvent event) {
        // Do a quick database check to see if the user is on the right experiment page, and if so forward them right away rather than load and render the
        // full experiment which can be expensive.
        if (getClass().getName().equals(SharedExperimentView.class.getClass().getName())) {
            // For Shared Experiment View
            loadFullExperimentData();
            return;
        }
        boolean isUsersExperiment = experimentDAO.getUserOfExperiment(experimentId).equals(userService.getCurrentUser());
        boolean isExperimentShared = experimentDAO.getExperimentIsShared(experimentId);
        if (!isUsersExperiment && isExperimentShared) {
            event.forwardTo(Routes.SHARED_EXPERIMENT, ""+experimentId);
            // After forwardTo, the BeforeEnterEvent code will not be run again
            // so we need to call the code that's essentially loadFullExperimentData() here
            experiment = experimentDAO.getExperimentForSupportIfAllowed(experimentId, SecurityUtils.getUserId())
                    .orElseThrow(() -> dataExceptionMessage());
            return;
        }
        experiment = getExperimentForUser(experimentId)
                .orElseThrow(() -> {
                    String errorMessage = "Attempted to access Experiment: ";
                    if (!isUsersExperiment && !isExperimentShared) {
                        errorMessage = "You don't have permission to access this experiment: ";
                    }
                    return new InvalidDataException(errorMessage + experimentId);
                });
    }

    /**
     * We need to override the method from PathmindDefaultView so that we can re-route if we trying to load
     * say an experiment with NewExperimentView and so on using the URL.
     */
    @Override
    protected boolean isValidView(BeforeEnterEvent event) {
        return isValidViewForExperiment(event);
    }

    @Override
    protected void initComponents() {
        updateComponents();
    }

    // Special case because in the new experiment view we add a lot of extra code therefore it's only considered a helper method.
    protected ExperimentNotesField createNotesField(Runnable segmentIntegratorRunnable, boolean allowAutoSave, boolean hideSaveButton) {
        return new ExperimentNotesField(this, experimentDAO, segmentIntegratorRunnable, allowAutoSave, hideSaveButton);
    }

    // Overridden in the SharedExperimentView so that we can get it based on the type of user (normal vs support user).
    protected Optional<Experiment> getExperimentForUser(long specificExperimentId) {
        return experimentDAO.getExperimentIfAllowed(specificExperimentId, SecurityUtils.getUserId());
    }

    public UserCaps getUserCaps() {
        return userCaps;
    }

    public void setUserCaps(int newRunDailyLimit, int newRunMonthlyLimit, int newRunNotificationThreshold) {
        this.userCaps = new UserCaps(newRunDailyLimit, newRunMonthlyLimit, newRunNotificationThreshold);
    }

    // ************************************ Methods below are needed for actions ******************************************* //
    public SegmentIntegrator getSegmentIntegrator() {
        return segmentIntegrator;
    }

    /**
     * Helper method because a ton of actions need this and this significantly simplifies the parameters.
     */
    public ExperimentDAO getExperimentDAO() {
        return experimentDAO;
    }

    public RunDAO getRunDAO() {
        return runDAO;
    }

    public List<SimulationParameter> getModelSimulationParameters() {
        return simulationParameterDAO.getSimulationParametersForModel(experiment.getModelId());
    }

    public TrainingService getTrainingService() {
        return trainingService;
    }
}
