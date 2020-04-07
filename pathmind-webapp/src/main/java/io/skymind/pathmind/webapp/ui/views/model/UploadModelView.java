package io.skymind.pathmind.webapp.ui.views.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.WildcardParameter;

import io.skymind.pathmind.db.dao.ProjectDAO;
import io.skymind.pathmind.db.dao.RewardVariableDAO;
import io.skymind.pathmind.services.ModelService;
import io.skymind.pathmind.services.project.AnylogicFileCheckResult;
import io.skymind.pathmind.services.project.FileCheckResult;
import io.skymind.pathmind.services.project.ProjectFileCheckService;
import io.skymind.pathmind.services.project.StatusUpdater;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.shared.data.Project;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.shared.security.PathmindUserDetails;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.webapp.data.utils.ModelUtils;
import io.skymind.pathmind.webapp.exception.InvalidDataException;
import io.skymind.pathmind.shared.featureflag.Feature;
import io.skymind.pathmind.shared.featureflag.FeatureManager;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.utils.FormUtils;
import io.skymind.pathmind.webapp.ui.utils.NotificationUtils;
import io.skymind.pathmind.webapp.ui.utils.PushUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import io.skymind.pathmind.webapp.ui.views.experiment.NewExperimentView;
import io.skymind.pathmind.webapp.ui.views.model.components.ModelDetailsWizardPanel;
import io.skymind.pathmind.webapp.ui.views.model.components.RewardVariablesPanel;
import io.skymind.pathmind.webapp.ui.views.model.components.UploadModelWizardPanel;
import lombok.extern.slf4j.Slf4j;

@Route(value = Routes.UPLOAD_MODEL, layout = MainLayout.class)
@Slf4j
public class UploadModelView extends PathMindDefaultView implements StatusUpdater, HasUrlParameter<String> {

	private static final int PROJECT_ID_SEGMENT = 0;
 	private static final int UPLOAD_MODE_SEGMENT = 1;
	private static final int MODEL_ID_SEGMENT = 2;

	@Autowired
	private ProjectDAO projectDAO;

	@Autowired
	private ModelService modelService;

	@Autowired
	private ProjectFileCheckService projectFileCheckService;

	@Autowired
	private SegmentIntegrator segmentIntegrator;
	
	@Autowired
	private FeatureManager featureManager;

	private Model model;

	private List<RewardVariable> rewardVariables = new ArrayList<>();

	private Binder<Model> modelBinder;

	private UI ui;

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
		this.ui = UI.getCurrent();
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
		modelDetailsWizardPanel.addSaveDraftClickListener(click -> handleSaveDraftClicked());
		rewardVariablesPanel.addButtonClickListener(click -> handleRewardVariablesClicked());
		rewardVariablesPanel.addSaveDraftClickListener(click -> handleRewardVariablesSaveDraftClicked());

		VerticalLayout wrapper = WrapperUtils.wrapFormCenterVertical(
				uploadModelWizardPanel,
				modelDetailsWizardPanel,
				rewardVariablesPanel);

		wrapper.getStyle().set("width", "auto");
		wrapper.getStyle().set("padding-top", "var(--lumo-space-xxl)");
		wrapper.addClassName("upload-model-view");
		return wrapper;
	}

	private void handleRewardVariablesSaveDraftClicked() {
		segmentIntegrator.modelDraftSaved();
		List<RewardVariable> rewardVariables = rewardVariablesPanel.getRewardVariables();
		modelService.updateModelRewardVariables(model, rewardVariables);
		NotificationUtils.showSuccess("Draft successfully saved");
	}

	private void handleSaveDraftClicked() {
		segmentIntegrator.modelDraftSaved();
		final String modelNotes = modelDetailsWizardPanel.getModelNotes();
		if (model.getId() == -1) {
			modelService.addDraftModelToProject(model, project.getId(), modelNotes);
		}
		else {
			modelService.updateDraftModel(model, modelNotes);
		}
		NotificationUtils.showSuccess("Draft successfully saved");
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
		uploadModelWizardPanel.setProjectName(project.getName());
		modelDetailsWizardPanel.setProjectName(project.getName());
		rewardVariablesPanel.setProjectName(project.getName());
	}

	private void handleRewardVariablesClicked() {
		saveAndNavigateToNewExperiment();
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
		
		if (!featureManager.isEnabled(Feature.REWARD_VARIABLES_FEATURE)) {
			saveAndNavigateToNewExperiment();
		} else {
			modelService.updateDraftModel(model, modelNotes);
			rewardVariablesPanel.setupRewardVariablesTable(model.getRewardVariablesCount(), rewardVariables);
			setVisibleWizardPanel(rewardVariablesPanel);
		}
	}
	
	private void saveAndNavigateToNewExperiment() {
		experimentId = modelService.resumeModelCreation(model, modelNotes);

		List<RewardVariable> rewardVariableList = rewardVariablesPanel.getRewardVariables();
		modelService.updateModelRewardVariables(model, rewardVariableList);

		UI.getCurrent().navigate(NewExperimentView.class, experimentId);
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
		PushUtils.push(ui, () ->
			uploadModelWizardPanel.setFileCheckStatusProgressBarValue(percentage));
	}

	@Override
	public void updateError(String error) {
		PushUtils.push(ui, () -> {
			uploadModelWizardPanel.setFileCheckStatusProgressBarValue(1.0);
			uploadModelWizardPanel.setError(error);
			segmentIntegrator.modelImported(false);
		});
	}

	@Override
	public void fileSuccessfullyVerified(FileCheckResult result) {
		PushUtils.push(ui, () -> {
			uploadModelWizardPanel.setFileCheckStatusProgressBarValue(1.0);
			setVisibleWizardPanel(modelDetailsWizardPanel);

			if (result != null) {
				model.setNumberOfPossibleActions(((AnylogicFileCheckResult) (result)).getNumAction());
				model.setNumberOfObservations(((AnylogicFileCheckResult) (result)).getNumObservation());
				model.setRewardVariablesCount(((AnylogicFileCheckResult) (result)).getRewardVariablesCount());
			}

			modelBinder.readBean(model);
			modelService.addDraftModelToProject(model, project.getId(), "");
			segmentIntegrator.modelImported(true);
		});
	}

	@Override
	protected boolean isAccessAllowedForUser() {
		return true;
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
