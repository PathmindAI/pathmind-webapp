package io.skymind.pathmind.webapp.ui.views.experiment.components.observations.subscribers;

import io.skymind.pathmind.db.dao.ObservationDAO;
import io.skymind.pathmind.shared.data.Observation;
import io.skymind.pathmind.webapp.bus.events.view.ExperimentChangedViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.view.ExperimentChangedViewSubscriber;
import io.skymind.pathmind.webapp.ui.views.model.components.ObservationsPanel;

import java.util.List;
import java.util.function.Supplier;

/**
 * This one is odd because we are re-using the subscriber in different views and in one view we need the experimentId whereas the other view
 * we use the modelId. Meaning we can't rely on the component for the value of the listener like we normally do, we have to override the
 * subscriber's setup in the view itself rather than the component to get the experiment id. In fact we don't need this type of
 * subscriber in the other view.
 */
public class ObservationsPanelExperimentChangedViewSubscriber extends ExperimentChangedViewSubscriber {

    private Supplier<Long> getExperimentIdSupplier;
    private ObservationsPanel observationsPanel;
    private ObservationDAO observationDAO;

    // We use a supplier because this subscriber is used by more than one view.
    public ObservationsPanelExperimentChangedViewSubscriber(Supplier<Long> getExperimentIdSupplier, ObservationDAO observationDAO, ObservationsPanel observationsPanel) {
        super();
        this.getExperimentIdSupplier = getExperimentIdSupplier;
        this.observationsPanel = observationsPanel;
        this.observationDAO = observationDAO;
    }

    @Override
    public void handleBusEvent(ExperimentChangedViewBusEvent event) {
        observationDAO.saveExperimentObservations(getExperimentIdSupplier.get(), observationsPanel.getSelectedObservations());
        List<Observation> experimentObservations = observationDAO.getObservationsForExperiment(event.getExperiment().getId());
        observationsPanel.setSelectedObservations(experimentObservations);
    }
}
