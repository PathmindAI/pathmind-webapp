package io.skymind.pathmind.webapp.ui.views.project;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.templatemodel.TemplateModel;
import io.skymind.pathmind.db.dao.ProjectDAO;
import io.skymind.pathmind.shared.data.Project;
import io.skymind.pathmind.shared.security.SecurityUtils;
import io.skymind.pathmind.webapp.data.utils.ProjectUtils;
import io.skymind.pathmind.webapp.ui.binders.ProjectBinders;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.utils.FormUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.NewExperimentView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

@Tag("choose-project-for-model-view-content")
@JsModule("./src/pages/choose-project-for-model-view-content.js")
@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ChooseProjectForModelViewContent extends PolymerTemplate<ChooseProjectForModelViewContent.Model> {

    @Id("projectDropdown")
    private ComboBox<Project> projectDropdown;

    @Id("projectName")
    private TextField projectNameTextField;

    private Binder<Project> binder;
    private Project newProject;
    private Project chosenProject;
    private ProjectDAO projectDAO;
    private SegmentIntegrator segmentIntegrator;
    private List<Project> projects;
    private Boolean addToNewProject = false;

    @Autowired
    public ChooseProjectForModelViewContent(ProjectDAO projectDAO, SegmentIntegrator segmentIntegrator) {
        this.projectDAO = projectDAO;
        this.segmentIntegrator = segmentIntegrator;
        projects = projectDAO.getProjectsForUser(SecurityUtils.getUserId())
                        .stream()
                        .filter(project -> !project.isArchived())
                        .sorted(Comparator.comparing(Project::getLastActivityDate).reversed())
                        .collect(Collectors.toList());
        setupProjectDropdown();
        initBinder();
    }

    private void setupProjectDropdown() {
        projectDropdown.setItemLabelGenerator(Project::getName);
        projectDropdown.setItems(projects);

    }

    @EventHandler
    private void handleSubmitButtonClicked() {
        addToNewProject = getModel().getShowCreateNewProjectSection();

        if (addToNewProject) {
            projectNameTextField.setRequired(true);
            projectDropdown.setRequired(false);

            if (!FormUtils.isValidForm(binder, newProject)) {
                return;
            }
        } else {
            projectNameTextField.setRequired(false);
            projectDropdown.setRequired(true);

            if (chosenProject == null) {
                projectDropdown.setInvalid(true);
                return;
            }
        }

        if (addToNewProject) {
            // final long projectId = projectDAO.createNewProject(project);
            System.out.println("Submission successful: add to new project "+newProject.getName());
        } else {
            chosenProject = projectDropdown.getValue();
            System.out.println("Submission successful: add to existing project "+chosenProject.getName());
        }
        // getUI().ifPresent(ui -> ui.navigate(NewExperimentView.class, experimentId));

    }

    @EventHandler
    private void selectOnChange() {

    }

    private void initBinder() {
        binder = new Binder<>(Project.class);
        this.newProject = ProjectUtils.generateNewDefaultProject();

        ProjectBinders.bindProjectName(binder, projectDAO, projectNameTextField);

        // This is used because we set up MockDefaultValues through ProjectUtils above.
        binder.readBean(newProject);

        binder.setBean(newProject);
    }

    public interface Model extends TemplateModel {
        Boolean getShowCreateNewProjectSection();
    }
}