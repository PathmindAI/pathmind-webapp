package io.skymind.pathmind.webapp.ui.views.experiment.components.rewardFunction;

import com.vaadin.flow.component.Component;

import io.skymind.pathmind.shared.data.RewardTerm;

import java.util.Optional;

public interface RewardTermRow {
    Component asComponent();
    RewardTerm getValue();

    Optional<RewardTerm> convertToValueIfValid(int index);
}
