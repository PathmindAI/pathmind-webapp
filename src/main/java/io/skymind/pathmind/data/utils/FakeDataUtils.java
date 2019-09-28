package io.skymind.pathmind.data.utils;

import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.constants.RunType;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.Run;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

	public static List<Policy> generateFakePoliciesForExperiment(Experiment experiment) {
		ArrayList<Policy> policies = new ArrayList<>();
		Run run = generateFakeRunForExperiment(experiment);
		for(int x=1; x<=10; x++) {
			Policy policy = new Policy();
			policy.setId(x);
			policy.setName("Policy " + x);
			policy.setRun(run);
			policy.setRunId(run.getId());
			policy.setExperiment(experiment);
			policy.getScores().addAll(FakeDataUtils.generateFakePolicyChartScores());
			policies.add(policy);
		}
		return policies;
	}

	public static Run generateFakeRunForExperiment(Experiment experiment) {
		Run run = new Run();
		run.setExperimentId(experiment.getId());
		run.setName("Run 1");
		run.setRunTypeEnum(RunType.TestRun);
		run.setStatusEnum(RunStatus.Running);
		run.setId(1);
		return run;
	}


	public static List<Number> generateFakePolicyChartScores() {
		int chartSize = getRandomInt(50);
		ArrayList<Number> numbers = new ArrayList<Number>();
		for(int x=0; x<chartSize; x++)
			numbers.add(getRandomInt(500));
		return numbers;
	}
}
