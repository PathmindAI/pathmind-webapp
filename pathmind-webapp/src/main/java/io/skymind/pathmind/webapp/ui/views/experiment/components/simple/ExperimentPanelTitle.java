package io.skymind.pathmind.webapp.ui.views.experiment.components.simple;

import com.vaadin.flow.component.html.Span;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles;
import io.skymind.pathmind.webapp.ui.views.experiment.components.ExperimentComponent;

public class ExperimentPanelTitle extends Span implements ExperimentComponent {

    public ExperimentPanelTitle() {
        super();
        addClassNames(CssPathmindStyles.SECTION_TITLE_LABEL);
    }

    public void setExperiment(Experiment experiment) {
        setText("Experiment #" + experiment.getName());
    }
}
