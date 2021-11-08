package io.skymind.pathmind.shared.utils;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class ExperimentUtilsTest {

    @Test
    void rewardEquals() {
        String rewardFunction = ExperimentUtils.collectRewardTermsToSnippet(
                List.of("reward = after.goalReached - 0.1;")
        );
        assertThat(rewardFunction, is("rewardTermsRaw[0] = after.goalReached - 0.1;"));
    }

    @Test
    void rewardEqualsTwice() {
        String rewardFunction = ExperimentUtils.collectRewardTermsToSnippet(
                List.of(
                        "reward = after.goalReached - 0.1;",
                        "reward = after.goalReached - 0.1;"
                )
        );
        assertThat(rewardFunction, is(
                "rewardTermsRaw[0] = after.goalReached - 0.1;\n" +
                        "rewardTermsRaw[1] = after.goalReached - 0.1;"
        ));
    }

    @Test
    void rewardTermsMixed() {
        String rewardFunction = ExperimentUtils.collectRewardTermsToSnippet(
                List.of(
                        "reward = -after.makespan; \n" +
                                "reward -= 500*Math.abs(after.workerpt[0]-after.workerpt[1]); \n" +
                                "reward -= 500*Math.abs(after.workerpt[1]-after.workerpt[2]); \n" +
                                "reward += 500*Math.abs(after.workerpt[1]-after.workerpt[2]); \n" +
                                "reward -= 500*Math.abs(after.workerpt[0]-after.workerpt[2]);"


                )
        );
        assertThat(rewardFunction, is(
                "rewardTermsRaw[0] = -after.makespan; \n" +
                        "rewardTermsRaw[0] += -1*(500*Math.abs(after.workerpt[0]-after.workerpt[1]));\n" +
                        "rewardTermsRaw[0] += -1*(500*Math.abs(after.workerpt[1]-after.workerpt[2]));\n" +
                        "rewardTermsRaw[0] += 500*Math.abs(after.workerpt[1]-after.workerpt[2]); \n" +
                        "rewardTermsRaw[0] += -1*(500*Math.abs(after.workerpt[0]-after.workerpt[2]));"
        ));

        rewardFunction = ExperimentUtils.collectRewardTermsToSnippet(
                List.of(
                        "reward += after.totalThroughput * 0.01; \n" +
                                "reward += ( after.machineUtil - before.machineUtil ) * 100; \n" +
                                "reward += (after.essentialDelivery - before.essentialDelivery) / 60 * 0.5; \n" +
                                "reward -= (after.fullQueue - before.fullQueue); \n" +
                                "reward -= after.fullConveyor - before.fullConveyor; \n" +
                                "reward -= (after.emptyOrigins - before.emptyOrigins); \n" +
                                "reward -= (after.tripDuration) / 140;"
                )
        );
         assertThat(rewardFunction, is("rewardTermsRaw[0] += after.totalThroughput * 0.01; \n" +
                 "rewardTermsRaw[0] += ( after.machineUtil - before.machineUtil ) * 100; \n" +
                 "rewardTermsRaw[0] += (after.essentialDelivery - before.essentialDelivery) / 60 * 0.5; \n" +
                 "rewardTermsRaw[0] += -1*((after.fullQueue - before.fullQueue));\n" +
                 "rewardTermsRaw[0] += -1*(after.fullConveyor - before.fullConveyor);\n" +
                 "rewardTermsRaw[0] += -1*((after.emptyOrigins - before.emptyOrigins));\n" +
                 "rewardTermsRaw[0] += -1*((after.tripDuration) / 140);"));
    }

    @Test
    void rewardPlus() {
        String rewardFunction = ExperimentUtils.collectRewardTermsToSnippet(
                List.of("reward += after.goalReached - 0.1;")
        );
        assertThat(rewardFunction, is("rewardTermsRaw[0] += after.goalReached - 0.1;"));
    }

    @Test
    void rewardMinus() {
        String rewardFunction = ExperimentUtils.collectRewardTermsToSnippet(
                List.of("reward -= after.goalReached - 0.1;")
        );
        assertThat(rewardFunction, is("rewardTermsRaw[0] += -1*(after.goalReached - 0.1);"));
    }

    @Test
    void rewardMinusMinusEquals() {
        String rewardFunction = ExperimentUtils.collectRewardTermsToSnippet(
                List.of(
                        "reward = after.goalReached - 0.1;",
                        "reward -= after.goalReached - 0.1;",
                        "reward += after.goalReached - 0.1;"
                )
        );
        assertThat(rewardFunction, is(
                "rewardTermsRaw[0] = after.goalReached - 0.1;\n" +
                        "rewardTermsRaw[1] += -1*(after.goalReached - 0.1);\n" +
                        "rewardTermsRaw[2] += after.goalReached - 0.1;"
        ));
    }
}