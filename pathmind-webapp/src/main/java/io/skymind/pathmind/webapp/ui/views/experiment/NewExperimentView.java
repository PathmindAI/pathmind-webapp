package io.skymind.pathmind.webapp.ui.views.experiment;

import java.util.List;

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
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveEvent.ContinueNavigationAction;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.Command;
import io.skymind.pathmind.services.RewardValidationService;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.shared.utils.ModelUtils;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.bus.events.view.experiment.ExperimentSavedViewBusEvent;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.alp.DownloadModelAlpLink;
import io.skymind.pathmind.webapp.ui.components.modelChecker.ModelCheckerService;
import io.skymind.pathmind.webapp.ui.components.molecules.ConfirmPopup;
import io.skymind.pathmind.webapp.ui.components.observations.ObservationsPanel;
import io.skymind.pathmind.webapp.ui.components.rewardVariables.RewardVariablesTable;
import io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.utils.GuiUtils;
import io.skymind.pathmind.webapp.ui.utils.NotificationUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.actions.newExperiment.StartRunAction;
import io.skymind.pathmind.webapp.ui.views.experiment.actions.shared.UnarchiveExperimentAction;
import io.skymind.pathmind.webapp.ui.views.experiment.components.experimentNotes.ExperimentNotesField;
import io.skymind.pathmind.webapp.ui.views.experiment.components.rewardFunction.RewardFunctionEditor;
import io.skymind.pathmind.webapp.ui.views.experiment.subscribers.main.newExperiment.NewExperimentViewExperimentStartTrainingSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.subscribers.view.newExperiment.NewExperimentViewExperimentChangedViewSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.subscribers.view.newExperiment.NewExperimentViewExperimentSwitchedViewSubscriber;
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
    // TODO -> STEPH -> We'll have to bring this back for the same reasons as in the experiment view - see those notes for more details.
    private Object experimentLock = new Object();

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
        setUserCaps(newRunDailyLimit, newRunMonthlyLimit, newRunNotificationThreshold);
        addClassName("new-experiment-view");
    }

    @Override
    protected void onAttach(AttachEvent event) {
        EventBus.subscribe(this, getUISupplier(),
                new NewExperimentViewExperimentStartTrainingSubscriber(this),
                new NewExperimentViewExperimentSwitchedViewSubscriber(this),
                new NewExperimentViewExperimentChangedViewSubscriber(this));
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

        createButtons();

        // It is the same for all experiments from the same model so it doesn't have to be updated as long
        // as the user is on the Experiment View (the nav bar only allows navigation to experiments from the same model)
        // If in the future we allow navigation to experiments from other models, then we'll need to update the button accordingly on navigation
        downloadModelAlpLink = new DownloadModelAlpLink(experiment.getProject().getName(), experiment.getModel(), modelService, segmentIntegrator);

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

        Span errorDescriptionLabel = modelCheckerService.createInvalidErrorLabel(experiment.getModel());

        VerticalLayout rewardVariablesPanel = WrapperUtils
                .wrapVerticalWithNoPaddingOrSpacing(
                        LabelFactory.createLabel("Reward Variables", CssPathmindStyles.BOLD_LABEL),
                        rewardVariablesTable);
        rewardVariablesPanel.addClassName("reward-variables-panel");

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

    // TODO -> STEPH -> Should use the same as the experimentView, that is the DEfaultExperimentView createsNotes method.
    private void createAndSetupNotesField() {
        notesField = createNotesField(() -> segmentIntegrator.addedNotesNewExperimentView());
        notesField.setPlaceholder("Add Notes (optional)");
        notesField.setOnNotesChangeHandler(() -> setNeedsSaving());
        if (experiment.isArchived()) {
            notesField.setReadonly(true);
        }
    }

    private void createButtons() {
        unarchiveExperimentButton = GuiUtils.getPrimaryButton("Unarchive", VaadinIcon.ARROW_BACKWARD.create(), click -> UnarchiveExperimentAction.unarchiveExperiment(experimentDAO, experiment, segmentIntegrator, getUI()));
        startRunButton = GuiUtils.getPrimaryButton("Train Policy", VaadinIcon.PLAY.create(), click -> StartRunAction.startRun(this, rewardFunctionEditor, trainingService, runDAO, experimentDAO, observationDAO));
        saveDraftButton = new Button("Save", click -> handleSaveDraftClicked(() -> { }));
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

    public void setUnsavedChangesLabel(boolean isVisible) {
        unsavedChanges.setVisible(isVisible);
    }

    public Experiment getUpdatedExperiment() {
        experiment.setRewardFunction(rewardFunctionEditor.getExperiment().getRewardFunction());
        experiment.setSelectedObservations(observationsPanel.getSelectedObservations());
        return experiment;
    }

    private void handleSaveDraftClicked(Command afterClickedCallback) {
        // TODO -> STEPH -> How do we do this in a nice and consistent manner?
        EventBus.post(new ExperimentSavedViewBusEvent());
        observationDAO.saveExperimentObservations(experiment.getId(), observationsPanel.getSelectedObservations());
        segmentIntegrator.draftSaved();
        disableSaveDraft();
        NotificationUtils.showSuccess("Draft successfully saved");
        isNeedsSaving = false;
        afterClickedCallback.execute();
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
    protected void updateComponentEnablements() {
        // TODO -> STEPH -> Move all button enablement and visibility code here when an experiment is set and/or updated.
        unarchiveExperimentButton.setVisible(experiment.isArchived());
        startRunButton.setEnabled(canStartTraining());
    }

    // TODO -> STEPH -> Re-add at the correct location.
    private void disableSaveDraft() {
        saveDraftButton.setEnabled(false);
        unsavedChanges.setVisible(false);
        notesSavedHint.setVisible(false);
    }

    @Override
    protected void validateCorrectViewForExperiment() {
        if(!experiment.isDraft()) {
            // TODO -> STEPH -> Why is this not forwarding correctly to the right page? Is there a ui.navigate somewhere else?
            // For some reason I have to use UI.getCurrent() rather than getUI().ifPresent() because it's the only way to navigate at this stage.
            UI.getCurrent().navigate(ExperimentView.class, experimentId);
        }
    }

    protected void createExperimentComponents() {
        // TODO -> STEPH -> create other components
        // TODO -> STEPH -> Create Notes should be similar to experiment where this is just the constructor.
        createAndSetupNotesField();
        rewardFunctionEditor = new RewardFunctionEditor(getUISupplier(), experimentDAO, rewardValidationService);
        // TODO -> STEPH -> Below are the components that should not include experiment as part of the constructor because it could be null for the comparison view.
        observationsPanel = new ObservationsPanel(experiment, false);
        rewardVariablesTable = new RewardVariablesTable(getUISupplier());

        experimentComponentList.addAll(List.of(
                notesField,
                rewardFunctionEditor,
                observationsPanel,
                rewardVariablesTable));
    }
}
