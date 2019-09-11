package io.skymind.pathmind.bus.data;

import io.skymind.pathmind.bus.PathmindBusEvent;
import io.skymind.pathmind.bus.BusEventType;
import io.skymind.pathmind.data.Project;

public class ProjectUpdateBusEvent implements PathmindBusEvent
{
	private Project project;

	public ProjectUpdateBusEvent(Project project) {
		this.project = project;
	}

	@Override
	public BusEventType getEventType() {
		return BusEventType.ProjectUpdate;
	}

	@Override
	public long getEventDataId() {
		return project.getId();
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}
}
