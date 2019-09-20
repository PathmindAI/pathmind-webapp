package io.skymind.pathmind.bus.data;

import io.skymind.pathmind.bus.PathmindBusEvent;
import io.skymind.pathmind.bus.BusEventType;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Project;

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

//	public long getProjectId() {
//		return experiment.getProjectId();
//	}

	public void setExperiment(Experiment experiment) {
		this.experiment = experiment;
	}

//	public boolean isForProject(Project project) {
//		return experiment.getProjectId() == project.getId();
//	}
}
