package io.skymind.pathmind.webapp.bus;

public enum BusEventType {
    PolicyUpdate,
    RunUpdate,
    UserUpdate,
    ExperimentCreated,
    ExperimentUpdate,
    ExperimentSwitched,
    ExperimentSaved,
    ExperimentStartTraining,
    ExperimentArchived,
    ExperimentFavorite,
    ExperimentChanged,
    RewardVariableSelected,
    SetQueryParameter,
    ModelUpdate
}