package io.skymind.pathmind.bddtests.steps;

import io.skymind.pathmind.bddtests.page.ExperimentView;
import net.thucydides.core.annotations.Step;

public class ExperimentViewSteps {

    private ExperimentView experimentView;

    @Step
    public void experimentViewCheckExperimentHeader(String slot, String header, String status, boolean stopTrainingBtn, boolean shareWithSpBtn) {
        experimentView.experimentViewCheckExperimentHeader(slot, header, status, stopTrainingBtn, shareWithSpBtn);
    }
}