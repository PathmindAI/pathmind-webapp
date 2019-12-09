package io.skymind.pathmind.ui.views.model;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;

import io.skymind.pathmind.data.Model;
import io.skymind.pathmind.db.dao.ModelDAO;
import io.skymind.pathmind.db.dao.ProjectDAO;
import io.skymind.pathmind.db.dao.UserDAO;
import io.skymind.pathmind.exception.InvalidDataException;
import io.skymind.pathmind.security.Routes;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.components.SearchBox;
import io.skymind.pathmind.ui.components.ViewSection;
import io.skymind.pathmind.ui.components.archive.ArchivesTabPanel;
import io.skymind.pathmind.ui.components.buttons.BackButton;
import io.skymind.pathmind.ui.components.buttons.UploadModelButton;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.renderer.ZonedDateTimeRenderer;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.ui.views.PathMindDefaultView;
import io.skymind.pathmind.ui.views.experiment.ExperimentsView;
import io.skymind.pathmind.ui.views.model.filter.ModelFilter;
import io.skymind.pathmind.ui.views.project.ProjectsView;
import io.skymind.pathmind.utils.DateAndTimeUtils;

@CssImport("./styles/styles.css")
@Route(value= Routes.MODELS_URL, layout = MainLayout.class)
public class ModelsView extends PathMindDefaultView implements HasUrlParameter<Long>
{
	@Autowired
	private ModelDAO modelDAO;
	@Autowired
	private ProjectDAO projectDAO;
	@Autowired
	private UserDAO userDAO;

	private long projectId;
	private String projectName;
	private List<Model> models;

	private ArchivesTabPanel archivesTabPanel;
	private Grid<Model> modelGrid;
	private ScreenTitlePanel titlePanel;

	public ModelsView()
	{
		super();
	}

	protected Component getMainContent()
	{
		setupGrid();
		setupArchivesTabPanel();

		addClassName("models-view");

		// BUG -> I didn't have to really investigate but it looks like we may need
		// to do something special to get the full size content in the AppLayout component which
		// is why the table is centered vertically: https://github.com/vaadin/vaadin-app-layout/issues/51
		// Hence the workaround below:
		VerticalLayout gridWrapper = WrapperUtils.wrapSizeFullVertical(
				new ViewSection(
  				WrapperUtils.wrapWidthFullCenterHorizontal(getBackToProjectsButton()),
						WrapperUtils.wrapWidthFullRightHorizontal(getSearchBox()),
						archivesTabPanel,
						modelGrid
				),
				WrapperUtils.wrapWidthFullCenterHorizontal(new UploadModelButton(projectId))
		);
		return gridWrapper;
	}

	private void setupArchivesTabPanel() {
		archivesTabPanel = new ArchivesTabPanel<Model>(
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
				.setResizable(true)
				.setSortable(true);
		modelGrid.addColumn(new ZonedDateTimeRenderer<>(Model::getDateCreated, DateAndTimeUtils.STANDARD_DATE_ONLY_FOMATTER))
				.setComparator(Comparator.comparing(Model::getDateCreated))
				.setHeader("Date Created")
				.setResizable(true)
				.setSortable(true);
		Grid.Column<Model> lastActivityColumn = modelGrid.addColumn(new ZonedDateTimeRenderer<>(Model::getLastActivityDate, DateAndTimeUtils.STANDARD_DATE_ONLY_FOMATTER))
				.setComparator(Comparator.comparing(Model::getLastActivityDate))
				.setHeader("Last Activity")
				.setResizable(true)
				.setSortable(true);

		modelGrid.addItemClickListener(event -> getUI().ifPresent(ui -> UI.getCurrent().navigate(ExperimentsView.class, event.getItem().getId())));

		// Sort by name by default
		modelGrid.sort(Arrays.asList(new GridSortOrder<>(nameColumn, SortDirection.DESCENDING)));
	}

	public List<Model> getModels() {
		return models;
	}

	private Button getBackToProjectsButton() {
		return new BackButton("Back to Projects",
				click -> UI.getCurrent().navigate(ProjectsView.class));
	}

	@Override
	protected Component getTitlePanel() {
		titlePanel = new ScreenTitlePanel("PROJECT");
		return titlePanel;
	}

	@Override
	protected boolean isAccessAllowedForUser() {
		return userDAO.isUserAllowedAccessToProject(projectId);
	}

	@Override
	protected void initLoadData() throws InvalidDataException {
		models = modelDAO.getModelsForProject(projectId);
		if(models == null || models.isEmpty())
			throw new InvalidDataException("Attempted to access Models for Project: " + projectId);
		// It was either a left join on all the models to get the project or a separate database call to get the project's name.
		projectName = projectDAO.getProject(projectId).getName();
	}

	@Override
	protected void initScreen(BeforeEnterEvent event) throws InvalidDataException {
		DateAndTimeUtils.withUserTimeZoneId(timeZoneId -> {
			modelGrid.setItems(models);
		});
		archivesTabPanel.initData();
		titlePanel.setSubtitle(projectName);
	}

	@Override
	public void setParameter(BeforeEvent event, Long projectId)
	{
		this.projectId = projectId;
	}
}
