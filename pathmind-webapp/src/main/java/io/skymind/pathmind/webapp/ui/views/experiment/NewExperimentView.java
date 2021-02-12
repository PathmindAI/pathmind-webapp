package io.skymind.pathmind.webapp.ui.views.experiment;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.Component;
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
import io.skymind.pathmind.shared.constants.ModelType;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.shared.data.PathmindUser;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.shared.utils.ModelUtils;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.security.UserService;
import io.skymind.pathmind.webapp.ui.components.FavoriteStar;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.alp.DownloadModelAlpLink;
import io.skymind.pathmind.webapp.ui.components.atoms.SplitButton;
import io.skymind.pathmind.webapp.ui.components.modelChecker.ModelCheckerService;
import io.skymind.pathmind.webapp.ui.components.molecules.ConfirmPopup;
import io.skymind.pathmind.webapp.ui.components.observations.ObservationsPanel;
import io.skymind.pathmind.webapp.ui.components.rewardVariables.RewardVariablesTable;
import io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.utils.GuiUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.actions.newExperiment.SaveDraftAction;
import io.skymind.pathmind.webapp.ui.views.experiment.actions.newExperiment.StartRunAction;
import io.skymind.pathmind.webapp.ui.views.experiment.actions.shared.UnarchiveExperimentAction;
import io.skymind.pathmind.webapp.ui.views.experiment.components.experimentNotes.ExperimentNotesField;
import io.skymind.pathmind.webapp.ui.views.experiment.components.rewardFunction.RewardFunctionEditor;
import io.skymind.pathmind.webapp.ui.views.experiment.subscribers.main.experiment.NewExperimentViewFavoriteSubscriber;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@CssImport("./styles/views/new-experiment-view.css")
@Route(value = Routes.NEW_EXPERIMENT, layout = MainLayout.class)
public class NewExperimentView extends AbstractExperimentView implements BeforeLeaveObserver {

    private final int REWARD_FUNCTION_MAX_LENGTH = 65535;
    protected ExperimentNotesField notesField;
    private RewardFunctionEditor rewardFunctionEditor;
    private RewardVariablesTable rewardVariablesTable;
    private ObservationsPanel observationsPanel;
    private FavoriteStar favoriteStar;
    private Button unarchiveExperimentButton;
    private Button saveDraftButton;
    private Button startRunButton;
    private SplitButton splitButton;
    private Anchor downloadModelAlpLink;
    private boolean isNeedsSaving = false;

    private final int allowedRunsNoVerified;

    @Autowired
    private RewardValidationService rewardValidationService;
    @Autowired
    private ModelCheckerService modelCheckerService;
    @Autowired
    private UserService userService;

    public NewExperimentView(
            @Value("${pm.allowed_run_no_verified}") int allowedRunsNoVerified,
            @Value("${pathmind.notification.newRunDailyLimit}") int newRunDailyLimit,
            @Value("${pathmind.notification.newRunMonthlyLimit}") int newRunMonthlyLimit,
            @Value("${pathmind.notification.newRunNotificationThreshold}") int newRunNotificationThreshold) {
        super();
        setUserCaps(newRunDailyLimit, newRunMonthlyLimit, newRunNotificationThreshold);
        addClassName("new-experiment-view");
        this.allowedRunsNoVerified = allowedRunsNoVerified;
    }

    @Override
    protected Component getMainContent() {
        HorizontalLayout mainContent = createMainPanel();
        return mainContent;
    }

    @Override
    protected void addEventBusSubscribers() {
        EventBus.subscribe(this, getUISupplier(), new NewExperimentViewFavoriteSubscriber(this));
    }

    private HorizontalLayout createMainPanel() {

        createButtons();

        // It is the same for all experiments from the same model so it doesn't have to be updated as long
        // as the user is on the Experiment View (the nav bar only allows navigation to experiments from the same model)
        // If in the future we allow navigation to experiments from other models, then we'll need to update the button accordingly on navigation
        downloadModelAlpLink = new DownloadModelAlpLink(experiment.getProject().getName(), experiment.getModel(), modelService, segmentIntegrator);

        VerticalLayout mainPanel = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing();
        mainPanel.setSpacing(true);
        Span verifyEmailReminder = LabelFactory.createLabel("To run more experiments, please verify your email.", CssPathmindStyles.WARNING_LABEL);
        verifyEmailReminder.setVisible(!userService.isCurrentUserVerified() && runDAO.numberOfRunsByUser(userService.getCurrentUser().getId()) >= allowedRunsNoVerified);
        favoriteStar = new FavoriteStar(false, newIsFavorite -> onFavoriteToggled(newIsFavorite, experiment));
        HorizontalLayout titleWithStar = new HorizontalLayout(experimentPanelTitle, favoriteStar);
        titleWithStar.setSpacing(false);
        titleWithStar.setAlignItems(FlexComponent.Alignment.CENTER);

        VerticalLayout panelTitle = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(
                verifyEmailReminder,
                WrapperUtils.wrapWidthFullHorizontal(
                        titleWithStar,
                        downloadModelAlpLink
                ),
                LabelFactory.createLabel(
                        "To judge if an action is a good one, we calculate a reward score. "
                                + "The reward score is based on the reward function.",
                        CssPathmindStyles.SECTION_SUBTITLE_LABEL));
        panelTitle.setClassName("panel-title");

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

        splitButton = createSplitButton();
        HorizontalLayout buttonsWrapper = new HorizontalLayout(
                splitButton,
                unarchiveExperimentButton);
        buttonsWrapper.setWidth(null);

        mainPanel.add(WrapperUtils.wrapWidthFullBetweenHorizontal(panelTitle, buttonsWrapper), errorDescriptionLabel,
                rewardFunctionAndObservationsWrapper, errorAndNotesContainer);
        mainPanel.setClassName("view-section");

        HorizontalLayout panelsWrapper = WrapperUtils.wrapWidthFullHorizontal(experimentsNavbar, mainPanel);
        panelsWrapper.setSpacing(false);
        return panelsWrapper;
    }

    private SplitButton createSplitButton() {
        List<Button> actionButtons = new ArrayList<Button>();
        actionButtons.add(startRunButton);
        actionButtons.add(saveDraftButton);
        SplitButton splitButton = new SplitButton(actionButtons);
        splitButton.addThemeName("new-experiment-split-button");
        return splitButton;
    }

    private void createAndSetupNotesField() {
        notesField = createNotesField(() -> segmentIntegrator.addedNotesNewExperimentView(), false, true);
        notesField.setPlaceholder("Add Notes (optional)");
        notesField.setOnNotesChangeHandler(() -> setNeedsSaving());
        if (experiment.isArchived()) {
            notesField.setReadonly(true);
        }
    }

    private void createButtons() {
        // The NewExperimentView doesn't need a lock on the archive because it can't be updated at the same time as an experiment is archived however to adhere to the action's requirement we just use the experiment.
        unarchiveExperimentButton = GuiUtils.getPrimaryButton("Unarchive", VaadinIcon.ARROW_BACKWARD.create(), click -> UnarchiveExperimentAction.unarchive(this, () -> getExperiment(), () -> getExperiment()));
        startRunButton = GuiUtils.getPrimaryButton("▶ Train Policy", click -> StartRunAction.startRun(this, rewardFunctionEditor));
        saveDraftButton = new Button("Save Draft", click -> handleSaveDraftClicked(() -> {
        }));
    }

    /************************************** UI element creations are above this line **************************************/

    // REFACTOR -> The logic should really be in ExperimentUtils because it's business logic rather than GUI logic but for now we'll just leave it here.
    private boolean canStartTraining() {
        PathmindUser currentUser = userService.getCurrentUser();
        Model model = experiment.getModel();
        boolean isPyModel = ModelType.isPythonModel(ModelType.fromValue(model.getModelType()));
        return ModelUtils.isValidModel(model)
                && (isPyModel || rewardFunctionEditor.isValidForTraining())
                && (isPyModel || (observationsPanel.getSelectedObservations() != null && !observationsPanel.getSelectedObservations().isEmpty()))
                && !experiment.isArchived()
                && (currentUser.getEmailVerifiedAt() != null || runDAO.numberOfRunsByUser(currentUser.getId()) < allowedRunsNoVerified);
    }

    /**
     * Only used by the Start Training so that we can disable the save on page leave.
     */
    public void removeNeedsSaving() {
        isNeedsSaving = false;
    }

    public void setNeedsSaving() {
        isNeedsSaving = true;
        saveDraftButton.setEnabled(rewardFunctionEditor.isRewardFunctionLessThanMaxLength());
        startRunButton.setEnabled(canStartTraining());
        splitButton.enableMainButton(canStartTraining());
    }

    public Experiment getUpdatedExperiment() {
        experiment.setRewardFunction(rewardFunctionEditor.getExperiment().getRewardFunction());
        experiment.setSelectedObservations(observationsPanel.getSelectedObservations());
        return experiment;
    }

    private void handleSaveDraftClicked(Command afterClickedCallback) {
        SaveDraftAction.saveDraft(this);
        afterClickedCallback.execute();
    }

    @Override
    public void beforeLeave(BeforeLeaveEvent event) {
        ContinueNavigationAction action = event.postpone();
        saveDraftExperiment(() -> action.proceed());
    }

    // STEPH -> TODO -> Clean this up so that it's consistent with the rest.
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

    // STEPH -> TODO -> Should be moved to a component rather than here in the view.
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
    public void updateComponents() {
        super.updateComponents();
        splitButton.setVisible(!experiment.isArchived());
        favoriteStar.setValue(experiment.isFavorite());
        unarchiveExperimentButton.setVisible(experiment.isArchived());
        saveDraftButton.setEnabled(isNeedsSaving);
        startRunButton.setEnabled(canStartTraining());
        splitButton.enableMainButton(canStartTraining());
    }

    @Override
    public void setExperiment(Experiment experiment) {
        saveDraftExperiment(() -> {
        });
        // We need to override this method so that we can reset the needs saving so that it doesn't retain the previous state.
        disableSaveNeeded();
        favoriteStar.setValue(experiment.isFavorite());
        super.setExperiment(experiment);
    }

    public Experiment getExperiment() {
        return experiment;
    }

    public void disableSaveNeeded() {
        saveDraftButton.setEnabled(false);
        isNeedsSaving = false;
    }

    @Override
    protected boolean isValidViewForExperiment(BeforeEnterEvent event) {
        if (experimentDAO.isDraftExperiment(experimentId)) {
            return true;
        } else {
            // If incorrect then we need to both use the event.forwardTo rather than ui.navigate otherwise it will continue to process the view.
            event.forwardTo(Routes.EXPERIMENT, experimentId);
            return false;
        }
    }

    protected void createExperimentComponents() {
        createAndSetupNotesField();
        rewardFunctionEditor = new RewardFunctionEditor(this, rewardValidationService);
        // This is an exception because the modelObservations are the same for all experiments in the same group.
        observationsPanel = new ObservationsPanel(experiment.getModelObservations(), false, this);
        rewardVariablesTable = new RewardVariablesTable();

        experimentComponentList.addAll(List.of(
                notesField,
                rewardFunctionEditor,
                observationsPanel,
                rewardVariablesTable));
    }
}
