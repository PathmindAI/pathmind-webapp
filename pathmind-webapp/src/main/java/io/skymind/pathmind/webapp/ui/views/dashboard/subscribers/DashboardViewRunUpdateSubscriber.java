package io.skymind.pathmind.webapp.ui.views.dashboard.subscribers;

import io.skymind.pathmind.webapp.bus.events.main.RunUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.RunUpdateSubscriber;
import io.skymind.pathmind.webapp.ui.views.dashboard.DashboardView;

public class DashboardViewRunUpdateSubscriber extends RunUpdateSubscriber {

    private DashboardView dashboardView;

    public DashboardViewRunUpdateSubscriber(DashboardView dashboardView) {
        this.dashboardView = dashboardView;
    }

    @Override
    public void handleBusEvent(RunUpdateBusEvent event) {
        dashboardView.refreshExperiment(event.getExperiment().getId());
    }

    @Override
    public boolean filterBusEvent(RunUpdateBusEvent event) {
        return event.getProject().getPathmindUserId() == dashboardView.getLoggedUserId();
    }
}
