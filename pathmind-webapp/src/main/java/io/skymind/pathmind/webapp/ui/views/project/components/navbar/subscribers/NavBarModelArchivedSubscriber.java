package io.skymind.pathmind.webapp.ui.views.project.components.navbar.subscribers;

import io.skymind.pathmind.webapp.bus.events.main.ModelArchivedBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.ModelArchivedSubscriber;
import io.skymind.pathmind.webapp.ui.views.project.components.navbar.ModelsNavbar;

public class NavBarModelArchivedSubscriber extends ModelArchivedSubscriber {

    private ModelsNavbar modelsNavBar;

    public NavBarModelArchivedSubscriber(ModelsNavbar modelsNavBar) {
        super();
        this.modelsNavBar = modelsNavBar;
    }

    @Override
    public void handleBusEvent(ModelArchivedBusEvent event) {
        modelsNavBar.setModelIsArchived(event.getModel(), event.getModel().isArchived());
    }

    @Override
    public boolean filterBusEvent(ModelArchivedBusEvent event) {
        return event.getModel().getProjectId() == modelsNavBar.getProjectId();
    }
}
