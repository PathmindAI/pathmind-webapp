package io.skymind.pathmind.webapp.ui.views.project;

import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;

import org.springframework.beans.factory.annotation.Autowired;

import io.skymind.pathmind.db.dao.ProjectDAO;
import io.skymind.pathmind.shared.data.Project;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.shared.security.SecurityUtils;
import io.skymind.pathmind.webapp.exception.InvalidDataException;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.ViewSection;
import io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;

@Route(value = Routes.CHOOSE_PROJECT_FOR_MODEL, layout = MainLayout.class)
public class ChooseProjectForModelView extends PathMindDefaultView {

    @Autowired
    private ProjectDAO projectDAO;

    @Autowired
    private SegmentIntegrator segmentIntegrator;

    private List<Project> projects;
    private Boolean addToNewProject = false;

    private FlexLayout gridWrapper;
    private VerticalLayout selectProjectSection;
    private VerticalLayout createProjectSection;
    private Select<Project> projectSelect;
    private TextField newProjectNameTextField;
    private Button chooseProjectButton;
    private Button createProjectButton;
    private Button submitButton;

    public ChooseProjectForModelView() {
        super();
    }

    protected Component getMainContent() {

        addClassName("choose-project-view");

        Span projectsTitle = LabelFactory.createLabel("Choose the Project for Your Model", CssPathmindStyles.SECTION_TITLE_LABEL);
        Paragraph modelUploadedMessage = new Paragraph("Model uploaded. Now let's assign it to a project.");

        setupNewProjectNameTextField();
        setupSubmitButton();

        selectProjectSection = setupSelectProjectSection();
        createProjectSection = setupCreateNewProjectSection();

        gridWrapper = new ViewSection(
            projectsTitle,
            modelUploadedMessage,
            selectProjectSection,
            createProjectSection,
            submitButton);
        showSection(false);

        return gridWrapper;
    }

    private VerticalLayout setupSelectProjectSection() {
        setupProjectSelect();
        setupCreateProjectForUserButton();
        Paragraph newProjectInstructions = new Paragraph("Alternatively, you may choose to create a new project for this model.");

        VerticalLayout selectProjectSection = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(
            projectSelect,
            newProjectInstructions,
            createProjectButton
        );
        return selectProjectSection;
    }

    private VerticalLayout setupCreateNewProjectSection() {
        setupChooseProjectButton();

        VerticalLayout createNewProjectSection = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(
            newProjectNameTextField,
            chooseProjectButton
        );
        return createNewProjectSection;
    }

    private void setupProjectSelect() {
        projectSelect = new Select<>();
        projectSelect.setItems(projects);
        projectSelect.setItemLabelGenerator(Project::getName);
        projectSelect.setPlaceholder("Choose an existing project");
    }

    private void setupChooseProjectButton() {
        chooseProjectButton = new Button("Add to Existing Project", click -> {
            addToNewProject = false;
            showSection(false);
        });
        chooseProjectButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
    }

    private void setupCreateProjectForUserButton() {
        createProjectButton = new Button("Add to New Project", click -> {
            addToNewProject = true;
            showSection(true);
        });
        createProjectButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
    }

    private void setupNewProjectNameTextField() {
        newProjectNameTextField = new TextField();
        newProjectNameTextField.setLabel("New Project Name");
    }

    private void setupSubmitButton() {
        submitButton = new Button("Next");
        submitButton .addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    }

    private void showSection(Boolean showCreateNewProjectSection) {
        selectProjectSection.setVisible(!showCreateNewProjectSection);
        createProjectSection.setVisible(showCreateNewProjectSection);
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
