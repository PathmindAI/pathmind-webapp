package io.skymind.pathmind.webapp.ui.views.model;

import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.SECTION_TITLE_LABEL;
import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.SECTION_TITLE_LABEL_REGULAR_FONT_WEIGHT;
import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.SECTION_SUBTITLE_LABEL;
import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.PROJECT_TITLE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import io.skymind.pathmind.shared.data.Action;
import com.vaadin.flow.component.html.Span;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.utils.ModelUtils;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.bus.events.ExperimentCreatedBusEvent;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.WildcardParameter;
import com.vaadin.flow.router.BeforeLeaveEvent.ContinueNavigationAction;

import io.skymind.pathmind.db.dao.ActionDAO;
import io.skymind.pathmind.db.dao.ObservationDAO;
import io.skymind.pathmind.db.dao.ProjectDAO;
import io.skymind.pathmind.db.dao.RewardVariableDAO;
import io.skymind.pathmind.services.ModelService;
import io.skymind.pathmind.services.project.AnylogicFileCheckResult;
import io.skymind.pathmind.services.project.FileCheckResult;
import io.skymind.pathmind.services.project.ProjectFileCheckService;
import io.skymind.pathmind.services.project.StatusUpdater;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.shared.data.Observation;
import io.skymind.pathmind.shared.data.Project;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.shared.featureflag.Feature;
import io.skymind.pathmind.shared.featureflag.FeatureManager;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.webapp.exception.InvalidDataException;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.utils.FormUtils;
import io.skymind.pathmind.webapp.ui.utils.PushUtils;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import io.skymind.pathmind.webapp.ui.views.experiment.NewExperimentView;
import io.skymind.pathmind.webapp.ui.views.model.components.ActionsPanel;
import io.skymind.pathmind.webapp.ui.views.model.components.ModelDetailsWizardPanel;
import io.skymind.pathmind.webapp.ui.views.model.components.ObservationsPanel;
import io.skymind.pathmind.webapp.ui.views.model.components.RewardVariablesPanel;
import io.skymind.pathmind.webapp.ui.views.model.components.UploadModelWizardPanel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.unit.DataSize;

@Route(value = Routes.UPLOAD_MODEL, layout = MainLayout.class)
public class UploadModelView extends PathMindDefaultView implements StatusUpdater, HasUrlParameter<String>, BeforeLeaveObserver {

	private static final int PROJECT_ID_SEGMENT = 0;
 	private static final int UPLOAD_MODE_SEGMENT = 1;
	private static final int MODEL_ID_SEGMENT = 2;

	@Autowired
	private ProjectDAO projectDAO;

	@Autowired
	private ModelService modelService;

	@Autowired
	private RewardVariableDAO rewardVariablesDAO;

	@Autowired
	private ActionDAO actionDAO;

	@Autowired
	private ObservationDAO observationDAO;

	@Autowired
	private ProjectFileCheckService projectFileCheckService;

	@Autowired
	private SegmentIntegrator segmentIntegrator;

	@Autowired
	private FeatureManager featureManager;

	@Autowired
    private ModelCheckerService modelCheckerService;

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSizeAsStr;

	private Model model;

	private List<RewardVariable> rewardVariables = new ArrayList<>();
    private List<Action> actions = new ArrayList<>();
    private List<Observation> observations = new ArrayList<>();

	private Binder<Model> modelBinder;

	private UploadModelWizardPanel uploadModelWizardPanel;
	private ModelDetailsWizardPanel modelDetailsWizardPanel;
	private RewardVariablesPanel rewardVariablesPanel;
	private ActionsPanel actionsPanel;
	private ObservationsPanel observationsPanel;

	private List<Component> wizardPanels;

	private long projectId;
	private long modelId = -1;
	private long experimentId;
	private String modelNotes;
	private Project project;
	
	private UploadMode uploadMode;

	public UploadModelView()
	{
		super();
	}

	protected Component getMainContent()
	{
		modelBinder = new Binder<>(Model.class);

		uploadModelWizardPanel = new UploadModelWizardPanel(model, uploadMode, (int)DataSize.parse(maxFileSizeAsStr).toBytes());
		modelDetailsWizardPanel = new ModelDetailsWizardPanel(modelBinder, isResumeUpload(), ModelUtils.isValidModel(model));
		rewardVariablesPanel = new RewardVariablesPanel();
		actionsPanel = new ActionsPanel();
		observationsPanel = new ObservationsPanel();

		modelBinder.readBean(model);

		wizardPanels = Arrays.asList(
				uploadModelWizardPanel,
				modelDetailsWizardPanel,
				rewardVariablesPanel,
				actionsPanel,
				observationsPanel);

		if (isResumeUpload()) {
			setVisibleWizardPanel(modelDetailsWizardPanel);
		}
		else {
			setVisibleWizardPanel(uploadModelWizardPanel);
		}

		uploadModelWizardPanel.addFileUploadCompletedListener(() -> handleUploadWizardClicked());
		uploadModelWizardPanel.addFileUploadFailedListener(errors -> handleUploadFailed(errors));
		modelDetailsWizardPanel.addButtonClickListener(click -> handleModelDetailsClicked());
		rewardVariablesPanel.addButtonClickListener(click -> handleRewardVariablesClicked());
		actionsPanel.addButtonClickListener(click -> handleActionsClicked());
		observationsPanel.addButtonClickListener(click -> handleObservationsClicked());

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
        sections.add(sectionTitleWrapper);
        sections.add(uploadModelWizardPanel);
        if (isResumeUpload() && !ModelUtils.isValidModel(model)) {
            sections.add(invalidModelErrorLabel);
        }
        sections.add(modelDetailsWizardPanel);
        sections.add(rewardVariablesPanel);
        sections.add(actionsPanel);
        sections.add(observationsPanel);
        VerticalLayout wrapper = new VerticalLayout(
                sections.toArray(new Component[0]));

		wrapper.addClassName("view-section");
		wrapper.setSpacing(false);
		addClassName("upload-model-view");
		return wrapper;
	}

    private void handleUploadFailed(Collection<String> errors) {
        uploadModelWizardPanel.showFileCheckPanel();
	    this.updateError(errors.iterator().next());
    }

	private void autosaveModelDetails() {
		if(!FormUtils.isValidForm(modelBinder, model)) {
			return;
		}

		segmentIntegrator.modelDraftSaved();
		final String modelNotes = modelDetailsWizardPanel.getModelNotes();
		if (model.getId() == -1) {
			modelService.addDraftModelToProject(model, project.getId(), modelNotes);
		}
		else {
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
			this.actions = actionDAO.getActionsForModel(modelId);
			this.observations = observationDAO.getObservationsForModel(modelId);
		}
		else {
			this.model = ModelUtils.generateNewDefaultModel();
			model.setProjectId(projectId);
		}
		project = projectDAO.getProject(projectId)
				.orElseThrow(() -> new InvalidDataException("Attempted to access Experiment: " + projectId));
    }

	private boolean isResumeUpload() {
		return modelId != -1;
	}

	@Override
	protected void initScreen(BeforeEnterEvent event) {
	}

	private void handleRewardVariablesClicked() {
		if (featureManager.isEnabled(Feature.ACTIONS_AND_OBSERVATION_FEATURE)) {
            actionsPanel.setupActionsTable(model.getNumberOfPossibleActions(), actions);
            setVisibleWizardPanel(actionsPanel);
        } else {
            saveAndNavigateToNewExperiment();
        }
	}
	private void handleActionsClicked() {
	    if (!actionsPanel.isInputValueValid()) {
	        return;
	    }
	    actions = actionsPanel.getActions();
	    actionDAO.updateModelActions(model.getId(), actions);
	    observationsPanel.setupObservationTable(model.getNumberOfObservations(), observations);
	    setVisibleWizardPanel(observationsPanel);
	}
	private void handleObservationsClicked() {
	    if (!actionsPanel.isInputValueValid()) {
	        return;
	    }
	    saveAndNavigateToNewExperiment();
	}

	private void handleModelDetailsClicked()
	{
		if(!FormUtils.isValidForm(modelBinder, model)) {
			return;
		}

		model.setDraft(false);
		modelNotes = modelDetailsWizardPanel.getModelNotes();

		if (!modelNotes.isEmpty()) {
			segmentIntegrator.addedNotesUploadModelView();
		}

		modelService.updateDraftModel(model, modelNotes);
		rewardVariablesPanel.setupRewardVariablesTable(model.getRewardVariablesCount(), rewardVariables);
		setVisibleWizardPanel(rewardVariablesPanel);
	}
	
	private void saveAndNavigateToNewExperiment() {
		Experiment experiment = modelService.resumeModelCreation(model, modelNotes);
		experimentId = experiment.getId();
        EventBus.post(new ExperimentCreatedBusEvent(experiment));

        if (featureManager.isEnabled(Feature.ACTIONS_AND_OBSERVATION_FEATURE)) {
            actions = actionsPanel.getActions();
            actionDAO.updateModelActions(model.getId(), actions);
            observations = observationsPanel.getObservations();
            observationDAO.updateModelObservations(model.getId(), observations);
        }

		getUI().ifPresent(ui -> ui.navigate(NewExperimentView.class, experimentId));
	}

    private void handleUploadWizardClicked() {
		uploadModelWizardPanel.showFileCheckPanel();
		projectFileCheckService.checkFile(this, model.getFile());
	}

	private void setVisibleWizardPanel(Component wizardPanel) {
		wizardPanels
				.forEach(panel -> panel.setVisible(panel.equals(wizardPanel)));
	}

	/**
	 * The titles are in the individual panels as the title changes based on the state.
	 */
	protected VerticalLayout getTitlePanel() {
		return null;
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
		}));
	}

	@Override
	public void fileSuccessfullyVerified(FileCheckResult result) {
		getUI().ifPresent(ui -> PushUtils.push(ui, () -> {
			uploadModelWizardPanel.setFileCheckStatusProgressBarValue(1.0);
			setVisibleWizardPanel(modelDetailsWizardPanel);

			if (result != null) {
				model.setNumberOfPossibleActions(((AnylogicFileCheckResult) (result)).getNumAction());
				model.setNumberOfObservations(((AnylogicFileCheckResult) (result)).getNumObservation());
				model.setRewardVariablesCount(((AnylogicFileCheckResult) (result)).getRewardVariablesCount());
				model.setActionTupleSize(((AnylogicFileCheckResult) (result)).getActionTupleSize());
			}
			modelDetailsWizardPanel.setIsValidModel(ModelUtils.isValidModel(model));

			modelBinder.readBean(model);
			modelService.addDraftModelToProject(model, project.getId(), "");
			rewardVariables = createDummyRewardVariables(model.getId(), model.getRewardVariablesCount());
			rewardVariablesDAO.updateModelRewardVariables(model.getId(), rewardVariables);
			segmentIntegrator.modelImported(true);
		}));
	}

	private List<RewardVariable> createDummyRewardVariables(long modelId, int size) {
        List<RewardVariable> rewardVariables = new ArrayList<>();
        String[] dataTypes = new String[] {"double", "long", "float", "int[]"};
        for (int i = 0; i < size; i++) {
            RewardVariable rv = new RewardVariable();
            rv.setArrayIndex(i);
            rv.setModelId(modelId);
            rv.setName("var" + i);
            rv.setDataType(dataTypes[i % 4]);
            rewardVariables.add(rv);
        }
        return rewardVariables;
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

	public static String createResumeUploadTarget(Project project, Model model) {
		return String.format("%s/%s/%s", project.getId(), UploadMode.RESUME, model.getId());
	}
}
