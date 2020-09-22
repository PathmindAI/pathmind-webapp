package io.skymind.pathmind.webapp.ui.views.project;

import java.util.List;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.shared.data.Project;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.db.dao.ModelDAO;
import io.skymind.pathmind.db.dao.ProjectDAO;
import io.skymind.pathmind.db.dao.RewardVariableDAO;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.shared.security.SecurityUtils;
import io.skymind.pathmind.shared.utils.DateAndTimeUtils;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.exception.InvalidDataException;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.webapp.ui.components.ViewSection;
import io.skymind.pathmind.webapp.ui.components.archive.ArchivesTabPanel;
import io.skymind.pathmind.webapp.ui.components.atoms.TagLabel;
import io.skymind.pathmind.webapp.ui.components.navigation.Breadcrumbs;
import io.skymind.pathmind.webapp.ui.components.notesField.NotesField;
import io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import io.skymind.pathmind.webapp.ui.views.model.ModelCheckerService;
import io.skymind.pathmind.webapp.ui.views.model.UploadModelView;
import io.skymind.pathmind.webapp.ui.views.model.components.ExperimentGrid;
import io.skymind.pathmind.webapp.ui.views.model.components.RewardVariablesTable;
import io.skymind.pathmind.webapp.ui.views.project.components.navbar.ModelsNavbar;
import io.skymind.pathmind.webapp.ui.views.project.components.dialogs.RenameProjectDialog;
import io.skymind.pathmind.webapp.utils.VaadinDateAndTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value= Routes.PROJECT_URL, layout = MainLayout.class)
public class ProjectView extends PathMindDefaultView implements HasUrlParameter<Long>
{
    @Autowired
    private ExperimentDAO experimentDAO;
	@Autowired
	private ModelDAO modelDAO;
	@Autowired
	private ProjectDAO projectDAO;
    @Autowired
    private RewardVariableDAO rewardVariableDAO;
	@Autowired
	private SegmentIntegrator segmentIntegrator;
    @Autowired
    private ModelCheckerService modelCheckerService;

	private long projectId;
    private Project project;
    private List<Model> models;
    private List<Experiment> experiments;
    private List<RewardVariable> rewardVariableNames;

	private ArchivesTabPanel<Experiment> archivesTabPanel;
	private Grid<Experiment> experimentGrid;
	
	private Span projectName;
    private Span createdDate;
    private TagLabel archivedLabel = new TagLabel("Archived", false, "small");
	private Span modelName;
    private Span modelCreatedDate;
    private TagLabel modelArchivedLabel = new TagLabel("Archived", false, "small");
    private ModelsNavbar modelsNavbar;
    private Model selectedModel;
    private RewardVariablesTable rewardVariablesTable;
    private NotesField modelNotesField;
    private SplitLayout modelWrapper;
	
	private ScreenTitlePanel titlePanel;

	public ProjectView() {
		super();
	}

	protected Component getMainContent() {
		
		addClassName("project-view");

		projectName = LabelFactory.createLabel("", CssPathmindStyles.SECTION_TITLE_LABEL, CssPathmindStyles.PROJECT_TITLE);
        createdDate = LabelFactory.createLabel("", CssPathmindStyles.SECTION_SUBTITLE_LABEL);
        NotesField projectNotesField = createNotesField();
        Button edit = new Button("Rename", evt -> renameProject());
        edit.setClassName("no-shrink");

        modelName = LabelFactory.createLabel("", CssPathmindStyles.SECTION_TITLE_LABEL, CssPathmindStyles.PROJECT_TITLE);
        modelCreatedDate = LabelFactory.createLabel("", CssPathmindStyles.SECTION_SUBTITLE_LABEL);
        modelArchivedLabel.setVisible(false);

        if (selectedModel != null) {
            setupGrid();
            setupArchivesTabPanel();
            modelNotesField = createModelNotesField();
            rewardVariablesTable = new RewardVariablesTable();
            rewardVariablesTable.setRewardVariables(rewardVariableNames);

            modelsNavbar = new ModelsNavbar(
                () -> getUI(),
                modelDAO,
                selectedModel,
                models,
                selectedModel -> selectModel(selectedModel),
                segmentIntegrator
            );
        }

		HorizontalLayout headerWrapper = WrapperUtils.wrapWidthFullHorizontal(
                WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(
                        WrapperUtils.wrapWidthFullHorizontalNoSpacingAlignCenter(projectName, edit),
                        WrapperUtils.wrapWidthFullHorizontalNoSpacingAlignCenter(createdDate, archivedLabel)
                ),
                projectNotesField
        );
		headerWrapper.addClassName("page-content-header");

        HorizontalLayout modelHeaderWrapper = WrapperUtils.wrapWidthFullHorizontal();
        modelHeaderWrapper.addClassName("page-content-header");

        if (selectedModel != null) {
            modelHeaderWrapper.add(
                WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(
                        WrapperUtils.wrapWidthFullHorizontalNoSpacingAlignCenter(modelName),
                        WrapperUtils.wrapWidthFullHorizontalNoSpacingAlignCenter(modelCreatedDate, modelArchivedLabel)
                ),
                modelNotesField
            );
            FlexLayout rightPanel = createRightPanel();
    
            modelWrapper = WrapperUtils.wrapCenterAlignmentFullSplitLayoutHorizontal(
                    WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(
                            modelHeaderWrapper, archivesTabPanel, experimentGrid
                    ),
                    rightPanel,
            70);
            modelWrapper.addClassName("model-wrapper");
        }

        FlexLayout gridWrapper = new ViewSection(headerWrapper);
        if (selectedModel != null) {
            gridWrapper.add(
                WrapperUtils.wrapSizeFullBetweenHorizontal(
                        modelsNavbar,
                        modelWrapper                        
                )
            );
        }
		gridWrapper.addClassName("page-content");
		
		return gridWrapper;
    }
    
    private FlexLayout createRightPanel() {
        Span errorMessage = modelCheckerService.createInvalidErrorLabel(selectedModel);

        FlexLayout rightPanelCard = new ViewSection(
                errorMessage,
                rewardVariablesTable);
        rightPanelCard.addClassName("card");

        return rightPanelCard;
    }

	private NotesField createNotesField() {
		return new NotesField(
				"Project Notes",
				project.getUserNotes(),
				updatedNotes -> {
						projectDAO.updateUserNotes(projectId, updatedNotes);
						segmentIntegrator.updatedNotesModelsView();
                },
                true
			);
	}

	private NotesField createModelNotesField() {
		return new NotesField(
				"Model Notes",
				selectedModel.getUserNotes(),
				updatedNotes -> {
						modelDAO.updateUserNotes(selectedModel.getId(), updatedNotes);
						segmentIntegrator.updatedNotesExperimentsView();
                },
                true
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
		archivesTabPanel = new ArchivesTabPanel<Experiment>(
				"Experiments",
				experimentGrid,
				this::getExperiments,
                (experiment, isArchivable) -> { 
                    ExperimentUtils.archiveExperiment(experimentDAO, experiment, isArchivable);
                    segmentIntegrator.archived(Experiment.class, isArchivable);
                });
    }
    
    private void setupGrid() {
        experimentGrid = new ExperimentGrid(experimentDAO);
    }

	public List<Model> getModels() {
		return project.getModels();
	}

	public List<Experiment> getExperiments() {
		return experiments;
	}

	private Breadcrumbs createBreadcrumbs() {
		return new Breadcrumbs(project);
    }

    private void selectModel(Model selectedModel) {
        this.selectedModel = selectedModel;
        getUI().ifPresent(ui -> {
            if (selectedModel.isDraft()) {
                String target = UploadModelView.createResumeUploadTarget(project, selectedModel);
                ui.navigate(UploadModelView.class, target);
            } else {
                modelsNavbar.setCurrentModel(selectedModel);
                experiments = experimentDAO.getExperimentsForModel(selectedModel.getId());
                experimentGrid.setItems(experiments);
                String modelNameText = "";
                modelNameText = "Model #"+selectedModel.getName();
                if (selectedModel.getPackageName() != null) {
                    modelNameText += " ("+selectedModel.getPackageName()+")";
                }
                modelArchivedLabel.setVisible(selectedModel.isArchived());
                modelName.setText(modelNameText);
                modelNotesField.setNotesText(selectedModel.getUserNotes());
                VaadinDateAndTimeUtils.withUserTimeZoneId(ui, timeZoneId -> {
                    modelCreatedDate.setText(String.format("Created %s", DateAndTimeUtils.formatDateAndTimeShortFormatter(selectedModel.getDateCreated(), timeZoneId)));
                });
            }
        });
    }

	@Override
	protected Component getTitlePanel() {
		titlePanel = new ScreenTitlePanel(createBreadcrumbs());
		return titlePanel;
	}

	@Override
	protected void initLoadData() {
        project = projectDAO.getProjectIfAllowed(projectId, SecurityUtils.getUserId())
                .orElseThrow(() -> new InvalidDataException("Attempted to access Project: " + projectId));
        models = modelDAO.getModelsForProject(projectId);
        project.setModels(models);
        if (models.size() > 0) {
            selectedModel = models.get(0);
            experiments = experimentDAO.getExperimentsForModel(selectedModel.getId());
            rewardVariableNames = rewardVariableDAO.getRewardVariablesForModel(selectedModel.getId());
        }
	}

	@Override
	protected void initScreen(BeforeEnterEvent event) {
		if (project.getModels().isEmpty()) {
            event.forwardTo(Routes.UPLOAD_MODEL, ""+projectId);
            return;
		}
        String modelNameText = "";
        modelNameText = "Model #"+selectedModel.getName();
        if (selectedModel.getPackageName() != null) {
            modelNameText += " ("+selectedModel.getPackageName()+")";
        }
        modelArchivedLabel.setVisible(selectedModel.isArchived());
        projectName.setText(project.getName());
        archivedLabel.setVisible(project.isArchived());
        modelName.setText(modelNameText);
        VaadinDateAndTimeUtils.withUserTimeZoneId(event.getUI(), timeZoneId -> {
            // experimentGrid uses ZonedDateTimeRenderer, making sure here that time zone id is loaded properly before setting items
            if (experimentGrid != null) {
                experimentGrid.setItems(experiments);
            }
            createdDate.setText(String.format("Created %s", DateAndTimeUtils.formatDateAndTimeShortFormatter(project.getDateCreated(), timeZoneId)));
            if (selectedModel != null) {
                modelCreatedDate.setText(String.format("Created %s", DateAndTimeUtils.formatDateAndTimeShortFormatter(selectedModel.getDateCreated(), timeZoneId)));
            }
        });
        archivesTabPanel.initData(event.getUI());
        recalculateGridColumnWidth(event.getUI().getPage(), experimentGrid);
	}

	@Override
	public void setParameter(BeforeEvent event, Long projectId)
	{
		this.projectId = projectId;
	}
}
