package io.skymind.pathmind.webapp.ui.views.experiment.components.codeViewer.subscribers;

import io.skymind.pathmind.webapp.bus.events.view.experiment.ExperimentSwitchedViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.view.experiment.ExperimentSwitchedViewSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.codeViewer.CodeViewer;

public class CodeViewerExperimentSwitchedViewSubscriber extends ExperimentSwitchedViewSubscriber {

    private CodeViewer codeViewer;

    public CodeViewerExperimentSwitchedViewSubscriber(CodeViewer codeViewer) {
        this.codeViewer = codeViewer;
    }

    @Override
    public void handleBusEvent(ExperimentSwitchedViewBusEvent event) {
        codeViewer.setExperiment(event.getExperiment());
    }
}
