package io.skymind.pathmind.webapp.ui.views.project;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.WildcardParameter;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.db.dao.ModelDAO;
import io.skymind.pathmind.db.dao.ObservationDAO;
import io.skymind.pathmind.db.dao.PolicyDAO;
import io.skymind.pathmind.db.dao.ProjectDAO;
import io.skymind.pathmind.db.dao.RewardVariableDAO;
import io.skymind.pathmind.services.ModelService;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.shared.data.Observation;
import io.skymind.pathmind.shared.data.Project;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.shared.security.SecurityUtils;
import io.skymind.pathmind.shared.utils.DateAndTimeUtils;
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
import io.skymind.pathmind.webapp.ui.components.alp.DownloadModelAlpLink;
import io.skymind.pathmind.webapp.ui.views.project.components.ExperimentGrid;
import io.skymind.pathmind.webapp.ui.views.project.components.dialogs.RenameProjectDialog;
import io.skymind.pathmind.webapp.ui.views.project.components.navbar.ModelsNavbar;
import io.skymind.pathmind.webapp.utils.PathmindUtils;
import io.skymind.pathmind.webapp.utils.VaadinDateAndTimeUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.BOLD_LABEL;

@Route(value = Routes.PROJECT, layout = MainLayout.class)
public class ProjectView extends PathMindDefaultView implements HasUrlParameter<String>, AfterNavigationObserver {
    private static final int PROJECT_ID_SEGMENT = 0;
    private static final int MODEL_ID_SEGMENT = 2;

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
    private ObservationDAO observationDAO;
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
    private List<Observation> modelObservations = new ArrayList<>();
    private String pageTitle;

    private ArchivesTabPanel<Experiment> archivesTabPanel;
    private NewExperimentButton newExperimentButton;
    private Grid<Experiment> experimentGrid;

    private Breadcrumbs pageBreadcrumbs;
    private Span projectName;
    private Span createdDate;
    private TagLabel archivedLabel = new TagLabel("Archived", false, "small");
    private Span modelName;
    private Span modelCreatedDate;
    private Anchor downloadAlpLink;
    private TagLabel modelArchivedLabel = new TagLabel("Archived", false, "small");
    private ModelsNavbar modelsNavbar;
    private Model selectedModel;
    private NotesField modelNotesField;
    private VerticalLayout modelWrapper;

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
            newExperimentButton = new NewExperimentButton(experimentDAO, modelId, ButtonVariant.LUMO_TERTIARY, segmentIntegrator);
            modelNotesField = createModelNotesField();

            modelsNavbar = new ModelsNavbar(
                    modelDAO,
                    selectedModel,
                    models,
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
            downloadAlpLink = new DownloadModelAlpLink(project.getName(), selectedModel, modelService, segmentIntegrator);
            modelHeaderWrapper.add(
                    WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(
                            WrapperUtils.wrapWidthFullHorizontalNoSpacingAlignCenter(modelName),
                            WrapperUtils.wrapWidthFullHorizontalNoSpacingAlignCenter(modelCreatedDate, modelArchivedLabel),
                            downloadAlpLink
                    ),
                    modelNotesField
            );

            HorizontalLayout experimentGridHeader = WrapperUtils.wrapWidthFullHorizontalNoSpacingAlignCenter(
                    archivesTabPanel, newExperimentButton
            );

            // To be moved to separate methods later
            HorizontalLayout metricSelectionRow = WrapperUtils.wrapWidthFullHorizontalNoSpacingAlignCenter(
                LabelFactory.createLabel("Metrics", BOLD_LABEL),
                createMetricSelectionGroup());
            metricSelectionRow.addClassName("metric-selection-row");

            HorizontalLayout observationSelectionRow = WrapperUtils.wrapWidthFullHorizontalNoSpacingAlignCenter(
                    LabelFactory.createLabel("Observations", BOLD_LABEL),
                    createObservationSelectionGroup());
            observationSelectionRow.addClassName("observation-selection-row");

            HorizontalLayout columnSelectionRow = WrapperUtils.wrapWidthFullHorizontalNoSpacingAlignCenter(
                    LabelFactory.createLabel("Columns", BOLD_LABEL),
                    createColumnSelectionGroup());
            columnSelectionRow.addClassName("column-selection-row");

            
            Span errorMessage = modelCheckerService.createInvalidErrorLabel(selectedModel);

            modelWrapper = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(
                            errorMessage,
                            modelHeaderWrapper,
                            experimentGridHeader,
                            metricSelectionRow,
                            observationSelectionRow,
                            columnSelectionRow,
                            experimentGrid
                    );
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

    private MultiSelectListBox<String> createColumnSelectionGroup() {
        MultiSelectListBox<String> multiSelectGroup = new MultiSelectListBox<>();
        multiSelectGroup.setItems("Favorite", "Id #", "Created", "Status", "Notes", "Action");
        multiSelectGroup.getElement().setAttribute("theme", "grid-select");
        return multiSelectGroup;
    }

    private MultiSelectListBox<String> createObservationSelectionGroup() {
        MultiSelectListBox<String> multiSelectGroup = new MultiSelectListBox<>();
        List<String> observationNames = modelObservations.stream()
                .map(obs -> obs.getVariable()).collect(Collectors.toList());
        multiSelectGroup.setItems(observationNames);
        multiSelectGroup.getElement().setAttribute("theme", "grid-select");
        return multiSelectGroup;
    }

    private MultiSelectListBox<String> createMetricSelectionGroup() {
        MultiSelectListBox<String> multiSelectGroup = new MultiSelectListBox<>();
        List<String> metricNames = rewardVariables.stream()
                .map(obs -> obs.getName()).collect(Collectors.toList());
        multiSelectGroup.setItems(metricNames);
        multiSelectGroup.getElement().setAttribute("theme", "grid-select");
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

    private void setupGrid() {
        experimentGrid = new ExperimentGrid(experimentDAO, policyDAO, rewardVariables);
    }

    public List<Model> getModels() {
        return project.getModels();
    }

    public List<Experiment> getExperiments() {
        return experiments;
    }

    private Breadcrumbs createBreadcrumbs() {
        return selectedModel != null ? new Breadcrumbs(project, selectedModel) : new Breadcrumbs(project);
    }

    @Override
    protected Component getTitlePanel() {
        pageBreadcrumbs = createBreadcrumbs();
        titlePanel = new ScreenTitlePanel(pageBreadcrumbs);
        return titlePanel;
    }

    @Override
    protected void initLoadData() {
        project = projectDAO.getProjectIfAllowed(projectId, SecurityUtils.getUserId())
                .orElseThrow(() -> new InvalidDataException("Attempted to access Project: " + projectId));
        models = modelDAO.getModelsForProject(projectId);
        project.setModels(models);
        pageTitle = "Pathmind | " + project.getName();
        if (models.size() > 0) {
            if (modelId == null) {
                if (models.size() > 1) {
                    selectedModel = models.stream().filter(model -> !model.isDraft()).findFirst().orElse(null);
                } else {
                    selectedModel = models.get(0);
                }
            } else {
                selectedModel = models.stream()
                        .filter(model -> modelId.equals(model.getId()))
                        .findFirst()
                        .orElse(models.get(0));
            }
            modelId = selectedModel != null ? selectedModel.getId() : null;
            experiments = experimentDAO.getExperimentsForModel(modelId);
            rewardVariables = rewardVariableDAO.getRewardVariablesForModel(modelId);
            modelObservations = observationDAO.getObservationsForModel(modelId);
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
        String modelNameText = "";
        modelNameText = "Model #" + selectedModel.getName();
        if (selectedModel.getPackageName() != null) {
            modelNameText += " (" + selectedModel.getPackageName() + ")";
        }
        modelArchivedLabel.setVisible(selectedModel.isArchived());
        projectName.setText(project.getName());
        archivedLabel.setVisible(project.isArchived());
        modelName.setText(modelNameText);
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
        archivesTabPanel.initData();
        recalculateGridColumnWidth(getUISupplier().get().get().getPage(), experimentGrid);
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
