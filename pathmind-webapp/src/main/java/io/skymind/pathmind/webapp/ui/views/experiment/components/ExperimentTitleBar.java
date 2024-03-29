package io.skymind.pathmind.webapp.ui.views.experiment.components;

import java.util.Optional;
import java.util.function.Supplier;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.db.dao.PolicyDAO;
import io.skymind.pathmind.db.dao.RunDAO;
import io.skymind.pathmind.db.dao.UserDAO;
import io.skymind.pathmind.services.ModelService;
import io.skymind.pathmind.services.PolicyFileService;
import io.skymind.pathmind.services.TrainingService;
import io.skymind.pathmind.shared.constants.ModelType;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.PathmindUser;
import io.skymind.pathmind.shared.featureflag.Feature;
import io.skymind.pathmind.shared.featureflag.FeatureManager;
import io.skymind.pathmind.shared.services.PolicyServerService;
import io.skymind.pathmind.webapp.ui.components.DownloadModelLink;
import io.skymind.pathmind.webapp.ui.components.FavoriteStar;
import io.skymind.pathmind.webapp.ui.components.atoms.SharedByUsername;
import io.skymind.pathmind.webapp.ui.components.atoms.TagLabel;
import io.skymind.pathmind.webapp.ui.utils.GuiUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.actions.experiment.ShareExperimentAction;
import io.skymind.pathmind.webapp.ui.views.experiment.actions.experiment.StopTrainingAction;
import io.skymind.pathmind.webapp.ui.views.experiment.actions.shared.ArchiveExperimentAction;
import io.skymind.pathmind.webapp.ui.views.experiment.actions.shared.UnarchiveExperimentAction;
import io.skymind.pathmind.webapp.ui.views.experiment.components.policy.ExportPolicyButton;
import io.skymind.pathmind.webapp.ui.views.experiment.components.policy.ServePolicyButton;
import io.skymind.pathmind.webapp.ui.views.experiment.components.simple.shared.ExperimentPanelTitle;
import io.skymind.pathmind.webapp.ui.views.experiment.components.trainingStatus.TrainingStatusDetailsPanel;

/**
 * IMPORTANT -> This class is very unique because it contains a number of actions that need to set the experiment depending on whether it's the actual
 * experiment or the comparison experiment, meaning it's not always the same set/get code on the view. If it was just a component rendering it would be a lot simpler.
 * In other words what adds to the complexity is that the button actions set/get different values based on which experiment this component is for. Meaning we
 * need to be a lot smarter then we probably want to be. If it wasn't for the actions on the buttons this class would again be a lot simpler.
 */
public class ExperimentTitleBar extends HorizontalLayout implements ExperimentComponent {

    private Experiment experiment;
    private ExperimentPanelTitle experimentPanelTitle;
    private FavoriteStar favoriteStar;
    private TagLabel archivedLabel = new TagLabel("Archived", false, "small");
    private final TagLabel sharedLabel = new TagLabel("Shared", true, "small");
    private TrainingStatusDetailsPanel trainingStatusDetailsPanel;
    private HorizontalLayout titleWithStar;

    private SharedByUsername sharedByTag;
    private ServePolicyButton servePolicyButton;
    private ExportPolicyButton exportPolicyButton;
    private Button stopTrainingButton;
    private Button archiveButton;
    private Button unarchiveButton;
    private DownloadModelLink downloadModelLink;
    private Button shareButton;
    private boolean isPythonModel;

    private ExperimentView experimentView;
    private TrainingService trainingService;
    private ModelService modelService;
    private UserDAO userDAO;
    private RunDAO runDAO;
    private Runnable updateExperimentViewRunnable;
    private Supplier<Object> getLockSupplier;
    private PolicyDAO policyDAO;
    private PolicyFileService policyFileService;
    private PolicyServerService policyServerService;

    private final FeatureManager featureManager;

    // This is for the action classes so that it's easier to read the code in the createButtons() method (it's not needed as per say).
    private Supplier<Experiment> getExperimentSupplier;

    public ExperimentTitleBar(ExperimentView experimentView, Runnable updateExperimentViewRunnable,
                              Supplier<Object> getLockSupplier, Supplier<Optional<UI>> getUISupplier,
                              RunDAO runDAO, FeatureManager featureManager, PolicyDAO policyDAO, PolicyFileService policyFileService, PolicyServerService policyServerService,
                              TrainingService trainingService, ModelService modelService, UserDAO userDAO) {
        this(experimentView, updateExperimentViewRunnable, getLockSupplier, getUISupplier, runDAO, featureManager, policyDAO, policyFileService, policyServerService, trainingService, modelService, userDAO, false);
    }

    public ExperimentTitleBar(ExperimentView experimentView, Runnable updateExperimentViewRunnable,
                              Supplier<Object> getLockSupplier, Supplier<Optional<UI>> getUISupplier,
                              RunDAO runDAO, FeatureManager featureManager, PolicyDAO policyDAO, PolicyFileService policyFileService, PolicyServerService policyServerService,
                              TrainingService trainingService, ModelService modelService, boolean isExportPolicyButtonOnly) {
        this(experimentView, updateExperimentViewRunnable, getLockSupplier, getUISupplier, runDAO, featureManager, policyDAO, policyFileService, policyServerService, trainingService, modelService, null, isExportPolicyButtonOnly);
    }

    /**
     * Extra constructor is needed for the support shared experiment view.
     */
    public ExperimentTitleBar(ExperimentView experimentView, Runnable updateExperimentViewRunnable,
                              Supplier<Object> getLockSupplier, Supplier<Optional<UI>> getUISupplier,
                              RunDAO runDAO, FeatureManager featureManager, PolicyDAO policyDAO, PolicyFileService policyFileService, PolicyServerService policyServerService,
                              TrainingService trainingService, ModelService modelService, UserDAO userDAO,
                              boolean isExportPolicyButtonOnly) {
        this.experimentView = experimentView;
        this.getExperimentSupplier = () -> getExperiment();
        this.updateExperimentViewRunnable = updateExperimentViewRunnable;
        this.getLockSupplier = getLockSupplier;
        this.trainingService = trainingService;
        this.modelService = modelService;
        this.userDAO = userDAO;
        this.runDAO = runDAO;
        this.featureManager = featureManager;
        this.policyDAO = policyDAO;
        this.policyFileService = policyFileService;
        this.policyServerService = policyServerService;

        Component[] buttons = createButtons(isExportPolicyButtonOnly);

        experimentPanelTitle = new ExperimentPanelTitle();
        favoriteStar = new FavoriteStar(false, newIsFavorite -> experimentView.onFavoriteToggled(newIsFavorite, experiment));
        trainingStatusDetailsPanel = new TrainingStatusDetailsPanel(getUISupplier);

        titleWithStar = new HorizontalLayout(experimentPanelTitle, favoriteStar);
        titleWithStar.setSpacing(false);
        titleWithStar.setAlignItems(FlexComponent.Alignment.CENTER);
        if (!isExportPolicyButtonOnly) {
            titleWithStar.add(shareButton, archiveButton, unarchiveButton);
        }
        if (experimentView.isReadOnly()) {
            sharedByTag = new SharedByUsername("");
            titleWithStar.add(sharedByTag);
        }

        sharedLabel.addClassName("shared-with-support-label");
        sharedLabel.getElement().addEventListener("click", click ->
                ShareExperimentAction.createInstructionDialog(experimentView));
        VerticalLayout titleBarWrapper = WrapperUtils.wrapVerticalWithNoPaddingOrSpacingAndWidthAuto(
                titleWithStar,
                WrapperUtils.wrapWidthFullHorizontalNoSpacing(archivedLabel, sharedLabel),
                trainingStatusDetailsPanel);
        titleBarWrapper.setPadding(true);
        add(titleBarWrapper, getButtonsWrapper(buttons));
        addClassName("experiment-header");
        setPadding(false);
        setSpacing(false);
        setWidthFull();
    }

    private Component[] createButtons(boolean isExportPolicyButtonOnly) {
        stopTrainingButton = new Button("Stop Training", click -> StopTrainingAction.stopTraining(experimentView, getExperimentSupplier, updateExperimentViewRunnable, getLockSupplier, trainingService, stopTrainingButton));
        shareButton = GuiUtils.getIconButton(new Icon(VaadinIcon.SHARE_SQUARE), click -> {
            ShareExperimentAction.shareExperiment(experimentView, getExperimentSupplier, true, this::updateComponentEnablements);
        });
        archiveButton = GuiUtils.getIconButton(new Icon(VaadinIcon.ARCHIVE), click -> ArchiveExperimentAction.archive(experiment, experimentView));
        unarchiveButton = GuiUtils.getIconButton(new Icon(VaadinIcon.ARROW_BACKWARD), click -> UnarchiveExperimentAction.unarchive(experimentView, getExperimentSupplier, getLockSupplier));
        exportPolicyButton = new ExportPolicyButton(experimentView.getSegmentIntegrator(), policyFileService, policyDAO, getExperimentSupplier);
        servePolicyButton = new ServePolicyButton(policyServerService, userDAO, runDAO, experimentView.getSegmentIntegrator());
        // It is the same for all experiments from the same model so it doesn't have to be updated as long
        // as the user is on the Experiment View (the nav bar only allows navigation to experiments from the same model)
        // If in the future we allow navigation to experiments from other models, then we'll need to update the button accordingly on navigation
        downloadModelLink = new DownloadModelLink(modelService, experimentView.getSegmentIntegrator(), true, isPythonModel);

        // isExportPolicyButtonOnly is true for SharedExperimentView only
        if (isExportPolicyButtonOnly) {
            return new Component[]{exportPolicyButton, servePolicyButton};
        } else {
            return new Component[]{stopTrainingButton, exportPolicyButton, servePolicyButton, downloadModelLink};
        }
    }

    protected Div getButtonsWrapper(Component... components) {
        Div buttonsWrapper = new Div(components);
        buttonsWrapper.addClassName("buttons-wrapper");
        return buttonsWrapper;
    }

    public void updateComponentEnablements() {

        boolean isCompletedWithPolicy =
                experiment.isTrainingCompleted()
                        && experiment.getBestPolicy() != null
                        && experiment.getBestPolicy().hasFile();
        exportPolicyButton.setVisible(isCompletedWithPolicy);
        if (featureManager.isEnabled(Feature.POLICY_SERVING)) {
            servePolicyButton.setServePolicyButtonText(isCompletedWithPolicy);
        } else {
            servePolicyButton.setVisible(false);
        }
        stopTrainingButton.setVisible(experiment.isTrainingRunning());

        archivedLabel.setVisible(experiment.isArchived());
        archiveButton.setVisible(!experiment.isArchived());
        unarchiveButton.setVisible(experiment.isArchived());
        sharedLabel.setVisible(experiment.isShared());
        shareButton.setVisible(!experiment.isShared());

        if (experimentView.isReadOnly()) {
            favoriteStar.setVisible(false);
            servePolicyButton.setVisible(false);
            exportPolicyButton.setVisible(false);
            sharedLabel.setVisible(false);
        }

    }

    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
        isPythonModel = ModelType.isPythonModel(ModelType.fromValue(experiment.getModel().getModelType()));
        favoriteStar.setValue(experiment.isFavorite());
        experimentPanelTitle.setExperiment(experiment);
        trainingStatusDetailsPanel.setExperiment(experiment);
        downloadModelLink.setExperiment(experiment);
        exportPolicyButton.setExperiment(experiment);
        setExperimentForServePolicyButton(experiment);
        if (experimentView.isReadOnly()) {
            PathmindUser sharedByUser = experimentView.getExperimentDAO().getUserOfExperiment(experiment.getId());
            sharedByTag.setUsername(sharedByUser.getFirstname() + " " + sharedByUser.getLastname());
        }
        updateComponentEnablements();
    }

    public void setExperimentForServePolicyButton(Experiment experiment) {
        if (featureManager.isEnabled(Feature.POLICY_SERVING)) {
            servePolicyButton.setExperiment(experiment);
        }
    }

    public Experiment getExperiment() {
        return experiment;
    }

}
