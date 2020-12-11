package io.skymind.pathmind.webapp.ui.views.experiment;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.db.dao.ObservationDAO;
import io.skymind.pathmind.db.dao.PolicyDAO;
import io.skymind.pathmind.db.dao.RewardVariableDAO;
import io.skymind.pathmind.db.dao.RunDAO;
import io.skymind.pathmind.services.ModelService;
import io.skymind.pathmind.services.TrainingService;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.security.SecurityUtils;
import io.skymind.pathmind.webapp.exception.InvalidDataException;
import io.skymind.pathmind.webapp.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import io.skymind.pathmind.webapp.ui.views.experiment.components.ExperimentComponent;
import io.skymind.pathmind.webapp.ui.views.experiment.components.experimentNotes.ExperimentNotesField;
import io.skymind.pathmind.webapp.ui.views.experiment.components.navbar.ExperimentsNavBar;
import io.skymind.pathmind.webapp.ui.views.experiment.components.simple.ExperimentBreadcrumbs;
import io.skymind.pathmind.webapp.ui.views.experiment.components.simple.ExperimentPanelTitle;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public abstract class DefaultExperimentView extends PathMindDefaultView implements HasUrlParameter<Long> {

    protected abstract void loadExperimentData();
    protected abstract void initializeComponentsWithData();
    protected abstract List<ExperimentComponent> createExperimentComponents();

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

    protected ExperimentBreadcrumbs experimentBreadcrumbs;
    protected ExperimentPanelTitle experimentPanelTitle;
    protected ExperimentsNavBar experimentsNavbar;

    private List<ExperimentComponent> experimentComponentList;

    // ExperimentID is also added as the experiment can be null whereas the experimentID always has to have a value. Used by the Subscribers for the view.
    protected long experimentId;
    protected Experiment experiment;

    @Override
    protected Component getTitlePanel() {
        experimentBreadcrumbs = new ExperimentBreadcrumbs(experiment);
        return new ScreenTitlePanel(experimentBreadcrumbs);
    }

    @Override
    protected void createScreens() {
        experimentPanelTitle = new ExperimentPanelTitle();
        experimentsNavbar = new ExperimentsNavBar(getUISupplier(), experimentDAO, policyDAO, experiment, segmentIntegrator);

        experimentComponentList = new ArrayList<>(createExperimentComponents());
        experimentComponentList.add(experimentPanelTitle);

        createSharedComponents();


        // TODO -> STEPH -> ExperimentsNavBar is special since it's not a specific experiment nor can one be set.
//        experimentComponentList.add(experimentsNavbar);

        // Special case for the breadcrumbs
        experimentComponentList.add(experimentBreadcrumbs);
    }

    private void createSharedComponents() {
    }

    @Override
    protected void initLoadData() {
        // REFACTOR -> STEPH -> #2203 -> https://github.com/SkymindIO/pathmind-webapp/issues/2203 Once we do that
        // we will no longer have to retrieve the user information when loading this page.
        experiment = experimentDAO.getExperimentIfAllowed(experimentId, SecurityUtils.getUserId())
                .orElseThrow(() -> new InvalidDataException("Attempted to access Experiment: " + experimentId));
        loadExperimentData();
        setExperiment(experiment);
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

    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
        experimentComponentList.forEach(experimentComponent -> experimentComponent.setExperiment(experiment));
    }

    @Override
    protected void initScreen(BeforeEnterEvent event) {
        initializeComponentsWithData();
    }

    // Special case because in the new experiment view we add a lot of extra code therefore it's only considered a helper method.
    protected ExperimentNotesField createNotesField(Runnable segmentIntegratorRunnable) {
        return new ExperimentNotesField(getUISupplier(), experimentDAO, segmentIntegratorRunnable,false,true);
    }
}
