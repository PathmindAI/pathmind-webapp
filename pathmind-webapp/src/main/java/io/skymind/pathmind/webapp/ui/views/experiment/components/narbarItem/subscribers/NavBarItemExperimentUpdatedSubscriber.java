package io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.subscribers;

import com.vaadin.flow.component.UI;
import io.skymind.pathmind.webapp.bus.events.main.ExperimentUpdatedBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.ExperimentUpdatedSubscriber;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.utils.PushUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.ExperimentsNavBarItem;

import java.util.Optional;
import java.util.function.Supplier;

public class NavBarItemExperimentUpdatedSubscriber extends ExperimentUpdatedSubscriber {

    private ExperimentsNavBarItem experimentsNavBarItem;

    public NavBarItemExperimentUpdatedSubscriber(Supplier<Optional<UI>> getUISupplier, ExperimentsNavBarItem experimentsNavBarItem) {
        super(getUISupplier);
        this.experimentsNavBarItem = experimentsNavBarItem;
    }

    public void handleBusEvent(ExperimentUpdatedBusEvent event) {
        PushUtils.push(getUiSupplier().get(), ui ->
            experimentsNavBarItem.updateExperiment(event.getExperiment()));
    }

    @Override
    public boolean filterBusEvent(ExperimentUpdatedBusEvent event) {
        if(event.getExperiment().isArchived())
            return false;
        return ExperimentUtils.isSameExperiment(experimentsNavBarItem.getExperiment(), event.getExperiment());
    }
}
