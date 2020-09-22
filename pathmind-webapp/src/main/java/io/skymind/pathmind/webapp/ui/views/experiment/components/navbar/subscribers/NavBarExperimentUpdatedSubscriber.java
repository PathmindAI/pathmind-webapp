package io.skymind.pathmind.webapp.ui.views.experiment.components.navbar.subscribers;

import com.vaadin.flow.component.UI;
import io.skymind.pathmind.webapp.bus.events.ExperimentUpdatedBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.ExperimentUpdatedSubscriber;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.utils.PushUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.components.navbar.ExperimentsNavBar;

import java.util.Optional;
import java.util.function.Supplier;

public class NavBarExperimentUpdatedSubscriber extends ExperimentUpdatedSubscriber {

    private ExperimentsNavBar experimentsNavBar;

    public NavBarExperimentUpdatedSubscriber(Supplier<Optional<UI>> getUISupplier, ExperimentsNavBar experimentsNavBar) {
        super(getUISupplier);
        this.experimentsNavBar = experimentsNavBar;
    }

    // We can ignore this code for archived experiments since the navbar is not visible for archived experiments.
    public void handleBusEvent(ExperimentUpdatedBusEvent event) {
        PushUtils.push(getUiSupplier().get(), ui -> {
            if(event.getExperiment().isArchived()) {
                experimentsNavBar.removeExperiment(event.getExperiment());
            } else {
                experimentsNavBar.addExperiment(event.getExperiment());
            }
        });
    }

    @Override
    public boolean filterBusEvent(ExperimentUpdatedBusEvent event) {
        // At this point the navbar only adds/removes elements when an experiment is archived or unarchived.
        return ExperimentUtils.isSameModel(event.getExperiment(), experimentsNavBar.getModelId()) && event.isArchiveEventType();
    }

    @Override
    public boolean isAttached() {
        return getUiSupplier().get().isPresent();
    }
}
