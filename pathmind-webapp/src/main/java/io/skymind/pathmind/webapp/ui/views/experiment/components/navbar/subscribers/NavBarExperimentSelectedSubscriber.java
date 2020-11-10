package io.skymind.pathmind.webapp.ui.views.experiment.components.navbar.subscribers;

import com.vaadin.flow.component.UI;
import io.skymind.pathmind.webapp.bus.events.view.ExperimentChangedViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.view.ExperimentChangedViewSubscriber;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.utils.PushUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.components.navbar.ExperimentsNavBar;

import java.util.Optional;
import java.util.function.Supplier;

public class NavBarExperimentSelectedSubscriber extends ExperimentChangedViewSubscriber {

    private ExperimentsNavBar experimentsNavBar;

    public NavBarExperimentSelectedSubscriber(Supplier<Optional<UI>> getUISupplier, ExperimentsNavBar experimentsNavBar) {
        super(getUISupplier);
        this.experimentsNavBar = experimentsNavBar;
    }

    @Override
    public void handleBusEvent(ExperimentChangedViewBusEvent event) {
        PushUtils.push(getUiSupplier().get(),
                ui -> experimentsNavBar.setCurrentExperiment(event.getExperiment()));
    }

    @Override
    public boolean isAttached() {
        return getUiSupplier().get().isPresent();
    }

    @Override
    public boolean filterBusEvent(ExperimentChangedViewBusEvent event) {
        return ExperimentUtils.isSameModel(event.getExperiment(), experimentsNavBar.getModelId());
    }
}
