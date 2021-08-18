package io.skymind.pathmind.webapp.ui.views.project;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
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
@JsModule("./src/pages/choose-project-for-model-view-content.ts")
@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ChooseProjectForModelViewContent extends LitTemplate {

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
    private String createNewProjectText = "Create a New Project";

    @Autowired
    public ChooseProjectForModelViewContent(ProjectDAO projectDAO, SegmentIntegrator segmentIntegrator) {
        this.projectDAO = projectDAO;
        this.segmentIntegrator = segmentIntegrator;
        Project dummyProject = new Project();
        dummyProject.setName(createNewProjectText);
        projects = projectDAO.getProjectsForUser(SecurityUtils.getUserId())
                        .stream()
                        .filter(project -> !project.isArchived())
                        .sorted(Comparator.comparing(Project::getLastActivityDate).reversed())
                        .collect(Collectors.toList());
        projects.add(0, dummyProject);
        setupProjectDropdown();
        initBinder();
    }

    private void setupProjectDropdown() {
        projectDropdown.setRequired(true);
        projectDropdown.setItemLabelGenerator(Project::getName);
        projectDropdown.setItems(projects);
        if (projects.size() == 1) {
            projectDropdown.setValue(projects.get(0));
            getElement().setProperty("isCreateNewProject", true);
        }
    }

    @ClientCallable
    private void handleSubmitButtonClicked() {

        chosenProject = projectDropdown.getValue();

        if (chosenProject == null) {
            projectDropdown.setInvalid(true);
            return;
        }

        addToNewProject = chosenProject.getName().equals(createNewProjectText);

        if (addToNewProject) {
            projectNameTextField.setRequired(true);

            if (!FormUtils.isValidForm(binder, newProject)) {
                return;
            }
        } else {
            projectNameTextField.setRequired(false);
        }

        if (addToNewProject) {
            // final long projectId = projectDAO.createNewProject(project);
            System.out.println("Submission successful: add to new project "+newProject.getName());
        } else {
            System.out.println("Submission successful: add to existing project "+chosenProject.getName());
        }
        // getUI().ifPresent(ui -> ui.navigate(NewExperimentView.class, experimentId));

    }

    @ClientCallable
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

}