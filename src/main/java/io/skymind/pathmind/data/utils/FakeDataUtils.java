package io.skymind.pathmind.data.utils;

import io.skymind.pathmind.constants.Algorithm;
import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.constants.RunType;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.data.Run;
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
//		experiment.setScores(FakeDataUtils.getFakeScores());
//		experiment.setStartTime(Instant.now());
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

	public static List<Policy> generateFakePoliciesForExperiment(long experimentId) {
		ArrayList<Policy> policies = new ArrayList<>();
		Run run = generateFakeRunForExperiment(experimentId);
		for(int x=1; x<=10; x++) {
			Policy policy = new Policy();
			policy.setId(x);
			policy.setName("Policy " + x);
			policy.setRun(run);
			policy.setRunId(run.getId());
			policy.setExperiment(generateFakeExperimentForRun(run.getId()));
			policy.getScores().addAll(FakeDataUtils.generateFakePolicyChartScores());
			policies.add(policy);
		}
		return policies;
	}

	public static Run generateFakeRunForExperiment(long experimentId) {
		Run run = new Run();
		run.setExperimentId(experimentId);
		run.setName("Run 1");
		run.setRunTypeEnum(RunType.TestRun);
		run.setStatusEnum(RunStatus.Running);
		run.setId(1);
		return run;
	}

	public static Experiment generateFakeExperimentForRun(long runId) {
		Experiment experiment = new Experiment();
		experiment.setId(1);
		experiment.setName("Experiment 1");
		return experiment;
	}

	public static List<Number> generateFakePolicyChartScores() {
		int chartSize = getRandomInt(50);
		ArrayList<Number> numbers = new ArrayList<Number>();
		for(int x=0; x<chartSize; x++)
			numbers.add(getRandomInt(500));
		return numbers;
	}
}
