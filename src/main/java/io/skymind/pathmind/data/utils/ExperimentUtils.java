package io.skymind.pathmind.data.utils;

import io.skymind.pathmind.constants.Algorithm;
import io.skymind.pathmind.constants.RunType;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.db.ExperimentRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExperimentUtils
{
	private ExperimentUtils() {
	}

	public static List<Experiment> generateNewDefaultExpirementList(Project project) {
		ArrayList<Experiment> experiments = new ArrayList<>();
		experiments.add(generateNewDefaultExperiment(project));
		return experiments;
	}

	// TODO -> Correctly implement the default values for a new Project.
	public static Experiment generateNewDefaultExperiment(Project project) {
		return new Experiment(
				"Experiment 1",
				LocalDate.now(),
				RunType.TestRun,
				Algorithm.DQN,
				0,
				0,
				"",
				project,
				0);
	}

	public static Experiment generateFakeExperiment(Project project) {
		return new Experiment(
				project.getName(),
				LocalDate.now(),
				RunType.DiscoverRun,
				Algorithm.DQN,
				374,
				1,
				"Function =",
				project,
				1);
	}

	public static long generateFakeExperiment(Project project, ExperimentRepository experimentRepository) {
		Experiment experiment = generateFakeExperiment(project);
		experiment.setId(experimentRepository.insertExperiment(experiment));
		return experiment.getId();
	}
}
