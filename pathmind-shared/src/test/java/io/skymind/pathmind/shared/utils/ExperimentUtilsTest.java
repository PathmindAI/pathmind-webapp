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