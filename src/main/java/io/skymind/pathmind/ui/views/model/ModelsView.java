package io.skymind.pathmind.ui.views.model;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.data.Model;
import io.skymind.pathmind.db.dao.ModelDAO;
import io.skymind.pathmind.exception.InvalidDataException;
import io.skymind.pathmind.ui.components.ActionMenu;
import io.skymind.pathmind.ui.components.ArchivesTabPanel;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.utils.NotificationUtils;
import io.skymind.pathmind.ui.utils.UIConstants;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.ui.views.PathMindDefaultView;
import io.skymind.pathmind.ui.views.TodoView;
import io.skymind.pathmind.ui.views.experiment.ExperimentsView;
import io.skymind.pathmind.ui.views.model.components.ModelSearchBox;
import io.skymind.pathmind.ui.views.project.ProjectsView;
import io.skymind.pathmind.utils.DateAndTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@StyleSheet("frontend://styles/styles.css")
@Route(value="models", layout = MainLayout.class)
public class ModelsView extends PathMindDefaultView implements HasUrlParameter<Long>
{
	@Autowired
	private ModelDAO modelDAO;

	private long projectId;
	private List<Model> models;

	private ModelSearchBox searchBox;
	private ArchivesTabPanel archivesTabPanel;
	private Grid<Model> modelGrid;

	public ModelsView()
	{
		super();
	}

	protected Component getMainContent()
	{
		setupTabPanel();
		setupGrid();
		setupSearchBox();

		// BUG -> I didn't have to really investigate but it looks like we may need
		// to do something special to get the full size content in the AppLayout component which
		// is why the table is centered vertically: https://github.com/vaadin/vaadin-app-layout/issues/51
		// Hence the workaround below:
		VerticalLayout gridWrapper = WrapperUtils.wrapCenterVertical(
				UIConstants.CENTERED_TABLE_WIDTH,
				WrapperUtils.wrapWidthFullRightHorizontal(searchBox),
				archivesTabPanel,
				modelGrid);
		gridWrapper.getElement().getStyle().set("padding-top", "100px");
		return gridWrapper;
	}

	private void setupTabPanel() {
		archivesTabPanel = new ArchivesTabPanel("Models",
				() -> UI.getCurrent().navigate(TodoView.class));
	}

	private void setupSearchBox() {
		searchBox = new ModelSearchBox(modelGrid, () -> getModels());
	}

	private void setupGrid()
	{
		modelGrid = new Grid<>();

		modelGrid.addColumn(Model::getName)
				.setHeader("Name")
				.setSortable(true);
		modelGrid.addColumn(
				new LocalDateTimeRenderer<>(Model::getDateCreated, DateAndTimeUtils.STANDARD_DATE_TIME_FOMATTER))
				.setHeader("Date Created")
				.setSortable(true);
		modelGrid.addColumn(
				new LocalDateTimeRenderer<>(Model::getLastActivityDate, DateAndTimeUtils.STANDARD_DATE_TIME_FOMATTER))
				.setHeader("Last Activity")
				.setSortable(true);

		modelGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
		modelGrid.addSelectionListener(event ->
				event.getFirstSelectedItem().ifPresent(selectedModel ->
						UI.getCurrent().navigate(ExperimentsView.class, selectedModel.getId())));

		modelGrid.setWidth(UIConstants.CENTERED_TABLE_WIDTH);
		modelGrid.setMaxWidth(UIConstants.CENTERED_TABLE_WIDTH);
		modelGrid.setMaxHeight("500px");
		modelGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
	}

	@Override
	protected ActionMenu getActionMenu() {
		return new ActionMenu(
				new Button("Back to Projects", click ->
						UI.getCurrent().navigate(ProjectsView.class)),
				new Button("Upload Model", click ->
						NotificationUtils.showTodoNotification()));
//						UI.getCurrent().getCurrent().navigate(NewProjectView.class)));
	}

	public List<Model> getModels() {
		return models;
	}

	@Override
	protected Component getTitlePanel() {
		return new ScreenTitlePanel("MODELS");
	}

	@Override
	protected void updateScreen(BeforeEnterEvent event) throws InvalidDataException
	{
		models = modelDAO.getModelsForProject(projectId);

		if(models == null || models.isEmpty())
			throw new InvalidDataException("Attempted to access Models for Project: " + projectId);

		modelGrid.setItems(models);
	}

	@Override
	public void setParameter(BeforeEvent event, Long projectId)
	{
		this.projectId = projectId;
	}
}
