package io.skymind.pathmind.webapp.ui.views.experiment.actions.experiment;

import java.util.function.Supplier;

import io.netty.channel.epoll.Epoll;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.services.PolicyServerService;
import io.skymind.pathmind.webapp.ui.utils.ConfirmationUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.components.ExperimentTitleBar;

public class ShutDownPolicyAction {

    public static void shutDown(ExperimentTitleBar experimentTitleBar, PolicyServerService policyServerService, Supplier<Experiment> getExperimentSupplier) {
        Experiment experiment = getExperimentSupplier.get();
        ConfirmationUtils.confirmationPopupDialog(
                "Shut down policy server",
                "This will shut down the deployed policy server for this experiment (id: "+experiment.getId()+"). You will be able to redeploy the policy server.",
                "Shut down",
                () -> {
                    policyServerService.destroyPolicyServerDeployment(experiment);
                    experimentTitleBar.updateComponentEnablements();
                });
    }

}
