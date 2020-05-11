package io.skymind.pathmind.webapp.bus.events;

import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.PathmindBusEvent;
import io.skymind.pathmind.shared.data.Run;

public class RunUpdateBusEvent implements PathmindBusEvent
{
	private Run run;

	public RunUpdateBusEvent(Run run)
	{
		if(run.getExperiment() == null)
			throw new IllegalStateException("Experiment is null");
		if(run.getModel() == null)
			throw new IllegalStateException("Model is null");
		if(run.getProject() == null)
			throw new IllegalStateException("Project is null");

		this.run = run;
	}

	@Override
	public BusEventType getEventType() {
		return BusEventType.RunUpdate;
	}

	public Run getRun() {
		return run;
	}

	public void setRun(Run run) {
		this.run = run;
	}
}
