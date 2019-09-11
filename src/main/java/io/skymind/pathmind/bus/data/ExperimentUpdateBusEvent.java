package io.skymind.pathmind.bus.data;

import io.skymind.pathmind.bus.PathmindBusEvent;
import io.skymind.pathmind.bus.BusEventType;
import io.skymind.pathmind.data.Experiment;

public class ExperimentUpdateBusEvent implements PathmindBusEvent
{
	private Experiment experiment;

	public ExperimentUpdateBusEvent(Experiment experiment) {
		this.experiment = experiment;
	}

	@Override
	public BusEventType getEventType() {
		return BusEventType.ExperimentUpdate;
	}

	@Override
	public long getEventDataId() {
		return experiment.getId();
	}

	public Experiment getExperiment() {
		return experiment;
	}

	public void setExperiment(Experiment experiment) {
		this.experiment = experiment;
	}
}
