package io.skymind.pathmind.bddtests.steps;

import io.skymind.pathmind.bddtests.page.NewExperimentV2Page;
import net.thucydides.core.annotations.Step;

public class NewExperimentV2Steps {

    private NewExperimentV2Page newExperimentV2Page;

    @Step
    public void switchToRewardTermsBeta() {
        newExperimentV2Page.switchToRewardTermsBeta();
    }

    @Step
    public void addRewardTerm(String rewardVariable, String goal, String weight) {
        newExperimentV2Page.clickRewardTermBtn();
        newExperimentV2Page.chooseRewardVar(rewardVariable, goal, weight);
    }
}
