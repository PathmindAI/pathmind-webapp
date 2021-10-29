package io.skymind.pathmind.webapp.ui.views.project.subscribers;

import io.skymind.pathmind.webapp.bus.events.main.ModelArchivedBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.ModelArchivedSubscriber;
import io.skymind.pathmind.webapp.ui.views.project.ProjectView;

public class ProjectViewModelArchiveSubscriber extends ModelArchivedSubscriber {

    private ProjectView projectView;

    public ProjectViewModelArchiveSubscriber(ProjectView projectView) {
        super();
        this.projectView = projectView;
    }

    @Override
    public void handleBusEvent(ModelArchivedBusEvent event) {
        projectView.setModelArchiveLabelVisible();
    }

    @Override
    public boolean filterBusEvent(ModelArchivedBusEvent event) {
        return event.getModel().getProjectId() == projectView.getProjectId();
    }
}
