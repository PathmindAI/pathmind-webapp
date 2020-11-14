package io.skymind.pathmind.webapp.bus;

public enum BusEventType {
    PolicyUpdate,
    RunUpdate,
    UserUpdate,
    ExperimentCreated,
    ExperimentUpdate,
    ExperimentChanged,
    ExperimentArchived,
    ExperimentFavorite,
    RewardVariableSelected,
    SetQueryParameter,
    ModelUpdate
}