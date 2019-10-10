package io.skymind.pathmind.ui.views.model;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.data.Model;
import io.skymind.pathmind.db.dao.ModelDAO;
import io.skymind.pathmind.db.dao.UserDAO;
import io.skymind.pathmind.exception.InvalidDataException;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.components.SearchBox;
import io.skymind.pathmind.ui.components.archive.ArchivesTabPanel;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.utils.UIConstants;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.ui.views.PathMindDefaultView;
import io.skymind.pathmind.ui.views.experiment.ExperimentsView;
import io.skymind.pathmind.ui.views.model.filter.ModelFilter;
import io.skymind.pathmind.utils.DateAndTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

@StyleSheet("frontend://styles/styles.css")
@Route(value="models", layout = MainLayout.class)
public class ModelsView extends PathMindDefaultView implements HasUrlParameter<Long>
{
	@Autowired
	private ModelDAO modelDAO;
	@Autowired
	private UserDAO userDAO;

	private long projectId;
	private List<Model> models;

	private Grid<Model> modelGrid;

	public ModelsView()
	{
		super();
	}

	protected Component getMainContent()
	{
		setupGrid();

		// BUG -> I didn't have to really investigate but it looks like we may need
		// to do something special to get the full size content in the AppLayout component which
		// is why the table is centered vertically: https://github.com/vaadin/vaadin-app-layout/issues/51
		// Hence the workaround below:
		VerticalLayout gridWrapper = WrapperUtils.wrapCenterVertical(
				UIConstants.CENTERED_TABLE_WIDTH,
				WrapperUtils.wrapWidthFullRightHorizontal(getSearchBox()),
				getArchivesTabPanel(),
				modelGrid);
		gridWrapper.getElement().getStyle().set("padding-top", "100px");
		return gridWrapper;
	}

	private ArchivesTabPanel getArchivesTabPanel() {
		return new ArchivesTabPanel<Model>(
				"Models",
				modelGrid,
				this::getModels,
				(modelId, isArchivable) -> modelDAO.archive(modelId, isArchivable));
	}

	private SearchBox getSearchBox() {
		return new SearchBox<Model>(modelGrid, new ModelFilter());
	}

	private void setupGrid()
	{
		modelGrid = new Grid<>();

		Grid.Column<Model> nameColumn = modelGrid.addColumn(Model::getName)
				.setHeader("Model")
				.setSortable(true);
		modelGrid.addColumn(new LocalDateTimeRenderer<>(Model::getDateCreated, DateAndTimeUtils.STANDARD_DATE_ONLY_FOMATTER))
				.setHeader("Date Created")
				.setSortable(true);
		Grid.Column<Model> lastActivityColumn = modelGrid.addColumn(new LocalDateTimeRenderer<>(Model::getLastActivityDate, DateAndTimeUtils.STANDARD_DATE_ONLY_FOMATTER))
				.setHeader("Last Activity")
				.setSortable(true);

		modelGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
		modelGrid.addSelectionListener(event ->
				event.getFirstSelectedItem().ifPresent(selectedModel ->
						UI.getCurrent().navigate(ExperimentsView.class, selectedModel.getId())));
		// Sort by name by default
		modelGrid.sort(Arrays.asList(new GridSortOrder<Model>(nameColumn, SortDirection.DESCENDING)));

		modelGrid.setWidth(UIConstants.CENTERED_TABLE_WIDTH);
		modelGrid.setMaxWidth(UIConstants.CENTERED_TABLE_WIDTH);
		modelGrid.setMaxHeight("500px");
		modelGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
	}

	public List<Model> getModels() {
		return models;
	}

	@Override
	protected Component getTitlePanel() {
		return new ScreenTitlePanel("MODELS");
	}

	@Override
	protected boolean isAccessAllowedForUser() {
		return userDAO.isUserAllowedAccessToProject(projectId);
	}

	@Override
	protected void loadData() throws InvalidDataException {
		models = modelDAO.getModelsForProject(projectId);
		if(models == null || models.isEmpty())
			throw new InvalidDataException("Attempted to access Models for Project: " + projectId);
	}

	@Override
	protected void updateScreen(BeforeEnterEvent event) throws InvalidDataException {
		modelGrid.setItems(models);
	}

	@Override
	public void setParameter(BeforeEvent event, Long projectId)
	{
		this.projectId = projectId;
	}
}
