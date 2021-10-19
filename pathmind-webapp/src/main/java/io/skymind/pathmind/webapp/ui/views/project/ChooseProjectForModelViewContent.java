package io.skymind.pathmind.webapp.ui.views.project;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.server.Command;
import com.vaadin.flow.spring.annotation.SpringComponent;
import io.skymind.pathmind.db.dao.ModelDAO;
import io.skymind.pathmind.db.dao.ProjectDAO;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.shared.data.Project;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.shared.security.SecurityUtils;
import io.skymind.pathmind.webapp.data.utils.ProjectUtils;
import io.skymind.pathmind.webapp.ui.binders.ProjectBinders;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.utils.FormUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

@Tag("choose-project-for-model-view-content")
@JsModule("./src/pages/choose-project-for-model-view-content.ts")
@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ChooseProjectForModelViewContent extends LitTemplate {

    private final ProjectDAO projectDAO;
    private final ModelDAO modelDAO;

    private final SegmentIntegrator segmentIntegrator;

    @Id("projectDropdown")
    private ComboBox<Project> projectDropdown;

    @Id("projectName")
    private TextField projectNameTextField;

    private Binder<Project> binder;

    private Project newProject;

    private Model model;

    private Command submitButtonCallback = () -> {};

    public ChooseProjectForModelViewContent(ProjectDAO projectDAO, ModelDAO modelDAO, SegmentIntegrator segmentIntegrator) {
        this.projectDAO = projectDAO;
        this.modelDAO = modelDAO;
        this.segmentIntegrator = segmentIntegrator;
    }

    private void setupProjectDropdown(List<Project> projects, Project project) {

        projectDropdown.setRequired(true);
        projectDropdown.setItemLabelGenerator(Project::getName);
        projectDropdown.setItems(projects);

        projectDropdown.setValue(project);

        if (projects.size() == 1) {
            projectDropdown.setValue(projects.get(0));
            getElement().setProperty("isCreateNewProject", true);
        }
    }

    @ClientCallable
    private void selectedProjectChanged() {
        String projectNotes = projectDropdown.getValue().getUserNotes();
        getElement().setProperty("projectNotes", projectNotes);
    }

    @ClientCallable
    private void handleSubmitButtonClicked() {

        Project chosenProject = projectDropdown.getValue();

        if (chosenProject == null) {
            projectDropdown.setInvalid(true);
            return;
        }

        if (chosenProject.getId() == newProject.getId()) {
            projectNameTextField.setRequired(true);
            if (!FormUtils.isValidForm(binder, newProject)) {
                return;
            }
            long newProjectId = projectDAO.createNewProject(newProject);
            chosenProject.setId(newProjectId);
        } else {
            projectNameTextField.setRequired(false);
        }

        if (chosenProject.getId() != model.getProjectId()) {
            modelDAO.assignProject(model.getId(), chosenProject.getId());

            final Project tp = chosenProject;
            getUI().ifPresent(ui -> ui.getPage().setLocation("project/" + tp.getId() + Routes.MODEL_PATH + model.getId()));
        }

        submitButtonCallback.execute();

    }

    private void initBinder() {
        binder = new Binder<>(Project.class);
        ProjectBinders.bindProjectName(binder, projectDAO, projectNameTextField);

        binder.readBean(newProject);
        binder.setBean(newProject);
    }

    public void setModel(Model model) {

        this.model = model;

        newProject = ProjectUtils.generateNewDefaultProject(new Random().nextLong() * -1, "Create a New Project");

        Project modelProject = projectDAO.getProjectIfAllowed(model.getProjectId(), SecurityUtils.getUserId()).get();

        List<Project> projects = projectDAO.getProjectsForUser(SecurityUtils.getUserId())
                .stream()
                .filter(project -> !project.isArchived())
                .sorted(Comparator.comparing(Project::getLastActivityDate).reversed())
                .collect(Collectors.toList());
        projects.add(0, newProject);

        setupProjectDropdown(projects, modelProject);
        initBinder();
    }

    public void setIsDialog(boolean isDialog) {
        getElement().setProperty("isDialog", isDialog);
    }

    public void setSubmitButtonCallback(Command callback) {
        this.submitButtonCallback = callback;
    }
}