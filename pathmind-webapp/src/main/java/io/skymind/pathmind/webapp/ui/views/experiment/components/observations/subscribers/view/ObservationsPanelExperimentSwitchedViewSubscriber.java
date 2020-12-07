package io.skymind.pathmind.webapp.ui.views.experiment.components.observations.subscribers.view;

import java.util.List;

import io.skymind.pathmind.db.dao.ObservationDAO;
import io.skymind.pathmind.shared.data.Observation;
import io.skymind.pathmind.webapp.bus.events.view.experiment.ExperimentSwitchedViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.view.experiment.ExperimentSwitchedViewSubscriber;
import io.skymind.pathmind.webapp.ui.components.observations.ObservationsPanel;

/**
 * This one is odd because we are re-using the subscriber in different views and in one view we need the experimentId whereas the other view
 * we use the modelId. Meaning we can't rely on the component for the value of the listener like we normally do, we have to override the
 * subscriber's setup in the view itself rather than the component to get the experiment id. In fact we don't need this type of
 * subscriber in the other view.
 */
public class ObservationsPanelExperimentSwitchedViewSubscriber extends ExperimentSwitchedViewSubscriber {

    private ObservationsPanel observationsPanel;
    private ObservationDAO observationDAO;

    // We use a supplier because this subscriber is used by more than one view.
    public ObservationsPanelExperimentSwitchedViewSubscriber(ObservationDAO observationDAO, ObservationsPanel observationsPanel) {
        super();
        this.observationsPanel = observationsPanel;
        this.observationDAO = observationDAO;
    }

    @Override
    public void handleBusEvent(ExperimentSwitchedViewBusEvent event) {
        observationsPanel.setExperiment(event.getExperiment());
        List<Observation> experimentObservations = observationDAO.getObservationsForExperiment(event.getExperiment().getId());
        observationsPanel.setSelectedObservations(experimentObservations);
    }
}
