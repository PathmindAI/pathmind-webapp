package io.skymind.pathmind.shared.data;

import io.skymind.pathmind.shared.constants.RunStatus;

import java.util.Collections;
import java.util.List;

import static io.skymind.pathmind.shared.constants.RunStatus.*;

public class ProviderJobStatus {
	private RunStatus runStatus;
	private List<String> description;

	public static ProviderJobStatus KILLED = new ProviderJobStatus(Killed);
	public static ProviderJobStatus RESTARTING = new ProviderJobStatus(Restarting);
	public static ProviderJobStatus COMPLETED = new ProviderJobStatus(Completed);
	public static ProviderJobStatus RUNNING = new ProviderJobStatus(Running);
	public static ProviderJobStatus STARTING = new ProviderJobStatus(Starting);
	public static ProviderJobStatus STOPPING = new ProviderJobStatus(Stopping);

	public ProviderJobStatus(RunStatus runStatus) {
		this(runStatus, Collections.emptyList());
	}

	public ProviderJobStatus(RunStatus runStatus, List<String> description) {
		this.runStatus = runStatus;
		this.description = description;
	}

	public RunStatus getRunStatus() {
		return runStatus;
	}

	public void setRunStatus(RunStatus runStatus) {
		this.runStatus = runStatus;
	}

	public List<String> getDescription() {
		return description;
	}

	public void setDescription(List<String> description) {
		this.description = description;
	}
}
