package io.skymind.pathmind.ui.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.db.ProjectRepository;
import io.skymind.pathmind.ui.components.ActionMenu;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.components.grid.GridButtonFactory;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.views.project.NewProjectView;
import io.skymind.pathmind.ui.views.project.ProjectView;
import io.skymind.pathmind.utils.DateUtils;
import io.skymind.pathmind.utils.UIConstants;
import io.skymind.pathmind.utils.WrapperUtils;
import org.springframework.beans.factory.annotation.Autowired;

@StyleSheet("frontend://styles/styles.css")
@Route(value="dashboard", layout = MainLayout.class)
public class DashboardView extends VerticalLayout implements BasicViewInterface
{
	private ProjectRepository projectRepository;

	// TODO -> There should be projects if you select Dashboard but just in case we should have some additional logic
	// to handle the case where the project list is empty.
	public DashboardView(@Autowired ProjectRepository projectRepository)
	{
		this.projectRepository = projectRepository;

		add(getActionMenu());
		add(getTitlePanel());
		add(getMainContent());
	}

	public Component getMainContent()
	{
		Grid<Project> projectGrid = new Grid<>();

		projectGrid.addColumn(Project::getName)
				.setHeader("Name")
				.setSortable(true)
				.setWidth("275px");
		projectGrid.addColumn(
				new LocalDateRenderer<>(Project::getDateCreated, DateUtils.STANDARD_DATE_TIME_FOMATTER))
				.setHeader("Date Created")
				.setSortable(true)
				.setWidth("275px");
		projectGrid.addColumn(getProjectButton())
				.setHeader("Show Experiments")
				.setWidth(UIConstants.GRID_BUTTON_WIDTH);

		projectGrid.setItems(projectRepository.getProjectsForUser());
		projectGrid.setWidth("700px");
		projectGrid.setMaxWidth("700px");
		projectGrid.setMaxHeight("500px");
		projectGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

		// TODO BUG -> I didn't have to really investigate but it looks like we may need
		// to do something special to get the full size content in the AppLayout component which
		// is why the table is centered vertically: https://github.com/vaadin/vaadin-app-layout/issues/51
		// Hence the workaround below:
		HorizontalLayout gridWrapper = WrapperUtils.wrapCenterAlignmentFullHorizontal(projectGrid);
		gridWrapper.getElement().getStyle().set("padding-top", "100px");
		return gridWrapper;
	}

	private ComponentRenderer<HorizontalLayout, Project> getProjectButton()
	{
		return new ComponentRenderer<>(project -> {
			return GridButtonFactory.getGridButton(">", click ->
					UI.getCurrent().navigate(ProjectView.class, project.getId()));
		});
	}

	@Override
	public ActionMenu getActionMenu() {
		return new ActionMenu(
				new Button("New Project", click ->
						UI.getCurrent().navigate(NewProjectView.class)));
	}

	@Override
	public Component getTitlePanel() {
		return new ScreenTitlePanel("PROJECTS");
	}
}
