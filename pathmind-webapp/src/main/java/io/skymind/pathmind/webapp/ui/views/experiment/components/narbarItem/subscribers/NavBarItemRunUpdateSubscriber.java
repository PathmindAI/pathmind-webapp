package io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.subscribers;

import com.vaadin.flow.component.UI;
import io.skymind.pathmind.webapp.bus.events.RunUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.RunUpdateSubscriber;
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
    public boolean isAttached() {
        return getUiSupplier().get().isPresent();
    }

    @Override
    public void handleBusEvent(RunUpdateBusEvent event) {
        PushUtils.push(getUiSupplier().get(), () ->
            experimentsNavBarItem.updateRun(event.getRun()));
    }

    @Override
    public boolean filterBusEvent(RunUpdateBusEvent event) {
        // If it's archived then we don't need to update anything since there is no NavBar.
        if(event.getRun().getExperiment().isArchived())
            return false;
        return ExperimentUtils.isSameExperiment(experimentsNavBarItem.getExperiment(), event.getRun().getExperiment());
    }
}
