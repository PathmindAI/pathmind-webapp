package io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.subscribers;

import com.vaadin.flow.component.UI;
import io.skymind.pathmind.webapp.bus.events.main.RunUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.RunUpdateSubscriber;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.utils.PushUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.ExperimentsNavBarItem;

import java.util.Optional;
import java.util.function.Supplier;

public class NavBarItemRunUpdateSubscriber extends RunUpdateSubscriber {

    private ExperimentsNavBarItem experimentsNavBarItem;

    public NavBarItemRunUpdateSubscriber(Supplier<Optional<UI>> getUISupplier, ExperimentsNavBarItem experimentsNavBarItem) {
        super(getUISupplier);
        this.experimentsNavBarItem = experimentsNavBarItem;
    }

    @Override
    public void handleBusEvent(RunUpdateBusEvent event) {
        ExperimentUtils.addOrUpdateRuns(experimentsNavBarItem.getExperiment(), event.getRuns());
        PushUtils.push(getUiSupplier().get(), () ->
            experimentsNavBarItem.update());
    }

    @Override
    public boolean filterBusEvent(RunUpdateBusEvent event) {
        // If it's archived then we don't need to update anything since there is no NavBar.
        if(event.getExperiment().isArchived())
            return false;
        return ExperimentUtils.isSameExperiment(experimentsNavBarItem.getExperiment(), event.getExperiment());
    }
}
