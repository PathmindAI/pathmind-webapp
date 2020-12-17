package io.skymind.pathmind.webapp.ui.views.project;

import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.templatemodel.Include;
import com.vaadin.flow.templatemodel.TemplateModel;
import io.skymind.pathmind.db.dao.ProjectDAO;
import io.skymind.pathmind.shared.data.Project;
import io.skymind.pathmind.shared.security.SecurityUtils;
import io.skymind.pathmind.webapp.data.utils.ProjectUtils;
import io.skymind.pathmind.webapp.ui.binders.ProjectBinders;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.utils.FormUtils;
import io.skymind.pathmind.webapp.ui.views.model.UploadModelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

@Tag("choose-project-for-model-view-content")
@JsModule("./src/pages/choose-project-for-model-view-content.js")
@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ChooseProjectForModelViewContent extends PolymerTemplate<ChooseProjectForModelViewContent.Model> {

    @Id("projectDropdown")
    private Select projectDropdown;

    @Id("projectName")
    private TextField projectNameTextField;

    private Binder<Project> binder;
    private Project project;
    private ProjectDAO projectDAO;
    private SegmentIntegrator segmentIntegrator;
    private List<Project> projects;

    @Autowired
    public ChooseProjectForModelViewContent(ProjectDAO projectDAO, SegmentIntegrator segmentIntegrator) {
        this.projectDAO = projectDAO;
        this.segmentIntegrator = segmentIntegrator;
        projects = projectDAO.getProjectsForUser(SecurityUtils.getUserId()).stream().filter(project -> !project.isArchived()).collect(Collectors.toList());
        setProjects(projects);
        initBinder();
    }

    @EventHandler
    private void handleSubmitButtonClicked() {
        if (!FormUtils.isValidForm(binder, project)) {
            return;
        }

        final long projectId = projectDAO.createNewProject(project);
        segmentIntegrator.projectCreated();
        getUI().ifPresent(ui -> ui.navigate(UploadModelView.class, "" + projectId));
    }

    private void initBinder() {
        binder = new Binder<>(Project.class);
        this.project = ProjectUtils.generateNewDefaultProject();

        ProjectBinders.bindProjectName(binder, projectDAO, projectNameTextField);

        // This is used because we set up MockDefaultValues through ProjectUtils above.
        binder.readBean(project);

        binder.setBean(project);
    }

    public void setProjects(List<Project> projects) {
        getModel().setProjects(projects);
    }

    public interface Model extends TemplateModel {

        @Include({ "name" })
        void setProjects(List<Project> projects);

    }
}