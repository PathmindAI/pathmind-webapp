package io.skymind.pathmind.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

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
}
