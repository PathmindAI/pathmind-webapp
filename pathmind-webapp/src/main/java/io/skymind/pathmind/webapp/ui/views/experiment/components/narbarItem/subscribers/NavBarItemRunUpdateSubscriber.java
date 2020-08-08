package io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.subscribers;

import com.vaadin.flow.component.UI;
import io.skymind.pathmind.webapp.bus.events.RunUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.RunUpdateSubscriber;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.utils.PushUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.ExperimentsNavBarItem;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class NavBarItemRunUpdateSubscriber implements RunUpdateSubscriber {

    private Supplier<Optional<UI>> getUISupplier;
    private ExperimentsNavBarItem experimentsNavBarItem;

    public NavBarItemRunUpdateSubscriber(Supplier<Optional<UI>> getUISupplier, ExperimentsNavBarItem experimentsNavBarItem) {
        this.getUISupplier = getUISupplier;
        this.experimentsNavBarItem = experimentsNavBarItem;
    }

    @Override
    public boolean isAttached() {
        return getUISupplier.get().isPresent();
    }

    @Override
    public void handleBusEvent(RunUpdateBusEvent event) {
        PushUtils.push(getUISupplier.get(), () ->
            experimentsNavBarItem.updateExperiment(event.getRun().getExperiment()));
    }

    @Override
    public boolean filterBusEvent(RunUpdateBusEvent event) {
        // If it's archived then we don't need to update anything since there is no NavBar.
        if(event.getRun().getExperiment().isArchived())
            return false;
        return ExperimentUtils.isSameExperiment(experimentsNavBarItem.getExperiment(), event.getRun().getExperiment());
    }
}
