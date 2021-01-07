package io.skymind.pathmind.webapp.ui.views.project;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import io.skymind.pathmind.db.dao.ModelDAO;
import io.skymind.pathmind.db.dao.ProjectDAO;
import io.skymind.pathmind.services.project.demo.DemoProjectService;
import io.skymind.pathmind.services.project.demo.ExperimentManifestRepository;
import io.skymind.pathmind.shared.data.Project;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.shared.security.SecurityUtils;
import io.skymind.pathmind.webapp.exception.InvalidDataException;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.ViewSection;
import io.skymind.pathmind.webapp.ui.components.archive.ArchivesTabPanel;
import io.skymind.pathmind.webapp.ui.components.atoms.DatetimeDisplay;
import io.skymind.pathmind.webapp.ui.components.buttons.NewProjectButton;
import io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import io.skymind.pathmind.webapp.ui.views.demo.DemoList;
import io.skymind.pathmind.webapp.ui.views.project.components.dialogs.RenameProjectDialog;
import io.skymind.pathmind.webapp.utils.VaadinDateAndTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = Routes.PROJECTS_URL, layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class ProjectsView extends PathMindDefaultView {
    @Autowired
    private ProjectDAO projectDAO;
    @Autowired
    private ModelDAO modelDAO;
    @Autowired
    private SegmentIntegrator segmentIntegrator;

    @Autowired
    private DemoProjectService demoProjectService;

    @Autowired
    private ExperimentManifestRepository experimentManifestRepository;

    private List<Project> projects;
    private Grid<Project> projectGrid;

    private FlexLayout gridWrapper;
    private ArchivesTabPanel<Project> archivesTabPanel;

    public ProjectsView() {
        super();
    }

    protected Component getMainContent() {
        setupProjectGrid();
        setupTabbedPanel();

        addClassName("projects-view");

        Span projectsTitle = LabelFactory.createLabel("Projects", CssPathmindStyles.SECTION_TITLE_LABEL, CssPathmindStyles.TRUNCATED_LABEL);

        HorizontalLayout headerWrapper = WrapperUtils.wrapLeftAndRightAligned(projectsTitle, new NewProjectButton());
        headerWrapper.addClassName("page-content-header");

        Paragraph demoMessage = new Paragraph("Choose a demo to start exploring Pathmind.");
        demoMessage.addClassName("demoMessage");

        gridWrapper = new ViewSection(
                headerWrapper,
                demoMessage,
                new DemoList(demoProjectService, experimentManifestRepository),
                archivesTabPanel,
                projectGrid);
        gridWrapper.addClassName("page-content");

        return gridWrapper;
    }

    private void setupTabbedPanel() {
        archivesTabPanel = new ArchivesTabPanel<>(
                "Active",
                projectGrid,
                this::getProjects,
                (project, isArchive) -> {
                    projectDAO.archive(project.getId(), isArchive);
                    project.setArchived(isArchive);
                    segmentIntegrator.archived(Project.class, isArchive);
                },
                getUISupplier());
    }

    private void setupProjectGrid() {
        projectGrid = new Grid<Project>();
        projectGrid.addThemeName("projects");

        projectGrid.addComponentColumn(project -> {
            String projectName = project.getName();
            Button renameProjectButton = new Button(new Icon(VaadinIcon.EDIT), evt -> renameProject(project));
            renameProjectButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            renameProjectButton.addClassName("action-button");
            HorizontalLayout projectNameColumn = WrapperUtils.wrapWidthFullHorizontalNoSpacingAlignCenter(
                    new Span(projectName), renameProjectButton);
            projectNameColumn.addClassName("project-name-column");
            return projectNameColumn;
        })
                .setHeader("Name")
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setResizable(true)
                .setSortable(true);

        projectGrid.addColumn(Project::getModelCount)
                .setHeader("Models")
                .setClassNameGenerator(column -> "align-right")
                .setFlexGrow(0)
                .setResizable(true)
                .setSortable(true);

        projectGrid.addComponentColumn(project ->
                new DatetimeDisplay(project.getDateCreated())
        )
                .setComparator(Comparator.comparing(Project::getDateCreated))
                .setHeader("Created")
                .setClassNameGenerator(column -> "align-right")
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setResizable(true);

        Grid.Column<Project> lastActivityColumn = projectGrid.addComponentColumn(project ->
                new DatetimeDisplay(project.getLastActivityDate())
        )
                .setComparator(Comparator.comparing(Project::getLastActivityDate))
                .setHeader("Last Activity")
                .setClassNameGenerator(column -> "align-right")
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setResizable(true);

        projectGrid.addColumn(project -> {
            String userNotes = project.getUserNotes();
            return userNotes.isEmpty() ? "—" : userNotes;
        })
                .setClassNameGenerator(column -> "grid-notes-column")
                .setHeader("Notes")
                .setResizable(true)
                .setSortable(false);

        projectGrid.sort(Arrays.asList(new GridSortOrder<>(lastActivityColumn, SortDirection.DESCENDING)));

        projectGrid.addItemClickListener(event ->
                getUI().ifPresent(ui -> ui.navigate(ProjectView.class, "" + event.getItem().getId())));
    }

    private List<Project> getProjects() {
        return projects;
    }

    private void renameProject(Project project) {
        RenameProjectDialog dialog = new RenameProjectDialog(project, projectDAO, updateProjectName -> {
            projectGrid.getDataProvider().refreshItem(project);
            // JS is used because projectGrid.recalculateColumnWidths(); does not work; probably a Vaadin Grid issue
            projectGrid.getElement().executeJs("setTimeout(() => { $0.recalculateColumnWidths(); }, 0)");
        });
        dialog.open();
    }

    @Override
    protected Component getTitlePanel() {
        return null;
    }

    @Override
    public String getPageTitle() {
        return "Pathmind | Projects";
    }

    @Override
    protected void initLoadData() throws InvalidDataException {
        projects = projectDAO.getProjectsForUser(SecurityUtils.getUserId());
        projects.stream().map(project -> {
            // We don't need the model count on any other pages of the site for now.
            // If we do need it at some point, this should not be dynamically mapped to the data object.
            Integer projectModelCount = modelDAO.getModelCountForProject(project.getId());
            project.setModelCount(projectModelCount);
            return project;
        }).collect(Collectors.toList());
    }

    @Override
    protected void initComponents() {
        VaadinDateAndTimeUtils.withUserTimeZoneId(getUISupplier(), timeZoneId -> {
            // projectGrid uses ZonedDateTimeRenderer, making sure here that time zone id is loaded properly before setting items
            projectGrid.setItems(projects);
        });
        archivesTabPanel.initData();

        recalculateGridColumnWidth(getUISupplier().get().get().getPage(), projectGrid);
    }
}
