package io.skymind.pathmind.webapp.ui.views.model;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveEvent.ContinueNavigationAction;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.WildcardParameter;
import io.skymind.pathmind.db.dao.ModelDAO;
import io.skymind.pathmind.db.dao.ObservationDAO;
import io.skymind.pathmind.db.dao.ProjectDAO;
import io.skymind.pathmind.db.dao.RewardVariableDAO;
import io.skymind.pathmind.db.utils.RewardVariablesUtils;
import io.skymind.pathmind.services.ModelService;
import io.skymind.pathmind.services.model.analyze.ModelBytes;
import io.skymind.pathmind.services.model.analyze.ModelFileVerifier;
import io.skymind.pathmind.services.project.AnylogicFileCheckResult;
import io.skymind.pathmind.services.project.Hyperparams;
import io.skymind.pathmind.services.project.ProjectFileCheckService;
import io.skymind.pathmind.services.project.StatusUpdater;
import io.skymind.pathmind.shared.constants.ModelType;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.shared.data.Observation;
import io.skymind.pathmind.shared.data.Project;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.shared.security.SecurityUtils;
import io.skymind.pathmind.shared.utils.ModelUtils;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.bus.events.main.ExperimentCreatedBusEvent;
import io.skymind.pathmind.webapp.exception.InvalidDataException;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.webapp.ui.components.modelChecker.ModelCheckerService;
import io.skymind.pathmind.webapp.ui.components.navigation.Breadcrumbs;
import io.skymind.pathmind.webapp.ui.components.rewardVariables.RewardVariablesPanel;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.utils.FormUtils;
import io.skymind.pathmind.webapp.ui.utils.PushUtils;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import io.skymind.pathmind.webapp.ui.views.experiment.NewExperimentView;
import io.skymind.pathmind.webapp.ui.views.model.components.ModelDetailsWizardPanel;
import io.skymind.pathmind.webapp.ui.views.model.components.UploadALPWizardPanel;
import io.skymind.pathmind.webapp.ui.views.model.components.UploadModelWizardPanel;
import io.skymind.pathmind.webapp.utils.PathmindUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.unit.DataSize;

import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.PROJECT_TITLE;
import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.SECTION_SUBTITLE_LABEL;
import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.SECTION_TITLE_LABEL;
import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.SECTION_TITLE_LABEL_REGULAR_FONT_WEIGHT;
import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.WARNING_LABEL;

@Slf4j
@Route(value = Routes.UPLOAD_MODEL, layout = MainLayout.class)
public class UploadModelView extends PathMindDefaultView implements StatusUpdater<AnylogicFileCheckResult>, HasUrlParameter<String>, BeforeLeaveObserver {

    private static final int PROJECT_ID_SEGMENT = 0;
    private static final int UPLOAD_MODE_SEGMENT = 1;
    private static final int MODEL_ID_SEGMENT = 2;

    @Autowired
    private ProjectDAO projectDAO;

    @Autowired
    private ModelService modelService;

    @Autowired
    private ModelDAO modelDAO;

    @Autowired
    private RewardVariableDAO rewardVariablesDAO;

    @Autowired
    private ObservationDAO observationDAO;

    @Autowired
    private ModelFileVerifier modelFileVerifier;

    @Autowired
    private ProjectFileCheckService projectFileCheckService;

    @Autowired
    private SegmentIntegrator segmentIntegrator;

    @Autowired
    private ModelCheckerService modelCheckerService;

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSizeAsStr;

    @Value(("${pathhmind.model.apl.max-size}"))
    private String alpFileSizeAsStr;

    private Model model;

    private List<RewardVariable> rewardVariables = new ArrayList<>();

    private Binder<Model> modelBinder;

    private UploadModelWizardPanel uploadModelWizardPanel;
    private UploadALPWizardPanel uploadALPWizardPanel;
    private ModelDetailsWizardPanel modelDetailsWizardPanel;
    private RewardVariablesPanel rewardVariablesPanel;

    private List<Component> wizardPanels;

    private long projectId;
    private long modelId = -1;
    private long experimentId;
    private String modelNotes;
    private Project project;

    private UploadMode uploadMode;

    public UploadModelView() {
        super();
    }

    public static String createResumeUploadTarget(Project project, Model model) {
        return PathmindUtils.getResumeUploadModelPath(project.getId(), model.getId());
    }

    public static Button createNextStepButton() {
        Button nextStepButton = new Button("Next", new Icon(VaadinIcon.CHEVRON_RIGHT));
        nextStepButton.setIconAfterText(true);
        nextStepButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        return nextStepButton;
    }

    protected Component getMainContent() {
        modelBinder = new Binder<>(Model.class);

        uploadModelWizardPanel = new UploadModelWizardPanel(model, uploadMode, (int) DataSize.parse(maxFileSizeAsStr).toBytes());
        uploadALPWizardPanel = new UploadALPWizardPanel(model, isResumeUpload(), ModelUtils.isValidModel(model), (int) DataSize.parse(alpFileSizeAsStr).toBytes());
        modelDetailsWizardPanel = new ModelDetailsWizardPanel(modelBinder);
        rewardVariablesPanel = new RewardVariablesPanel();

        modelBinder.readBean(model);

        wizardPanels = Arrays.asList(
                uploadModelWizardPanel,
                uploadALPWizardPanel,
                modelDetailsWizardPanel,
                rewardVariablesPanel);

        if (isResumeUpload()) {
            setVisibleWizardPanel(uploadALPWizardPanel);
        } else {
            setVisibleWizardPanel(uploadModelWizardPanel);
        }

        uploadModelWizardPanel.addFileUploadCompletedListener(this::handleUploadWizardClicked);
        uploadModelWizardPanel.addFileUploadFailedListener(this::handleUploadFailed);
        uploadALPWizardPanel.addButtonClickListener(click -> handleUploadALPClicked());
        modelDetailsWizardPanel.addButtonClickListener(click -> handleModelDetailsClicked());
        rewardVariablesPanel.addButtonClickListener(click -> handleRewardVariablesClicked());

        Div sectionTitleWrapper = new Div();

        sectionTitleWrapper.add(
                LabelFactory.createLabel("Project: ", SECTION_TITLE_LABEL),
                LabelFactory.createLabel(project.getName(), SECTION_TITLE_LABEL_REGULAR_FONT_WEIGHT, SECTION_SUBTITLE_LABEL)
        );
        sectionTitleWrapper.addClassName(PROJECT_TITLE);

        Span invalidModelErrorLabel = modelCheckerService.createInvalidErrorLabel(model);
        invalidModelErrorLabel.getStyle().set("margin-top", "10px");
        invalidModelErrorLabel.getStyle().set("margin-bottom", "10px");

        List<Component> sections = new ArrayList<>();
        if (isResumeUpload() && model.isArchived()) {
            sections.add(
                    LabelFactory.createLabel("This draft model is archived.", WARNING_LABEL)
            );
        }
        sections.add(sectionTitleWrapper);
        sections.add(uploadModelWizardPanel);
        if (isResumeUpload() && !ModelUtils.isValidModel(model)) {
            sections.add(invalidModelErrorLabel);
        }
        sections.add(uploadALPWizardPanel);
        sections.add(modelDetailsWizardPanel);
        sections.add(rewardVariablesPanel);
        VerticalLayout wrapper = new VerticalLayout(
                sections.toArray(new Component[0]));

        wrapper.addClassName("view-section");
        wrapper.setSpacing(false);
        addClassName("upload-model-view");
        return wrapper;
    }

    private void handleUploadALPClicked() {
        if (model.getAlpFile() != null && model.getAlpFile().length > 0) {
            modelService.saveModelAlp(model);
        } else {
            // for the case we are resuming a model creation and we had already uploaded the alp file
            modelService.removeModelAlp(model);
        }
        setVisibleWizardPanel(modelDetailsWizardPanel);
    }

    private void handleUploadFailed(Collection<String> errors) {
        uploadModelWizardPanel.showFileCheckPanel();
        this.updateError(errors.iterator().next());
    }

    private void autosaveModelDetails() {
        if (!FormUtils.isValidForm(modelBinder, model)) {
            return;
        }

        segmentIntegrator.modelDraftSaved();
        final String modelNotes = modelDetailsWizardPanel.getModelNotes();
        if (model.getId() == -1) {
            modelService.addDraftModelToProject(model, project.getId(), modelNotes);
        } else {
            modelService.updateDraftModel(model, modelNotes);
        }
    }

    @Override
    public void beforeLeave(BeforeLeaveEvent event) {
        ContinueNavigationAction action = event.postpone();
        if (modelDetailsWizardPanel.isVisible()) {
            autosaveModelDetails();
        }
        action.proceed();
    }

    @Override
    protected void initLoadData() throws InvalidDataException {
        if (isResumeUpload()) {
            this.model = modelService.getModel(modelId)
                    .orElseThrow(() -> new InvalidDataException("Attempted to access Invalid model: " + modelId));
            this.rewardVariables = rewardVariablesDAO.getRewardVariablesForModel(modelId);
        } else {
            this.model = ModelUtils.generateNewDefaultModel();
            model.setProjectId(projectId);
        }
        project = projectDAO.getProjectIfAllowed(projectId, SecurityUtils.getUserId())
                .orElseThrow(() -> new InvalidDataException("Attempted to access Experiment: " + projectId));
    }

    private boolean isResumeUpload() {
        return modelId != -1;
    }

    @Override
    protected void initScreen(BeforeEnterEvent event) {
    }

    private void handleRewardVariablesClicked() {
        if (rewardVariablesPanel.canSaveChanges()) {
            rewardVariablesDAO.updateModelAndRewardVariables(model, rewardVariables);
            saveAndNavigateToNewExperiment();
        }
    }

    private void handleModelDetailsClicked() {
        if (!FormUtils.isValidForm(modelBinder, model)) {
            return;
        }

        model.setDraft(false);
        modelNotes = modelDetailsWizardPanel.getModelNotes();

        if (!modelNotes.isEmpty()) {
            segmentIntegrator.addedNotesUploadModelView();
        }

        modelService.updateDraftModel(model, modelNotes);
        rewardVariablesPanel.setupRewardVariables(rewardVariables);
        setVisibleWizardPanel(rewardVariablesPanel);
    }

    private void saveAndNavigateToNewExperiment() {
        Experiment experiment = modelService.resumeModelCreation(model, modelNotes);
        experimentId = experiment.getId();
        EventBus.post(new ExperimentCreatedBusEvent(experiment));
        getUI().ifPresent(ui -> ui.navigate(NewExperimentView.class, experimentId));
    }

    private void handleUploadWizardClicked(ModelBytes modelBytes) {
        byte[] file = modelFileVerifier.assureModelBytes(modelBytes).getBytes();
        model.setFile(file);
        uploadModelWizardPanel.showFileCheckPanel();
        projectFileCheckService.checkFile(this, model);
    }

    private void setVisibleWizardPanel(Component wizardPanel) {
        wizardPanels
                .forEach(panel -> panel.setVisible(panel.equals(wizardPanel)));
    }

    private Breadcrumbs createBreadcrumbs() {
        return new Breadcrumbs(project, model);
    }

    @Override
    protected Component getTitlePanel() {
        return new ScreenTitlePanel(createBreadcrumbs());
    }

    @Override
    public void updateStatus(double percentage) {
        getUI().ifPresent(ui -> PushUtils.push(ui, () ->
                uploadModelWizardPanel.setFileCheckStatusProgressBarValue(percentage)));
    }

    @Override
    public void updateError(String error) {
        getUI().ifPresent(ui -> PushUtils.push(ui, () -> {
            uploadModelWizardPanel.setFileCheckStatusProgressBarValue(1.0);
            uploadModelWizardPanel.setError(error);
            segmentIntegrator.modelImported(false);
            log.info("Error occurred : " + error);
        }));
    }

    @Override
    public void fileSuccessfullyVerified(AnylogicFileCheckResult result) {
        getUI().ifPresent(ui -> PushUtils.push(ui, () -> {
            uploadModelWizardPanel.setFileCheckStatusProgressBarValue(1.0);
            setVisibleWizardPanel(uploadALPWizardPanel);
            List<Observation> observationList = new ArrayList<>();
            if (result != null) {
                Hyperparams alResult = result.getParams();
                rewardVariables = ModelUtils.convertToRewardVariables(model.getId(), alResult.getRewardVariableNames(), alResult.getRewardVariableTypes());
                observationList = ModelUtils.convertToObservations(alResult.getObservationNames(), alResult.getObservationTypes());
                model.setNumberOfObservations(alResult.getNumObservation());
                model.setRewardVariablesCount(rewardVariables.size());
                model.setModelType(ModelType.fromName(alResult.getModelType()).getValue());
                model.setNumberOfAgents(alResult.getNumberOfAgents());
            }
            uploadALPWizardPanel.setIsValidModel(ModelUtils.isValidModel(model));

            modelBinder.readBean(model);
            modelService.addDraftModelToProject(model, project.getId(), "");
            RewardVariablesUtils.copyGoalsFromPreviousModel(rewardVariablesDAO, modelDAO, model.getProjectId(), model.getId(), rewardVariables);
            rewardVariablesDAO.updateModelAndRewardVariables(model, rewardVariables);
            observationDAO.updateModelObservations(model.getId(), observationList);
            segmentIntegrator.modelImported(true);
        }));
    }

    @Override
    public void setParameter(BeforeEvent event, @WildcardParameter String parameter) {
        String[] segments = parameter.split("/");
        uploadMode = UploadMode.FOLDER;

        if (NumberUtils.isDigits(segments[PROJECT_ID_SEGMENT])) {
            this.projectId = Long.parseLong(segments[PROJECT_ID_SEGMENT]);
        }
        if (segments.length > 1) {
            uploadMode = UploadMode.getEnumFromValue(segments[UPLOAD_MODE_SEGMENT]).orElse(UploadMode.FOLDER);
            if (uploadMode == UploadMode.RESUME) {
                modelId = Long.parseLong(segments[MODEL_ID_SEGMENT]);
            }
        }
    }
}
