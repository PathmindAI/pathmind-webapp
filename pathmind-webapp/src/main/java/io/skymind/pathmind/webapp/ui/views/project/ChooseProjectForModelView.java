package io.skymind.pathmind.webapp.ui.views.project;

import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;

import org.springframework.beans.factory.annotation.Autowired;

import io.skymind.pathmind.db.dao.ProjectDAO;
import io.skymind.pathmind.shared.data.Project;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.shared.security.SecurityUtils;
import io.skymind.pathmind.webapp.exception.InvalidDataException;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;

@Route(value = Routes.CHOOSE_PROJECT_FOR_MODEL, layout = MainLayout.class)
public class ChooseProjectForModelView extends PathMindDefaultView {

    @Autowired
    private ProjectDAO projectDAO;

    @Autowired
    private SegmentIntegrator segmentIntegrator;

    private final ChooseProjectForModelViewContent chooseProjectForModelViewContent;

    private List<Project> projects;
    private Boolean addToNewProject = false;

    @Autowired
    public ChooseProjectForModelView(ChooseProjectForModelViewContent chooseProjectForModelViewContent) {
        this.chooseProjectForModelViewContent = chooseProjectForModelViewContent;
    }

    protected Component getMainContent() {
        return chooseProjectForModelViewContent;
    }

    @Override
    protected Component getTitlePanel() {
        return null;
    }

    @Override
    protected void initLoadData() throws InvalidDataException {
        projects = projectDAO.getProjectsForUser(SecurityUtils.getUserId()).stream().filter(project -> !project.isArchived()).collect(Collectors.toList());
    }

    @Override
    protected void initScreen(BeforeEnterEvent event) {
    }
    
}
