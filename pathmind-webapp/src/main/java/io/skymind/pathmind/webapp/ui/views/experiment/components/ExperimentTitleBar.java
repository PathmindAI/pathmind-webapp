package io.skymind.pathmind.webapp.ui.views.experiment.components;

import java.util.List;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.ui.components.atoms.TagLabel;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.components.simple.shared.ExperimentPanelTitle;

public class ExperimentTitleBar extends HorizontalLayout implements ExperimentComponent {

    private Experiment experiment;
    private ExperimentPanelTitle experimentPanelTitle;
    private TagLabel archivedLabel = new TagLabel("Archived", false, "small");
    private TagLabel sharedWithSupportLabel = new TagLabel("Shared with Support", true, "small");

    public ExperimentTitleBar(Component... components) {
        experimentPanelTitle = new ExperimentPanelTitle();
        add(WrapperUtils.wrapVerticalWithNoPaddingOrSpacingAndWidthAuto(
            experimentPanelTitle, archivedLabel, sharedWithSupportLabel));
        add(components);
        setPadding(true);
        setWidthFull();
    }

    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
        experimentPanelTitle.setExperiment(experiment);
    }
    
    public Experiment getExperiment() {
        return experiment;
    }

    @Override
    public void updateExperiment() {
    }

}
