package io.skymind.pathmind.webapp.bus;

public enum BusEventType {
    PolicyUpdate,
    RunUpdate,
    UserUpdate,
    ExperimentCreated,
    ExperimentUpdate,
    ExperimentStartTraining,
    ExperimentArchived,
    ExperimentFavorite,
    ModelUpdate
}