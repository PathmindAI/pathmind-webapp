package io.skymind.pathmind.webapp.ui.views.model;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.shared.security.SecurityUtils;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.NewExperimentView;
import io.skymind.pathmind.webapp.ui.views.model.components.RewardVariablesTable;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.db.dao.ModelDAO;
import io.skymind.pathmind.db.dao.RewardVariableDAO;
import io.skymind.pathmind.webapp.exception.InvalidDataException;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.webapp.ui.components.ViewSection;
import io.skymind.pathmind.webapp.ui.components.archive.ArchivesTabPanel;
import io.skymind.pathmind.webapp.ui.components.buttons.NewExperimentButton;
import io.skymind.pathmind.webapp.ui.components.navigation.Breadcrumbs;
import io.skymind.pathmind.webapp.ui.components.notesField.NotesField;
import io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.shared.utils.DateAndTimeUtils;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import io.skymind.pathmind.webapp.ui.views.project.components.panels.ExperimentGrid;
import io.skymind.pathmind.webapp.utils.VaadinDateAndTimeUtils;

@Route(value = Routes.MODEL_URL, layout = MainLayout.class)
public class ModelView extends PathMindDefaultView implements HasUrlParameter<Long> {
    @Autowired
    private ExperimentDAO experimentDAO;
    @Autowired
    private ModelDAO modelDAO;
    @Autowired
    private RewardVariableDAO rewardVariableDAO;
    @Autowired
    private SegmentIntegrator segmentIntegrator;
    @Autowired
    private NonTupleModelService nonTupleModelService;

    private long modelId;
    private Model model;
    private List<Experiment> experiments;
    private List<RewardVariable> rewardVariableNames;

    private ArchivesTabPanel<Experiment> archivesTabPanel;
    private ExperimentGrid experimentGrid;

    private Span modelName;
    private Span createdDate;
    private Paragraph packageNameText;
    private Paragraph actionsText;
    private Paragraph observationsText;
    private Div rewardVariableNamesText;

    public ModelView() {
        super();
    }

    protected Component getMainContent() {
        setupExperimentListPanel();
        setupArchivesTabPanel();

        addClassName("model-view");

        modelName = LabelFactory.createLabel("", CssPathmindStyles.SECTION_TITLE_LABEL);
        createdDate = LabelFactory.createLabel("", CssPathmindStyles.SECTION_SUBTITLE_LABEL);

        HorizontalLayout headerWrapper = WrapperUtils.wrapLeftAndRightAligned(
            WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(modelName, createdDate),
            new NewExperimentButton(experimentDAO, modelId));
        headerWrapper.addClassName("page-content-header");

        FlexLayout leftPanel = new ViewSection(headerWrapper, archivesTabPanel, experimentGrid);
        FlexLayout rightPanel = createRightPanel();

        SplitLayout gridWrapper = WrapperUtils.wrapCenterAlignmentFullSplitLayoutHorizontal(
            leftPanel,
            rightPanel,
        70);
        gridWrapper.addClassName("page-content");

        return gridWrapper;
    }

    private FlexLayout createRightPanel() {
        Span panelTitle = LabelFactory.createLabel("Model Details", CssPathmindStyles.SECTION_TITLE_LABEL);
        Span errorMessage = nonTupleModelService.createNonTupleErrorLabel(model);
        packageNameText = new Paragraph(LabelFactory.createLabel("Package Name", CssPathmindStyles.BOLD_LABEL));
        actionsText = new Paragraph(LabelFactory.createLabel("Actions", CssPathmindStyles.BOLD_LABEL));
        observationsText = new Paragraph(LabelFactory.createLabel("Observations", CssPathmindStyles.BOLD_LABEL));
        rewardVariableNamesText = new Div();
        rewardVariableNamesText.addClassName("model-reward-variables");

        NotesField notesField = createViewNotesField();
        FlexLayout rightPanelCard = new ViewSection(
                panelTitle,
                errorMessage,
                packageNameText,
                actionsText,
                observationsText,
                new Div(LabelFactory.createLabel("Reward Variables", CssPathmindStyles.BOLD_LABEL), rewardVariableNamesText),
                notesField);
        rightPanelCard.addClassName("card");

        return rightPanelCard;
    }

    /**
     * Using any experiment's getProject() since they should all be the same. I'm assuming at this point
     * that there has to be at least one experiment to be able to get here.
     */
    private Breadcrumbs createBreadcrumbs() {
        return new Breadcrumbs(experiments.get(0).getProject(), model);
    }

    private void setupArchivesTabPanel() {
        archivesTabPanel = new ArchivesTabPanel<Experiment>(
                "Experiments",
                true,
                experimentGrid,
                this::getExperiments,
                (experiment, isArchivable) -> { 
                    ExperimentUtils.archiveExperiment(experimentDAO, experiment, isArchivable);
                    segmentIntegrator.archived(Experiment.class, isArchivable);
                });
    }

    private void setupExperimentListPanel() {
        experimentGrid = new ExperimentGrid(experimentDAO);
        experimentGrid.addItemClickListener(event -> handleExperimentClick(event.getItem()));
    }

    private NotesField createViewNotesField() {
        return new NotesField(
            "Notes",
            model.getUserNotes(),
            updatedNotes -> {
                modelDAO.updateUserNotes(modelId, updatedNotes);
                segmentIntegrator.updatedNotesExperimentsView();
            }
        );
    }

    private void handleExperimentClick(Experiment experiment) {
        if (ExperimentUtils.isDraftRunType(experiment)) {
            getUI().ifPresent(ui -> ui.navigate(NewExperimentView.class, experiment.getId()));
        } else {
            getUI().ifPresent(ui -> ui.navigate(ExperimentView.class, experiment.getId()));
        }
    }

    @Override
    protected Component getTitlePanel() {
        return new ScreenTitlePanel(createBreadcrumbs());
    }

    public List<Experiment> getExperiments() {
        return experiments;
    }

    @Override
    protected void initLoadData() {
        model = modelDAO.getModelIfAllowed(modelId, SecurityUtils.getUserId())
                .orElseThrow(() -> new InvalidDataException("Attempted to access Model: " + modelId));
        experiments = experimentDAO.getExperimentsForModel(modelId);
        if (experiments == null || experiments.isEmpty())
            throw new InvalidDataException("Attempted to access Experiments for Model: " + modelId);
        rewardVariableNames = rewardVariableDAO.getRewardVariablesForModel(modelId);
    }

    @Override
    protected void initScreen(BeforeEnterEvent event) {
        String packageName = (model.getPackageName() != null) ? model.getPackageName() : "—";
        modelName.setText("Model #"+model.getName());

        VaadinDateAndTimeUtils.withUserTimeZoneId(event.getUI(), timeZoneId -> {
            // experimentGrid uses ZonedDateTimeRenderer, making sure here that time zone id is loaded properly before setting items
            experimentGrid.setItems(experiments);
            LocalDateTime dateCreatedData = model.getDateCreated();
            createdDate.setText(String.format("Uploaded on %s", DateAndTimeUtils.formatDateAndTimeShortFormatter(dateCreatedData, timeZoneId)));
        });
        packageNameText.add(packageName);
        actionsText.add(""+model.getNumberOfPossibleActions());
        observationsText.add(""+model.getNumberOfObservations());

        if (rewardVariableNames.size() > 0) {
            RewardVariablesTable rewardVariablesTable = new RewardVariablesTable();
            rewardVariablesTable.setIsReadOnly(true);
            rewardVariableNames.sort(Comparator.comparingInt(RewardVariable::getArrayIndex));
            rewardVariablesTable.setVariableSize(model.getRewardVariablesCount());
            rewardVariablesTable.setValue(rewardVariableNames);
            rewardVariableNamesText.add(rewardVariablesTable);
        } else {
            rewardVariableNamesText.add("All reward variables are unnamed. You can name them when you create a new experiment for this model.");
        }
        archivesTabPanel.initData(event.getUI());

        recalculateGridColumnWidth(event.getUI().getPage(), experimentGrid);
    }

    @Override
    public void setParameter(BeforeEvent event, Long modelId) {
        this.modelId = modelId;
    }
}
