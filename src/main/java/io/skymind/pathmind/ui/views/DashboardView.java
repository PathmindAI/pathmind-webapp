package io.skymind.pathmind.ui.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.data.Run;
import io.skymind.pathmind.db.ProjectRepository;
import io.skymind.pathmind.ui.components.ActionMenu;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.ui.views.project.NewProjectView;
import io.skymind.pathmind.ui.views.project.ProjectView;
import io.skymind.pathmind.utils.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;

@StyleSheet("frontend://styles/styles.css")
@Route(value="dashboard", layout = MainLayout.class)
public class DashboardView extends PathMindDefaultView
{
	@Autowired
	private ProjectRepository projectRepository;

	private Grid<Project> projectGrid;

	public DashboardView()
	{
		super();
	}

	protected Component getMainContent()
	{
		projectGrid = new Grid<>();

		projectGrid.addColumn(Project::getName)
				.setHeader("Name")
				.setSortable(true);
//				.setWidth("275px");
		projectGrid.addColumn(
				new LocalDateRenderer<>(Project::getDateCreated, DateTimeUtils.STANDARD_DATE_TIME_FOMATTER))
				.setHeader("Date Created")
				.setSortable(true);
//				.setWidth("275px");
		projectGrid.addColumn(
				new LocalDateRenderer<>(Project::getLastActivityDate, DateTimeUtils.STANDARD_DATE_TIME_FOMATTER))
				.setHeader("Last Activity")
				.setSortable(true);
//				.setWidth("275px");

		projectGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
		projectGrid.addSelectionListener(event ->
				event.getFirstSelectedItem().ifPresent(selectedProject ->
						UI.getCurrent().navigate(ProjectView.class, selectedProject.getId())));

		projectGrid.setWidth("700px");
		projectGrid.setMaxWidth("700px");
		projectGrid.setMaxHeight("500px");
		projectGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

		// BUG -> I didn't have to really investigate but it looks like we may need
		// to do something special to get the full size content in the AppLayout component which
		// is why the table is centered vertically: https://github.com/vaadin/vaadin-app-layout/issues/51
		// Hence the workaround below:
		HorizontalLayout gridWrapper = WrapperUtils.wrapSizeFullCenterHorizontal(projectGrid);
		gridWrapper.getElement().getStyle().set("padding-top", "100px");
		return gridWrapper;
	}

	@Override
	protected ActionMenu getActionMenu() {
		return new ActionMenu(
				new Button("New Project", click ->
						UI.getCurrent().getCurrent().navigate(NewProjectView.class)));
	}

	@Override
	protected Component getTitlePanel() {
		return new ScreenTitlePanel("PROJECTS");
	}

	@Override
	protected void updateScreen(BeforeEnterEvent event) {
		projectGrid.setItems(projectRepository.getProjectsForUser());
	}
}
