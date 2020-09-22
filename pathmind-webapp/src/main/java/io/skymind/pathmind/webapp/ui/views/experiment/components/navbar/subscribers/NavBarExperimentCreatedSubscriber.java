package io.skymind.pathmind.webapp.ui.views.experiment.components.navbar.subscribers;

import com.vaadin.flow.component.UI;
import io.skymind.pathmind.webapp.bus.events.ExperimentCreatedBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.ExperimentCreatedSubscriber;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.utils.PushUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.components.navbar.ExperimentsNavBar;

import java.util.Optional;
import java.util.function.Supplier;

public class NavBarExperimentCreatedSubscriber extends ExperimentCreatedSubscriber {

    private ExperimentsNavBar experimentsNavBar;

    public NavBarExperimentCreatedSubscriber(Supplier<Optional<UI>> getUISupplier, ExperimentsNavBar experimentsNavBar) {
        super(getUISupplier);
        this.experimentsNavBar = experimentsNavBar;
    }

    @Override
    public void handleBusEvent(ExperimentCreatedBusEvent event) {
        PushUtils.push(getUiSupplier().get(),
                ui -> experimentsNavBar.addExperiment(event.getExperiment()));
    }

    @Override
    public boolean isAttached() {
        return getUiSupplier().get().isPresent();
    }

    @Override
    public boolean filterBusEvent(ExperimentCreatedBusEvent event) {
        return ExperimentUtils.isNewExperimentForModel(event.getExperiment(), experimentsNavBar.getExperiments(), experimentsNavBar.getModelId());
    }
}
