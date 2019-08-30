package io.skymind.pathmind.data.utils;

import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Project;

import java.time.LocalDate;

public class ExperimentUtils
{
	private ExperimentUtils() {
	}

	public static Experiment generateNewExperiment(Project project) {
		return new Experiment(
				project.getName(),
				LocalDate.now(),
				1,
				1,
				project);
	}
}
