package io.skymind.pathmind.webapp.ui.views.experiment.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.db.dao.RunDAO;
import io.skymind.pathmind.services.ModelService;
import io.skymind.pathmind.services.TrainingService;
import io.skymind.pathmind.shared.constants.ModelType;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.featureflag.Feature;
import io.skymind.pathmind.shared.featureflag.FeatureManager;
import io.skymind.pathmind.shared.services.PolicyServerService;
import io.skymind.pathmind.webapp.ui.components.DownloadModelLink;
import io.skymind.pathmind.webapp.ui.components.FavoriteStar;
import io.skymind.pathmind.webapp.ui.components.atoms.ActionDropdown;
import io.skymind.pathmind.webapp.ui.components.atoms.ButtonWithLoading;
import io.skymind.pathmind.webapp.ui.components.atoms.TagLabel;
import io.skymind.pathmind.webapp.ui.utils.GuiUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.actions.experiment.ExportPolicyAction;
import io.skymind.pathmind.webapp.ui.views.experiment.actions.experiment.ServePolicyAction;
import io.skymind.pathmind.webapp.ui.views.experiment.actions.experiment.ShareWithSupportAction;
import io.skymind.pathmind.webapp.ui.views.experiment.actions.experiment.StopTrainingAction;
import io.skymind.pathmind.webapp.ui.views.experiment.actions.shared.ArchiveExperimentAction;
import io.skymind.pathmind.webapp.ui.views.experiment.actions.shared.UnarchiveExperimentAction;
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
    private ActionDropdown actionDropdown;
    private TagLabel archivedLabel = new TagLabel("Archived", false, "small");
    private TagLabel sharedWithSupportLabel = new TagLabel("Shared with Support", true, "small");
    private TrainingStatusDetailsPanel trainingStatusDetailsPanel;

    private ButtonWithLoading servePolicyButton;
    private Button exportPolicyButton;
    private Button stopTrainingButton;
    private Button archiveButton;
    private Button unarchiveButton;
    private DownloadModelLink downloadModelLink;
    private Button shareButton;
    private boolean isPythonModel;

    private ExperimentView experimentView;
    private TrainingService trainingService;
    private ModelService modelService;
    private Runnable updateExperimentViewRunnable;
    private Supplier<Object> getLockSupplier;
    private Supplier<Optional<UI>> getUISupplier;
    private PolicyServerService policyServerService;

    private final FeatureManager featureManager;

    // This is for the action classes so that it's easier to read the code in the createButtons() method (it's not needed as per say).
    private Supplier<Experiment> getExperimentSupplier;

    public ExperimentTitleBar(ExperimentView experimentView, Runnable updateExperimentViewRunnable,
                              Supplier<Object> getLockSupplier, Supplier<Optional<UI>> getUISupplier,
                              RunDAO runDAO, FeatureManager featureManager, PolicyServerService policyServerService,
                              TrainingService trainingService, ModelService modelService) {
        this(experimentView, updateExperimentViewRunnable, getLockSupplier, getUISupplier, runDAO, featureManager, policyServerService, trainingService, modelService, false);
    }

    /**
     * Extra constructor is needed for the support shared experiment view.
     */
    public ExperimentTitleBar(ExperimentView experimentView, Runnable updateExperimentViewRunnable,
                              Supplier<Object> getLockSupplier, Supplier<Optional<UI>> getUISupplier,
                              RunDAO runDAO, FeatureManager featureManager, PolicyServerService policyServerService,
                              TrainingService trainingService, ModelService modelService,
                              boolean isExportPolicyButtonOnly) {
        this.experimentView = experimentView;
        this.getExperimentSupplier = () -> getExperiment();
        this.updateExperimentViewRunnable = updateExperimentViewRunnable;
        this.getLockSupplier = getLockSupplier;
        this.trainingService = trainingService;
        this.modelService = modelService;
        this.getUISupplier = getUISupplier;
        this.featureManager = featureManager;
        this.policyServerService = policyServerService;

        Component[] buttons = createButtons(isExportPolicyButtonOnly);

        experimentPanelTitle = new ExperimentPanelTitle();
        favoriteStar = new FavoriteStar(false, newIsFavorite -> experimentView.onFavoriteToggled(newIsFavorite, experiment));
        trainingStatusDetailsPanel = new TrainingStatusDetailsPanel(getUISupplier);

        HorizontalLayout titleWithStar = new HorizontalLayout(experimentPanelTitle, favoriteStar);
        titleWithStar.setSpacing(false);
        titleWithStar.setAlignItems(FlexComponent.Alignment.CENTER);
        if (!isExportPolicyButtonOnly) {
            titleWithStar.add(actionDropdown);
        }

        VerticalLayout titleBarWrapper = WrapperUtils.wrapVerticalWithNoPaddingOrSpacingAndWidthAuto(
                titleWithStar,
                new HorizontalLayout(archivedLabel, sharedWithSupportLabel),
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
        shareButton = new Button("Share with support", click -> ShareWithSupportAction.shareWithSupport(experimentView, getExperimentSupplier, sharedWithSupportLabel, shareButton));
        archiveButton = GuiUtils.getPrimaryButton("Archive", click -> ArchiveExperimentAction.archive(experiment, experimentView));
        unarchiveButton = GuiUtils.getPrimaryButton("Unarchive", click -> UnarchiveExperimentAction.unarchive(experimentView, getExperimentSupplier, getLockSupplier));
        exportPolicyButton = GuiUtils.getPrimaryButton("Export Policy", click -> ExportPolicyAction.exportPolicy(getExperimentSupplier, getUISupplier), false);
        servePolicyButton = new ButtonWithLoading("Serve Policy", () -> ServePolicyAction.servePolicy(getExperimentSupplier, servePolicyButton, policyServerService), true);
        // It is the same for all experiments from the same model so it doesn't have to be updated as long
        // as the user is on the Experiment View (the nav bar only allows navigation to experiments from the same model)
        // If in the future we allow navigation to experiments from other models, then we'll need to update the button accordingly on navigation
        downloadModelLink = new DownloadModelLink(modelService, experimentView.getSegmentIntegrator(), true, isPythonModel);

        // Even though in this case we're constructing extra buttons for nothing they should be very lightweight and it makes the code a lot easier to manage.
        if (isExportPolicyButtonOnly) {
            return new Component[]{exportPolicyButton, servePolicyButton};
        } else {
            List<Button> actionButtons = new ArrayList<Button>();
            actionButtons.add(shareButton);
            actionButtons.add(archiveButton);
            actionButtons.add(unarchiveButton);
            actionDropdown = new ActionDropdown(actionButtons);
            return new Component[]{stopTrainingButton, exportPolicyButton, servePolicyButton, downloadModelLink};
        }
    }

    protected Div getButtonsWrapper(Component... components) {
        Div buttonsWrapper = new Div(components);
        buttonsWrapper.addClassName("buttons-wrapper");
        return buttonsWrapper;
    }

    private void updateComponentEnablements() {
        archivedLabel.setVisible(experiment.isArchived());
        sharedWithSupportLabel.setVisible(experiment.isSharedWithSupport());

        if (actionDropdown != null) {
            actionDropdown.setItemEnabledProvider(item -> {
                if (experiment.isArchived()) {
                    return !archiveButton.getText().equals(item);
                }
                return !unarchiveButton.getText().equals(item);
            });
        }

        unarchiveButton.setVisible(experiment.isArchived());
        boolean isCompletedWithPolicy =
                experiment.isTrainingCompleted()
                        && experiment.getBestPolicy() != null
                        && experiment.getBestPolicy().hasFile();
        exportPolicyButton.setVisible(isCompletedWithPolicy);
        if (featureManager.isEnabled(Feature.POLICY_SERVING)) {
            servePolicyButton.setVisible(isPythonModel && isCompletedWithPolicy);
        }
        stopTrainingButton.setVisible(experiment.isTrainingRunning());

        archivedLabel.setVisible(experiment.isArchived());

        // Update components with SharedExperimentView (share through support).
        sharedWithSupportLabel.setVisible(experiment.isSharedWithSupport());
        shareButton.setVisible(!experiment.isSharedWithSupport());
    }

    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
        isPythonModel = ModelType.isPythonModel(ModelType.fromValue(experiment.getModel().getModelType()));
        favoriteStar.setValue(experiment.isFavorite());
        experimentPanelTitle.setExperiment(experiment);
        trainingStatusDetailsPanel.setExperiment(experiment);
        downloadModelLink.setExperiment(experiment);
        updateComponentEnablements();
    }

    public Experiment getExperiment() {
        return experiment;
    }

}
