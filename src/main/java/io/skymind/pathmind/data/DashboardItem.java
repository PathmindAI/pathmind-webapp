package io.skymind.pathmind.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class DashboardItem {
	private Project project;
	private Model model;
	private Experiment experiment;

	public LocalDateTime getLatestUpdateTime() {
	// TODO KW: 16.01.2020 refactor
		List<LocalDateTime> dates = new ArrayList<>();
		if(project != null) {
			dates.add(project.getLastActivityDate());
		}
		if(model != null) {
			dates.add(model.getLastActivityDate());
		}
		if(experiment != null) {
			dates.add(experiment.getLastActivityDate());
		}

		return dates.stream()
				.filter(Objects::nonNull)
				.max(LocalDateTime::compareTo)
				.orElse(LocalDateTime.now());
	}

	public static DashboardItem ofExperiment(Experiment exp) {
		Model model = exp.getModel().getId() == 0 ? null : exp.getModel();
		Project project = exp.getProject();
		// TODO KW: 16.01.2020 change to check RunId instead
		if(!exp.getRuns().isEmpty() && exp.getRuns().get(0).getExperimentId() == 0) {
			exp.setRuns(List.of());
		}
		Experiment experiment = exp.getId() == 0 ? null : exp;

		return DashboardItem.builder()
				.experiment(experiment)
				.model(model)
				.project(project)
				.build();
	}

}
