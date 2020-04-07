package io.skymind.pathmind.webapp.ui.views.project;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.shared.data.Project;
import io.skymind.pathmind.db.dao.ModelDAO;
import io.skymind.pathmind.db.dao.ProjectDAO;
import io.skymind.pathmind.db.dao.UserDAO;
import io.skymind.pathmind.shared.data.Data;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.shared.utils.DateAndTimeUtils;
import io.skymind.pathmind.webapp.exception.InvalidDataException;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.webapp.ui.components.TabPanel;
import io.skymind.pathmind.webapp.ui.components.ViewSection;
import io.skymind.pathmind.webapp.ui.components.archive.ArchivesTabPanel;
import io.skymind.pathmind.webapp.ui.components.buttons.UploadModelButton;
import io.skymind.pathmind.webapp.ui.components.navigation.Breadcrumbs;
import io.skymind.pathmind.webapp.ui.components.notesField.NotesField;
import io.skymind.pathmind.webapp.ui.constants.CssMindPathStyles;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.renderer.ZonedDateTimeRenderer;
import io.skymind.pathmind.webapp.ui.utils.NotificationUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import io.skymind.pathmind.webapp.ui.views.model.ModelView;
import io.skymind.pathmind.webapp.ui.views.model.UploadModelView;
import io.skymind.pathmind.webapp.ui.views.project.components.dialogs.RenameProjectDialog;
import io.skymind.pathmind.webapp.utils.VaadinDateAndTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;

@CssImport("./styles/styles.css")
@Route(value= Routes.PROJECT_URL, layout = MainLayout.class)
public class ProjectView extends PathMindDefaultView implements HasUrlParameter<Long>
{
	@Autowired
	private ModelDAO modelDAO;
	@Autowired
	private ProjectDAO projectDAO;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private SegmentIntegrator segmentIntegrator;

	private long projectId;
	private Project project;

	private ArchivesTabPanel<Model> archivesTabPanel;
	private Grid<Model> modelGrid;
	
	private Span projectName;
	private Span createdDate;
	
	private ScreenTitlePanel titlePanel;
	

	public ProjectView()
	{
		super();
	}

	protected Component getMainContent()
	{
		setupGrid();
		setupArchivesTabPanel();
		
		addClassName("project-view");

		// BUG -> I didn't have to really investigate but it looks like we may need
		// to do something special to get the full size content in the AppLayout component which
		// is why the table is centered vertically: https://github.com/vaadin/vaadin-app-layout/issues/51
		// Hence the workaround below:
		VerticalLayout leftPanel = WrapperUtils.wrapSizeFullVertical(
			archivesTabPanel,
			new ViewSection(modelGrid)
		);
		VerticalLayout rightPanel = createRightPanel();
		leftPanel.setPadding(false);
		rightPanel.setPadding(false);
		VerticalLayout gridWrapper = WrapperUtils.wrapSizeFullVertical(
			WrapperUtils.wrapCenterAlignmentFullSplitLayoutHorizontal(
				leftPanel,
				rightPanel,
			70),
			WrapperUtils.wrapWidthFullCenterHorizontal(new UploadModelButton(projectId))
		);
		gridWrapper.addClassName("page-content");
		gridWrapper.setPadding(false);
		
		return WrapperUtils.wrapSizeFullVertical(gridWrapper);
	}

	private VerticalLayout createRightPanel() {
		projectName = LabelFactory.createLabel("", CssMindPathStyles.SECTION_TITLE_LABEL, CssMindPathStyles.TRUNCATED_LABEL);
		createdDate = LabelFactory.createLabel("", CssMindPathStyles.SECTION_SUBTITLE_LABEL);
		Button edit = new Button("Rename", evt -> renameProject());
		edit.setClassName("no-shrink");
		NotesField notesField = new NotesField(
				"Project Notes",
				project.getUserNotes(),
				updatedNotes -> {
						projectDAO.updateUserNotes(projectId, updatedNotes);
						NotificationUtils.showSuccess("Notes saved");
						segmentIntegrator.updatedNotesModelsView();
				}
			);
		return WrapperUtils.wrapSizeFullVertical(
				new TabPanel("Details"),
				new ViewSection(WrapperUtils.wrapLeftAndRightAligned(projectName, edit), createdDate, notesField)
			);
	}

	private void renameProject() {
		RenameProjectDialog dialog = new RenameProjectDialog(project, projectDAO, updateProjectName -> {
			project.setName(updateProjectName);
			projectName.setText(updateProjectName);
			titlePanel.removeAll();
			titlePanel.add(createBreadcrumbs());
		});
		dialog.open();
	}

	private void setupArchivesTabPanel() {
		archivesTabPanel = new ArchivesTabPanel<>(
				"Models",
				modelGrid,
				this::getModels,
				(modelId, isArchivable) -> modelDAO.archive(modelId, isArchivable));
	}

	private void setupGrid()
	{
		modelGrid = new Grid<>();

		Grid.Column<Model> nameColumn = modelGrid
				.addColumn(TemplateRenderer.<Model> of("[[item.name]] <span class='tag'>[[item.draft]]</span>")
						.withProperty("name", Data::getName)
						.withProperty("draft", model -> model.isDraft() ? "Draft" : ""))
				.setHeader("#")
				.setComparator(Comparator.comparing(Model::getName))
				.setAutoWidth(true)
				.setFlexGrow(0)
				.setResizable(true)
				.setSortable(true);
		modelGrid.addColumn(new ZonedDateTimeRenderer<>(Model::getDateCreated, DateAndTimeUtils.STANDARD_DATE_ONLY_FOMATTER))
				.setComparator(Comparator.comparing(Model::getDateCreated))
				.setHeader("Created")
				.setAutoWidth(true)
				.setFlexGrow(0)
				.setResizable(true)
				.setSortable(true);
		modelGrid.addColumn(new ZonedDateTimeRenderer<>(Model::getLastActivityDate, DateAndTimeUtils.STANDARD_DATE_ONLY_FOMATTER))
				.setComparator(Comparator.comparing(Model::getLastActivityDate))
				.setHeader("Last Activity")
				.setAutoWidth(true)
				.setFlexGrow(0)
				.setResizable(true)
				.setSortable(true);
		modelGrid.addColumn(model -> {
				String userNotes = model.getUserNotes();
				return userNotes.isEmpty() ? "—" : userNotes;
		})
				.setHeader("Notes")
				.setResizable(true)
				.setSortable(false);

		modelGrid.addItemClickListener(event -> getUI().ifPresent(ui -> {
			Model model = event.getItem();
			if (model.isDraft()) {
				String target = UploadModelView.createResumeUploadTarget(project, model);
				UI.getCurrent().navigate(UploadModelView.class, target);
			}
			else {
				UI.getCurrent().navigate(ModelView.class, model.getId());
			}
		}));

		// Sort by name by default
		modelGrid.sort(Arrays.asList(new GridSortOrder<>(nameColumn, SortDirection.DESCENDING)));
	}

	public List<Model> getModels() {
		return project.getModels();
	}

	private Breadcrumbs createBreadcrumbs() {
		return new Breadcrumbs(project);
	}

	@Override
	protected Component getTitlePanel() {
		titlePanel = new ScreenTitlePanel(createBreadcrumbs());
		return titlePanel;
	}

	@Override
	protected boolean isAccessAllowedForUser() {
		return userDAO.isUserAllowedAccessToProject(projectId);
	}

	@Override
	protected void initLoadData() {
		project = projectDAO.getProject(projectId)
				.orElseThrow(() -> new InvalidDataException("Attempted to access Project: " + projectId));
		project.setModels(modelDAO.getModelsForProject(projectId));
	}

	@Override
	protected void initScreen(BeforeEnterEvent event) {
		projectName.setText(project.getName());
		if (project.getModels().isEmpty()) {
			event.forwardTo(Routes.UPLOAD_MODEL, ""+projectId);
		}
		VaadinDateAndTimeUtils.withUserTimeZoneId(timeZoneId -> {
			// modelGrid uses ZonedDateTimeRenderer, making sure here that time zone id is loaded properly before setting items
			modelGrid.setItems(project.getModels());
			createdDate.setText(String.format("Created %s", DateAndTimeUtils.formatDateAndTimeShortFormatter(project.getDateCreated(), timeZoneId)));
		});

		archivesTabPanel.initData();
	}

	@Override
	public void setParameter(BeforeEvent event, Long projectId)
	{
		this.projectId = projectId;
	}
}
