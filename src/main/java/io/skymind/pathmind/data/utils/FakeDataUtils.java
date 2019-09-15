package io.skymind.pathmind.data.utils;

import io.skymind.pathmind.constants.Algorithm;
import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.constants.RunType;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Project;
import org.apache.commons.lang3.RandomUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FakeDataUtils
{
	public static final int EXPERIMENT_SCORE_MAX = 1000;

	private static final Random RANDOM = new Random();

	private FakeDataUtils() {
	}

	public static int getRandomInt(int bound) {
		return RANDOM.nextInt(bound);
	}

	public static long getRandomLong(long bound) {
		return RANDOM.nextLong() % bound;
	}

	public static List<Number> getFakeScores() {
		ArrayList<Number> scores = new ArrayList<>();
		int multiplier = RandomUtils.nextInt(1, 10);
		for(int x=0; x<20; x++)
			scores.add(RandomUtils.nextInt(0, EXPERIMENT_SCORE_MAX) * multiplier);
		return scores;
	}

	public static void loadExperimentWithFakeData(Experiment experiment) {
		experiment.setScores(FakeDataUtils.getFakeScores());
		experiment.setStartTime(Instant.now());
//		experiment.setStartTime(Instant.now().minusSeconds(RandomUtils.nextLong(0, 600)));
//		experiment.setStatusEnum(RunStatus.Running);
	}

//	public static Experiment generateFakeExperiment(Project project) {
//		return new Experiment(
//				"Experiment " + (project.getExperiments().size() + 1),
//				LocalDate.now(),
//				RunType.TestRun,
//				Algorithm.DQN,
//				0,
//				0,
//				"RF",
//				project,
//				1);
//	}
}
