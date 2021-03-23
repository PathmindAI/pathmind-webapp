package io.skymind.pathmind.webapp.ui.views.experiment.actions.experiment;

import java.util.function.Supplier;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.progressbar.ProgressBar;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.services.PolicyServerService;
import io.skymind.pathmind.webapp.ui.components.molecules.CopyField;

public class ServePolicyAction {

    public static void servePolicy(Supplier<Experiment> getExperimentSupplier, PolicyServerService policyServerService) {
        PolicyServerService.DeploymentStatus deploymentStatus = policyServerService.getPolicyServerStatus(experiment);
        Experiment experiment = getExperimentSupplier.get();
        Dialog dialog = new Dialog();
        Div dialogContent = new Div();
        ProgressBar progressBar = new ProgressBar(0, 100);
        Button closeButton = new Button(VaadinIcon.CLOSE_SMALL.create());
        closeButton.addClickListener(event -> {
            dialog.close();
        });

        switch (deploymentStatus) {
            case FAILED: {
                dialogContent.add(
                        new H3("Error"),
                        new Paragraph(
                            new Span("The policy server deployment was unsuccessful."),
                            new Html("<br/>"),
                            new Span("Please contact Pathmind for assistance.")
                        )
                );
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
                progressBar.setValue(42); // need to get updated according to deployment progress
                dialogContent.add(
                        new H3("Deploying Policy Server"),
                        new Paragraph(
                            new Span("The policy server may take up to an hour to be ready."),
                            new Html("<br/>"),
                            new Span("We’ll send you an email once it’s complete.")
                        ),
                        progressBar
                );
                break;
            }
        }

        dialogContent.addClassName("serve-policy-instructions");
        dialog.add(dialogContent, closeButton);
        dialog.open();
    }
}
