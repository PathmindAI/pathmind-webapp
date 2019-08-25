package io.skymind.pathmind.ui.views.project;

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
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

@StyleSheet("frontend://styles/styles.css")
@Route(value="projects", layout = MainLayout.class)
public class ProjectsView extends VerticalLayout
{
	public ProjectsView(@Autowired ProjectRepository projectRepository)
	{
		add(new ActionMenu(new Button("New Project")));
		add(new ScreenTitlePanel("PROJECTS"));
		add(getProjectGrid(projectRepository));
	}

	private HorizontalLayout getProjectGrid(@Autowired ProjectRepository projectRepository)
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
				.setWidth("150px");

		projectGrid.setItems(projectRepository.findAll());
		projectGrid.setWidth("700px");
		projectGrid.setMaxWidth("700px");
		projectGrid.setMaxHeight("500px");
		projectGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

		HorizontalLayout gridWrapper = new HorizontalLayout();
		gridWrapper.add(projectGrid);
		gridWrapper.setSizeFull();
		gridWrapper.setJustifyContentMode(JustifyContentMode.CENTER);

		// TODO BUG -> I didn't have to really investigate but it looks like we may need
		// to do something special to get the full size content in the AppLayout component which
		// is why the table is centered vertically: https://github.com/vaadin/vaadin-app-layout/issues/51
		// Hence the workaround below:
		gridWrapper.setDefaultVerticalComponentAlignment(Alignment.CENTER);
		gridWrapper.getElement().getStyle().set("padding-top", "100px");
		return gridWrapper;
	}

	private ComponentRenderer<HorizontalLayout, Project> getProjectButton()
	{
		// TODO BUG -> It appears as though it's not possible to center a component in a grid unless you
		// wrap it up around something else like a HorizontalLayout:
		// https://vaadin.com/forum/thread/17111806/how-to-set-column-alignment-in-grid
		return new ComponentRenderer<>(project ->
		{
			Button button = new Button(">", click -> {
				UI.getCurrent().navigate(ProjectView.class, project.getId());
			});

			button.setThemeName("tertiary-inline");

			HorizontalLayout horizontalLayout = new HorizontalLayout(button);
			horizontalLayout.setWidthFull();
			horizontalLayout.setJustifyContentMode(JustifyContentMode.CENTER);
			return horizontalLayout;
		});
	}
}
