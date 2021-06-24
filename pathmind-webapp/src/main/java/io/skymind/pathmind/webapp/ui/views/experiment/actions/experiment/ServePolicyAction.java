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
import io.skymind.pathmind.webapp.ui.utils.GuiUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.components.ExperimentTitleBar;

public class ServePolicyAction {

    public static void servePolicy(Supplier<Experiment> getExperimentSupplier, PolicyServerService policyServerService, ExperimentTitleBar experimentTitleBar) {
        Experiment experiment = getExperimentSupplier.get();
        PolicyServerService.DeploymentStatus deploymentStatus = policyServerService.getPolicyServerStatus(experiment);
        Dialog dialog = new Dialog();
        Div dialogContent = new Div();
        Button closeButton = new Button(VaadinIcon.CLOSE_SMALL.create());
        closeButton.addClickListener(event -> {
            dialog.close();
        });

        dialog.addDialogCloseActionListener(close -> {
            experimentTitleBar.setServePolicyButtonText(true);
        });

        switch (deploymentStatus) {
            case FAILED: {
                final Button retryButton = GuiUtils.getPrimaryButton("Redeploy Now", click -> {
                    policyServerService.triggerPolicyServerDeployment(experiment);
                    dialog.close();
                });
                dialogContent.add(
                        new H3("Error"),
                        new Paragraph(
                            new Span("The policy server deployment was unsuccessful."),
                            new Html("<br/>"),
                            new Span("Click the button below to retry."),
                            new Html("<br/>"),
                            new Span("If the problem persists, please contact Pathmind for assistance.")
                        ),
                        retryButton
                );
                break;
            }
            case DEPLOYED: {
                final String url = policyServerService.getPolicyServerUrl(experiment);
                dialogContent.add(
                        new H3("The Policy is Live"),
                        new Span("The policy is being served at this URL:"),
                        new CopyField(url, true),
                        new Paragraph(new Span("Read the docs for more details:"),
                                new Html("<br/>"),
                                new Anchor(url + "/docs", url + "/docs"))
                );
                break;
            }
            case NOT_DEPLOYED: {
                policyServerService.triggerPolicyServerDeployment(experiment);
                // intentional fallthrough to PENDING state
            }
            case PENDING: {
                ProgressBar progressBar = new ProgressBar(0, 100);
                progressBar.setIndeterminate(true);
                dialogContent.add(
                        new H3("Deploying Policy Server"),
                        new Paragraph(
                            "Your policy will be available in about five minutes."
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
