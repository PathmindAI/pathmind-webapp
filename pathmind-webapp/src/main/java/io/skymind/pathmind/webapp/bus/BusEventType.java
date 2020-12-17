package io.skymind.pathmind.webapp.bus;

public enum BusEventType {
    PolicyUpdate,
    RunUpdate,
    UserUpdate,
    ExperimentCreated,
    ExperimentUpdate,
    ExperimentCompare,
    ExperimentSwitched,
    ExperimentStartTraining,
    ExperimentArchived,
    ExperimentFavorite,
    ExperimentChanged,
    RewardVariableSelected,
    SetQueryParameter,
    ModelUpdate
}