package io.skymind.pathmind.webapp.bus;

public enum BusEventType {
    PolicyUpdate,
    RunUpdate,
    UserUpdate,
    ExperimentCreated,
    ExperimentStopTraining,
    ExperimentStartTraining,
    ExperimentArchived,
    ExperimentFavorite,
    ModelArchived,
    PolicyServerDeploymentUpdate,
}