package io.skymind.pathmind.webapp.ui.views.experiment.actions.experiment;

import java.util.function.Supplier;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.services.PolicyServerService;
import io.skymind.pathmind.webapp.ui.components.atoms.ButtonWithLoading;
import io.skymind.pathmind.webapp.ui.components.molecules.CopyField;

public class ServePolicyAction {

    public static void servePolicy(Supplier<Experiment> getExperimentSupplier, ButtonWithLoading buttonWithLoading, PolicyServerService policyServerService) {
        Experiment experiment = getExperimentSupplier.get();
        Dialog dialog = new Dialog();
        Div dialogContent = new Div();
        Button closeButton = new Button(VaadinIcon.CLOSE_SMALL.create());
        closeButton.addClickListener(event -> {
            buttonWithLoading.setLoading(false);
            buttonWithLoading.setDisabled(false);
            dialog.close();
        });

        PolicyServerService.DeploymentStatus deploymentStatus = policyServerService.getPolicyServerStatus(experiment);

        switch (deploymentStatus) {
            case FAILED: {
                // TODO: show some message on fail deployment
                break;
            }
            case DEPLOYED: {
                final String url = policyServerService.getPolicyServerUrl(experiment);
                dialogContent.add(
                        new H3("Policy Serving"),
                        new Paragraph("This is the serving endpoint for the experiment's best policy."),
                        new CopyField(url),
                        new Paragraph(new Span("Read the "),
                                new Anchor("link", url + "/redoc"),
                                new Span(" to learn more about its usage."))
                );
                break;
            }
            case NOT_DEPLOYED: {
                policyServerService.triggerPolicyServerDeployment(experiment);
                // intentional fallthrough to PENDING state
            }
            case PENDING: {
                dialogContent.add(
                        // TODO: UI/UX and dynamic update
                        new H3("Policy Serving is deploying"),
                        new Paragraph("TODO: Visit this message when it's deployed")
                );
                break;
            }
        }

        dialogContent.addClassName("serve-policy-instructions");
        dialog.add(dialogContent, closeButton);
        dialog.open();
    }
}
