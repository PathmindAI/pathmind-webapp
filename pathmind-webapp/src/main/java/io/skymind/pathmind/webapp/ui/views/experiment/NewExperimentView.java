package io.skymind.pathmind.webapp.ui.views.experiment;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
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
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
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
import io.skymind.pathmind.webapp.ui.views.experiment.components.ExperimentsNavbar;
import io.skymind.pathmind.webapp.ui.views.experiment.components.RewardFunctionEditor;
import io.skymind.pathmind.webapp.ui.views.model.components.RewardVariablesTable;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CssImport("./styles/views/new-experiment-view.css")
@Route(value = Routes.NEW_EXPERIMENT, layout = MainLayout.class)
public class NewExperimentView extends PathMindDefaultView implements HasUrlParameter<Long> {

	// We have to use a lock object rather than the experiment because we are changing it's reference which makes it not thread safe. As well we cannot lock
	// on this because part of the synchronization is in the eventbus listener in a subclass (which is also why we can't use synchronize on the method.
	private Object experimentLock = new Object();

	private long experimentId = -1;
	private long modelId = -1;
	private List<RewardVariable> rewardVariables;
	private Experiment experiment;
	private List<Experiment> experiments = new ArrayList<>();

	private Div errorMessageWrapper;
	private RewardFunctionEditor rewardFunctionEditor;
	private ExperimentsNavbar experimentsNavbar;
	private NotesField notesField;
	private RewardVariablesTable rewardVariablesTable;
	private Span unsavedChanges;
	private Span notesSavedHint;

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

	private Breadcrumbs pageBreadcrumbs;
	private Binder<Experiment> binder;

    public NewExperimentView() {
        super();
        addClassName("new-experiment-view");
    }

    @Override
    protected Component getTitlePanel() {
		pageBreadcrumbs = createBreadcrumbs();
		return new ScreenTitlePanel(pageBreadcrumbs);
    }

	@Override
	protected Component getMainContent() {
		HorizontalLayout mainContent = createMainPanel();
		binder = new Binder<>(Experiment.class);
		setupBinder();
		return mainContent;
	}

	private HorizontalLayout createMainPanel() {
		experimentsNavbar = new ExperimentsNavbar(experimentDAO, experiment.getModelId(), selectedExperiment -> selectExperiment(selectedExperiment));
		
		startRunButton = new Button("Train Policy", VaadinIcon.PLAY.create(), click -> handleStartRunButtonClicked());
		startRunButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		startRunButton.setEnabled(false);

		VerticalLayout mainPanel = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing();
		mainPanel.setSpacing(true);
		VerticalLayout panelTitle = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(LabelFactory.createLabel("Write your reward function", CssMindPathStyles.SECTION_TITLE_LABEL),
				LabelFactory.createLabel("To judge if an action is a good one, we calculate a reward score. " + "The reward score is based on the reward function.", CssMindPathStyles.SECTION_SUBTITLE_LABEL));
		panelTitle.setClassName("panel-title");

		unsavedChanges = LabelFactory.createLabel("Unsaved changes!", "hint-label");
		unsavedChanges.setVisible(false);
		notesSavedHint = LabelFactory.createLabel("Notes saved!", "fade-out-hint-label");
		notesSavedHint.setVisible(false);

		VerticalLayout rewardFnPanel = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(LabelFactory.createLabel("Reward Function", CssMindPathStyles.BOLD_LABEL), getRewardFnEditorPanel());
		rewardFnPanel.addClassName("reward-fn-editor-panel");

		HorizontalLayout rewardFunctionWrapper = WrapperUtils.wrapSizeFullBetweenHorizontal(
				rewardFnPanel, 
				WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(LabelFactory.createLabel("Reward Variables", CssMindPathStyles.BOLD_LABEL), getRewardVariableNamesPanel()));
		rewardFunctionWrapper.setClassName("reward-function-wrapper");
		rewardFunctionWrapper.setPadding(false);
		rewardFunctionWrapper.setSpacing(true);

		HorizontalLayout errorAndNotesContaner = WrapperUtils.wrapWidthFullHorizontal(getErrorsPanel(), createNotesField());
		errorAndNotesContaner.setClassName("error-and-notes-container");

		VerticalLayout saveButtonAndHintsWrapper = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(getActionButton(), unsavedChanges, notesSavedHint);
		saveButtonAndHintsWrapper.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
		HorizontalLayout buttonsWrapper = new HorizontalLayout(saveButtonAndHintsWrapper, startRunButton);
		buttonsWrapper.setWidth(null);

		mainPanel.add(WrapperUtils.wrapWidthFullBetweenHorizontal(panelTitle, buttonsWrapper), rewardFunctionWrapper, errorAndNotesContaner);
		mainPanel.setClassName("view-section");

		HorizontalLayout panelsWrapper = WrapperUtils.wrapWidthFullHorizontal(experimentsNavbar, mainPanel);
		panelsWrapper.setPadding(true);
		return panelsWrapper;
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
				startRunButton.setEnabled(true);
			} else {
				errorMessageWrapper.setText(errorText);
				startRunButton.setEnabled(false);
			}
			errorMessageWrapper.removeClassNames("hasError", "noError");
			errorMessageWrapper.addClassName(wrapperClassName);
		});

		VerticalLayout rewardFnEditorPanel = WrapperUtils.wrapSizeFullVertical(rewardFunctionEditor);
		rewardFnEditorPanel.addClassName("reward-fn-editor-panel");
		rewardFnEditorPanel.setPadding(false);
		return rewardFnEditorPanel;
	}
	private Component getErrorsPanel() {
		errorMessageWrapper = new Div();
		errorMessageWrapper.addClassName("error-message-wrapper");
		Div errorsPanel = new Div(LabelFactory.createLabel("Errors", CssMindPathStyles.BOLD_LABEL), errorMessageWrapper);
		errorsPanel.addClassName("errors-wrapper");
		return errorsPanel;
	}

	private void setupBinder() {
		binder.forField(rewardFunctionEditor).asRequired().bind(Experiment::getRewardFunction, Experiment::setRewardFunction);
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
		Button saveDraftButton = new Button("Save", click -> handleSaveDraftClicked());
		saveDraftButton.addThemeName("secondary");
		return saveDraftButton;
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
		notesSavedHint.setVisible(false);
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
				notesSavedHint.setVisible(true);
				// addClassName() only works for the first time when the class name is removed via JS; so JS is used instead
				notesSavedHint.getElement().executeJs("$0.classList.add('fade-in'); setTimeout(() => {$0.classList.remove('fade-in');}, 3000)");
				segmentIntegrator.addedNotesNewExperimentView();
			}
		);
		notesField.setPlaceholder("Add Notes (optional)");
		return notesField;
	}

	private void selectExperiment(Experiment selectedExperiment) {
		// The only reason I'm synchronizing here is in case an event is fired while it's still loading the data (which can take several seconds). We should still be on the
		// same experiment but just because right now loads can take up to several seconds I'm being extra cautious.
		synchronized (experimentLock) {
			experimentId = selectedExperiment.getId();
			experiment = experimentDAO.getExperiment(experimentId)
					.orElseThrow(() -> new InvalidDataException("Attempted to access Experiment: " + experimentId));
			loadExperimentData();
			updateScreenComponents();
			notesField.setNotesText(experiment.getUserNotes());
			pageBreadcrumbs.setText(3, "Experiment #" + experiment.getName());
			
			if (ExperimentUtils.isDraftRunType(selectedExperiment)) {
				getUI().ifPresent(ui -> ui.getPage().getHistory().pushState(null, "newExperiment/" + experimentId));
			} else {
				getUI().ifPresent(ui -> ui.navigate(ExperimentView.class, experimentId));
			}
		}
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
		loadExperimentData();
	}

	private void loadExperimentData() {
		modelId = experiment.getModelId();
		if (featureManager.isEnabled(Feature.REWARD_VARIABLES_FEATURE)) {
			rewardVariables = rewardVariableDAO.getRewardVariablesForModel(modelId);
		}
		if (!experiment.isArchived()) {
			experiments = experimentDAO.getExperimentsForModel(modelId).stream().filter(exp -> !exp.isArchived()).collect(Collectors.toList());
		}
	}

	@Override
	protected void initScreen(BeforeEnterEvent event) {
		updateScreenComponents();
		experimentsNavbar.setExperiments(event.getUI(), experiments, experiment);
	}

	private void updateScreenComponents() {
		binder.setBean(experiment);
		experimentsNavbar.setVisible(!experiment.isArchived());
		rewardFunctionEditor.setValue(experiment.getRewardFunction());		
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
		notesSavedHint.setVisible(false);
	}
}
