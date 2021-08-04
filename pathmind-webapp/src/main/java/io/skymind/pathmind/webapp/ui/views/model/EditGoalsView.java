package io.skymind.pathmind.webapp.ui.views.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;

import org.springframework.beans.factory.annotation.Autowired;

import io.skymind.pathmind.db.dao.ProjectDAO;
import io.skymind.pathmind.db.dao.RewardVariableDAO;
import io.skymind.pathmind.services.ModelService;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.shared.data.Project;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.shared.security.SecurityUtils;
import io.skymind.pathmind.webapp.exception.InvalidDataException;
import io.skymind.pathmind.webapp.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.webapp.ui.components.navigation.Breadcrumbs;
import io.skymind.pathmind.webapp.ui.components.rewardVariables.RewardVariablesPanel;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import io.skymind.pathmind.webapp.ui.views.experiment.NewExperimentView;
import io.skymind.pathmind.webapp.ui.views.project.ProjectView;
import io.skymind.pathmind.webapp.utils.PathmindUtils;

@Route(value = Routes.EDIT_GOALS, layout = MainLayout.class)
public class EditGoalsView extends PathMindDefaultView implements HasUrlParameter<Long> {

    @Autowired
    private ProjectDAO projectDAO;
    @Autowired
    private ModelService modelService;
    @Autowired
    private RewardVariableDAO rewardVariablesDAO;
    @Autowired
    private SegmentIntegrator segmentIntegrator;

    private long projectId;
    private long modelId = -1;
    private Long experimentId;
    private Project project;
    private Model model;
    private List<RewardVariable> rewardVariables = new ArrayList<>();
    private RewardVariablesPanel rewardVariablesPanel;
    private H2 pageHeader = new H2();
    
    protected Component getMainContent() {
        rewardVariablesPanel = new RewardVariablesPanel(experimentId == null);
        rewardVariablesPanel.addButtonClickListener(click -> handleSaveButtonClicked());
        VerticalLayout wrapper = new VerticalLayout(
            pageHeader,
            rewardVariablesPanel
        );
        wrapper.addClassName("view-section");
        wrapper.setSpacing(false);
        addClassName("edit-goals-view");
        return wrapper;
    }

    private void handleSaveButtonClicked() {
        if (rewardVariablesPanel.canSaveChanges()) {
            segmentIntegrator.editGoals();
            rewardVariablesDAO.updateModelAndRewardVariables(model, rewardVariables);
            if (experimentId != null) {
                getUI().ifPresent(ui -> ui.navigate(NewExperimentView.class, ""+experimentId));
            } else {
                getUI().ifPresent(ui -> ui.navigate(ProjectView.class, PathmindUtils.getProjectModelParameter(projectId, modelId)));
            }
        }
    }

    private Breadcrumbs createBreadcrumbs() {
        return new Breadcrumbs(project, model);
    }

    @Override
    protected Component getTitlePanel() {
        return new ScreenTitlePanel(createBreadcrumbs());
    }

    @Override
    public void setParameter(BeforeEvent event, Long parameter) {
        this.modelId = parameter;
        Location location = event.getLocation();
        QueryParameters queryParameters = location.getQueryParameters();

        Map<String, List<String>> parametersMap = queryParameters.getParameters();
        List<String> experimentIdList = parametersMap.get("experiment");
        if (experimentIdList != null) {
            experimentId = Long.valueOf(experimentIdList.get(0));
            System.out.println(experimentId);
        }
    }

    @Override
    protected void initLoadData() throws InvalidDataException {
        this.model = modelService.getModel(modelId)
                .orElseThrow(() -> new InvalidDataException("Attempted to access model: " + modelId));
        this.projectId = model.getProjectId();
        this.rewardVariables = rewardVariablesDAO.getRewardVariablesForModel(modelId);
        project = projectDAO.getProjectIfAllowed(projectId, SecurityUtils.getUserId())
                .orElseThrow(() -> new InvalidDataException("Attempted to access project: " + projectId));
    }

    @Override
    protected void initComponents() {
        rewardVariablesPanel.setupRewardVariables(rewardVariables);
        pageHeader.setText("Goals for Model #"+model.getName());
    }
}
