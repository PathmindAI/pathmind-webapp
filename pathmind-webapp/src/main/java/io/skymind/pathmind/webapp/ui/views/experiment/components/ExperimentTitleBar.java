package io.skymind.pathmind.webapp.ui.views.experiment.components;

import java.util.Optional;
import java.util.function.Supplier;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.db.dao.RunDAO;
import io.skymind.pathmind.services.ModelService;
import io.skymind.pathmind.services.TrainingService;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.utils.ModelUtils;
import io.skymind.pathmind.webapp.ui.components.alp.DownloadModelAlpLink;
import io.skymind.pathmind.webapp.ui.components.atoms.TagLabel;
import io.skymind.pathmind.webapp.ui.utils.GuiUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.actions.experiment.ExportPolicyAction;
import io.skymind.pathmind.webapp.ui.views.experiment.actions.experiment.ShareWithSupportAction;
import io.skymind.pathmind.webapp.ui.views.experiment.actions.experiment.StopTrainingAction;
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
    private TagLabel archivedLabel = new TagLabel("Archived", false, "small");
    private TagLabel sharedWithSupportLabel = new TagLabel("Shared with Support", true, "small");
    private TrainingStatusDetailsPanel trainingStatusDetailsPanel;

    private Button exportPolicyButton;
    private Button stopTrainingButton;
    private Button unarchiveButton;
    private DownloadModelAlpLink downloadModelAlpLink;
    private Button shareButton;

    private ExperimentView experimentView;
    private RunDAO runDAO;
    private TrainingService trainingService;
    private ModelService modelService;
    private Runnable updateExperimentViewRunnable;
    private Supplier<Object> getLockSupplier;
    private Supplier<Optional<UI>> getUISupplier;

    // This is for the action classes so that it's easier to read the code in the createButtons() method (it's not needed as per say).
    private Supplier<Experiment> getExperimentSupplier;

    public ExperimentTitleBar(ExperimentView experimentView, Runnable updateExperimentViewRunnable, Supplier<Object> getLockSupplier, RunDAO runDAO, TrainingService trainingService, ModelService modelService, Supplier<Optional<UI>> getUISupplier) {
        this(experimentView, updateExperimentViewRunnable, getLockSupplier, runDAO, trainingService, modelService, getUISupplier, false);
    }

    /**
     * Extra constructor is needed for the support shared experiment view.
     */
    public ExperimentTitleBar(ExperimentView experimentView, Runnable updateExperimentViewRunnable, Supplier<Object> getLockSupplier, RunDAO runDAO, TrainingService trainingService, ModelService modelService, Supplier<Optional<UI>> getUISupplier, boolean isExportPolicyButtonOnly) {
        this.experimentView = experimentView;
        this.getExperimentSupplier = () -> getExperiment();
        this.updateExperimentViewRunnable = updateExperimentViewRunnable;
        this.getLockSupplier = getLockSupplier;
        this.runDAO = runDAO;
        this.trainingService = trainingService;
        this.modelService = modelService;
        this.getUISupplier = getUISupplier;

        Component[] buttons = createButtons(isExportPolicyButtonOnly);

        experimentPanelTitle = new ExperimentPanelTitle();
        trainingStatusDetailsPanel = new TrainingStatusDetailsPanel(getUISupplier);
        add(WrapperUtils.wrapVerticalWithNoPaddingOrSpacingAndWidthAuto(
                    experimentPanelTitle, archivedLabel, sharedWithSupportLabel),
            trainingStatusDetailsPanel,
            getButtonsWrapper(buttons));
        setPadding(true);
        setWidthFull();
    }

    private Component[] createButtons(boolean isExportPolicyButtonOnly) {
        stopTrainingButton = new Button("Stop Training", click -> StopTrainingAction.stopTraining(experimentView, getExperimentSupplier, updateExperimentViewRunnable, getLockSupplier, trainingService, stopTrainingButton));
        shareButton = new Button("Share with support", click -> ShareWithSupportAction.shareWithSupport(experimentView, getExperimentSupplier, sharedWithSupportLabel, shareButton));
        unarchiveButton = GuiUtils.getPrimaryButton("Unarchive", VaadinIcon.ARROW_BACKWARD.create(), click -> UnarchiveExperimentAction.unarchive(experimentView, getExperimentSupplier, getLockSupplier));
        exportPolicyButton = GuiUtils.getPrimaryButton("Export Policy", click -> ExportPolicyAction.exportPolicy(getExperimentSupplier, getUISupplier), false);
        // It is the same for all experiments from the same model so it doesn't have to be updated as long
        // as the user is on the Experiment View (the nav bar only allows navigation to experiments from the same model)
        // If in the future we allow navigation to experiments from other models, then we'll need to update the button accordingly on navigation
        downloadModelAlpLink = new DownloadModelAlpLink(modelService, experimentView.getSegmentIntegrator(), false);

        // Even though in this case we're constructing extra buttons for nothing they should be very lightweight and it makes the code a lot easier to manage.
        if(isExportPolicyButtonOnly) {
            return new Component[] {exportPolicyButton};
        } else {
            return new Component[] {stopTrainingButton, shareButton, unarchiveButton, exportPolicyButton, downloadModelAlpLink};
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

        unarchiveButton.setVisible(experiment.isArchived());
        exportPolicyButton.setVisible(experiment.isTrainingCompleted() && experiment.getBestPolicy() != null && experiment.getBestPolicy().hasFile());
        stopTrainingButton.setVisible(experiment.isTrainingRunning());

        archivedLabel.setVisible(experiment.isArchived());

        // Update components with SharedExperimentView (share through support).
        sharedWithSupportLabel.setVisible(experiment.isSharedWithSupport());
        shareButton.setVisible(!experiment.isSharedWithSupport());
    }

    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
        experimentPanelTitle.setExperiment(experiment);
        trainingStatusDetailsPanel.setExperiment(experiment);
        downloadModelAlpLink.setExperiment(experiment);
        updateComponentEnablements();
    }
    
    public Experiment getExperiment() {
        return experiment;
    }

    @Override
    public void updateExperiment() {
    }

}
