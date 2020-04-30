package io.skymind.pathmind.webapp.ui.views.project;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;

import io.skymind.pathmind.shared.data.Project;
import io.skymind.pathmind.db.dao.ProjectDAO;
import io.skymind.pathmind.webapp.exception.InvalidDataException;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.shared.security.SecurityUtils;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.webapp.ui.components.ViewSection;
import io.skymind.pathmind.webapp.ui.components.archive.ArchivesTabPanel;
import io.skymind.pathmind.webapp.ui.components.buttons.NewProjectButton;
import io.skymind.pathmind.webapp.ui.constants.CssMindPathStyles;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.renderer.ZonedDateTimeRenderer;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import io.skymind.pathmind.shared.utils.DateAndTimeUtils;
import io.skymind.pathmind.webapp.utils.VaadinDateAndTimeUtils;

@Route(value= Routes.PROJECTS_URL, layout = MainLayout.class)
public class ProjectsView extends PathMindDefaultView
{
	@Autowired
	private ProjectDAO projectDAO;

	private List<Project> projects;
	private Grid<Project> projectGrid;

	private ArchivesTabPanel<Project> archivesTabPanel;

	public ProjectsView() {
		super();
	}

	protected Component getMainContent()
	{
		setupProjectGrid();
		setupTabbedPanel();

		addClassName("projects-view");

		Span projectsTitle = LabelFactory.createLabel("Projects", CssMindPathStyles.SECTION_TITLE_LABEL, CssMindPathStyles.TRUNCATED_LABEL);

		HorizontalLayout headerWrapper = WrapperUtils.wrapLeftAndRightAligned(projectsTitle, new NewProjectButton());
		headerWrapper.addClassName("page-content-header");

		FlexLayout gridWrapper = new ViewSection(
				headerWrapper, 
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
				(projectId, isArchive) -> projectDAO.archive(projectId, isArchive));
	}

	private void setupProjectGrid()
	{
		projectGrid = new Grid<Project>();

		projectGrid.addColumn(Project::getName)
				.setHeader("Name")
				.setAutoWidth(true)
				.setFlexGrow(0)
				.setResizable(true)
				.setSortable(true);

		projectGrid.addColumn(new ZonedDateTimeRenderer<>(Project::getDateCreated, DateAndTimeUtils.STANDARD_DATE_AND_TIME_SHORT_FOMATTER))
				.setComparator(Comparator.comparing(Project::getDateCreated))
				.setHeader("Created")
				.setAutoWidth(true)
				.setFlexGrow(0)
				.setResizable(true)
				.setSortable(true);

		Grid.Column<Project> lastActivityColumn = projectGrid.addColumn(new ZonedDateTimeRenderer<>(Project::getLastActivityDate, DateAndTimeUtils.STANDARD_DATE_AND_TIME_SHORT_FOMATTER))
				.setComparator(Comparator.comparing(Project::getLastActivityDate))
				.setHeader("Last Activity")
				.setAutoWidth(true)
				.setFlexGrow(0)
				.setResizable(true)
				.setSortable(true);

		projectGrid.addColumn(project -> {
				String userNotes = project.getUserNotes();
				return userNotes.isEmpty() ? "—" : userNotes;
		})
				.setHeader("Notes")
				.setResizable(true)
				.setSortable(false);

		projectGrid.sort(Arrays.asList(new GridSortOrder<>(lastActivityColumn, SortDirection.DESCENDING)));

		projectGrid.addItemClickListener(event ->
				getUI().ifPresent(ui -> ui.navigate(ProjectView.class, event.getItem().getId())));
	}

	private List<Project> getProjects() {
		return projects;
	}

	@Override
	protected Component getTitlePanel() {
		return new ScreenTitlePanel("Projects");
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
			getUI().ifPresent(ui -> ui.navigate(NewProjectView.class));
			return;
		}
	}

	@Override
	protected void initScreen(BeforeEnterEvent event)
	{
		getUI().ifPresent(ui -> VaadinDateAndTimeUtils.withUserTimeZoneId(ui, timeZoneId -> {
			// projectGrid uses ZonedDateTimeRenderer, making sure here that time zone id is loaded properly before setting items
			projectGrid.setItems(projects);
		}));
		archivesTabPanel.initData();

		recalculateGridColumnWidth(event.getUI().getPage(), projectGrid);		
	}
}
