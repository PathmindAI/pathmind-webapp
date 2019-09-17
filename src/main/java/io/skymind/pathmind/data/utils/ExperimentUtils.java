package io.skymind.pathmind.data.utils;

import io.skymind.pathmind.constants.Algorithm;
import io.skymind.pathmind.constants.RunType;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.db.ExperimentRepository;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExperimentUtils
{
	private ExperimentUtils() {
	}

//	public static List<Experiment> generateNewDefaultExpirementList(Model model) {
//		ArrayList<Experiment> experiments = new ArrayList<>();
//		experiments.add(generateNewDefaultExperiment(project));
//		return experiments;
//	}
//
//	// TODO -> Correctly implement the default values for a new Project.
//	public static Experiment generateNewDefaultExperiment(Project project) {
//		return new Experiment(
//				"Experiment 1",
//				LocalDate.now(),
//				RunType.TestRun,
//				Algorithm.DQN,
//				0,
//				0,
//				"",
//				project,
//				0);
//	}
//
//	public static Experiment generateFakeExperiment(Project project) {
//		return new Experiment(
//				project.getName(),
//				LocalDate.now(),
//				RunType.DiscoverRun,
//				Algorithm.DQN,
//				374,
//				1,
//				"Function =",
//				project,
//				1);
//	}

//	public static long generateFakeExperiment(Project project, ExperimentRepository experimentRepository) {
//		Experiment experiment = generateFakeExperiment(project);
//		experiment.setId(experimentRepository.insertExperiment(experiment));
//		return experiment.getId();
//	}

	public static long getElapsedTime(Experiment experiment)
	{
		// TODO -> Implement with new data model.
		return 0;

//		if(experiment.getStartTime() == null)
//			return 0;
//
//		return experiment.getEndTime() == null ?
//				Duration.between(experiment.getStartTime(), Instant.now()).toSeconds() :
//				Duration.between(experiment.getStartTime(), experiment.getEndTime()).toSeconds();
	}
}
