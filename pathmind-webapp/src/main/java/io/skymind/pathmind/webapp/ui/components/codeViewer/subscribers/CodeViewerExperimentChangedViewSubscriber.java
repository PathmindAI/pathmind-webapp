package io.skymind.pathmind.webapp.ui.components.codeViewer.subscribers;

import io.skymind.pathmind.webapp.bus.events.view.ExperimentChangedViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.view.ExperimentChangedViewSubscriber;
import io.skymind.pathmind.webapp.ui.components.codeViewer.CodeViewer;

public class CodeViewerExperimentChangedViewSubscriber extends ExperimentChangedViewSubscriber {

    private CodeViewer codeViewer;

    public CodeViewerExperimentChangedViewSubscriber(CodeViewer codeViewer) {
        this.codeViewer = codeViewer;
    }

    @Override
    public void handleBusEvent(ExperimentChangedViewBusEvent event) {
        codeViewer.setExperiment(event.getExperiment());
    }
}
