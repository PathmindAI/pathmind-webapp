package io.skymind.pathmind.webapp.ui.views.experiment.components.navbar.subscribers;

import com.vaadin.flow.component.UI;
import io.skymind.pathmind.webapp.bus.events.RunUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.RunUpdateSubscriber;
import io.skymind.pathmind.webapp.ui.utils.PushUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.ExperimentsNavBarItem;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class NavBarRunUpdateSubscriber implements RunUpdateSubscriber {

    private Supplier<Optional<UI>> getUISupplier;
    private List<ExperimentsNavBarItem> experimentsNavBarItems;

    public NavBarRunUpdateSubscriber(Supplier<Optional<UI>> getUISupplier, List<ExperimentsNavBarItem> experimentsNavBarItems) {
        this.getUISupplier = getUISupplier;
        this.experimentsNavBarItems = experimentsNavBarItems;
    }

    @Override
    public boolean isAttached() {
        return getUISupplier.get().isPresent();
    }

    @Override
    public void handleBusEvent(RunUpdateBusEvent event) {
        PushUtils.push(getUISupplier.get(), () -> {
            // Look into each experiment to see if any run matches and if so update that experiment nav item.
            experimentsNavBarItems.stream()
                    .forEach(experimentsNavBarItem -> {
                        if(experimentsNavBarItem.getExperiment().getRuns().stream().anyMatch(run -> run.getId() == event.getRun().getId()))
                            experimentsNavBarItem.updateStatus(event.getRun().getStatusEnum());
                    });
        });
    }

    @Override
    public boolean filterBusEvent(RunUpdateBusEvent event) {
        // We are interested in any run update that's related to any experiment
        return experimentsNavBarItems.stream().anyMatch(
                experimentsNavBarItem -> experimentsNavBarItem.getExperiment().getRuns().stream().anyMatch(run -> run.getId() == event.getRun().getId()));
    }
}
