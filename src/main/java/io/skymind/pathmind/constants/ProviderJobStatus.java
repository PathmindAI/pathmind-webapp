package io.skymind.pathmind.constants;

import java.util.Collections;
import java.util.List;

// TODO (KW): 04.02.2020 move to another package
public class ProviderJobStatus {
	private RunStatus runStatus;
	private List<String> description;

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
