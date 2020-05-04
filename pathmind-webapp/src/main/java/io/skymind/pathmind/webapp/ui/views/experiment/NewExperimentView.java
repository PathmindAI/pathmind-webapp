package io.skymind.pathmind.webapp.ui.views.experiment;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.db.dao.RewardVariableDAO;
import io.skymind.pathmind.db.dao.UserDAO;
import io.skymind.pathmind.services.RewardValidationService;
import io.skymind.pathmind.services.TrainingService;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.shared.featureflag.Feature;
import io.skymind.pathmind.shared.featureflag.FeatureManager;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.shared.security.SecurityUtils;
import io.skymind.pathmind.webapp.exception.InvalidDataException;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.webapp.ui.components.navigation.Breadcrumbs;
import io.skymind.pathmind.webapp.ui.components.notesField.NotesField;
import io.skymind.pathmind.webapp.ui.constants.CssMindPathStyles;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.utils.FormUtils;
import io.skymind.pathmind.webapp.ui.utils.NotificationUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import io.skymind.pathmind.webapp.ui.views.experiment.components.RewardFunctionEditor;
import io.skymind.pathmind.webapp.ui.views.model.components.RewardVariablesTable;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@CssImport("./styles/views/new-experiment-view.css")
@Route(value = Routes.NEW_EXPERIMENT, layout = MainLayout.class)
public class NewExperimentView extends PathMindDefaultView implements HasUrlParameter<Long> {
	private long experimentId = -1;
	private Experiment experiment;
	private List<RewardVariable> rewardVariables;

	private Div errorMessageWrapper;
	private RewardFunctionEditor rewardFunctionEditor;
	private NotesField notesField;
	private RewardVariablesTable rewardVariablesTable;
	private Span unsavedChanges;
	private Span rewardEditorErrorLabel;
	private Button saveDraftButton;

	private Button startRunButton;

	@Autowired
	private ExperimentDAO experimentDAO;
	@Autowired
	private RewardVariableDAO rewardVariableDAO;
	@Autowired
	private TrainingService trainingService;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private SegmentIntegrator segmentIntegrator;
	@Autowired
	private RewardValidationService rewardValidationService;
	@Autowired
	private FeatureManager featureManager;

	private Binder<Experiment> binder;

    public NewExperimentView() {
        super();
        addClassName("new-experiment-view");
    }

    @Override
    protected Component getTitlePanel() {
        return new ScreenTitlePanel(createBreadcrumbs());
    }

	@Override
	protected Component getMainContent() {
		VerticalLayout mainContent = WrapperUtils.wrapSizeFullVertical(createMainPanel());
		binder = new Binder<>(Experiment.class);
		setupBinder();
		return mainContent;
	}

	private Component createMainPanel() {
		startRunButton = new Button("Train Policy", VaadinIcon.PLAY.create(), click -> handleStartRunButtonClicked());
		startRunButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		startRunButton.setEnabled(false);

		VerticalLayout mainPanel = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing();
		mainPanel.setSpacing(true);
		VerticalLayout panelTitle = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(LabelFactory.createLabel("Write your reward function", CssMindPathStyles.SECTION_TITLE_LABEL),
				LabelFactory.createLabel("To judge if an action is a good one, we calculate a reward score. " + "The reward score is based on the reward function.", CssMindPathStyles.SECTION_SUBTITLE_LABEL));
		panelTitle.setClassName("panel-title");

		unsavedChanges = LabelFactory.createLabel("Unsaved Draft!", "unsaved-draft-label");
		unsavedChanges.setVisible(false);
		saveDraftButton = getActionButton();
		HorizontalLayout header = WrapperUtils.wrapWidthFullBetweenHorizontal(LabelFactory.createLabel("Reward Function", CssMindPathStyles.BOLD_LABEL), unsavedChanges,
				saveDraftButton);
		header.getStyle().set("align-items", "center");

		HorizontalLayout rewardFunctionWrapper = WrapperUtils.wrapSizeFullBetweenHorizontal(getRewardFnEditorPanel(), getRewardVariableNamesPanel());
		rewardFunctionWrapper.setClassName("reward-function-wrapper");
		rewardFunctionWrapper.setSpacing(true);
		rewardFunctionWrapper.setPadding(true);

		VerticalLayout rewardFnEditorPanel = WrapperUtils.wrapSizeFullVertical(header, rewardFunctionWrapper);
		rewardFnEditorPanel.addClassName("reward-fn-panel-container");
		rewardFnEditorPanel.setPadding(false);
		rewardFnEditorPanel.setSpacing(false);

		HorizontalLayout errorAndNotesContaner = WrapperUtils.wrapWidthFullHorizontal(getErrorsPanel(), createNotesField());
		errorAndNotesContaner.setClassName("error-and-notes-container");

		mainPanel.add(WrapperUtils.wrapWidthFullBetweenHorizontal(panelTitle, startRunButton), rewardFnEditorPanel, errorAndNotesContaner);
		mainPanel.setClassName("view-section");
		return mainPanel;
	}

	private VerticalLayout getRewardFnEditorPanel() {
		rewardFunctionEditor = new RewardFunctionEditor();
		rewardFunctionEditor.addValueChangeListener(changeEvent -> {
			unsavedChanges.setVisible(true);
			final List<String> errors = rewardValidationService.validateRewardFunction(changeEvent.getValue());
			final String errorText = String.join("\n", errors);
			final String wrapperClassName = (errorText.length() == 0) ? "noError" : "hasError";

			errorMessageWrapper.removeAll();
			if ((errorText.length() == 0)) {
				errorMessageWrapper.add(new Icon(VaadinIcon.CHECK), new Span("No Errors"));
			} else {
				errorMessageWrapper.setText(errorText);
			}
			errorMessageWrapper.removeClassNames("hasError", "noError");
			errorMessageWrapper.addClassName(wrapperClassName);
			if (changeEvent.getValue().length() > 1000) {
				rewardEditorErrorLabel.setVisible(true);
			}
			else {
				rewardEditorErrorLabel.setVisible(false);
			}

			startRunButton.setEnabled(canStartTraining());
			saveDraftButton.setEnabled(canSaveDataInDB());
		});
		rewardEditorErrorLabel = LabelFactory.createLabel("Reward Function must not exceed 1000 characters", "reward-editor-error");
		rewardEditorErrorLabel.setVisible(false);
		VerticalLayout rewardFnEditorPanel = WrapperUtils.wrapSizeFullVertical(rewardEditorErrorLabel, rewardFunctionEditor);
		rewardFnEditorPanel.addClassName("reward-fn-editor-panel");
		rewardFnEditorPanel.setPadding(false);
		return rewardFnEditorPanel;
	}

	private boolean canStartTraining() {
    	return errorMessageWrapper.hasClassName("noError") && canSaveDataInDB();
	}

	private boolean canSaveDataInDB() {
    	return rewardFunctionEditor.getValue().length() <= 1000 && !rewardVariablesTable.isInvalid();
	}

	private Component getErrorsPanel() {
		errorMessageWrapper = new Div();
		errorMessageWrapper.addClassName("error-message-wrapper");
		Div errorsPanel = new Div(LabelFactory.createLabel("Errors", CssMindPathStyles.BOLD_LABEL), errorMessageWrapper);
		errorsPanel.addClassName("errors-wrapper");
		return errorsPanel;
	}

	private void setupBinder() {
		binder.forField(rewardFunctionEditor)
				.asRequired()
				.bind(Experiment::getRewardFunction, Experiment::setRewardFunction);
	}

	private RewardVariablesTable getRewardVariableNamesPanel() {
		rewardVariablesTable = new RewardVariablesTable();
		rewardVariablesTable.setCodeEditorMode();
		rewardVariablesTable.setSizeFull();
		rewardVariablesTable.addValueChangeListener(evt -> handleRewardVariableNameChanged(evt.getValue()));
		return rewardVariablesTable;
	}

	private void handleRewardVariableNameChanged(List<RewardVariable> updatedRewardVariables) {
		unsavedChanges.setVisible(true);
		rewardFunctionEditor.setVariableNames(updatedRewardVariables);
		startRunButton.setEnabled(canStartTraining());
		saveDraftButton.setEnabled(canSaveDataInDB());
	}

	private void handleStartRunButtonClicked() {
		if (!FormUtils.isValidForm(binder, experiment)) {
			return;
		}

		List<RewardVariable> rewardVariables = rewardVariablesTable.getValue();
		if (rewardVariables != null) {
			rewardVariables.forEach(rv -> rv.setModelId(experiment.getModelId()));
			rewardVariableDAO.saveRewardVariables(rewardVariables);
		}
		experimentDAO.updateExperiment(experiment);
		segmentIntegrator.rewardFuntionCreated();

		trainingService.startRun(experiment);
		segmentIntegrator.discoveryRunStarted();

		getUI().ifPresent(ui -> ui.navigate(ExperimentView.class, experimentId));
	}

	private Button getActionButton() {
		return new Button("Save Draft", click -> handleSaveDraftClicked());
	}

	private void handleSaveDraftClicked() {
		List<RewardVariable> rewardVariables = rewardVariablesTable.getValue();
		if (rewardVariables != null) {
			rewardVariables.forEach(rv -> rv.setModelId(experiment.getModelId()));
			rewardVariableDAO.saveRewardVariables(rewardVariables);
		}
		experimentDAO.updateExperiment(experiment);
		segmentIntegrator.draftSaved();
		unsavedChanges.setVisible(false);
		NotificationUtils.showSuccess("Draft successfully saved");
	}

	private Breadcrumbs createBreadcrumbs() {
		return new Breadcrumbs(experiment.getProject(), experiment.getModel(), experiment);
	}

	private NotesField createNotesField() {
		notesField = new NotesField(
			"Experiment Notes",
			experiment.getUserNotes(),
			updatedNotes -> {
				experiment.setUserNotes(updatedNotes);
				experimentDAO.updateUserNotes(experimentId, updatedNotes);
				NotificationUtils.showSuccess("Notes saved");
				segmentIntegrator.addedNotesNewExperimentView();
			}
		);
		notesField.setPlaceholder("Add Notes (optional)");
		return notesField;
	}

	@Override
	public void setParameter(BeforeEvent event, Long experimentId) {
		this.experimentId = experimentId;
	}

	@Override
	protected void initLoadData() {
		experiment = experimentDAO.getExperimentIfAllowed(experimentId, SecurityUtils.getUserId())
				.orElseThrow(() -> new InvalidDataException("Attempted to access Experiment: " + experimentId));
		rewardVariables = rewardVariableDAO.getRewardVariablesForModel(experiment.getModelId());
	}

	@Override
	protected void initScreen(BeforeEnterEvent event) {
		binder.setBean(experiment);
		if (featureManager.isEnabled(Feature.REWARD_VARIABLES_FEATURE)) {
			if (!rewardVariables.isEmpty()) {
				rewardFunctionEditor.setVariableNames(rewardVariables);
				rewardVariablesTable.setValue(rewardVariables);
			} else {
				rewardVariablesTable.setVariableSize(experiment.getModel().getRewardVariablesCount());
			}
		} else {
			rewardVariablesTable.setVisible(false);
		}
		unsavedChanges.setVisible(false);
	}
}
