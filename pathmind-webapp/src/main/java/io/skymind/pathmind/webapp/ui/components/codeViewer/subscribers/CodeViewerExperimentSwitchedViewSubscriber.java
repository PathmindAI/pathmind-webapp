package io.skymind.pathmind.webapp.ui.components.codeViewer.subscribers;

import io.skymind.pathmind.webapp.bus.events.view.ExperimentSwitchedViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.view.ExperimentSwitchedViewSubscriber;
import io.skymind.pathmind.webapp.ui.components.codeViewer.CodeViewer;

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
