package io.skymind.pathmind.webapp.ui.views.experiment.components;

import io.skymind.pathmind.shared.data.Experiment;

public interface ExperimentComponent {

    void setExperiment(Experiment experiment);

    // Do nothing by default for view only components. This is mainly for components the edit screens like those on NewExperimentView.
    default void updateExperiment() {
    }
}
