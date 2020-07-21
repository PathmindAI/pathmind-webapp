package io.skymind.pathmind.webapp.ui.views.experiment;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import io.skymind.pathmind.webapp.ui.views.model.NonTupleModelService;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
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
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveEvent.ContinueNavigationAction;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.Command;

import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.db.dao.RewardVariableDAO;
import io.skymind.pathmind.services.RewardValidationService;
import io.skymind.pathmind.services.TrainingService;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.bus.events.ExperimentCreatedBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.ExperimentCreatedSubscriber;
import io.skymind.pathmind.shared.security.SecurityUtils;
import io.skymind.pathmind.shared.utils.ModelUtils;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.exception.InvalidDataException;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.webapp.ui.components.navigation.Breadcrumbs;
import io.skymind.pathmind.webapp.ui.components.notesField.NotesField;
import io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.utils.*;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import io.skymind.pathmind.webapp.ui.views.experiment.components.ExperimentsNavbar;
import io.skymind.pathmind.webapp.ui.views.experiment.components.RewardFunctionEditor;
import io.skymind.pathmind.webapp.ui.views.model.ModelView;
import io.skymind.pathmind.webapp.ui.views.model.components.RewardVariablesTable;

@CssImport("./styles/views/new-experiment-view.css")
@Route(value = Routes.NEW_EXPERIMENT, layout = MainLayout.class)
public class NewExperimentView extends PathMindDefaultView implements HasUrlParameter<Long>, BeforeLeaveObserver {

    private final NewExperimentViewExperimentCreatedSubscriber experimentCreatedSubscriber;
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
	private Span rewardEditorErrorLabel;
	private Button unarchiveExperimentButton;
	private Button saveDraftButton;
	private Button startRunButton;

	private final int REWARD_FUNCTION_MAX_LENGTH = 65535;

	@Autowired
	private ExperimentDAO experimentDAO;
	@Autowired
	private RewardVariableDAO rewardVariableDAO;
	@Autowired
	private TrainingService trainingService;
	@Autowired
	private SegmentIntegrator segmentIntegrator;
	@Autowired
	private RewardValidationService rewardValidationService;
	@Autowired
    private NonTupleModelService nonTupleModelService;

	private Breadcrumbs pageBreadcrumbs;
	private Binder<Experiment> binder;

	public NewExperimentView() {
		super();
		addClassName("new-experiment-view");
        experimentCreatedSubscriber = new NewExperimentViewExperimentCreatedSubscriber();
	}

    @Override
    protected void onDetach(DetachEvent event) {
        EventBus.unsubscribe(experimentCreatedSubscriber);
    }

    @Override
    protected void onAttach(AttachEvent event) {
        EventBus.subscribe(experimentCreatedSubscriber);
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
		experimentsNavbar = new ExperimentsNavbar(experimentDAO, experiment.getModelId(), selectedExperiment -> selectExperiment(selectedExperiment), experimentToArchive -> archiveExperiment(experimentToArchive));

        unarchiveExperimentButton = new Button("Unarchive", VaadinIcon.ARROW_BACKWARD.create(), click -> unarchiveExperiment());
        unarchiveExperimentButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		startRunButton = new Button("Train Policy", VaadinIcon.PLAY.create(), click -> handleStartRunButtonClicked());
		startRunButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		startRunButton.setEnabled(false);
		
		saveDraftButton = new Button("Save", click -> handleSaveDraftClicked(() -> {}));
		saveDraftButton.setEnabled(false);

		VerticalLayout mainPanel = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing();
		mainPanel.setSpacing(true);
		VerticalLayout panelTitle = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(LabelFactory.createLabel("Write your reward function", CssPathmindStyles.SECTION_TITLE_LABEL),
				LabelFactory.createLabel("To judge if an action is a good one, we calculate a reward score. " + "The reward score is based on the reward function.", CssPathmindStyles.SECTION_SUBTITLE_LABEL));
		panelTitle.setClassName("panel-title");

		unsavedChanges = LabelFactory.createLabel("Unsaved changes!", "hint-label");
		unsavedChanges.setVisible(false);
		notesSavedHint = LabelFactory.createLabel("Notes saved!", "fade-out-hint-label");
		notesSavedHint.setVisible(false);

		VerticalLayout rewardFnPanel = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(LabelFactory.createLabel("Reward Function", CssPathmindStyles.BOLD_LABEL), getRewardFnEditorPanel());
		rewardFnPanel.addClassName("reward-fn-editor-panel");

        Span errorDescriptionLabel = nonTupleModelService.createNonTupleErrorLabel(experiment.getModel());

		HorizontalLayout rewardFunctionWrapper = WrapperUtils.wrapSizeFullBetweenHorizontal(
				rewardFnPanel, 
				WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(LabelFactory.createLabel("Reward Variables", CssPathmindStyles.BOLD_LABEL), getRewardVariableNamesPanel()));
		rewardFunctionWrapper.setClassName("reward-function-wrapper");
		rewardFunctionWrapper.setPadding(false);
		rewardFunctionWrapper.setSpacing(true);

		HorizontalLayout errorAndNotesContainer = WrapperUtils.wrapWidthFullHorizontal(getErrorsPanel(), createNotesField());
		errorAndNotesContainer.setClassName("error-and-notes-container");

		VerticalLayout saveButtonAndHintsWrapper = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(saveDraftButton, unsavedChanges, notesSavedHint);
		saveButtonAndHintsWrapper.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
		HorizontalLayout buttonsWrapper = new HorizontalLayout(saveButtonAndHintsWrapper, startRunButton, unarchiveExperimentButton);
		buttonsWrapper.setWidth(null);

		mainPanel.add(WrapperUtils.wrapWidthFullBetweenHorizontal(panelTitle, buttonsWrapper), errorDescriptionLabel, rewardFunctionWrapper, errorAndNotesContainer);
		mainPanel.setClassName("view-section");

		HorizontalLayout panelsWrapper = WrapperUtils.wrapWidthFullHorizontal(experimentsNavbar, mainPanel);
		panelsWrapper.setPadding(true);
		return panelsWrapper;
	}

	private VerticalLayout getRewardFnEditorPanel() {
		rewardFunctionEditor = new RewardFunctionEditor();
		rewardFunctionEditor.addValidationListener(evt -> {
		    errorMessageWrapper.removeAll();
		    final List<String> errors = new ArrayList<>();
		    if (!evt.isValid()) {
		        evt.getInvalidLineVariableIndexPairs().forEach(pair -> errors.add(String.format("ERROR: Line %s: Invalid variable index:%s", pair.getFirst(), pair.getSecond())));
		    } else {
		        errors.addAll(rewardValidationService.validateRewardFunction(rewardFunctionEditor.getValue()));
		    }
		    final String errorText = String.join("\n", errors);
		    final String wrapperClassName = (errorText.length() == 0) ? "noError" : "hasError";
		    if ((errorText.length() == 0)) {
		        errorMessageWrapper.add(new Icon(VaadinIcon.CHECK), new Span("No Errors"));
		    } else {
		        errorMessageWrapper.setText(errorText);
		    }
		    errorMessageWrapper.removeClassNames("hasError", "noError");
		    errorMessageWrapper.addClassName(wrapperClassName);
		    
		    startRunButton.setEnabled(canStartTraining());
		    saveDraftButton.setEnabled(canSaveDataInDB());
		    
		});
		rewardFunctionEditor.addValueChangeListener(changeEvent -> {
			unsavedChanges.setVisible(true);
			rewardEditorErrorLabel.setVisible(changeEvent.getValue().length() > REWARD_FUNCTION_MAX_LENGTH);
		});
		rewardEditorErrorLabel = LabelFactory.createLabel("Reward Function must not exceed " + REWARD_FUNCTION_MAX_LENGTH + " characters", "reward-editor-error");
		rewardEditorErrorLabel.setVisible(false);
		VerticalLayout rewardFnEditorPanel = WrapperUtils.wrapSizeFullVertical(rewardEditorErrorLabel, rewardFunctionEditor);
        rewardFnEditorPanel.setPadding(false);
        if (experiment.isArchived()) {
            rewardFnEditorPanel.setEnabled(false);
        }
		return rewardFnEditorPanel;
	}

	private boolean canStartTraining() {
		return ModelUtils.isTupleModel(experiment.getModel()) && errorMessageWrapper.hasClassName("noError") && canSaveDataInDB();
	}

	private boolean canSaveDataInDB() {
		return errorMessageWrapper.hasClassName("noError") && rewardFunctionEditor.getValue().length() <= REWARD_FUNCTION_MAX_LENGTH && !rewardVariablesTable.isInvalid();
	}

	private Component getErrorsPanel() {
		errorMessageWrapper = new Div();
		errorMessageWrapper.addClassName("error-message-wrapper");
		Div errorsPanel = new Div(LabelFactory.createLabel("Errors", CssPathmindStyles.BOLD_LABEL), errorMessageWrapper);
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
        if (!experiment.isArchived()) {
            rewardVariablesTable.addValueChangeListener(evt -> handleRewardVariableNameChanged(evt.getValue()));
        } else {
            rewardVariablesTable.setEnabled(false);
        }
		return rewardVariablesTable;
	}

	private void handleRewardVariableNameChanged(List<RewardVariable> updatedRewardVariables) {
		unsavedChanges.setVisible(true);
		rewardFunctionEditor.setVariableNames(updatedRewardVariables, experiment.getModel().getRewardVariablesCount());
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

		unsavedChanges.setVisible(false);

		getUI().ifPresent(ui -> ui.navigate(ExperimentView.class, experimentId));
	}

	private void handleSaveDraftClicked(Command afterClickedCallback) {
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
		afterClickedCallback.execute();
	}

    private void archiveExperiment(Experiment experimentToArchive) {
        ConfirmationUtils.archive("Experiment #"+experimentToArchive.getName(), () -> {
            experimentDAO.archive(experimentToArchive.getId(), true);
            experiments.remove(experimentToArchive);
            if (experiments.isEmpty()) {
                getUI().ifPresent(ui -> ui.navigate(ModelView.class, experimentToArchive.getModelId()));
            } else {
                Experiment currentExperiment = (experimentToArchive.getId() == experimentId) ? experiments.get(0) : experiment;
                if (experimentToArchive.getId() == experimentId) {
                    selectExperiment(currentExperiment);
                }
                getUI().ifPresent(ui -> experimentsNavbar.setExperiments(ui, experiments, currentExperiment));
            }
        });
    }

    private void unarchiveExperiment() {
        ConfirmationUtils.unarchive("experiment", () -> {
            experimentDAO.archive(experiment.getId(), false);
            getUI().ifPresent(ui -> ui.navigate(ExperimentView.class, experiment.getId()));
        });
    }

	private Breadcrumbs createBreadcrumbs() {
		return new Breadcrumbs(experiment.getProject(), experiment.getModel(), experiment);
	}

	private NotesField createNotesField() {
		notesField = new NotesField(
			"Notes",
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
        if (experiment.isArchived()) {
            notesField.setEnabled(false);
        }
		return notesField;
	}

	private void selectExperiment(Experiment selectedExperiment) {
		triggerSaveDraft(() -> navigateToAnotherDraftExperiment(selectedExperiment));
		if (saveDraftButton.isEnabled()) {
			navigateToAnotherDraftExperiment(selectedExperiment);
		}
	}
	
	private void navigateToAnotherDraftExperiment(Experiment selectedExperiment) {
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
			experimentsNavbar.setCurrentExperiment(selectedExperiment);
			
			if (ExperimentUtils.isDraftRunType(selectedExperiment)) {
				getUI().ifPresent(ui -> ui.getPage().getHistory().pushState(null, "newExperiment/" + experimentId));
			} else {
				getUI().ifPresent(ui -> ui.navigate(ExperimentView.class, experimentId));
			}
		}
	}

	private void triggerSaveDraft(Command cancelListener) {
		if (unsavedChanges.isVisible()) {
			if (saveDraftButton.isEnabled()) {
				handleSaveDraftClicked(cancelListener);
			} else {
				ConfirmationUtils.leavePage(cancelListener);
			}
		} else {
			cancelListener.execute();
		}
	}

	@Override
	public void beforeLeave(BeforeLeaveEvent event) {
		ContinueNavigationAction action = event.postpone();
		triggerSaveDraft(() -> action.proceed());
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
		rewardVariables = rewardVariableDAO.getRewardVariablesForModel(modelId);
		if (!experiment.isArchived()) {
            experiments = experimentDAO.getExperimentsForModel(modelId).stream()
                                    .sorted(Comparator.comparing(Experiment::getDateCreated).reversed())
                                    .filter(exp -> !exp.isArchived()).collect(Collectors.toList());
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
		startRunButton.setVisible(!experiment.isArchived());
		saveDraftButton.setVisible(!experiment.isArchived());
		rewardFunctionEditor.setValue(experiment.getRewardFunction());		
		if (!rewardVariables.isEmpty()) {
			rewardVariablesTable.setValue(rewardVariables);
		} else {
			rewardVariablesTable.setVariableSize(experiment.getModel().getRewardVariablesCount());
		}
		rewardFunctionEditor.setVariableNames(rewardVariables, experiment.getModel().getRewardVariablesCount());
		unsavedChanges.setVisible(false);
        notesSavedHint.setVisible(false);
        unarchiveExperimentButton.setVisible(experiment.isArchived());
	}

    // Note: these 3 methods were copied and pasted from ExperimentView. Duplication will be gone when #1697 is implemented.
    private boolean isNewExperimentForThisViewModel(Experiment eventExperiment, long modelId) {
        return isSameModel(modelId) && !experiments.contains(eventExperiment);
    }

    private void updateNavBarExperiments() {
        experiments = experimentDAO.getExperimentsForModel(modelId).stream().filter(exp -> !exp.isArchived()).collect(Collectors.toList());
        PushUtils.push(getUI(), ui -> experimentsNavbar.setExperiments(ui, experiments, experiment));
    }

    private boolean isSameModel(long modelId) {
        return experiment != null && experiment.getModelId() == modelId;
    }

    class NewExperimentViewExperimentCreatedSubscriber implements ExperimentCreatedSubscriber {

        @Override
        public void handleBusEvent(ExperimentCreatedBusEvent event) {
            if (isNewExperimentForThisViewModel(event.getExperiment(), event.getModelId())) {
                updateNavBarExperiments();
            }
        }

        @Override
        public boolean isAttached() {
            return NewExperimentView.this.getUI().isPresent();
        }
    }

}
