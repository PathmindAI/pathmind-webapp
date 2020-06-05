package io.skymind.pathmind.webapp.ui.views.experiment.utils;


import com.vaadin.flow.component.UI;

import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.bus.events.ExperimentCreatedBusEvent;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.webapp.ui.views.experiment.NewExperimentView;

public class ExperimentViewNavigationUtils
{
	public static void createAndNavigateToNewExperiment(UI ui, ExperimentDAO experimentDAO, long modelId) {
		long newExperimentId = experimentDAO.createNewExperiment(modelId);
		experimentDAO.getExperiment(newExperimentId).ifPresent(e -> EventBus.post(new ExperimentCreatedBusEvent(e)));
		ui.navigate(NewExperimentView.class, newExperimentId);
	}
}
