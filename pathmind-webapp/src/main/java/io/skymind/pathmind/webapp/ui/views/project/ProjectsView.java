package io.skymind.pathmind.webapp.ui.views.project;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import io.skymind.pathmind.db.dao.ProjectDAO;
import io.skymind.pathmind.services.project.demo.DemoProjectService;
import io.skymind.pathmind.services.project.demo.ExperimentManifestRepository;
import io.skymind.pathmind.shared.data.Project;
import io.skymind.pathmind.shared.featureflag.Feature;
import io.skymind.pathmind.shared.featureflag.FeatureManager;
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
import io.skymind.pathmind.webapp.ui.views.demo.DemoViewContent;
import io.skymind.pathmind.webapp.utils.VaadinDateAndTimeUtils;
import io.skymind.pathmind.webapp.ui.views.project.dataprovider.ProjectGridDataProvider;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = Routes.PROJECTS, layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class ProjectsView extends PathMindDefaultView {

    @Autowired
    private ProjectGridDataProvider projectGridDataProvider;
    @Autowired
    private ProjectDAO projectDAO;
    @Autowired
    private SegmentIntegrator segmentIntegrator;

    @Autowired
    private DemoProjectService demoProjectService;

    @Autowired
    private ExperimentManifestRepository experimentManifestRepository;

    private Integer projectCount;
    private Grid<Project> projectGrid;

    private ConfigurableFilterDataProvider<Project, Void, Boolean> dataProvider;
    private FlexLayout pageWrapper;
    private ArchivesTabPanel<Project> archivesTabPanel;
    private VerticalLayout gridWrapper;
    private DemoViewContent demoViewContent;
    private Dialog demoDialog;

    private FeatureManager featureManager;

    public ProjectsView(FeatureManager featureManager) {
        super();
        this.featureManager = featureManager;

    }

    protected Component getMainContent() {
        setupProjectGrid();
        setupTabbedPanel();

        addClassName("projects-view");

        Span projectsTitle = LabelFactory.createLabel("Projects", CssPathmindStyles.SECTION_TITLE_LABEL, CssPathmindStyles.TRUNCATED_LABEL);
        Button showDemosButton = showDemosButton();

        HorizontalLayout headerWrapper = WrapperUtils.wrapLeftAndRightAligned(projectsTitle,
                WrapperUtils.wrapWidthFullRightHorizontal(new NewProjectButton(), showDemosButton));
        headerWrapper.addClassName("page-content-header");

        demoViewContent = new DemoViewContent(demoProjectService, experimentManifestRepository, segmentIntegrator);

        gridWrapper = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(
            archivesTabPanel,
            projectGrid);

        HorizontalLayout contentWrapper = WrapperUtils.wrapWidthFullHorizontal(
            gridWrapper, demoViewContent
        );
        contentWrapper.addClassName("content-wrapper");

        pageWrapper = new ViewSection(headerWrapper, contentWrapper);
        pageWrapper.addClassName("page-content");

        if (projectCount.equals(0)) {
            gridWrapper.setVisible(false);
        } else {
            demoViewContent.setIsVertical(true);
            showDemosButton.setVisible(featureManager.isEnabled(Feature.EXAMPLE_PROJECTS));
        }
        if (featureManager.isEnabled(Feature.EXAMPLE_PROJECTS)) {
            showDemosButton.setVisible(false);
            demoViewContent.setVisible(true);
        } else {
            demoViewContent.setVisible(false);
            showDemosButton.setVisible(false);
        }

        return pageWrapper;
    }

    private Button showDemosButton() {
        Button showDemosButton = new Button("Example Projects");
        showDemosButton.addClickListener(click -> {
            if (!projectCount.equals(0)) {
                demoDialog.open();
            }
        });
        return showDemosButton;
    }

    private void setupTabbedPanel() {
        archivesTabPanel = new ArchivesTabPanel<>(
                "Active",
                projectGrid,
                (project, isArchive) -> {
                    projectDAO.archive(project.getId(), isArchive);
                    project.setArchived(isArchive);
                    segmentIntegrator.archived(Project.class, isArchive);
                });
    }

    private void setupProjectGrid() {
        projectGrid = new Grid<Project>();
        projectGrid.addThemeName("projects");

        projectGrid.addColumn(Project::getName)
                .setHeader("Name")
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setResizable(true)
                .setSortProperty("name");

        projectGrid.addColumn(Project::getModelCount)
                .setHeader("Models")
                .setClassNameGenerator(column -> "align-right")
                .setFlexGrow(0)
                .setResizable(true)
                .setSortProperty("models");

        projectGrid.addComponentColumn(project ->
                new DatetimeDisplay(project.getDateCreated())
        )
                .setHeader("Created")
                .setClassNameGenerator(column -> "align-right")
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setResizable(true)
                .setSortProperty("date_created");

        Grid.Column<Project> lastActivityColumn = projectGrid.addComponentColumn(project ->
                new DatetimeDisplay(project.getLastActivityDate())
        )
                .setHeader("Last Activity")
                .setClassNameGenerator(column -> "align-right")
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setResizable(true)
                .setSortProperty("last_activity_date");

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
        projectCount = projectDAO.countProjects(SecurityUtils.getUserId());
    }

    @Override
    protected void initComponents() {
        dataProvider = projectGridDataProvider.withConfigurableFilter();
        dataProvider.setFilter(false);
        archivesTabPanel.addTabClickListener(name -> {
            dataProvider.setFilter(name.equals(archivesTabPanel.getArchivesTabName()));
            dataProvider.refreshAll();
        });
        projectGrid.setPageSize(10);
        projectGrid.setDataProvider(dataProvider);

        recalculateGridColumnWidth(getUISupplier().get().get().getPage(), projectGrid);
    }
}
