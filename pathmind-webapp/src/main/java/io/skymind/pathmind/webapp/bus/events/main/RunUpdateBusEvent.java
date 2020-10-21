package io.skymind.pathmind.webapp.bus.events.main;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Project;
import io.skymind.pathmind.shared.data.Run;
import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.PathmindBusEvent;

import java.util.List;
import java.util.stream.Collectors;

public class RunUpdateBusEvent implements PathmindBusEvent
{
	private List<Run> runs;
	private Experiment experiment;

    public RunUpdateBusEvent(Run run) {
        this(List.of(run));
    }

	public RunUpdateBusEvent(List<Run> runs)
	{
        if(runs == null || runs.size() == 0)
            throw new IllegalStateException("Runs cannot be null or empty");

        runs.forEach(run -> {
            if(run.getExperiment() == null)
                throw new IllegalStateException("Experiment is null");
            if(run.getModel() == null)
                throw new IllegalStateException("Model is null");
            if(run.getProject() == null)
                throw new IllegalStateException("Project is null");
        });

        experiment = runs.get(0).getExperiment();

        if(runs.stream().anyMatch(run -> run.getExperiment().getId() != experiment.getId()))
            throw new IllegalStateException("One of the runs is for a different experiment");

        this.runs = runs;
	}

	@Override
	public BusEventType getEventType() {
		return BusEventType.RunUpdate;
	}

	public Experiment getExperiment() {
        return runs.get(0).getExperiment();
    }

    // Helper method.
    public long getExperimentId() {
        return getExperiment().getId();
    }

    // Helper method.
    public Project getProject() {
        return runs.get(0).getProject();
    }
	public List<Run> getRuns() {
		return runs;
	}

	public long getModelId() {
	    return runs.get(0).getModel().getId();
    }
    @Override
    public RunUpdateBusEvent cloneForEventBus() {
        return new RunUpdateBusEvent(runs.stream()
                .map(run -> run.deepClone())
                .collect(Collectors.toList()));
    }
}
