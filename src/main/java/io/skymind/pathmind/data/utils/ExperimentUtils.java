package io.skymind.pathmind.data.utils;

import io.skymind.pathmind.constants.RunType;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.db.ExperimentRepository;

import java.time.LocalDate;

public class ExperimentUtils
{
	private ExperimentUtils() {
	}

	public static Experiment generateNewExperiment(Project project) {
		return new Experiment(
				project.getName(),
				LocalDate.now(),
				RunType.DiscoverRun.getValue(),
				1,
				"Function =",
				project);
	}

	public static long generateNewExperiment(Project project, ExperimentRepository experimentRepository) {
		Experiment experiment = generateNewExperiment(project);
		experiment.setId(experimentRepository.insertExperiment(experiment));
		return experiment.getId();
	}
}
