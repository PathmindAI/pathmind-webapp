package io.skymind.pathmind.webapp.ui.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.shared.constants.ViewPermission;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.webapp.bus.EventBusSubscriber;
import io.skymind.pathmind.webapp.security.annotation.Permission;
import io.skymind.pathmind.webapp.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Route(value = Routes.SECOND_SHARED_EXPERIMENT, layout = MainLayout.class)
@Slf4j
@Permission(permissions = ViewPermission.EXTENDED_READ)
public class SecondSharedExperimentView extends ExperimentView
{
    public SecondSharedExperimentView(int newRunDailyLimit, int newRunMonthlyLimit, int newRunNotificationThreshold) {
        super(newRunDailyLimit, newRunMonthlyLimit, newRunNotificationThreshold);
    }

    // For the shared view we don't do not want to listen to the new experiment created subscriber.
    @Override
    protected List<EventBusSubscriber> getViewSubscribers() {
        return super.getViewSubscribers()
                .stream().filter(eventBusSubscriber -> eventBusSubscriber instanceof ExperimentViewExperimentCreatedSubscriber)
                .collect(Collectors.toList());
    }

    @Override
    protected Component getTitlePanel() {
        return new ScreenTitlePanel("Shared Experiment");
    }

}
