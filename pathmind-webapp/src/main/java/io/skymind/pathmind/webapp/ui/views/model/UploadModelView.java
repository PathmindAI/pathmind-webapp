package io.skymind.pathmind.webapp.ui.views.model;

import static io.skymind.pathmind.webapp.ui.constants.CssMindPathStyles.SECTION_TITLE_LABEL;
import static io.skymind.pathmind.webapp.ui.constants.CssMindPathStyles.SECTION_TITLE_LABEL_REGULAR_FONT_WEIGHT;
import static io.skymind.pathmind.webapp.ui.constants.CssMindPathStyles.SECTION_SUBTITLE_LABEL;
import static io.skymind.pathmind.webapp.ui.constants.CssMindPathStyles.PROJECT_TITLE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.utils.ModelUtils;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.bus.events.ExperimentCreatedBusEvent;
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

import io.skymind.pathmind.db.dao.ProjectDAO;
import io.skymind.pathmind.services.ModelService;
import io.skymind.pathmind.services.project.AnylogicFileCheckResult;
import io.skymind.pathmind.services.project.FileCheckResult;
import io.skymind.pathmind.services.project.ProjectFileCheckService;
import io.skymind.pathmind.services.project.StatusUpdater;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.shared.data.Project;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.webapp.exception.InvalidDataException;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.utils.FormUtils;
import io.skymind.pathmind.webapp.ui.utils.PushUtils;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import io.skymind.pathmind.webapp.ui.views.experiment.NewExperimentView;
import io.skymind.pathmind.webapp.ui.views.model.components.ModelDetailsWizardPanel;
import io.skymind.pathmind.webapp.ui.views.model.components.RewardVariablesPanel;
import io.skymind.pathmind.webapp.ui.views.model.components.UploadModelWizardPanel;

@Route(value = Routes.UPLOAD_MODEL, layout = MainLayout.class)
public class UploadModelView extends PathMindDefaultView implements StatusUpdater, HasUrlParameter<String>, BeforeLeaveObserver {

	private static final int PROJECT_ID_SEGMENT = 0;
 	private static final int UPLOAD_MODE_SEGMENT = 1;
	private static final int MODEL_ID_SEGMENT = 2;

	@Autowired
	private ProjectDAO projectDAO;

	@Autowired
    private ExperimentDAO experimentDAO;

	@Autowired
	private ModelService modelService;

	@Autowired
	private ProjectFileCheckService projectFileCheckService;

	@Autowired
	private SegmentIntegrator segmentIntegrator;
	
	private Model model;

	private List<RewardVariable> rewardVariables = new ArrayList<>();

	private Binder<Model> modelBinder;

	private UploadModelWizardPanel uploadModelWizardPanel;
	private ModelDetailsWizardPanel modelDetailsWizardPanel;
	private RewardVariablesPanel rewardVariablesPanel;

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

		uploadModelWizardPanel = new UploadModelWizardPanel(model, uploadMode);
		modelDetailsWizardPanel = new ModelDetailsWizardPanel(modelBinder, isResumeUpload());
		rewardVariablesPanel = new RewardVariablesPanel();

		modelBinder.readBean(model);

		wizardPanels = Arrays.asList(
				uploadModelWizardPanel,
				modelDetailsWizardPanel,
				rewardVariablesPanel);

		if (isResumeUpload()) {
			setVisibleWizardPanel(modelDetailsWizardPanel);
		}
		else {
			setVisibleWizardPanel(uploadModelWizardPanel);
		}

		uploadModelWizardPanel.addFileUploadCompletedListener(() -> handleUploadWizardClicked());
		modelDetailsWizardPanel.addButtonClickListener(click -> handleMoreDetailsClicked());
		rewardVariablesPanel.addButtonClickListener(click -> handleRewardVariablesClicked());

		Div sectionTitleWrapper = new Div();
		
		sectionTitleWrapper.add(
			LabelFactory.createLabel("Project: ", SECTION_TITLE_LABEL),
			LabelFactory.createLabel(project.getName(), SECTION_TITLE_LABEL_REGULAR_FONT_WEIGHT, SECTION_SUBTITLE_LABEL)
		);
		sectionTitleWrapper.addClassName(PROJECT_TITLE);

		VerticalLayout wrapper = new VerticalLayout(
				sectionTitleWrapper,
				uploadModelWizardPanel,
				modelDetailsWizardPanel,
				rewardVariablesPanel);

		wrapper.addClassName("view-section");
		wrapper.setSpacing(false);
		addClassName("upload-model-view");
		return wrapper;
	}

	private void autosaveRewardVariables() {
		if (!rewardVariablesPanel.isInputValueValid()) {
			return;
		}

		segmentIntegrator.modelDraftSaved();
		List<RewardVariable> rewardVariables = rewardVariablesPanel.getRewardVariables();
		modelService.updateModelRewardVariables(model, rewardVariables);
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
        if (rewardVariablesPanel.isVisible()) {
            autosaveRewardVariables();
        }
		action.proceed();
    }

	@Override
    protected void initLoadData() throws InvalidDataException {
		if (isResumeUpload()) {
			this.model = modelService.getModel(modelId)
					.orElseThrow(() -> new InvalidDataException("Attempted to access Invalid model: " + modelId));
			this.rewardVariables = modelService.getModelRewardVariables(modelId);
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
		if (rewardVariablesPanel.isInputValueValid()) {
			saveAndNavigateToNewExperiment();
		}
	}

	private void handleMoreDetailsClicked()
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

        List<RewardVariable> rewardVariableList = rewardVariablesPanel.getRewardVariables();
		modelService.updateModelRewardVariables(model, rewardVariableList);

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

			modelBinder.readBean(model);
			modelService.addDraftModelToProject(model, project.getId(), "");
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

	public static String createResumeUploadTarget(Project project, Model model) {
		return String.format("%s/%s/%s", project.getId(), UploadMode.RESUME, model.getId());
	}
}
