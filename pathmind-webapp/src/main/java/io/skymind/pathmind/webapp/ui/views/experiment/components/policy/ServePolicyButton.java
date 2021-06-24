package io.skymind.pathmind.webapp.ui.views.experiment.components.policy;

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

public class ServePolicyButton extends Button {

    private PolicyServerService policyServerService;
    private Experiment experiment;
    private Dialog dialog;
    private Button closeButton;

    public ServePolicyButton(PolicyServerService policyServerService) {
        super();
        this.policyServerService = policyServerService;
        closeButton = new Button(VaadinIcon.CLOSE_SMALL.create());
        addClickListener(click -> openDialog());
    }

    public void setServePolicyButtonText(Boolean isCompletedWithPolicy) {
        if (!isCompletedWithPolicy) {
            setVisible(false);
            return;
        }
        setVisible(true);
        PolicyServerService.DeploymentStatus deploymentStatus = policyServerService.getPolicyServerStatus(experiment);
        String servePolicyButtonText;
        switch(deploymentStatus) {
            case FAILED:
                servePolicyButtonText = "Deployment Failed";
                break;
            case DEPLOYED:
                servePolicyButtonText = "Policy Server Live";
                break;
            case PENDING:
                servePolicyButtonText = "Policy Server Deploying";
                break;
            case NOT_DEPLOYED:
                // fallthrough
            default:
                servePolicyButtonText = "Start Policy Server";
        } 
        setText(servePolicyButtonText);
    }

    private void openDialog() {
        dialog = new Dialog();
        closeButton.addClickListener(event -> {
            dialog.close();
        });

        dialog.addOpenedChangeListener(close -> {
            if (!dialog.isOpened()) {
                // set Server Policy button text again when the user closes the popup dialog
                setServePolicyButtonText(true);
            }
        });

        updateDialogContent();

        dialog.open();
    }

    private void updateDialogContent() {
        if (dialog != null) {
            dialog.removeAll();
            Div dialogContent = new Div();
            PolicyServerService.DeploymentStatus deploymentStatus = policyServerService.getPolicyServerStatus(experiment);
            switch (deploymentStatus) {
                case FAILED: {
                    final Button retryButton = GuiUtils.getPrimaryButton("Redeploy Now", click -> {
                        policyServerService.triggerPolicyServerDeployment(experiment);
                        updateDialogContent();
                        setServePolicyButtonText(true);
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
                case PENDING:
                    // fallthrough
                default: {
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
        }
    }

    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
        boolean isCompletedWithPolicy = experiment.isTrainingCompleted() && experiment.getBestPolicy() != null && experiment.getBestPolicy().hasFile();
        setServePolicyButtonText(isCompletedWithPolicy);
        if (!isCompletedWithPolicy) {
            return;
        }
        updateDialogContent();
    }
    
}
