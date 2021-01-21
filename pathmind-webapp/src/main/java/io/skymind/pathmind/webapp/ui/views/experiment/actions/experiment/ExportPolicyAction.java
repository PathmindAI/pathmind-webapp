package io.skymind.pathmind.webapp.ui.views.experiment.actions.experiment;

import java.util.Optional;
import java.util.function.Supplier;

import com.vaadin.flow.component.UI;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.ui.views.policy.ExportPolicyView;

public class ExportPolicyAction {
    public static void exportPolicy(Supplier<Experiment> getExperimentSupplier, Supplier<Optional<UI>> getUISupplier) {
        getUISupplier.get().ifPresent(ui -> ui.navigate(ExportPolicyView.class, getExperimentSupplier.get().getBestPolicy().getId()));
    }
}
