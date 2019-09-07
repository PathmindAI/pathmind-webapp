package io.skymind.pathmind.data.utils;

import io.skymind.pathmind.constants.Algorithm;
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
				Algorithm.DQN,
				374,
				1,
				"Function =",
				project,
				1);
	}

	public static long generateNewExperiment(Project project, ExperimentRepository experimentRepository) {
		Experiment experiment = generateNewExperiment(project);
		experiment.setId(experimentRepository.insertExperiment(experiment));
		return experiment.getId();
	}
}
