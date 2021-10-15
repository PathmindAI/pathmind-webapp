package io.skymind.pathmind.webapp.ui.views.experiment.components.rewardFunction;

import java.util.Optional;

import com.vaadin.flow.component.Component;
import io.skymind.pathmind.shared.data.RewardTerm;

public interface RewardTermRow {
    Component asComponent();

    RewardTerm getValue();

    Optional<RewardTerm> convertToValueIfValid(int index);

    default boolean hasErrors() {
        return false;
    }

    default void resize() {
        // ~ no op
    }

}
