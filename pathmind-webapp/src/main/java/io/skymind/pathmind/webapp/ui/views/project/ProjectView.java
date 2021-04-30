package io.skymind.pathmind.webapp.ui.views.project;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.WildcardParameter;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.db.dao.ModelDAO;
import io.skymind.pathmind.db.dao.PolicyDAO;
import io.skymind.pathmind.db.dao.ProjectDAO;
import io.skymind.pathmind.db.dao.RewardVariableDAO;
import io.skymind.pathmind.services.ModelService;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.shared.data.Project;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.shared.security.SecurityUtils;
import io.skymind.pathmind.shared.utils.DateAndTimeUtils;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.bus.EventBusSubscriber;
import io.skymind.pathmind.webapp.data.utils.ExperimentGuiUtils;
import io.skymind.pathmind.webapp.exception.InvalidDataException;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.webapp.ui.components.ViewSection;
import io.skymind.pathmind.webapp.ui.components.archive.ArchivesTabPanel;
import io.skymind.pathmind.webapp.ui.components.atoms.TagLabel;
import io.skymind.pathmind.webapp.ui.components.buttons.NewExperimentButton;
import io.skymind.pathmind.webapp.ui.components.molecules.NotesField;
import io.skymind.pathmind.webapp.ui.components.navigation.Breadcrumbs;
import io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import io.skymind.pathmind.webapp.ui.components.modelChecker.ModelCheckerService;
import io.skymind.pathmind.webapp.ui.components.DownloadModelLink;
import io.skymind.pathmind.webapp.ui.views.project.components.ExperimentGrid;
import io.skymind.pathmind.webapp.ui.views.project.components.dialogs.RenameProjectDialog;
import io.skymind.pathmind.webapp.ui.views.project.components.navbar.ModelsNavbar;
import io.skymind.pathmind.webapp.ui.views.project.subscribers.ProjectViewFavoriteSubscriber;
import io.skymind.pathmind.webapp.utils.PathmindUtils;
import io.skymind.pathmind.webapp.utils.VaadinDateAndTimeUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.gatanaso.MultiselectComboBox;

import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.BOLD_LABEL;

@Route(value = Routes.PROJECT, layout = MainLayout.class)
public class ProjectView extends PathMindDefaultView implements HasUrlParameter<String>, AfterNavigationObserver {
    private static final int PROJECT_ID_SEGMENT = 0;
    private static final int MODEL_ID_SEGMENT = 2;

    private final Object modelLock = new Object();

    @Autowired
    private ExperimentDAO experimentDAO;
    @Autowired
    private PolicyDAO policyDAO;
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
    @Autowired
    private ModelService modelService;

    private long projectId;
    private Long modelId;
    private Project project;
    private List<Model> models;
    private List<Experiment> experiments;
    private List<RewardVariable> rewardVariables;
    private String pageTitle;
    private boolean isPythonModel = false;

    private ArchivesTabPanel<Experiment> archivesTabPanel;
    private NewExperimentButton newExperimentButton;
    private MultiselectComboBox<RewardVariable> metricMultiSelect;
    private ExperimentGrid experimentGrid;

    private Breadcrumbs pageBreadcrumbs;
    private Span projectName;
    private Span createdDate;
    private TagLabel archivedLabel = new TagLabel("Archived", false, "small");
    private Span modelName;
    private Span modelCreatedDate;
    private DownloadModelLink downloadLink;
    private TagLabel modelArchivedLabel = new TagLabel("Archived", false, "small");
    private ModelsNavbar modelsNavbar;
    private Model selectedModel;
    private NotesField modelNotesField;
    private VerticalLayout modelWrapper;

    public ProjectView() {
        super();
    }

    protected Component getMainContent() {

        addClassName("project-view");

        projectName = LabelFactory.createLabel("", CssPathmindStyles.SECTION_TITLE_LABEL);
        createdDate = LabelFactory.createLabel("", CssPathmindStyles.SECTION_SUBTITLE_LABEL);
        NotesField projectNotesField = createNotesField();
        Button edit = new Button("Rename", evt -> renameProject());

        modelName = LabelFactory.createLabel("", CssPathmindStyles.SECTION_TITLE_LABEL);
        modelCreatedDate = LabelFactory.createLabel("", CssPathmindStyles.SECTION_SUBTITLE_LABEL);
        modelArchivedLabel.setVisible(false);

        experimentGrid = new ExperimentGrid(experimentDAO, policyDAO, rewardVariables);
        setupArchivesTabPanel();
        newExperimentButton = new NewExperimentButton(experimentDAO, modelId, ButtonVariant.LUMO_TERTIARY,
                segmentIntegrator);
        modelNotesField = createModelNotesField();
        modelsNavbar = new ModelsNavbar(this, modelDAO, selectedModel, models, segmentIntegrator);

        HorizontalLayout headerWrapper = WrapperUtils.wrapWidthFullHorizontal(
                WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(
                        WrapperUtils.wrapWidthFullHorizontalNoSpacingAlignCenter(projectName, edit),
                        WrapperUtils.wrapWidthFullHorizontalNoSpacingAlignCenter(createdDate, archivedLabel)),
                projectNotesField);
        headerWrapper.addClassName("page-content-header");

        HorizontalLayout modelHeaderWrapper = WrapperUtils.wrapWidthFullHorizontal();
        modelHeaderWrapper.addClassName("page-content-header");

        downloadLink = new DownloadModelLink(project.getName(), selectedModel, modelService,
                segmentIntegrator, false, isPythonModel);
        modelHeaderWrapper
                .add(WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(
                        WrapperUtils.wrapWidthFullHorizontalNoSpacingAlignCenter(modelName), WrapperUtils
                                .wrapWidthFullHorizontalNoSpacingAlignCenter(modelCreatedDate, modelArchivedLabel),
                        downloadLink), modelNotesField);

        HorizontalLayout experimentGridHeader = WrapperUtils
                .wrapWidthFullHorizontalNoSpacingAlignCenter(archivesTabPanel, newExperimentButton);

        metricMultiSelect = createMetricSelectionGroup();
        HorizontalLayout metricSelectionRow = WrapperUtils.wrapWidthFullHorizontalNoSpacingAlignCenter(
                LabelFactory.createLabel("Metrics", BOLD_LABEL), metricMultiSelect);
        metricSelectionRow.addClassName("metric-selection-row");

        HorizontalLayout columnSelectionRow = WrapperUtils.wrapWidthFullHorizontalNoSpacingAlignCenter(
                LabelFactory.createLabel("Columns", BOLD_LABEL), createColumnSelectionGroup());
        columnSelectionRow.addClassName("column-selection-row");

        Span errorMessage = modelCheckerService.createInvalidErrorLabel(selectedModel);

        modelWrapper = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(errorMessage, modelHeaderWrapper,
                experimentGridHeader, metricSelectionRow, columnSelectionRow,
                experimentGrid);
        modelWrapper.addClassName("model-wrapper");

        return new ViewSection(
                headerWrapper,
                WrapperUtils.wrapSizeFullBetweenHorizontal(modelsNavbar, modelWrapper));
    }

    private MultiselectComboBox<String> createColumnSelectionGroup() {
        MultiselectComboBox<String> multiSelectGroup = new MultiselectComboBox<>();
        Map<String, Column> experimentGridColumns = experimentGrid.getColumnList();
        Set<String> columnList = experimentGridColumns.keySet();
        multiSelectGroup.setPlaceholder("Customize your table columns");
        multiSelectGroup.setItems(columnList);
        multiSelectGroup.setValue(columnList);
        multiSelectGroup.addSelectionListener(event -> {
            String addedSelection = String.join("", event.getAddedSelection());
            if (!addedSelection.isEmpty()) {
                experimentGridColumns.get(addedSelection).setVisible(true);
            }
            String removedSelection = String.join("", event.getRemovedSelection());
            if (!removedSelection.isEmpty()) {
                experimentGridColumns.get(removedSelection).setVisible(false);
            }
        });
        return multiSelectGroup;
    }

    private MultiselectComboBox<RewardVariable> createMetricSelectionGroup() {
        MultiselectComboBox<RewardVariable> multiSelectGroup = new MultiselectComboBox<>();
        Map<String, Column> experimentGridAdditionalColumns = experimentGrid.getAdditionalColumnList();
        multiSelectGroup.setRenderer(TemplateRenderer.<RewardVariable>of("[[item.name]]").withProperty("name", RewardVariable::getName));
        multiSelectGroup.setItemLabelGenerator(rewardVariable -> rewardVariable.getName());
        multiSelectGroup.setItems(rewardVariables);
        multiSelectGroup.setPlaceholder("Select simulation metrics to show on the table");
        multiSelectGroup.addSelectionListener(event -> {
            RewardVariable addedSelection = event.getAddedSelection().stream().findFirst().orElse(null);
            if (addedSelection != null) {
                experimentGrid.addOrShowColumn(addedSelection);
            }
            RewardVariable removedSelection = event.getRemovedSelection().stream().findFirst().orElse(null);
            if (removedSelection != null) {
                experimentGridAdditionalColumns.get(removedSelection.getName()).setVisible(false);
            }
        });
        return multiSelectGroup;
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
        RenameProjectDialog dialog = new RenameProjectDialog(project, projectDAO, updatedProjectName -> {
            project.setName(updatedProjectName);
            projectName.setText(updatedProjectName);
            pageBreadcrumbs.setText(1, updatedProjectName);
        });
        dialog.open();
    }

    private void setupArchivesTabPanel() {
        archivesTabPanel = new ArchivesTabPanel<>(
                "Experiments",
                experimentGrid,
                this::getExperiments,
                (experiment, isArchivable) -> {
                    ExperimentGuiUtils.archiveExperiment(experimentDAO, experiment, isArchivable);
                    segmentIntegrator.archived(Experiment.class, isArchivable);
                },
                getUISupplier());
    }

    public List<Model> getModels() {
        return project.getModels();
    }

    public List<Experiment> getExperiments() {
        return experiments;
    }

    public List<Experiment> getExperimentList() {
        return experiments;
    }

    public ExperimentGrid getExperimentGrid() {
        return experimentGrid;
    }

    public void setModel(Model model) {
        synchronized (modelLock) {
            selectedModel = model;
            loadModelData();
            updateComponents();
        }
    }

    public Object getModelLock() {
        return modelLock;
    }

    public void loadModelData() {
        modelId = selectedModel != null ? selectedModel.getId() : null;
        experiments = experimentDAO.getExperimentsForModel(modelId);
        rewardVariables = rewardVariableDAO.getRewardVariablesForModel(modelId);
    }

    public void setModelArchiveLabelVisible() {
        modelArchivedLabel.setVisible(selectedModel.isArchived());
    }

    public long getProjectId() {
        return projectId;
    }

    private void updateComponents() {
        String modelNameText = "Model #" + selectedModel.getName();
        if (selectedModel.getPackageName() != null) {
            modelNameText += " (" + selectedModel.getPackageName() + ")";
        }
        pageBreadcrumbs.setText(2, modelNameText);
        modelArchivedLabel.setVisible(selectedModel.isArchived());
        projectName.setText(project.getName());
        archivedLabel.setVisible(project.isArchived());
        modelName.setText(modelNameText);
        modelDAO.getModelIfAllowed(modelId, SecurityUtils.getUserId()).ifPresent(model -> {
            // the selectedModel on the navbar is referencing the model item with the old notes
            // this is to get the most updated notes
            modelNotesField.setNotesText(model.getUserNotes());
        });
        newExperimentButton.setModelId(selectedModel.getId());
        VaadinDateAndTimeUtils.withUserTimeZoneId(getUISupplier(), timeZoneId -> {
            // experimentGrid uses ZonedDateTimeRenderer, making sure here that time zone id is loaded properly before setting items
            if (experimentGrid != null) {
                experimentGrid.setItems(experiments);
            }
            createdDate.setText(String.format("Created %s", DateAndTimeUtils.formatDateAndTimeShortFormatter(project.getDateCreated(), timeZoneId)));
            if (selectedModel != null) {
                modelCreatedDate.setText(String.format("Created %s", DateAndTimeUtils.formatDateAndTimeShortFormatter(selectedModel.getDateCreated(), timeZoneId)));
            }
        });
        if (downloadLink != null) {
            downloadLink.setModel(selectedModel);
        }
        if (metricMultiSelect != null) {
            metricMultiSelect.setItems(rewardVariables);
        }
        archivesTabPanel.initData();
        recalculateGridColumnWidth(getUISupplier().get().get().getPage(), experimentGrid);
    }

    @Override
    protected Component getTitlePanel() {
        pageBreadcrumbs = new Breadcrumbs(project, selectedModel);
        return new ScreenTitlePanel(pageBreadcrumbs);
    }

    @Override
    protected void addEventBusSubscribers() {
        EventBus.subscribe(this, getUISupplier(), List.of(new ProjectViewFavoriteSubscriber(this)));
    }

    @Override
    protected void initLoadData() {
        synchronized (modelLock) {
            project = projectDAO.getProjectIfAllowed(projectId, SecurityUtils.getUserId())
                .orElseThrow(() -> new InvalidDataException("Attempted to access Project: " + projectId));
            models = modelDAO.getModelsForProject(projectId);
            project.setModels(models);
            pageTitle = "Pathmind | " + project.getName();
            
            if (models.size() > 0) {
                selectedModel = models.stream()
                    .filter(model -> modelId == null ? !model.isDraft() : modelId.equals(model.getId()))
                    .findFirst()
                    .orElse(models.get(0));
                loadModelData();
            }
        }
    }

    @Override
    protected boolean isValidView(BeforeEnterEvent event) {
        if (project.getModels().isEmpty() || modelId == null) {
            event.forwardTo(Routes.UPLOAD_MODEL, "" + projectId);
            return false;
        }
        if (selectedModel.isDraft()) {
            if (project.getModels().size() == 1) {
                String target = PathmindUtils.getResumeUploadModelPath(projectId, modelId);
                event.forwardTo(Routes.UPLOAD_MODEL, target);
                return false;
            }
        }
        return true;
    }

    @Override
    protected void initComponents() {
        updateComponents();
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        getUI().ifPresent(ui -> ui.getPage().getHistory().replaceState(null, "project/" + projectId + Routes.MODEL_PATH + modelId));
    }

    @Override
    public void setParameter(BeforeEvent event, @WildcardParameter String parameter) {
        String[] segments = parameter.split("/");

        if (NumberUtils.isDigits(segments[PROJECT_ID_SEGMENT])) {
            this.projectId = Long.parseLong(segments[PROJECT_ID_SEGMENT]);
        }
        if (segments.length == 3) {
            this.modelId = Long.parseLong(segments[MODEL_ID_SEGMENT]);
        }
    }

    @Override
    public String getPageTitle() {
        return pageTitle;
    }
}
