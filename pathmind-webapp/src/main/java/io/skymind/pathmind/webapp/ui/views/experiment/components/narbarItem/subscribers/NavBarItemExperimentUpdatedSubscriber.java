package io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.subscribers;

import com.vaadin.flow.component.UI;
import io.skymind.pathmind.webapp.bus.events.ExperimentUpdatedBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.ExperimentUpdatedSubscriber;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.utils.PushUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.ExperimentsNavBarItem;

import java.util.Optional;
import java.util.function.Supplier;

public class NavBarItemExperimentUpdatedSubscriber implements ExperimentUpdatedSubscriber {

    private Supplier<Optional<UI>> getUISupplier;
    private ExperimentsNavBarItem experimentsNavBarItem;

    public NavBarItemExperimentUpdatedSubscriber(Supplier<Optional<UI>> getUISupplier, ExperimentsNavBarItem experimentsNavBarItem) {
        this.getUISupplier = getUISupplier;
        this.experimentsNavBarItem = experimentsNavBarItem;
    }

    public void handleBusEvent(ExperimentUpdatedBusEvent event) {
        PushUtils.push(getUISupplier.get(), ui ->
            experimentsNavBarItem.updateExperiment(event.getExperiment()));
    }

    @Override
    public boolean filterBusEvent(ExperimentUpdatedBusEvent event) {
        return ExperimentUtils.isSameExperiment(experimentsNavBarItem.getExperiment(), event.getExperiment());
    }

    @Override
    public boolean isAttached() {
        return getUISupplier.get().isPresent();
    }
}
