package io.skymind.pathmind.webapp.ui.views.experiment;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
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
import io.skymind.pathmind.webapp.ui.components.rewardVariables.RewardVariablesTable;
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
import io.skymind.pathmind.webapp.ui.views.experiment.subscribers.NewExperimentViewFavoriteSubscriber;
import io.skymind.pathmind.webapp.ui.views.settings.BetaFeatureSettingsView;
import io.skymind.pathmind.webapp.ui.views.settings.SettingsViewContent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@CssImport("./styles/views/new-experiment-view.css")
@Route(value = Routes.NEW_EXPERIMENT, layout = MainLayout.class)
public class NewExperimentView extends AbstractExperimentView implements BeforeLeaveObserver {

    protected ExperimentNotesField notesField;
    private RewardFunctionBuilder rewardFunctionBuilder;
    private RewardVariablesTable rewardVariablesTable;
    private SimulationParametersPanel simulationParametersPanel;
    private ObservationsPanel observationsPanel;
    private SettingsViewContent settingsPanel;
    private FavoriteStar favoriteStar;
    private Div upgradeBannerExpCt;
    private Div upgradeBannerActMask;
    private Select<Experiment> diffSelect;
    private Button betaRewardTermsBanner;
    private Button unarchiveExperimentButton;
    private Button saveDraftButton;
    private Button startRunButton;
    private Button archiveButton;
    private SplitButton splitButton;
    private Anchor downloadModelLink;
    private boolean isNeedsSaving = false;
    private boolean isPythonModel = false;
    private boolean hasRunningExperiments = false;
    private boolean isTrialPlanUser = true;

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
        createBetaRewardTermsBanner();
        favoriteStar = new FavoriteStar(false, newIsFavorite -> onFavoriteToggled(newIsFavorite, experiment));
        HorizontalLayout titlePanel = new HorizontalLayout(
                experimentPanelTitle,
                favoriteStar,
                downloadModelLink
        );
        titlePanel.setSpacing(false);

        VerticalLayout panelTitle = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(
                verifyEmailReminder,
                upgradeBannerExpCt,
                upgradeBannerActMask,
                betaRewardTermsBanner,
                titlePanel);
        panelTitle.setClassName("panel-title");
        panelTitle.setWidth(null);

        SplitLayout simulationParametersAndObservationsWrapper = WrapperUtils.wrapCenterAlignmentFullSplitLayoutHorizontal(
                simulationParametersPanel,
                observationsPanel,
                50);

        settingsPanel = new SettingsViewContent(userService.getCurrentUser(), environmentManager, segmentIntegrator, true);

        VerticalLayout rewardVariablesPanel = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(
                LabelFactory.createLabel("Reward Variables", CssPathmindStyles.BOLD_LABEL),
                rewardVariablesTable
        );
        rewardVariablesPanel.addClassName("reward-variables-panel");

        SplitLayout notesAndSettings = WrapperUtils.wrapCenterAlignmentFullSplitLayoutHorizontal(
                notesField,
                settingsPanel,
                50);

        SplitLayout bottomPanel = WrapperUtils.wrapCenterAlignmentFullSplitLayoutHorizontal(
                rewardVariablesPanel,
                notesAndSettings,
                33);
        bottomPanel.setClassName("bottom-panel");
        
        SplitLayout panelsWrapper = WrapperUtils.wrapCenterAlignmentFullSplitLayoutVertical(
                simulationParametersAndObservationsWrapper,
                bottomPanel,
                50);
        panelsWrapper.setClassName("panels-wrapper");

        diffSelect = createDiffSelect();
        HorizontalLayout diffToolWrapper = new HorizontalLayout(
            new Span("Highlight difference from"),
            diffSelect
        );
        diffToolWrapper.setClassName("diff-tool-wrapper");

        splitButton = createSplitButton();
        HorizontalLayout buttonsWrapper = new HorizontalLayout(
                diffToolWrapper,
                splitButton,
                unarchiveExperimentButton);
        buttonsWrapper.setWidth(null);

        SplitLayout splitWrapper = WrapperUtils.wrapCenterAlignmentFullSplitLayoutHorizontal(
            rewardFunctionBuilder,
            panelsWrapper,
            48);
        splitWrapper.setClassName("split-wrapper");
        splitWrapper.addSplitterDragendListener(dragged -> rewardFunctionBuilder.resize());

        VerticalLayout viewSection = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(
            WrapperUtils.wrapWidthFullBetweenHorizontal(panelTitle, buttonsWrapper),
            modelCheckerService.createInvalidErrorLabel(experiment.getModel()),
            splitWrapper
        );

        viewSection.setClassName("view-section");

        HorizontalLayout experimentViewWrapper = WrapperUtils.wrapWidthFullHorizontal(experimentsNavbar, viewSection);
        experimentViewWrapper.setSpacing(false);
        return experimentViewWrapper;
    }

    @Override
    protected void addEventBusSubscribers() {
        EventBus.subscribe(this, getUISupplier(), new NewExperimentViewFavoriteSubscriber(this));
    }

    private Select<Experiment> createDiffSelect() {
        Select<Experiment> select = new Select<>();
        select.setItemLabelGenerator(item -> {
            if (item != null) {
                if (item.getName() != "Model Default") {
                    return "Experiment #"+item.getName();
                }
                return item.getName();
            }
            return "None";
        });
        select.setEmptySelectionAllowed(true);
        select.setEmptySelectionCaption("None");
        select.getElement().setAttribute("theme", "small");
        select.addValueChangeListener(valueChangedEvent -> {
            Experiment selectedExp = valueChangedEvent.getValue();
            if (selectedExp != null) {
                simulationParametersPanel.setComparisonModeTheOtherParameters(selectedExp.getSimulationParameters());
                observationsPanel.setComparisonModeTheOtherSelectedObservations(selectedExp.getSelectedObservations());
            } else {
                simulationParametersPanel.setComparisonModeTheOtherParameters(null);
                observationsPanel.setComparisonModeTheOtherSelectedObservations(null);
            }
        });
        return select;
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

    private void createBetaRewardTermsBanner() {
        betaRewardTermsBanner = new Button(new Div(new Span("Reward Terms UI"),
            new Html("<sup>BETA</sup>"),
            new Span("out now!")), click -> {
                    segmentIntegrator.navigatedToBetaFeaturePageFromNewExpViewBanner();
                    getUI().ifPresent(ui -> ui.navigate(BetaFeatureSettingsView.class));
        });
        betaRewardTermsBanner.addClassName("beta-feature-banner");
    }

    private void createButtons() {
        unarchiveExperimentButton = GuiUtils.getPrimaryButton("Unarchive", VaadinIcon.ARROW_BACKWARD.create(), click -> UnarchiveExperimentAction.unarchive(this, () -> getExperiment(), () -> getExperiment()));
        startRunButton = GuiUtils.getPrimaryButton("▶ Train Policy", click -> StartRunAction.startRun(this));
        saveDraftButton = new Button("Save Draft", click -> SaveDraftAction.saveDraft(this));
        archiveButton = GuiUtils.getPrimaryButton("Archive", click -> ArchiveExperimentAction.archive(experiment, this));
    }

    /************************************** UI element creations are above this line **************************************/

    // REFACTOR -> The logic should really be in ExperimentUtils because it's business logic rather than GUI logic but for now we'll just leave it here.
    private boolean canStartTraining() {
        // SLIN -> I am disabling new trainings as we wind down the product.
        return false;

        // PathmindUser currentUser = userService.getCurrentUser();
        // Model model = experiment.getModel();
        // boolean isPyModel = ModelType.isPythonModel(ModelType.fromValue(model.getModelType())) || ModelType.isPathmindModel(ModelType.fromValue(model.getModelType()));
        // return ModelUtils.isValidModel(model)
        //         && (!isTrialPlanUser || (isTrialPlanUser && !hasRunningExperiments))
        //         && (!isTrialPlanUser || (isTrialPlanUser && !model.isActionmask()))
        //         && (isPyModel || rewardFunctionBuilder.isValidForTraining())
        //         && (isPyModel || (observationsPanel.getSelectedObservations() != null && !observationsPanel.getSelectedObservations().isEmpty()))
        //         && !experiment.isArchived()
        //         && (currentUser.getEmailVerifiedAt() != null || runDAO.numberOfRunsByUser(currentUser.getId()) < allowedRunsNoVerified);
    }

    /**
     * Only used by the Start Training so that we can disable the save on page leave.
     */
    public void removeNeedsSaving() {
        isNeedsSaving = false;
    }

    public void setNeedsSaving() {
        isNeedsSaving = true;
        setButtonsEnablement();
    }

    public void setButtonsEnablement() {
        saveDraftButton.setEnabled(isNeedsSaving);
        startRunButton.setEnabled(canStartTraining());
        splitButton.enableMainButton(canStartTraining());
    }

    public void saveAdvancedSettings() {
        settingsPanel.saveSettings();
    }

    public String getSettingsText() {
        return settingsPanel.getSettingsText();
    }

    private void handleSaveDraftClicked(Command afterClickedCallback) {
        SaveDraftAction.saveDraft(this);
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
        isTrialPlanUser = userService.getCurrentUser().isTrialPlanUser();
        hasRunningExperiments = experimentDAO.getExperimentsWithRunStatusCountForUser(userService.getCurrentUserId(), RunStatus.RUNNING_STATES_CODES) > 0;
        splitButton.setVisible(!experiment.isArchived());
        favoriteStar.setValue(experiment.isFavorite());
        unarchiveExperimentButton.setVisible(experiment.isArchived());
        saveDraftButton.setEnabled(isNeedsSaving);
        startRunButton.setEnabled(canStartTraining());
        splitButton.enableMainButton(canStartTraining());
        upgradeBannerExpCt.setVisible(userService.isCurrentUserVerified() && isTrialPlanUser && hasRunningExperiments);
        upgradeBannerActMask.setVisible(userService.isCurrentUserVerified() && isTrialPlanUser && experiment.getModel().isActionmask());
        betaRewardTermsBanner.setVisible(!userService.getCurrentUser().isRewardTermsOn());
        List<Experiment> experimentsExcludingThisExp = experimentDAO.getExperimentsForModel(experiment.getModelId(), true)
                                                        .stream()
                                                        .filter(exp -> exp.getId() != experiment.getId())
                                                        .collect(Collectors.toList());
        Experiment modelDefaultExperiment = new Experiment();
        modelDefaultExperiment.setName("Model Default");
        modelDefaultExperiment.setModelId(experiment.getModelId());
        modelDefaultExperiment.setSimulationParameters(getModelSimulationParameters());
        modelDefaultExperiment.setSelectedObservations(experiment.getModelObservations());
        experimentsExcludingThisExp.add(modelDefaultExperiment);
        diffSelect.setItems(experimentsExcludingThisExp);
        diffSelect.setValue(modelDefaultExperiment);
    }

    @Override
    public void setExperiment(Experiment experiment) {
        if (isNeedsSaving) {
            SaveDraftAction.saveDraft(this);
        }
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
        rewardFunctionBuilder = new RewardFunctionBuilder(this, rewardValidationService, userService.getCurrentUser().isRewardTermsOn());
        // This is an exception because the modelObservations are the same for all experiments in the same group.
        observationsPanel = new ObservationsPanel(experiment.getModelObservations(), false, this);
        rewardVariablesTable = new RewardVariablesTable();
        simulationParametersPanel = new SimulationParametersPanel(this, false, userService.getCurrentUser(), segmentIntegrator);

        experimentComponentList.addAll(List.of(
                notesField,
                rewardFunctionBuilder,
                observationsPanel,
                rewardVariablesTable,
                simulationParametersPanel));
    }
}
