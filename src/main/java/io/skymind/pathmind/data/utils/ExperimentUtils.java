package io.skymind.pathmind.data.utils;

import io.skymind.pathmind.data.Experiment;

import java.time.LocalDateTime;

public class ExperimentUtils
{
	private ExperimentUtils() {
	}

	// TODO -> Correctly implement the default values for a new Project.
	public static Experiment generateNewDefaultExperiment(int experimentNumber) {
		Experiment experiment = new Experiment();
		experiment.setName("Experiment #" + experimentNumber);
		experiment.setDateCreated(LocalDateTime.now());
		return experiment;
	}
}
