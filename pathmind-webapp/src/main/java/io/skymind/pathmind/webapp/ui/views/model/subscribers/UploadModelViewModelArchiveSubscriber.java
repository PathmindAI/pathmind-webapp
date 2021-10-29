package io.skymind.pathmind.webapp.ui.views.model.subscribers;

import io.skymind.pathmind.webapp.bus.events.main.ModelArchivedBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.ModelArchivedSubscriber;
import io.skymind.pathmind.webapp.ui.views.model.UploadModelView;
import io.skymind.pathmind.webapp.ui.views.project.ProjectView;

public class UploadModelViewModelArchiveSubscriber extends ModelArchivedSubscriber {

    private UploadModelView uploadModelView;

    public UploadModelViewModelArchiveSubscriber(UploadModelView uploadModelView) {
        super();
        this.uploadModelView = uploadModelView;
    }

    @Override
    public void handleBusEvent(ModelArchivedBusEvent event) {
        uploadModelView.getArchivedBanner().setVisible(event.isArchived());
        uploadModelView.getUI().ifPresent(ui -> ui.navigate(ProjectView.class, ""+event.getModel().getProjectId()));
    }

    @Override
    public boolean filterBusEvent(ModelArchivedBusEvent event) {
        return event.getModel().getId() == uploadModelView.getModelId();
    }
}
