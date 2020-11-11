package io.skymind.pathmind.webapp.ui.views.experiment.components.observations.subscribers;

import io.skymind.pathmind.db.dao.ObservationDAO;
import io.skymind.pathmind.shared.data.Observation;
import io.skymind.pathmind.webapp.bus.events.view.ExperimentChangedViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.view.ExperimentChangedViewSubscriber;
import io.skymind.pathmind.webapp.ui.views.model.components.ObservationsPanel;

import java.util.List;

/**
 * This one is odd because we are re-using the subscriber in different views and in one view we need the experimentId whereas the other view
 * we use the modelId. Meaning we can't rely on the component for the value of the listener like we normally do, we have to override the
 * subscriber's setup in the view itself rather than the component to get the experiment id. In fact we don't need this type of
 * subscriber in the other view.
 */
public class ObservationsPanelExperimentChangedViewSubscriber extends ExperimentChangedViewSubscriber {

    private ObservationsPanel observationsPanel;
    private ObservationDAO observationDAO;

    private long experimentId;

    public ObservationsPanelExperimentChangedViewSubscriber(ObservationDAO observationDAO, ObservationsPanel observationsPanel) {
        super();
        this.observationsPanel = observationsPanel;
        this.observationDAO = observationDAO;
    }

    // Set the initial experimentID on page load. Once set it should never be called again and we should ony be relying on the event itself.
    public void setExperimentId(long experimentId) {
        this.experimentId = experimentId;
    }

    @Override
    public void handleBusEvent(ExperimentChangedViewBusEvent event) {
        observationDAO.saveExperimentObservations(experimentId, observationsPanel.getSelectedObservations());
        experimentId = event.getExperiment().getId();
        List<Observation> experimentObservations = observationDAO.getObservationsForExperiment(event.getExperiment().getId());
        observationsPanel.setSelectedObservations(experimentObservations);
    }
}
