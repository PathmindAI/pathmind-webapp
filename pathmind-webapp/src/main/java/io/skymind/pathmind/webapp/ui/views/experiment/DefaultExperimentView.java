package io.skymind.pathmind.webapp.ui.views.experiment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.db.dao.ObservationDAO;
import io.skymind.pathmind.db.dao.PolicyDAO;
import io.skymind.pathmind.db.dao.RewardVariableDAO;
import io.skymind.pathmind.db.dao.RunDAO;
import io.skymind.pathmind.db.dao.TrainingErrorDAO;
import io.skymind.pathmind.services.ModelService;
import io.skymind.pathmind.services.TrainingService;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.user.UserCaps;
import io.skymind.pathmind.shared.security.SecurityUtils;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.exception.InvalidDataException;
import io.skymind.pathmind.webapp.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import io.skymind.pathmind.webapp.ui.views.experiment.components.ExperimentComponent;
import io.skymind.pathmind.webapp.ui.views.experiment.components.experimentNotes.ExperimentNotesField;
import io.skymind.pathmind.webapp.ui.views.experiment.components.navbar.ExperimentsNavBar;
import io.skymind.pathmind.webapp.ui.views.experiment.components.simple.shared.ExperimentBreadcrumbs;
import io.skymind.pathmind.webapp.ui.views.experiment.components.simple.shared.ExperimentPanelTitle;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class DefaultExperimentView extends PathMindDefaultView implements HasUrlParameter<Long> {

    protected abstract void createExperimentComponents();
    protected abstract void updateComponentEnablements();

    @Autowired
    protected ModelService modelService;
    @Autowired
    protected ExperimentDAO experimentDAO;
    @Autowired
    protected RewardVariableDAO rewardVariableDAO;
    @Autowired
    protected PolicyDAO policyDAO;
    @Autowired
    protected RunDAO runDAO;
    @Autowired
    protected ObservationDAO observationDAO;
    @Autowired
    protected TrainingService trainingService;
    @Autowired
    protected SegmentIntegrator segmentIntegrator;
    @Autowired
    private TrainingErrorDAO trainingErrorDAO;

    protected ExperimentBreadcrumbs experimentBreadcrumbs;
    protected ExperimentPanelTitle experimentPanelTitle;
    protected ExperimentsNavBar experimentsNavbar;

    protected List<ExperimentComponent> experimentComponentList = new ArrayList<>();
    // Although this is really only for the experiment view it's a lot simpler to put it at the parent level otherwise a lot of methods would have to be overriden in ExperimentView.
    protected List<ExperimentComponent> comparisonExperimentComponents = new ArrayList<>();

    // ExperimentID is also added as the experiment can be null whereas the experimentID always has to have a value. Used by the Subscribers for the view.
    protected long experimentId;
    protected Experiment experiment;

    private UserCaps userCaps;

    @Override
    protected Component getTitlePanel() {
        return new ScreenTitlePanel(experimentBreadcrumbs);
    }

    @Override
    protected void createScreens() {
        createSharedComponents();
        createExperimentComponents();
        createComparisonComponents();
   }

    // Implemented because it's not used in NewExperimentView.
    protected void createComparisonComponents() {
    }

    private void createSharedComponents() {
        experimentBreadcrumbs = new ExperimentBreadcrumbs(experiment);
        experimentPanelTitle = new ExperimentPanelTitle();

        experimentComponentList.add(experimentPanelTitle);
        experimentComponentList.add(experimentBreadcrumbs);

        // TODO -> STEPH -> ExperimentsNavBar is special since it's not a specific experiment nor can one be set but we may still
        // need to do something so for now I'm leaving it here as a reminder to deal with later.
        experimentsNavbar = new ExperimentsNavBar(getUISupplier(), experimentDAO, policyDAO, experiment, segmentIntegrator);
    }

    @Override
    protected void initLoadData() {
        // REFACTOR -> STEPH -> #2203 -> https://github.com/SkymindIO/pathmind-webapp/issues/2203 Once we do that
        // we will no longer have to retrieve the user information when loading this page.
        // TODO -> REFACTOR -> This will probably be need to be adjusted when I'm done.
        loadFullExperimentData();
    }

    @Override
    public void setParameter(BeforeEvent event, Long experimentId) {
        this.experimentId = experimentId;
    }

    public long getExperimentId() {
        return experimentId;
    }

    public Experiment getExperiment() {
        return experiment;
    }

    // TODO -> STEPH -> We're reloading the experiment data every time here because we're assuming the data is not complete. This should be decided later as we know more
    // because it will depend on who calls it. For example should this be done in the subscriber, etc. Keep in mind we'll also need the same code for initLoadData(). Right now
    // loadExperimentData() is still loading everything, I haven't had the time to fully refactor everything.
    public void setExperiment(Experiment experiment) {
        // Update experiment instances and load full experiment data.
        // TODO -> STEPH -> this.experiment = experiment doesn't really do much right now but if we change the eventbus not to clone then this line could change. If not then remove the line.
        this.experiment = experiment;
        this.experimentId = experiment.getId();
        loadFullExperimentData();
        updateComponents();
    }

    private void updateComponents() {
        experimentComponentList.forEach(experimentComponent -> experimentComponent.setExperiment(this.experiment));
        updateComponentEnablements();
        experimentsNavbar.setVisible(!experiment.isArchived());
    }

    // TODO -> STEPH -> Not sure if this really belongs here or in the view but for now I'm putting it here. It will really depend on if we're
    // using cloned instances of objects in the eventbus or not. If we don't this code becomes way faster.
    // The reason updateComponents() and updateDetailsForExperiment() are separated is because updateDetailsForExperiment() may change and so for now it's more of a placeholder.
    public void updateDetailsForExperiment() {
        updateComponents();
    }

    private void loadFullExperimentData() {
        experiment = getExperimentForUser(experimentId)
                .orElseThrow(() -> new InvalidDataException("Attempted to access Experiment: " + experimentId));
        // TODO -> STEPH -> reload has to happen on switch experiment also. As well as on compare.
        // TODO -> STEPH -> This has to be done outside of ExperimentDAO for now because the policy also loads additional data that the experiment later requires.
        experiment.setPolicies(policyDAO.getPoliciesForExperiment(experimentId));
        // There are no extra costs if the experiment is in draft because all the values will be empty.
        ExperimentUtils.updateExperimentInternalValues(experiment, trainingErrorDAO);
    }

    public void setComparisonExperiment(Experiment comparisonExperiment) {
        comparisonExperimentComponents.forEach(comparisonExperimentComponent -> comparisonExperimentComponent.setExperiment(comparisonExperiment));
    }

    @Override
    protected void initScreen(BeforeEnterEvent event) {
        initializeComponentsWithData();
    }

    // Special case because in the new experiment view we add a lot of extra code therefore it's only considered a helper method.
    protected ExperimentNotesField createNotesField(Runnable segmentIntegratorRunnable) {
        return new ExperimentNotesField(getUISupplier(), experimentDAO, segmentIntegratorRunnable,false,true);
    }

    // TODO -> STEPH -> For now the comparison experiment components are set with the experiment when it should be null for performance reasons but
    // until the code is ready for that I'm just setting it to the current experiment.
    protected void initializeComponentsWithData() {
        updateComponents();
        setComparisonExperiment(experiment);
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
}
