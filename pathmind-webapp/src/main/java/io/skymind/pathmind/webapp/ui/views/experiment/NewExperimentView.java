package io.skymind.pathmind.webapp.ui.views.experiment;

import java.util.List;
import java.util.Optional;

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
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveEvent.ContinueNavigationAction;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.Command;
import io.skymind.pathmind.services.RewardValidationService;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.shared.data.user.UserCaps;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.shared.security.SecurityUtils;
import io.skymind.pathmind.shared.utils.ModelUtils;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.bus.events.main.ExperimentStartTrainingBusEvent;
import io.skymind.pathmind.webapp.bus.events.view.experiment.ExperimentSavedViewBusEvent;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.alp.DownloadModelAlpLink;
import io.skymind.pathmind.webapp.ui.components.modelChecker.ModelCheckerService;
import io.skymind.pathmind.webapp.ui.components.molecules.ConfirmPopup;
import io.skymind.pathmind.webapp.ui.components.observations.ObservationsPanel;
import io.skymind.pathmind.webapp.ui.components.rewardVariables.RewardVariablesTable;
import io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.utils.ConfirmationUtils;
import io.skymind.pathmind.webapp.ui.utils.GuiUtils;
import io.skymind.pathmind.webapp.ui.utils.NotificationUtils;
import io.skymind.pathmind.webapp.ui.utils.PushUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.components.experimentNotes.ExperimentNotesField;
import io.skymind.pathmind.webapp.ui.views.experiment.components.observations.subscribers.view.ObservationsPanelExperimentSwitchedViewSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.rewardFunction.RewardFunctionEditor;
import io.skymind.pathmind.webapp.ui.views.experiment.subscribers.main.newExperiment.NewExperimentViewExperimentStartTrainingSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.subscribers.view.newExperiment.NewExperimentViewExperimentChangedViewSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.subscribers.view.newExperiment.NewExperimentViewExperimentSwitchedViewSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.utils.ExperimentCapLimitVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@CssImport("./styles/views/new-experiment-view.css")
@Route(value = Routes.NEW_EXPERIMENT, layout = MainLayout.class)
public class NewExperimentView extends DefaultExperimentView implements BeforeLeaveObserver {

    // We have to use a lock object rather than the experiment because we are
    // changing it's reference which makes it not thread safe. As well we cannot
    // lock
    // on this because part of the synchronization is in the eventbus listener in a
    // subclass (which is also why we can't use synchronize on the method.
    private Object experimentLock = new Object();

    private List<RewardVariable> rewardVariables;

    private RewardFunctionEditor rewardFunctionEditor;
    private RewardVariablesTable rewardVariablesTable;
    private ObservationsPanel observationsPanel;
    private Span unsavedChanges;
    private Span notesSavedHint;
    private Button unarchiveExperimentButton;
    private Button saveDraftButton;
    private Button startRunButton;
    private Anchor downloadModelAlpLink;

    private final int REWARD_FUNCTION_MAX_LENGTH = 65535;

    private UserCaps userCaps;

    private boolean isNeedsSaving = false;

    @Autowired
    private RewardValidationService rewardValidationService;
    @Autowired
    private ModelCheckerService modelCheckerService;
    protected ExperimentNotesField notesField;

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
        EventBus.subscribe(this, getUISupplier(),
                new NewExperimentViewExperimentStartTrainingSubscriber(this),
                new NewExperimentViewExperimentSwitchedViewSubscriber(this),
                new NewExperimentViewExperimentChangedViewSubscriber(this),
                new ObservationsPanelExperimentSwitchedViewSubscriber(observationDAO, observationsPanel));
    }

    @Override
    protected void onDetach(DetachEvent event) {
        EventBus.unsubscribe(this);
    }

    @Override
    protected Component getMainContent() {
        HorizontalLayout mainContent = createMainPanel();
        return mainContent;
    }

    private HorizontalLayout createMainPanel() {
        unarchiveExperimentButton = GuiUtils.getPrimaryButton("Unarchive", VaadinIcon.ARROW_BACKWARD.create(), click -> unarchiveExperiment());

        // It is the same for all experiments from the same model so it doesn't have to be updated as long
        // as the user is on the Experiment View (the nav bar only allows navigation to experiments from the same model)
        // If in the future we allow navigation to experiments from other models, then we'll need to update the button accordingly on navigation
        downloadModelAlpLink = new DownloadModelAlpLink(experiment.getProject().getName(), experiment.getModel(), modelService, segmentIntegrator);

        startRunButton = GuiUtils.getPrimaryButton("Train Policy", VaadinIcon.PLAY.create(), click -> handleStartRunButtonClicked());
        saveDraftButton = new Button("Save", click -> handleSaveDraftClicked(() -> {
        }));

        VerticalLayout mainPanel = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing();
        mainPanel.setSpacing(true);
        VerticalLayout panelTitle = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(
                WrapperUtils.wrapWidthFullHorizontal(
                        experimentPanelTitle,
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

        rewardFunctionEditor = new RewardFunctionEditor(getUISupplier(), experimentDAO, experiment, rewardVariables, rewardValidationService);

        Span errorDescriptionLabel = modelCheckerService.createInvalidErrorLabel(experiment.getModel());

        rewardVariablesTable = new RewardVariablesTable(getUISupplier());
        VerticalLayout rewardVariablesPanel = WrapperUtils
                .wrapVerticalWithNoPaddingOrSpacing(
                        LabelFactory.createLabel("Reward Variables", CssPathmindStyles.BOLD_LABEL),
                        rewardVariablesTable);
        rewardVariablesPanel.addClassName("reward-variables-panel");

        observationsPanel = new ObservationsPanel(experiment, false);

        HorizontalLayout rewardFunctionAndObservationsWrapper = WrapperUtils.wrapWidthFullHorizontal(
                rewardFunctionEditor,
                rewardVariablesPanel,
                observationsPanel);
        rewardFunctionAndObservationsWrapper.setClassName("reward-function-wrapper");
        HorizontalLayout errorAndNotesContainer = WrapperUtils.wrapWidthFullHorizontal(
                rewardFunctionEditor.getRewardFunctionErrorPanel(),
                notesField);
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

    private void createAndSetupNotesField() {
        createNotesField(() -> segmentIntegrator.addedNotesNewExperimentView());
        notesField.setPlaceholder("Add Notes (optional)");
        notesField.setOnNotesChangeHandler(() -> setNeedsSaving());
        if (experiment.isArchived()) {
            notesField.setReadonly(true);
        }
    }

    /************************************** UI element creations are above this line **************************************/

    private boolean canStartTraining() {
        return ModelUtils.isValidModel(experiment.getModel())
                && rewardFunctionEditor.isValidForTraining()
                && observationsPanel.getSelectedObservations() != null && !observationsPanel.getSelectedObservations().isEmpty()
                && !experiment.isArchived();
    }

    public void setNeedsSaving() {
        isNeedsSaving = true;
        unsavedChanges.setVisible(true);
        saveDraftButton.setEnabled(rewardFunctionEditor.isRewardFunctionLessThanMaxLength());
        startRunButton.setEnabled(canStartTraining());
    }

    private void handleStartRunButtonClicked() {
        if (!rewardFunctionEditor.validateBinder()) {
            return;
        }
        if (!ExperimentCapLimitVerifier.isUserWithinCapLimits(runDAO, userCaps, segmentIntegrator)) {
            return;
        }

        // These two are an exception to the eventbus because we need to save and run rather than just run. Once we recombine things
        // after finishing all the refactorings this will be cleaner. The notes can be saved normally because the run doesn't
        // rely on the information in the notes.
        experiment.setRewardFunction(rewardFunctionEditor.getExperiment().getRewardFunction());
        experimentDAO.updateRewardFunction(experiment);
        experiment.setSelectedObservations(observationsPanel.getSelectedObservations());
        observationDAO.saveExperimentObservations(experiment.getId(), observationsPanel.getSelectedObservations());

        trainingService.startRun(experiment);
        EventBus.post(new ExperimentStartTrainingBusEvent(experiment));
        segmentIntegrator.startTraining();

        unsavedChanges.setVisible(false);

        getUI().ifPresent(ui -> ui.navigate(ExperimentView.class, experimentId));
    }

    private void handleSaveDraftClicked(Command afterClickedCallback) {
        EventBus.post(new ExperimentSavedViewBusEvent());
        observationDAO.saveExperimentObservations(experiment.getId(), observationsPanel.getSelectedObservations());
        segmentIntegrator.draftSaved();
        disableSaveDraft();
        NotificationUtils.showSuccess("Draft successfully saved");
        isNeedsSaving = false;
        afterClickedCallback.execute();
    }

    private void unarchiveExperiment() {
        ConfirmationUtils.unarchive("experiment", () -> {
            ExperimentUtils.archiveExperiment(experimentDAO, experiment, false);
            segmentIntegrator.archived(Experiment.class, false);
            getUI().ifPresent(ui -> ui.navigate(ExperimentView.class, experiment.getId()));
        });
    }

    @Override
    public void beforeLeave(BeforeLeaveEvent event) {
        ContinueNavigationAction action = event.postpone();
        saveDraftExperiment(() -> action.proceed());
    }

    public void saveDraftExperiment(Command afterClickedCallback) {
        if (isNeedsSaving) {
            if (saveDraftButton.isEnabled()) {
                handleSaveDraftClicked(afterClickedCallback);
            } else {
                errorPopup(afterClickedCallback);
            }
        } else {
            afterClickedCallback.execute();
        }
    }

    private void errorPopup(Command afterClickedCallback) {
        String header = "Before you leave....";
        String text = "";
        if (rewardFunctionEditor.isRewardFunctionMoreThanMaxLength()) {
            text += "Your changes in the reward function cannot be saved because it has exceeded " + REWARD_FUNCTION_MAX_LENGTH + " characters. ";
        }
        text += "Please check and fix the errors.";
        ConfirmPopup popup = new ConfirmPopup(header, text);
        popup.setConfirmButtonText("Stay");
        popup.setCancelButton("Leave", afterClickedCallback);
        popup.open();
    }

    @Override
    protected void loadExperimentData() {
        rewardVariables = rewardVariableDAO.getRewardVariablesForModel(experiment.getModelId());
    }

    @Override
    protected void initScreen(BeforeEnterEvent event) {
        updateScreenComponents();
    }

    private void updateScreenComponents() {
        experimentsNavbar.setVisible(!experiment.isArchived());
        experimentPanelTitle.setExperiment(experiment);
        rewardVariablesTable.setRewardVariables(rewardVariables);
        disableSaveDraft();
        unarchiveExperimentButton.setVisible(experiment.isArchived());
        startRunButton.setEnabled(canStartTraining());
    }

    private void disableSaveDraft() {
        saveDraftButton.setEnabled(false);
        unsavedChanges.setVisible(false);
        notesSavedHint.setVisible(false);
    }

    @Override
    protected void initializeComponentsWithData() {
        rewardVariablesTable.setRewardVariables(rewardVariables);
        disableSaveDraft();
        unarchiveExperimentButton.setVisible(experiment.isArchived());
        startRunButton.setEnabled(canStartTraining());
    }

    protected void createExperimentComponents() {
        // TODO -> STEPH -> create other components
        createAndSetupNotesField();

        experimentComponentList.addAll(List.of(notesField));
    }
}
