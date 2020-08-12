package io.skymind.pathmind.webapp.ui.views.experiment;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.*;
import com.vaadin.flow.router.BeforeLeaveEvent.ContinueNavigationAction;
import com.vaadin.flow.server.Command;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.db.dao.RewardVariableDAO;
import io.skymind.pathmind.db.dao.RunDAO;
import io.skymind.pathmind.services.RewardValidationService;
import io.skymind.pathmind.services.TrainingService;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.shared.data.user.UserCaps;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.shared.security.SecurityUtils;
import io.skymind.pathmind.shared.utils.ModelUtils;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.bus.events.ExperimentCreatedBusEvent;
import io.skymind.pathmind.webapp.bus.events.ExperimentUpdatedBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.ExperimentCreatedSubscriber;
import io.skymind.pathmind.webapp.bus.subscribers.ExperimentUpdatedSubscriber;
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
import io.skymind.pathmind.webapp.ui.views.experiment.components.RewardFunctionEditor;
import io.skymind.pathmind.webapp.ui.views.experiment.components.RewardFunctionErrorPanel;
import io.skymind.pathmind.webapp.ui.views.experiment.components.navbar.ExperimentsNavBar;
import io.skymind.pathmind.webapp.ui.views.experiment.subscribers.NotificationExperimentUpdatedSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.utils.ExperimentCapLimitVerifier;
import io.skymind.pathmind.webapp.ui.views.model.ModelView;
import io.skymind.pathmind.webapp.ui.views.model.NonTupleModelService;
import io.skymind.pathmind.webapp.ui.views.model.components.RewardVariablesTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CssImport("./styles/views/new-experiment-view.css")
@Route(value = Routes.NEW_EXPERIMENT, layout = MainLayout.class)
public class NewExperimentView extends PathMindDefaultView implements HasUrlParameter<Long>, BeforeLeaveObserver {

    // We have to use a lock object rather than the experiment because we are changing it's reference which makes it not thread safe. As well we cannot lock
	// on this because part of the synchronization is in the eventbus listener in a subclass (which is also why we can't use synchronize on the method.
	private Object experimentLock = new Object();

	private long experimentId = -1;
	private long modelId = -1;
	private List<RewardVariable> rewardVariables;
	private Experiment experiment;
	private List<Experiment> experiments = new ArrayList<>();
	private List<String> rewardFunctionErrors = new ArrayList<>();

	private RewardFunctionEditor rewardFunctionEditor;
	private RewardFunctionErrorPanel rewardFunctionErrorPanel;
	private ExperimentsNavBar experimentsNavbar;
	private NotesField notesField;
	private RewardVariablesTable rewardVariablesTable;
	private Span unsavedChanges;
	private Span notesSavedHint;
	private Span rewardEditorErrorLabel;
	private Button unarchiveExperimentButton;
	private Button saveDraftButton;
	private Button startRunButton;

	private final int REWARD_FUNCTION_MAX_LENGTH = 65535;

    private UserCaps userCaps;

	@Autowired
	private ExperimentDAO experimentDAO;
    @Autowired
    private RunDAO runDAO;
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

    // REFACTOR -> Temporary placeholder until I finish the merging
    private NotificationExperimentUpdatedSubscriber notificationExperimentUpdatedSubscriber;

    public NewExperimentView(
            @Value("${pathmind.notification.newRunDailyLimit}") int newRunDailyLimit,
            @Value("${pathmind.notification.newRunMonthlyLimit}") int newRunMonthlyLimit,
            @Value("${pathmind.notification.newRunNotificationThreshold}") int newRunNotificationThreshold) {
		super();
        this.userCaps = new UserCaps(newRunDailyLimit, newRunMonthlyLimit, newRunNotificationThreshold);
        notificationExperimentUpdatedSubscriber = new NotificationExperimentUpdatedSubscriber(() -> getUI());
		addClassName("new-experiment-view");
	}

    @Override
    protected void onDetach(DetachEvent event) {
        EventBus.unsubscribe(this);
    }

    @Override
    protected void onAttach(AttachEvent event) {
        EventBus.subscribe(this,
                new NewExperimentViewExperimentCreatedSubscriber(),
                new NewExperimentViewExperimentUpdatedSubscriber(),
                notificationExperimentUpdatedSubscriber);
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
		experimentsNavbar = new ExperimentsNavBar(
		        () -> getUI(),
                experimentDAO,
                experiment,
                experiments,
                selectedExperiment -> selectExperiment(selectedExperiment),
                segmentIntegrator);

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

		VerticalLayout rewardFnPanel = getRewardFnEditorPanel();
		rewardFnPanel.addClassName("reward-fn-editor-panel");

        Span errorDescriptionLabel = nonTupleModelService.createNonTupleErrorLabel(experiment.getModel());

		HorizontalLayout rewardFunctionWrapper = WrapperUtils.wrapSizeFullBetweenHorizontal(
				rewardFnPanel, 
				WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(LabelFactory.createLabel("Reward Variables", CssPathmindStyles.BOLD_LABEL), getRewardVariableNamesPanel()));
		rewardFunctionWrapper.setClassName("reward-function-wrapper");
		rewardFunctionWrapper.setPadding(false);
		rewardFunctionWrapper.setSpacing(true);
        rewardFunctionErrorPanel = new RewardFunctionErrorPanel();
		HorizontalLayout errorAndNotesContainer = WrapperUtils.wrapWidthFullHorizontal(rewardFunctionErrorPanel, createNotesField());
		errorAndNotesContainer.setClassName("error-and-notes-container");

		VerticalLayout saveButtonAndHintsWrapper = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(saveDraftButton, unsavedChanges, notesSavedHint);
		saveButtonAndHintsWrapper.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
		HorizontalLayout buttonsWrapper = new HorizontalLayout(saveButtonAndHintsWrapper, startRunButton, unarchiveExperimentButton);
		buttonsWrapper.setWidth(null);

		mainPanel.add(WrapperUtils.wrapWidthFullBetweenHorizontal(panelTitle, buttonsWrapper), errorDescriptionLabel, rewardFunctionWrapper, errorAndNotesContainer);
		mainPanel.setClassName("view-section");

		HorizontalLayout panelsWrapper = WrapperUtils.wrapWidthFullHorizontal(experimentsNavbar, mainPanel);
		panelsWrapper.setSpacing(false);
		return panelsWrapper;
	}

	private VerticalLayout getRewardFnEditorPanel() {
		rewardFunctionEditor = new RewardFunctionEditor();
		rewardFunctionEditor.addValidationListener(evt -> {
            rewardFunctionErrors = new ArrayList<>();
		    if (!evt.isValid()) {
		        evt.getInvalidLineVariableIndexPairs().forEach(pair -> rewardFunctionErrors.add(String.format("ERROR: Line %s: Invalid variable index:%s", pair.getFirst(), pair.getSecond())));
		    } else {
                rewardFunctionErrors.addAll(rewardValidationService.validateRewardFunction(rewardFunctionEditor.getValue()));
		    }
		    rewardFunctionErrorPanel.showErrors(rewardFunctionErrors);

		    startRunButton.setEnabled(canStartTraining());
		    saveDraftButton.setEnabled(canSaveDataInDB());

		});
		rewardFunctionEditor.addValueChangeListener(changeEvent -> {
			unsavedChanges.setVisible(true);
			rewardEditorErrorLabel.setVisible(changeEvent.getValue().length() > REWARD_FUNCTION_MAX_LENGTH);
		});
		rewardEditorErrorLabel = LabelFactory.createLabel("Reward Function must not exceed " + REWARD_FUNCTION_MAX_LENGTH + " characters", "reward-editor-error");
		rewardEditorErrorLabel.setVisible(false);
		VerticalLayout rewardFnEditorPanel = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(
                WrapperUtils.wrapWidthFullBetweenHorizontal(
                        LabelFactory.createLabel("Reward Function", CssPathmindStyles.BOLD_LABEL), rewardEditorErrorLabel),
                rewardFunctionEditor);
        if (experiment.isArchived()) {
            rewardFnEditorPanel.setEnabled(false);
        }
		return rewardFnEditorPanel;
	}

	private boolean canStartTraining() {
		return ModelUtils.isTupleModel(experiment.getModel()) && rewardFunctionErrors.size() == 0 && canSaveDataInDB();
	}

	private boolean canSaveDataInDB() {
		return rewardFunctionEditor.getValue().length() <= REWARD_FUNCTION_MAX_LENGTH && !rewardVariablesTable.isInvalid();
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
		if (!FormUtils.isValidForm(binder, experiment))
			return;
		if(!ExperimentCapLimitVerifier.isUserWithinCapLimits(runDAO, userCaps, segmentIntegrator))
            return;

		List<RewardVariable> rewardVariables = rewardVariablesTable.getValue();
		rewardVariableDAO.updateModelRewardVariables(experiment.getModelId(), rewardVariables);
		experimentDAO.updateExperiment(experiment);
		segmentIntegrator.rewardFuntionCreated();

		trainingService.startRun(experiment);
        EventBus.post(new ExperimentUpdatedBusEvent(experiment, ExperimentUpdatedBusEvent.ExperimentUpdateType.StartTraining));
		segmentIntegrator.discoveryRunStarted();

		unsavedChanges.setVisible(false);

		getUI().ifPresent(ui -> ui.navigate(ExperimentView.class, experimentId));
	}

    private void handleSaveDraftClicked(Command afterClickedCallback) {
		List<RewardVariable> rewardVariables = rewardVariablesTable.getValue();
		rewardVariableDAO.updateModelRewardVariables(experiment.getModelId(), rewardVariables);
		experimentDAO.updateExperiment(experiment);
		segmentIntegrator.draftSaved();
		unsavedChanges.setVisible(false);
		notesSavedHint.setVisible(false);
		NotificationUtils.showSuccess("Draft successfully saved");
		afterClickedCallback.execute();
	}

    private void unarchiveExperiment() {
        ConfirmationUtils.unarchive("experiment", () -> {
            ExperimentUtils.archiveExperiment(experimentDAO, experiment, false);
            segmentIntegrator.archived(Experiment.class, false);
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

	private void navigateToExperimentView(Experiment experiment) {
	    PushUtils.push(getUI(), ui -> {
            ui.navigate(ExperimentView.class, experiment.getId());
        });
    }

	private void triggerSaveDraft(Command cancelListener) {
		if (unsavedChanges.isVisible()) {
			if (saveDraftButton.isEnabled()) {
				handleSaveDraftClicked(cancelListener);
			} else {
				errorPopup(cancelListener);
			}
		} else {
			cancelListener.execute();
		}
    }

    private void errorPopup(Command cancelAction) {
        Boolean isRewardFunctionTooLong = rewardFunctionEditor.getValue().length() > REWARD_FUNCTION_MAX_LENGTH;
        Boolean isRewardVariablesTableInvalid = rewardVariablesTable.isInvalid();
        String header = "Before you leave....";
        String text = "";
        if (isRewardFunctionTooLong && !isRewardVariablesTableInvalid) {
            text += "Your changes in the reward function cannot be saved because it has exceeded "+REWARD_FUNCTION_MAX_LENGTH+" characters. ";
        } else if (!isRewardFunctionTooLong && isRewardVariablesTableInvalid) {
            text += "Your changes in the reward variables cannot be saved. ";
        } else if (isRewardFunctionTooLong && isRewardVariablesTableInvalid) {
            text += "Your changes cannot be saved because the reward function has exceeded "+REWARD_FUNCTION_MAX_LENGTH+" characters and the reward variables are invalid. ";
        }
        text += "Please check and fix the errors.";
        String confirmText = "Stay";
        ConfirmDialog dialog = new ConfirmDialog(header, text, confirmText, evt -> evt.getSource().close());
        dialog.setCancelButton("Leave", evt -> cancelAction.execute(), UIConstants.DEFAULT_BUTTON_THEME);
        dialog.open();
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
        notificationExperimentUpdatedSubscriber.setExperiment(experiment);
		loadExperimentData();
	}

	private void loadExperimentData() {
		modelId = experiment.getModelId();
		rewardVariables = rewardVariableDAO.getRewardVariablesForModel(modelId);
		if (!experiment.isArchived()) {
            experiments = experimentDAO.getExperimentsForModel(modelId).stream()
                                    .filter(exp -> !exp.isArchived()).collect(Collectors.toList());
		}
	}

	@Override
	protected void initScreen(BeforeEnterEvent event) {
        // The reward variables table should only be initialized once for the Experiment Page
        // no matter which Experiment of the same model the user visits later on.
        // This may have to be changed if we allow users to navigate Experiments of different models.
	    rewardVariablesTable.setVariableSize(experiment.getModel().getRewardVariablesCount());
		updateScreenComponents();
	}

	private void updateScreenComponents() {
		binder.setBean(experiment);
		startRunButton.setVisible(!experiment.isArchived());
		saveDraftButton.setVisible(!experiment.isArchived());
		rewardFunctionEditor.setValue(experiment.getRewardFunction());
		rewardVariablesTable.setValue(rewardVariables);
		rewardFunctionEditor.setVariableNames(rewardVariables, experiment.getModel().getRewardVariablesCount());
		unsavedChanges.setVisible(false);
        notesSavedHint.setVisible(false);
        unarchiveExperimentButton.setVisible(experiment.isArchived());
	}

    private boolean isSameExperiment(Experiment eventExperiment) {
        return ExperimentUtils.isSameModel(experiment, eventExperiment.getModelId()) && experiment.equals(eventExperiment);
    }

    private void updateExperimentComponents() {
        experiments = experimentDAO.getExperimentsForModel(modelId).stream().filter(exp -> !exp.isArchived()).collect(Collectors.toList());

        if (experiments.isEmpty()) {
            PushUtils.push(getUI(), ui -> ui.navigate(ModelView.class, experiment.getModelId()));
        } else {
            boolean selectedExperimentWasArchived = experiments.stream()
                    .noneMatch(e -> e.getId() == experimentId);
            if (selectedExperimentWasArchived) {
                Experiment newSelectedExperiment = experiments.get(0);
                PushUtils.push(getUI(), ui -> navigateToExperiment(ui, newSelectedExperiment));
            }
            else {
                PushUtils.push(getUI(), ui -> {
                    selectExperiment(experiment);
                });
            }
        }
    }

    private void navigateToExperiment(UI ui, Experiment targetExperiment) {
        if (ExperimentUtils.isDraftRunType(targetExperiment)) {
            ui.navigate(NewExperimentView.class, targetExperiment.getId());
        } else {
            ui.navigate(ExperimentView.class, targetExperiment.getId());
        }
    }

    class NewExperimentViewExperimentCreatedSubscriber implements ExperimentCreatedSubscriber {

        @Override
        public void handleBusEvent(ExperimentCreatedBusEvent event) {
            if (ExperimentUtils.isNewExperimentForModel(event.getExperiment(), experiments, event.getModelId())) {
                updateExperimentComponents();
            }
        }

        @Override
        public boolean isAttached() {
            return isViewAttached();
        }
    }

    class NewExperimentViewExperimentUpdatedSubscriber implements ExperimentUpdatedSubscriber {

        @Override
        public void handleBusEvent(ExperimentUpdatedBusEvent event) {
            if (isSameExperiment(event.getExperiment()) && event.isStartedTraining()) {
                navigateToExperimentView(event.getExperiment());
            }
            else if (ExperimentUtils.isSameModel(experiment, event.getModelId())) {
                updateExperimentComponents();
            }
        }

        @Override
        public boolean isAttached() {
            return isViewAttached();
        }
    }
}
