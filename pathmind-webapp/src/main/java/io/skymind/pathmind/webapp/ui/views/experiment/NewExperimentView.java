package io.skymind.pathmind.webapp.ui.views.experiment;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;
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
import io.skymind.pathmind.db.dao.ObservationDAO;
import io.skymind.pathmind.db.dao.PolicyDAO;
import io.skymind.pathmind.db.dao.RewardVariableDAO;
import io.skymind.pathmind.db.dao.RunDAO;
import io.skymind.pathmind.services.ModelService;
import io.skymind.pathmind.services.RewardValidationService;
import io.skymind.pathmind.services.TrainingService;
import io.skymind.pathmind.shared.constants.GoalConditionType;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.shared.data.user.UserCaps;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.shared.security.SecurityUtils;
import io.skymind.pathmind.shared.utils.ModelUtils;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.bus.events.main.ExperimentStartTrainingBusEvent;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.exception.InvalidDataException;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.webapp.ui.components.molecules.ConfirmPopup;
import io.skymind.pathmind.webapp.ui.components.navigation.Breadcrumbs;
import io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.utils.ConfirmationUtils;
import io.skymind.pathmind.webapp.ui.utils.FormUtils;
import io.skymind.pathmind.webapp.ui.utils.GuiUtils;
import io.skymind.pathmind.webapp.ui.utils.NotificationUtils;
import io.skymind.pathmind.webapp.ui.utils.PushUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import io.skymind.pathmind.webapp.ui.views.experiment.components.ExperimentNotesField;
import io.skymind.pathmind.webapp.ui.views.experiment.components.RewardFunctionEditor;
import io.skymind.pathmind.webapp.ui.views.experiment.components.RewardFunctionErrorPanel;
import io.skymind.pathmind.webapp.ui.views.experiment.components.navbar.ExperimentsNavBar;
import io.skymind.pathmind.webapp.ui.views.experiment.components.observations.subscribers.view.ObservationsPanelExperimentChangedViewSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.subscribers.main.NewExperimentViewExperimentCreatedSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.subscribers.main.NewExperimentViewExperimentStartTrainingSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.subscribers.main.NewExperimentViewExperimentUpdatedSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.subscribers.view.NewExperimentViewExperimentChangedViewSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.utils.ExperimentCapLimitVerifier;
import io.skymind.pathmind.webapp.ui.components.modelChecker.ModelCheckerService;
import io.skymind.pathmind.webapp.ui.components.alp.DownloadModelAlpLink;
import io.skymind.pathmind.webapp.ui.components.observations.ObservationsPanel;
import io.skymind.pathmind.webapp.ui.components.rewardVariables.RewardVariablesTable;
import io.skymind.pathmind.webapp.ui.views.project.ProjectView;
import io.skymind.pathmind.webapp.utils.PathmindUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@CssImport("./styles/views/new-experiment-view.css")
@Route(value = Routes.NEW_EXPERIMENT, layout = MainLayout.class)
public class NewExperimentView extends PathMindDefaultView implements HasUrlParameter<Long>, BeforeLeaveObserver {

    // We have to use a lock object rather than the experiment because we are
    // changing it's reference which makes it not thread safe. As well we cannot
    // lock
    // on this because part of the synchronization is in the eventbus listener in a
    // subclass (which is also why we can't use synchronize on the method.
    private Object experimentLock = new Object();

    private long experimentId = -1;
    private long modelId = -1;
    private List<RewardVariable> rewardVariables;
    private Experiment experiment;
    private List<Experiment> experiments = new ArrayList<>();
    private List<String> rewardFunctionErrors = new ArrayList<>();

    private RewardFunctionEditor rewardFunctionEditor;
    private RewardFunctionErrorPanel rewardFunctionErrorPanel;
    private RewardVariablesTable rewardVariablesTable;
    private ObservationsPanel observationsPanel;
    private ExperimentsNavBar experimentsNavbar;
    private ExperimentNotesField notesField;
    private Span panelTitleText;
    private Span unsavedChanges;
    private Span notesSavedHint;
    private Span rewardEditorErrorLabel;
    private Button unarchiveExperimentButton;
    private Button saveDraftButton;
    private Button startRunButton;
    private Anchor downloadModelAlpLink;

    private final int REWARD_FUNCTION_MAX_LENGTH = 65535;

    private UserCaps userCaps;

    @Autowired
    private ModelService modelService;
    @Autowired
    private ExperimentDAO experimentDAO;
    @Autowired
    private RunDAO runDAO;
    @Autowired
    private PolicyDAO policyDAO;
    @Autowired
    private RewardVariableDAO rewardVariableDAO;
    @Autowired
    private ObservationDAO observationDAO;
    @Autowired
    private TrainingService trainingService;
    @Autowired
    private SegmentIntegrator segmentIntegrator;
    @Autowired
    private RewardValidationService rewardValidationService;
    @Autowired
    private ModelCheckerService modelCheckerService;

    private Breadcrumbs pageBreadcrumbs;
    private Binder<Experiment> binder;

    public NewExperimentView(
            @Value("${pathmind.notification.newRunDailyLimit}") int newRunDailyLimit,
            @Value("${pathmind.notification.newRunMonthlyLimit}") int newRunMonthlyLimit,
            @Value("${pathmind.notification.newRunNotificationThreshold}") int newRunNotificationThreshold) {
        super();
        this.userCaps = new UserCaps(newRunDailyLimit, newRunMonthlyLimit, newRunNotificationThreshold);
        addClassName("new-experiment-view");
    }

    @Override
    protected void onAttach(AttachEvent event) {
        EventBus.subscribe(this, () -> getUI(),
                new NewExperimentViewExperimentCreatedSubscriber(this),
                new NewExperimentViewExperimentUpdatedSubscriber(this),
                new NewExperimentViewExperimentStartTrainingSubscriber(this),
                new NewExperimentViewExperimentChangedViewSubscriber(this),
                new ObservationsPanelExperimentChangedViewSubscriber(observationDAO, observationsPanel));
    }

    @Override
    protected void onDetach(DetachEvent event) {
        EventBus.unsubscribe(this);
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
        experimentsNavbar = new ExperimentsNavBar(() -> getUI(), experimentDAO, policyDAO, experiment, experiments, segmentIntegrator);
        experimentsNavbar.setAllowNewExperimentCreation(ModelUtils.isValidModel(experiment.getModel()));

        unarchiveExperimentButton = GuiUtils.getPrimaryButton("Unarchive", VaadinIcon.ARROW_BACKWARD.create(), click -> unarchiveExperiment());

        // It is the same for all experiments from the same model so it doesn't have to be updated as long
        // as the user is on the Experiment View (the nav bar only allows navigation to experiments from the same model)
        // If in the future we allow navigation to experiments from other models, then we'll need to update the button accordingly on navigation
        downloadModelAlpLink = new DownloadModelAlpLink(experiment.getProject().getName(), experiment.getModel(), modelService, segmentIntegrator);

        startRunButton = GuiUtils.getPrimaryButton("Train Policy", VaadinIcon.PLAY.create(), click -> handleStartRunButtonClicked());
        saveDraftButton = new Button("Save", click -> handleSaveDraftClicked(() -> {
        }));
        setButtonsEnablement();

        VerticalLayout mainPanel = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing();
        mainPanel.setSpacing(true);
        panelTitleText = LabelFactory.createLabel("Experiment #" + experiment.getName(), CssPathmindStyles.SECTION_TITLE_LABEL);
        VerticalLayout panelTitle = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(
                WrapperUtils.wrapWidthFullHorizontal(
                        panelTitleText,
                        downloadModelAlpLink
                ),
                LabelFactory.createLabel(
                        "To judge if an action is a good one, we calculate a reward score. "
                                + "The reward score is based on the reward function.",
                        CssPathmindStyles.SECTION_SUBTITLE_LABEL));
        panelTitle.setClassName("panel-title");

        unsavedChanges = LabelFactory.createLabel("Unsaved changes!", "hint-label");
        unsavedChanges.setVisible(false);
        notesSavedHint = LabelFactory.createLabel("Notes saved!", "fade-out-hint-label");
        notesSavedHint.setVisible(false);

        VerticalLayout rewardFnEditorWrapper = getRewardFnEditorPanel();
        rewardFnEditorWrapper.addClassName("reward-fn-editor-panel");

        Span errorDescriptionLabel = modelCheckerService.createInvalidErrorLabel(experiment.getModel());

        rewardVariablesTable = new RewardVariablesTable(() -> getUI());
        VerticalLayout rewardVariablesPanel = WrapperUtils
                .wrapVerticalWithNoPaddingOrSpacing(
                        LabelFactory.createLabel("Reward Variables", CssPathmindStyles.BOLD_LABEL),
                        rewardVariablesTable);
        rewardVariablesPanel.addClassName("reward-variables-panel");

        observationsPanel = new ObservationsPanel(experiment, false);
        observationsPanel.addValueChangeListener(evt -> {
            if (observationsPanel.getExperiment().equals(experiment)) {
                setButtonsEnablement();
            }
        });

        HorizontalLayout rewardFunctionAndObservationsWrapper = WrapperUtils.wrapWidthFullHorizontal(
                rewardFnEditorWrapper,
                rewardVariablesPanel,
                observationsPanel);
        rewardFunctionAndObservationsWrapper.setClassName("reward-function-wrapper");
        rewardFunctionErrorPanel = new RewardFunctionErrorPanel();
        HorizontalLayout errorAndNotesContainer = WrapperUtils.wrapWidthFullHorizontal(
                rewardFunctionErrorPanel,
                createNotesField());
        errorAndNotesContainer.setClassName("error-and-notes-container");

        VerticalLayout saveButtonAndHintsWrapper = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(saveDraftButton,
                unsavedChanges, notesSavedHint);
        saveButtonAndHintsWrapper.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        HorizontalLayout buttonsWrapper = new HorizontalLayout(saveButtonAndHintsWrapper, startRunButton,
                unarchiveExperimentButton);
        buttonsWrapper.setWidth(null);

        mainPanel.add(WrapperUtils.wrapWidthFullBetweenHorizontal(panelTitle, buttonsWrapper), errorDescriptionLabel,
                rewardFunctionAndObservationsWrapper, errorAndNotesContainer);
        mainPanel.setClassName("view-section");

        HorizontalLayout panelsWrapper = WrapperUtils.wrapWidthFullHorizontal(experimentsNavbar, mainPanel);
        panelsWrapper.setSpacing(false);
        return panelsWrapper;
    }

    private VerticalLayout getRewardFnEditorPanel() {
        rewardFunctionEditor = new RewardFunctionEditor();
        rewardFunctionEditor.addValueChangeListener(changeEvent -> {
            rewardEditorErrorLabel.setVisible(changeEvent.getValue().length() > REWARD_FUNCTION_MAX_LENGTH);
            rewardFunctionErrors = rewardValidationService.validateRewardFunction(rewardFunctionEditor.getValue(),
                    rewardVariables);
            rewardFunctionErrorPanel.showErrors(rewardFunctionErrors);

            setButtonsEnablement();
        });
        rewardEditorErrorLabel = LabelFactory.createLabel(
                "Reward Function must not exceed " + REWARD_FUNCTION_MAX_LENGTH + " characters", "reward-editor-error");
        rewardEditorErrorLabel.setVisible(false);
        VerticalLayout rewardFnEditorPanel = WrapperUtils
                .wrapVerticalWithNoPaddingOrSpacing(WrapperUtils.wrapWidthFullBetweenHorizontal(
                        LabelFactory.createLabel("Reward Function", CssPathmindStyles.BOLD_LABEL),
                        rewardEditorErrorLabel), rewardFunctionEditor);
        if (experiment.isArchived()) {
            rewardFnEditorPanel.setEnabled(false);
        }
        return rewardFnEditorPanel;
    }

    private Breadcrumbs createBreadcrumbs() {
        return new Breadcrumbs(experiment.getProject(), experiment.getModel(), experiment);
    }

    private ExperimentNotesField createNotesField() {
        notesField = new ExperimentNotesField(
                () -> getUI(),
                "Notes",
                experiment,
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
            notesField.setReadonly(true);
        }
        return notesField;
    }

    /************************************** UI element creations are above this line **************************************/

    private boolean canStartTraining() {
        if (rewardFunctionEditor == null || observationsPanel == null) {
            return false;
        }
        return ModelUtils.isValidModel(experiment.getModel())
                && rewardFunctionEditor.getOptionalValue().isPresent() && !rewardFunctionEditor.getValue().isEmpty()
                && rewardFunctionErrors.size() == 0
                && observationsPanel.getSelectedObservations() != null && !observationsPanel.getSelectedObservations().isEmpty()
                && canSaveDataInDB()
                && !experiment.isArchived();
    }

    private boolean canSaveDataInDB() {
        return rewardFunctionEditor.getValue().length() <= REWARD_FUNCTION_MAX_LENGTH;
    }

    private boolean experimentDetailsHasChanged() {
        if (rewardFunctionEditor == null || observationsPanel == null) {
            return false;
        }
        return !experiment.getRewardFunction().equals(rewardFunctionEditor.getValue()) ||
                !observationsPanel.getSelectedObservations().equals(experiment.getSelectedObservations());
    }

    private void setButtonsEnablement() {
        boolean hasChanged = experimentDetailsHasChanged();
        if (unsavedChanges != null) {
            unsavedChanges.setVisible(hasChanged);
        }
        startRunButton.setEnabled(canStartTraining());
        saveDraftButton.setEnabled(hasChanged && canSaveDataInDB());
    }

    private void setupBinder() {
        // To allow saving when the reward function editor is empty,
        // the field is not set to forField(...).asRequired().bind(...)
        binder.forField(rewardFunctionEditor).bind(Experiment::getRewardFunction,
                Experiment::setRewardFunction);
    }

    private void handleStartRunButtonClicked() {
        if (!FormUtils.isValidForm(binder, experiment)) {
            return;
        }
        if (!ExperimentCapLimitVerifier.isUserWithinCapLimits(runDAO, userCaps, segmentIntegrator)) {
            return;
        }

        experimentDAO.updateExperiment(experiment);
        observationDAO.saveExperimentObservations(experiment.getId(), observationsPanel.getSelectedObservations());

        trainingService.startRun(experiment);
        EventBus.post(new ExperimentStartTrainingBusEvent(experiment));
        segmentIntegrator.startTraining();

        unsavedChanges.setVisible(false);

        getUI().ifPresent(ui -> ui.navigate(ExperimentView.class, experimentId));
    }

    private void handleSaveDraftClicked(Command afterClickedCallback) {
        experimentDAO.updateExperiment(experiment);
        observationDAO.saveExperimentObservations(experiment.getId(), observationsPanel.getSelectedObservations());
        segmentIntegrator.draftSaved();
        disabledSaveDraft();
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

    public void setExperiment(Experiment selectedExperiment) {
        triggerSaveDraft(() -> navigateToAnotherDraftExperiment(selectedExperiment));
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
            pageBreadcrumbs.setText(3, "Experiment #" + experiment.getName());

            PushUtils.push(getUI(), ui -> {
                navigateToExperiment(ui, selectedExperiment);
            });
        }
    }

    private void navigateToExperiment(UI ui, Experiment targetExperiment) {
        if (ExperimentUtils.isDraftRunType(targetExperiment)) {
            ui.getPage().getHistory().pushState(null, "newExperiment/" + targetExperiment.getId());
        } else {
            navigateToExperimentView(targetExperiment);
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
        String header = "Before you leave....";
        String text = "";
        if (isRewardFunctionTooLong) {
            text += "Your changes in the reward function cannot be saved because it has exceeded " + REWARD_FUNCTION_MAX_LENGTH + " characters. ";
        }
        text += "Please check and fix the errors.";
        ConfirmPopup popup = new ConfirmPopup(header, text);
        popup.setConfirmButtonText("Stay");
        popup.setCancelButton("Leave", cancelAction);
        popup.open();
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
        loadExperimentData();
    }

    private void loadExperimentData() {
        modelId = experiment.getModelId();
        rewardVariables = rewardVariableDAO.getRewardVariablesForModel(modelId);
        experiment.setModelObservations(observationDAO.getObservationsForModel(experiment.getModelId()));
        experiment.setSelectedObservations(observationDAO.getObservationsForExperiment(experimentId));
        if (!experiment.isArchived()) {
            experiments = experimentDAO.getExperimentsForModel(modelId).stream()
                    .filter(exp -> !exp.isArchived()).collect(Collectors.toList());
        }
    }

    @Override
    protected void initScreen(BeforeEnterEvent event) {
        updateScreenComponents();
    }

    private void updateScreenComponents() {
        binder.setBean(experiment);
        experimentsNavbar.setVisible(!experiment.isArchived());
        panelTitleText.setText("Experiment #" + experiment.getName());
        experimentDetailsHasChanged();
        rewardFunctionEditor.setValue(StringUtils.defaultIfEmpty(experiment.getRewardFunction(), generateRewardFunction()));
        rewardFunctionEditor.setVariableNames(rewardVariables);
        rewardVariablesTable.setRewardVariables(rewardVariables);
        disabledSaveDraft();
        unarchiveExperimentButton.setVisible(experiment.isArchived());
    }

    private void disabledSaveDraft() {
        saveDraftButton.setEnabled(false);
        unsavedChanges.setVisible(false);
        notesSavedHint.setVisible(false);
    }

    private String generateRewardFunction() {
        StringBuilder sb = new StringBuilder();
        if (experiment.isHasGoals()) {
            for (RewardVariable rv : rewardVariables) {
                GoalConditionType goal = rv.getGoalConditionTypeEnum();
                if (goal != null) {
                    sb.append(MessageFormat.format("reward {0}= after.{1} - before.{1};", goal.getMathOperation(), rv.getName()));
                    sb.append("\n");
                }
            }
        }
        return sb.toString();
    }

    public Experiment getExperiment() {
        return experiment;
    }

    public void updateExperimentComponents() {
        experiments = experimentDAO.getExperimentsForModel(modelId, false);

        if (experiments.isEmpty()) {
            Model model = modelService.getModel(modelId)
                    .orElseThrow(() -> new InvalidDataException("Attempted to access Invalid model: " + modelId));

            PushUtils.push(getUI(), ui -> ui.navigate(ProjectView.class, PathmindUtils.getProjectModelParameter(model.getProjectId(), modelId)));
        } else {
            boolean selectedExperimentWasArchived = experiments.stream()
                    .noneMatch(e -> e.getId() == experimentId);
            if (selectedExperimentWasArchived) {
                Experiment newSelectedExperiment = experiments.get(0);
                PushUtils.push(getUI(), ui -> navigateToExperiment(ui, newSelectedExperiment));
            }
        }
    }

    public List<Experiment> getExperiments() {
        return experiments;
    }

    public long getModelId() {
        return modelId;
    }
}
