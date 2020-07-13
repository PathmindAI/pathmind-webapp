package io.skymind.pathmind.shared.data;

import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.data.rllib.ExperimentState;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static io.skymind.pathmind.shared.constants.RunStatus.*;

@Getter
public class ProviderJobStatus {
	@Setter
	private RunStatus runStatus;
	@Setter
	private List<String> description;
	private ExperimentState experimentState;

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

	public ProviderJobStatus addExperimentState(ExperimentState experimentState) {
		this.experimentState = experimentState;
		return this;
	}
}
