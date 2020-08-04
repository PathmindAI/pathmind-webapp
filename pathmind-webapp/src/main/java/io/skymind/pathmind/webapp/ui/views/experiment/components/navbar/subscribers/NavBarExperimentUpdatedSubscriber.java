package io.skymind.pathmind.webapp.ui.views.experiment.components.navbar.subscribers;

import com.vaadin.flow.component.UI;
import io.skymind.pathmind.webapp.bus.events.ExperimentUpdatedBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.ExperimentUpdatedSubscriber;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.utils.PushUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.components.navbar.ExperimentsNavBar;

import java.util.Optional;
import java.util.function.Supplier;

public class NavBarExperimentUpdatedSubscriber implements ExperimentUpdatedSubscriber {

    private Supplier<Optional<UI>> getUISupplier;
    private ExperimentsNavBar experimentsNavBar;

    public NavBarExperimentUpdatedSubscriber(Supplier<Optional<UI>> getUISupplier, ExperimentsNavBar experimentsNavBar) {
        this.getUISupplier = getUISupplier;
        this.experimentsNavBar = experimentsNavBar;
    }

    public void handleBusEvent(ExperimentUpdatedBusEvent event) {
        PushUtils.push(getUISupplier.get(), ui -> {
            // We can ignore this code for archived experiments since the navbar is not visible for archived experiments.
            if(event.getExperiment().isArchived()) {
                experimentsNavBar.removeExperiment(event.getExperiment());
            }
        });
    }

    @Override
    public boolean filterBusEvent(ExperimentUpdatedBusEvent event) {
        return ExperimentUtils.isSameModel(event.getExperiment(), experimentsNavBar.getModelId());
    }

    @Override
    public boolean isAttached() {
        return getUISupplier.get().isPresent();
    }
}
