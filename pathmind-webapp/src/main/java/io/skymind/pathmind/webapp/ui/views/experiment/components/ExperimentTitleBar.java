package io.skymind.pathmind.webapp.ui.views.experiment.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.ui.components.atoms.TagLabel;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.components.simple.shared.ExperimentPanelTitle;
import io.skymind.pathmind.webapp.ui.views.experiment.components.trainingStatus.TrainingStatusDetailsPanel;

public class ExperimentTitleBar extends HorizontalLayout implements ExperimentComponent {

    private Experiment experiment;
    private ExperimentPanelTitle experimentPanelTitle;
    private TagLabel archivedLabel = new TagLabel("Archived", false, "small");
    private TagLabel sharedWithSupportLabel = new TagLabel("Shared with Support", true, "small");
    private TrainingStatusDetailsPanel trainingStatusDetailsPanel = new TrainingStatusDetailsPanel();

    public ExperimentTitleBar(Component... components) {
        experimentPanelTitle = new ExperimentPanelTitle();
        add(WrapperUtils.wrapVerticalWithNoPaddingOrSpacingAndWidthAuto(
                    experimentPanelTitle, archivedLabel, sharedWithSupportLabel),
            trainingStatusDetailsPanel);
            //getButtonsWrapper()
        add(components);
        setPadding(true);
        setWidthFull();
    }

    private void updateComponentEnablements() {
        archivedLabel.setVisible(experiment.isArchived());
        sharedWithSupportLabel.setVisible(experiment.isSharedWithSupport());
    }

    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
        experimentPanelTitle.setExperiment(experiment);
        trainingStatusDetailsPanel.setExperiment(experiment);
        updateComponentEnablements();
    }
    
    public Experiment getExperiment() {
        return experiment;
    }

    @Override
    public void updateExperiment() {
    }

}
