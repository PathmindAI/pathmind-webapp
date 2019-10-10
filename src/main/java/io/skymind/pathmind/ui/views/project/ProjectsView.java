package io.skymind.pathmind.ui.views.project;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.db.dao.ProjectDAO;
import io.skymind.pathmind.exception.InvalidDataException;
import io.skymind.pathmind.security.SecurityUtils;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.components.SearchBox;
import io.skymind.pathmind.ui.components.ViewSection;
import io.skymind.pathmind.ui.components.archive.ArchivesTabPanel;
import io.skymind.pathmind.ui.components.buttons.NewProjectButton;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.utils.UIConstants;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.ui.views.PathMindDefaultView;
import io.skymind.pathmind.ui.views.model.ModelsView;
import io.skymind.pathmind.ui.views.project.filter.ProjectFilter;
import io.skymind.pathmind.utils.DateAndTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value="projects", layout = MainLayout.class)
public class ProjectsView extends PathMindDefaultView
{
	@Autowired
	private ProjectDAO projectDAO;

	private List<Project> projects;
	private Grid<Project> projectGrid;

	public ProjectsView()
	{
		super();
	}

	protected Component getMainContent()
	{
		setupProjectGrid();
		addClassName("projects-view");

		VerticalLayout gridWrapper = WrapperUtils.wrapSizeFullVertical(
				new ViewSection(
					WrapperUtils.wrapWidthFullRightHorizontal(getSearchBox()),
					getTabbedPanel(),
					projectGrid
				),
				WrapperUtils.wrapWidthFullCenterHorizontal(new NewProjectButton()));
		return gridWrapper;
	}

	private SearchBox<Project> getSearchBox() {
		return new SearchBox<Project>(projectGrid, new ProjectFilter());
	}

	private ArchivesTabPanel getTabbedPanel() {
		return new ArchivesTabPanel<Project>(
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
				.setSortable(true);
		projectGrid.addColumn(new LocalDateTimeRenderer<>(Project::getDateCreated, DateAndTimeUtils.STANDARD_DATE_ONLY_FOMATTER))
				.setHeader("Date Created")
				.setSortable(true);
		projectGrid.addColumn(new LocalDateTimeRenderer<>(Project::getLastActivityDate, DateAndTimeUtils.STANDARD_DATE_ONLY_FOMATTER))
				.setHeader("Last Activity")
				.setSortable(true);

		projectGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
		projectGrid.addSelectionListener(event ->
				event.getFirstSelectedItem().ifPresent(selectedProject ->
						UI.getCurrent().navigate(ModelsView.class, selectedProject.getId())));
	}

	private List<Project> getProjects() {
		return projects;
	}

	@Override
	protected Component getTitlePanel() {
		return new ScreenTitlePanel("PROJECTS");
	}

	@Override
	protected void loadData() throws InvalidDataException {
		projects = projectDAO.getProjectsForUser(SecurityUtils.getUserId());
		if(projects == null || projects.isEmpty()) {
			UI.getCurrent().navigate(NewProjectView.class);
			return;
		}
	}

	@Override
	protected void updateScreen(BeforeEnterEvent event)
	{
		projectGrid.setItems(projects);
	}
}