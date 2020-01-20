package io.skymind.pathmind.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * DTO for dashboard purposes
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
public class DashboardItem {
	private Project project;
	private Model model;
	private Experiment experiment;
	private Run latestRun;
	private LocalDateTime latestUpdateTime;
	/**
	 * Flag indicates if a policy for {@link DashboardItem#latestRun} was already exported by a user
	 */
	private boolean policyExported;
}
