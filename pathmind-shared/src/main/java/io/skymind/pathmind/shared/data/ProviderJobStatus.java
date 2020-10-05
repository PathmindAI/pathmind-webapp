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

	private final RunStatus runStatus;

	private final List<String> description;

	private final ExperimentState experimentState;

	public static ProviderJobStatus KILLED = new ProviderJobStatus(Killed);
	public static ProviderJobStatus RESTARTING = new ProviderJobStatus(Restarting);
	public static ProviderJobStatus COMPLETED = new ProviderJobStatus(Completed);
	public static ProviderJobStatus RUNNING = new ProviderJobStatus(Running);
	public static ProviderJobStatus STARTING = new ProviderJobStatus(Starting);
	public static ProviderJobStatus STOPPING = new ProviderJobStatus(Stopping);
	public static ProviderJobStatus COMPLETING = new ProviderJobStatus(Completing);

	public ProviderJobStatus(RunStatus runStatus) {
		this(runStatus, Collections.emptyList());
	}

	public ProviderJobStatus(RunStatus runStatus, ExperimentState experimentState) {
		this(runStatus, Collections.emptyList(), experimentState);
	}

	public ProviderJobStatus(RunStatus runStatus, List<String> description) {
		this(runStatus, description, null);
	}

	public ProviderJobStatus(RunStatus runStatus, List<String> description, ExperimentState experimentState) {
		this.runStatus = runStatus;
		this.description = description;
		this.experimentState = experimentState;
	}

}
