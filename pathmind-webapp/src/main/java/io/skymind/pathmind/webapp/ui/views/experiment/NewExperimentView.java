package io.skymind.pathmind.webapp.ui.views.experiment;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveEvent.ContinueNavigationAction;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.Command;
import io.skymind.pathmind.services.RewardValidationService;
import io.skymind.pathmind.shared.constants.ModelType;
import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.shared.data.PathmindUser;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.shared.services.training.environment.ExecutionEnvironmentManager;
import io.skymind.pathmind.shared.utils.ModelUtils;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.security.UserService;
import io.skymind.pathmind.webapp.ui.components.FavoriteStar;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.DownloadModelLink;
import io.skymind.pathmind.webapp.ui.components.atoms.SplitButton;
import io.skymind.pathmind.webapp.ui.components.modelChecker.ModelCheckerService;
import io.skymind.pathmind.webapp.ui.components.observations.ObservationsPanel;
import io.skymind.pathmind.webapp.ui.components.simulationParameters.SimulationParametersPanel;
import io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.utils.GuiUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.account.AccountUpgradeView;
import io.skymind.pathmind.webapp.ui.views.experiment.actions.newExperiment.SaveDraftAction;
import io.skymind.pathmind.webapp.ui.views.experiment.actions.newExperiment.StartRunAction;
import io.skymind.pathmind.webapp.ui.views.experiment.actions.shared.ArchiveExperimentAction;
import io.skymind.pathmind.webapp.ui.views.experiment.actions.shared.UnarchiveExperimentAction;
import io.skymind.pathmind.webapp.ui.views.experiment.components.experimentNotes.ExperimentNotesField;
import io.skymind.pathmind.webapp.ui.views.experiment.components.rewardFunction.RewardFunctionBuilder;
import io.skymind.pathmind.webapp.ui.views.experiment.components.rewardFunction.RewardFunctionErrorPanel;
import io.skymind.pathmind.webapp.ui.views.experiment.subscribers.NewExperimentViewFavoriteSubscriber;
import io.skymind.pathmind.webapp.ui.views.settings.SettingsViewContent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@CssImport("./styles/views/new-experiment-view.css")
@Route(value = Routes.NEW_EXPERIMENT, layout = MainLayout.class)
public class NewExperimentView extends AbstractExperimentView implements BeforeLeaveObserver {

    protected ExperimentNotesField notesField;
    private RewardFunctionBuilder rewardFunctionBuilder;
    // private RewardFunctionEditor rewardFunctionEditor;
    private SimulationParametersPanel simulationParametersPanel;
    private RewardFunctionErrorPanel rewardFunctionErrorPanel;
    private ObservationsPanel observationsPanel;
    private SettingsViewContent settingsPanel;
    private FavoriteStar favoriteStar;
    private Div upgradeBannerExpCt;
    private Div upgradeBannerActMask;
    private Button unarchiveExperimentButton;
    private Button saveDraftButton;
    private Button startRunButton;
    private Button archiveButton;
    private SplitButton splitButton;
    private Anchor downloadModelLink;
    private boolean isNeedsSaving = false;
    private boolean isPythonModel = false;
    private boolean hasRunningExperiments = false;
    private boolean isBasicPlanUser = true;

    private final int allowedRunsNoVerified;

    @Autowired
    private RewardValidationService rewardValidationService;
    @Autowired
    private ModelCheckerService modelCheckerService;
    @Autowired
    private UserService userService;
    @Autowired
    private ExecutionEnvironmentManager environmentManager;

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
        createButtons();

        // It is the same for all experiments from the same model so it doesn't have to be updated as long
        // as the user is on the Experiment View (the nav bar only allows navigation to experiments from the same model)
        // If in the future we allow navigation to experiments from other models, then we'll need to update the button accordingly on navigation
        downloadModelLink = new DownloadModelLink(experiment.getProject().getName(), experiment.getModel(), modelService, segmentIntegrator, false, isPythonModel);

        VerticalLayout mainPanel = new VerticalLayout();
        mainPanel.setPadding(false);
        Span verifyEmailReminder = LabelFactory.createLabel("To run more experiments, please verify your email.", CssPathmindStyles.WARNING_LABEL);
        verifyEmailReminder.setVisible(!userService.isCurrentUserVerified() && runDAO.numberOfRunsByUser(userService.getCurrentUser().getId()) >= allowedRunsNoVerified);
        createUpgradeBanners();
        favoriteStar = new FavoriteStar(false, newIsFavorite -> onFavoriteToggled(newIsFavorite, experiment));
        HorizontalLayout titleWithStar = new HorizontalLayout(experimentPanelTitle, favoriteStar);
        titleWithStar.setSpacing(false);
        titleWithStar.setAlignItems(FlexComponent.Alignment.CENTER);
        HorizontalLayout titlePanel = WrapperUtils.wrapWidthFullHorizontal(
                titleWithStar,
                downloadModelLink
        );
        titlePanel.setPadding(true);

        VerticalLayout panelTitle = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(
                verifyEmailReminder,
                upgradeBannerExpCt,
                upgradeBannerActMask,
                titlePanel);
        panelTitle.setClassName("panel-title");

        Span errorDescriptionLabel = modelCheckerService.createInvalidErrorLabel(experiment.getModel());
        rewardFunctionErrorPanel = new RewardFunctionErrorPanel();
        
        SplitLayout simulationParametersAndObservationsWrapper = WrapperUtils.wrapCenterAlignmentFullSplitLayoutHorizontal(
                simulationParametersPanel,
                observationsPanel,
                50);
        
        SplitLayout rewardFunctionAndObservationsWrapper = WrapperUtils.wrapCenterAlignmentFullSplitLayoutHorizontal(
                rewardFunctionBuilder,
                simulationParametersAndObservationsWrapper,
                52.5);
        rewardFunctionAndObservationsWrapper.setClassName("reward-function-wrapper");

        settingsPanel = new SettingsViewContent(userService.getCurrentUser(), environmentManager, segmentIntegrator, true);

        rewardFunctionErrorPanel = new RewardFunctionErrorPanel();
        SplitLayout errorAndNotesContainer = WrapperUtils.wrapCenterAlignmentFullSplitLayoutHorizontal(
                rewardFunctionErrorPanel,
                notesField,
                68.85);
        errorAndNotesContainer.setClassName("error-and-notes-container");

        SplitLayout bottomPanel = WrapperUtils.wrapCenterAlignmentFullSplitLayoutHorizontal(
                errorAndNotesContainer,
                settingsPanel,
                76.25);
        bottomPanel.setClassName("bottom-panel");

        splitButton = createSplitButton();
        HorizontalLayout buttonsWrapper = new HorizontalLayout(
                splitButton,
                unarchiveExperimentButton);
        buttonsWrapper.setWidth(null);

        SplitLayout splitWrapper = WrapperUtils.wrapCenterAlignmentFullSplitLayoutVertical(
            WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(
                WrapperUtils.wrapWidthFullBetweenHorizontal(panelTitle, buttonsWrapper),
                modelCheckerService.createInvalidErrorLabel(experiment.getModel()),
                rewardFunctionAndObservationsWrapper
            ),
            bottomPanel,
            60);

        splitWrapper.setClassName("view-section");

        HorizontalLayout panelsWrapper = WrapperUtils.wrapWidthFullHorizontal(experimentsNavbar, splitWrapper);
        panelsWrapper.setSpacing(false);
        return panelsWrapper;
    }

    @Override
    protected void addEventBusSubscribers() {
        EventBus.subscribe(this, getUISupplier(), new NewExperimentViewFavoriteSubscriber(this));
    }

    private SplitButton createSplitButton() {
        List<Button> actionButtons = new ArrayList<Button>();
        actionButtons.add(startRunButton);
        actionButtons.add(saveDraftButton);
        actionButtons.add(archiveButton);
        SplitButton splitButton = new SplitButton(actionButtons);
        splitButton.addThemeName("new-experiment-split-button");
        return splitButton;
    }

    private void createAndSetupNotesField() {
        notesField = createNotesField(() -> segmentIntegrator.addedNotesNewExperimentView(), false, true);
        notesField.setPlaceholder("Add Notes (optional)");
        notesField.setSecondaryStyle(true);
        notesField.setOnNotesChangeHandler(() -> setNeedsSaving());
        if (experiment.isArchived()) {
            notesField.setReadonly(true);
        }
    }

    private void createUpgradeBanners() {
        Button upgradeLink = new Button("Upgrade now", click -> {
            segmentIntegrator.navigatedToPricingFromNewExpViewBanner();
            getUI().ifPresent(ui -> ui.navigate(AccountUpgradeView.class));
        });
        upgradeLink.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);

        // upgrade banner for Experiment count
        upgradeBannerExpCt = new Div(new Span("You have one experiment running already."));
        upgradeBannerExpCt.add(upgradeLink);
        upgradeBannerExpCt.add(new Span("to run multiple experiments in parallel."));
        upgradeBannerExpCt.addClassName(CssPathmindStyles.WARNING_LABEL);

        Button upgradeLink1 = new Button("Upgrade now", click -> {
            segmentIntegrator.navigatedToPricingFromNewExpViewBanner();
            getUI().ifPresent(ui -> ui.navigate(AccountUpgradeView.class));
        });
        upgradeLink1.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);

        // upgrade banner for Action masking
        upgradeBannerActMask = new Div(new Span("A Pathmind subscription is required to use action masking."));
        upgradeBannerActMask.add(upgradeLink1);
        upgradeBannerActMask.add(new Span("to enable action masking and more"));
        upgradeBannerActMask.addClassName(CssPathmindStyles.WARNING_LABEL);
    }

    private void createButtons() {
        unarchiveExperimentButton = GuiUtils.getPrimaryButton("Unarchive", VaadinIcon.ARROW_BACKWARD.create(), click -> UnarchiveExperimentAction.unarchive(this, () -> getExperiment(), () -> getExperiment()));
        startRunButton = GuiUtils.getPrimaryButton("▶ Train Policy", click -> StartRunAction.startRun(this));
        saveDraftButton = new Button("Save Draft", click -> handleSaveDraftClicked());
        archiveButton = GuiUtils.getPrimaryButton("Archive", click -> ArchiveExperimentAction.archive(experiment, this));
    }

    /************************************** UI element creations are above this line **************************************/

    // REFACTOR -> The logic should really be in ExperimentUtils because it's business logic rather than GUI logic but for now we'll just leave it here.
    private boolean canStartTraining() {
        PathmindUser currentUser = userService.getCurrentUser();
        Model model = experiment.getModel();
        boolean isPyModel = ModelType.isPythonModel(ModelType.fromValue(model.getModelType())) || ModelType.isPathmindModel(ModelType.fromValue(model.getModelType()));
        return ModelUtils.isValidModel(model)
                && (!isBasicPlanUser || (isBasicPlanUser && !hasRunningExperiments))
                && (!isBasicPlanUser || (isBasicPlanUser && !model.isActionmask()))
                && (isPyModel) //|| rewardFunctionEditor.isValidForTraining())
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
        saveDraftButton.setEnabled(isNeedsSaving);
        startRunButton.setEnabled(canStartTraining());
        splitButton.enableMainButton(canStartTraining());
    }

    public Experiment getUpdatedExperiment() {
        // TODO -> set new reward function rows
        experiment.setSelectedObservations(observationsPanel.getSelectedObservations());
        return experiment;
    }

    public void saveAdvancedSettings() {
        settingsPanel.saveSettings();
    }

    public String getSettingsText() {
        return settingsPanel.getSettingsText();
    }

    private void handleSaveDraftClicked() {
        SaveDraftAction.saveDraft(this);
    }

    private void handleSaveDraftClicked(Command afterClickedCallback) {
        handleSaveDraftClicked();
        afterClickedCallback.execute();
    }

    @Override
    public void beforeLeave(BeforeLeaveEvent event) {
        ContinueNavigationAction action = event.postpone();
        if (isNeedsSaving) {
            handleSaveDraftClicked(action::proceed);
        } else {
            action.proceed();
        }
    }

    @Override
    public void updateComponents() {
        super.updateComponents();
        isBasicPlanUser = userService.getCurrentUser().isBasicPlanUser();
        hasRunningExperiments = experimentDAO.getExperimentsWithRunStatusCountForUser(userService.getCurrentUserId(), RunStatus.RUNNING_STATES_CODES) > 0;
        splitButton.setVisible(!experiment.isArchived());
        favoriteStar.setValue(experiment.isFavorite());
        unarchiveExperimentButton.setVisible(experiment.isArchived());
        saveDraftButton.setEnabled(isNeedsSaving);
        startRunButton.setEnabled(canStartTraining());
        splitButton.enableMainButton(canStartTraining());
        upgradeBannerExpCt.setVisible(userService.isCurrentUserVerified() && isBasicPlanUser && hasRunningExperiments);
        upgradeBannerActMask.setVisible(userService.isCurrentUserVerified() && isBasicPlanUser && experiment.getModel().isActionmask());
    }

    @Override
    public void setExperiment(Experiment experiment) {
        handleSaveDraftClicked();
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
        if (experiment.isDraft()) {
            return true;
        }
        event.forwardTo(Routes.EXPERIMENT, ""+experimentId);
        return false;
    }

    protected void createExperimentComponents() {
        createAndSetupNotesField();
        rewardFunctionBuilder = new RewardFunctionBuilder(this);
        // This is an exception because the modelObservations are the same for all experiments in the same group.
        observationsPanel = new ObservationsPanel(experiment.getModelObservations(), false, this);
        simulationParametersPanel = new SimulationParametersPanel(this, false, userService.getCurrentUser(), segmentIntegrator);

        experimentComponentList.addAll(List.of(
                notesField,
                rewardFunctionBuilder,
                observationsPanel,
                simulationParametersPanel));
    }
}
