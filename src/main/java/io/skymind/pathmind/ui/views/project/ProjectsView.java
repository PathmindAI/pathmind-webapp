package io.skymind.pathmind.ui.views.project;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.db.dao.ProjectDAO;
import io.skymind.pathmind.exception.InvalidDataException;
import io.skymind.pathmind.security.Routes;
import io.skymind.pathmind.security.SecurityUtils;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.components.SearchBox;
import io.skymind.pathmind.ui.components.ViewSection;
import io.skymind.pathmind.ui.components.archive.ArchivesTabPanel;
import io.skymind.pathmind.ui.components.buttons.NewProjectButton;
import io.skymind.pathmind.ui.components.navigation.Breadcrumbs;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.renderer.ZonedDateTimeRenderer;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.ui.views.PathMindDefaultView;
import io.skymind.pathmind.ui.views.model.ModelsView;
import io.skymind.pathmind.ui.views.project.filter.ProjectFilter;
import io.skymind.pathmind.utils.DateAndTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@CssImport("./styles/styles.css")
@Route(value= Routes.PROJECTS_URL, layout = MainLayout.class)
public class ProjectsView extends PathMindDefaultView
{
	@Autowired
	private ProjectDAO projectDAO;

	private List<Project> projects;
	private Grid<Project> projectGrid;

	private ArchivesTabPanel archivesTabPanel;

	public ProjectsView() {
		super();
	}

	protected Component getMainContent()
	{
		setupProjectGrid();
		setupTabbedPanel();

		addClassName("projects-view");

		VerticalLayout gridWrapper = WrapperUtils.wrapSizeFullVertical(
					archivesTabPanel,
					new ViewSection(
						WrapperUtils.wrapWidthFullRightHorizontal(getSearchBox()),
					projectGrid
				),
				WrapperUtils.wrapWidthFullCenterHorizontal(new NewProjectButton()));
		gridWrapper.addClassName("content");
		
		return WrapperUtils.wrapSizeFullVertical(
				createBreadcrumbs(),
				gridWrapper);
	}

	private SearchBox<Project> getSearchBox() {
		return new SearchBox<Project>(projectGrid, new ProjectFilter());
	}

	private void setupTabbedPanel() {
		archivesTabPanel = new ArchivesTabPanel<Project>(
				"Projects",
				projectGrid,
				this::getProjects,
				(projectId, isArchive) -> projectDAO.archive(projectId, isArchive));
	}

	private void setupProjectGrid()
	{
		projectGrid = new Grid<Project>();

		projectGrid.addColumn(Project::getName)
				.setHeader("Name")
				.setResizable(true)
				.setSortable(true);

		projectGrid.addColumn(new ZonedDateTimeRenderer<>(Project::getDateCreated, DateAndTimeUtils.STANDARD_DATE_AND_TIME_SHORT_FOMATTER))
				.setComparator(Comparator.comparing(Project::getDateCreated))
				.setHeader("Date Created")
				.setResizable(true)
				.setSortable(true);

		Grid.Column<Project> lastActivityColumn = projectGrid.addColumn(new ZonedDateTimeRenderer<>(Project::getLastActivityDate, DateAndTimeUtils.STANDARD_DATE_AND_TIME_SHORT_FOMATTER))
				.setComparator(Comparator.comparing(Project::getLastActivityDate))
				.setHeader("Last Activity")
				.setResizable(true)
				.setSortable(true);

		projectGrid.addColumn(Project::getUserNotes)
				.setHeader("Notes")
				.setResizable(true)
				.setSortable(false);

		projectGrid.sort(Arrays.asList(new GridSortOrder<>(lastActivityColumn, SortDirection.DESCENDING)));

		projectGrid.addItemClickListener(event ->
				getUI().ifPresent(ui -> ui.navigate(ModelsView.class, event.getItem().getId())));
	}

	private List<Project> getProjects() {
		return projects;
	}

	private Breadcrumbs createBreadcrumbs() {        
		return new Breadcrumbs(null, null, null);
	}

	@Override
	protected Component getTitlePanel() {
		return new ScreenTitlePanel("PROJECTS");
	}

	@Override
	protected boolean isAccessAllowedForUser() {
		// Not needed since the loadData loads the data based on the user's id.
		return true;
	}
	@Override
	protected void initLoadData() throws InvalidDataException {
		projects = projectDAO.getProjectsForUser(SecurityUtils.getUserId());
		if(projects == null || projects.isEmpty()) {
			UI.getCurrent().navigate(NewProjectView.class);
			return;
		}
	}

	@Override
	protected void initScreen(BeforeEnterEvent event)
	{
		DateAndTimeUtils.withUserTimeZoneId(timeZoneId -> {
			// projectGrid uses ZonedDateTimeRenderer, making sure here that time zone id is loaded properly before setting items
			projectGrid.setItems(projects);
		});
		archivesTabPanel.initData();
	}
}