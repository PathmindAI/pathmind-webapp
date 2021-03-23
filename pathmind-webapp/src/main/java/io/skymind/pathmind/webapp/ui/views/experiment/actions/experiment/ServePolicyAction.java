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
        Experiment experiment = getExperimentSupplier.get();
        PolicyServerService.DeploymentStatus deploymentStatus = policyServerService.getPolicyServerStatus(experiment);
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
                        new H3("The Policy is Live"),
                        new Paragraph("The policy is being served at this URL:"),
                        new CopyField(url),
                        new Paragraph(new Span("Read the docs for more details:"),
                                new Anchor("link", url + "/docs"))
                );
                break;
            }
            case NOT_DEPLOYED: {
                policyServerService.triggerPolicyServerDeployment(experiment);
                // intentional fallthrough to PENDING state
            }
            case PENDING: {
                progressBar.setIndeterminate(true);
                dialogContent.add(
                        new H3("Deploying Policy Server"),
                        new Paragraph(
                            new Span("Your policy will be available in about five minutes.")
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
