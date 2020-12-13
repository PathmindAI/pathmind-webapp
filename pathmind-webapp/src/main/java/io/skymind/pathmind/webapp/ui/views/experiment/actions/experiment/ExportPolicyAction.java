package io.skymind.pathmind.webapp.ui.views.experiment.actions.experiment;

import java.util.Optional;

import com.vaadin.flow.component.UI;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.ui.views.policy.ExportPolicyView;

public class ExportPolicyAction {
    public static void exportPolicy(Experiment experiment, Optional<UI> uiOptional) {
        uiOptional.ifPresent(ui -> ui.navigate(ExportPolicyView.class, experiment.getBestPolicy().getId()));
    }
}
