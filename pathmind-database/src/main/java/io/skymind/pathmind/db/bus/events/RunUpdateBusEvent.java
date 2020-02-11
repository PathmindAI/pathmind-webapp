package io.skymind.pathmind.db.bus.events;

import io.skymind.pathmind.shared.bus.BusEventType;
import io.skymind.pathmind.shared.bus.PathmindBusEvent;
import io.skymind.pathmind.db.data.Run;

public class RunUpdateBusEvent implements PathmindBusEvent
{
	private Run run;

	public RunUpdateBusEvent(Run run)
	{
		if(run.getExperiment() == null)
			throw new RuntimeException("Experiment is null");
		if(run.getModel() == null)
			throw new RuntimeException("Model is null");
		if(run.getProject() == null)
			throw new RuntimeException("Project is null");

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
