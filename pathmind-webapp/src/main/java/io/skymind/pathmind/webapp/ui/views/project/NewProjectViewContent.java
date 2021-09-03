package io.skymind.pathmind.webapp.ui.views.project;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import io.skymind.pathmind.db.dao.ProjectDAO;
import io.skymind.pathmind.shared.data.Project;
import io.skymind.pathmind.webapp.data.utils.ProjectUtils;
import io.skymind.pathmind.webapp.ui.binders.ProjectBinders;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.utils.FormUtils;
import io.skymind.pathmind.webapp.ui.views.model.UploadModelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

@Tag("new-project-view")
@JsModule("./src/pages/new-project-view-content.ts")
@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class NewProjectViewContent extends LitTemplate {

    @Id("projectName")
    private TextField projectNameTextField;

    private Binder<Project> binder;
    private Project project;
    private ProjectDAO projectDAO;
    private SegmentIntegrator segmentIntegrator;

    @Autowired
    public NewProjectViewContent(ProjectDAO projectDAO, SegmentIntegrator segmentIntegrator) {
        this.projectDAO = projectDAO;
        this.segmentIntegrator = segmentIntegrator;
        initBinder();
    }

    @ClientCallable
    private void handleNewProjectClicked() {
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
}