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

import io.skymind.pathmind.db.dao.UserDAO;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.PathmindUser;
import io.skymind.pathmind.shared.services.PolicyServerService;
import io.skymind.pathmind.webapp.ui.components.molecules.CopyField;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.utils.ConfirmationUtils;
import io.skymind.pathmind.webapp.ui.utils.GuiUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.account.AccountUpgradeView;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;
public class ServePolicyButton extends Button {

    private PolicyServerService policyServerService;
    private Experiment experiment;
    private Dialog dialog;
    private Button closeButton;
    private UserDAO userDAO;
    private SegmentIntegrator segmentIntegrator;

    public ServePolicyButton(PolicyServerService policyServerService, UserDAO userDAO, SegmentIntegrator segmentIntegrator) {
        super();
        this.policyServerService = policyServerService;
        this.userDAO = userDAO;
        this.segmentIntegrator = segmentIntegrator;
        closeButton = new Button(VaadinIcon.CLOSE_SMALL.create());
        addClickListener(click -> {
            Boolean hasExistingDeployedPolicyServer = true; // TODO -> get the actual value
            // Check if the user is a free user & whether the user has an existing deployed policy server
            Boolean canDeploy = false;
            // Boolean canDeploy = !userDAO.findById(experiment.getProject().getPathmindUserId()).isBasicPlanUser() || (
            //     userDAO.findById(experiment.getProject().getPathmindUserId()).isBasicPlanUser() &&
            //     !hasExistingDeployedPolicyServer
            // );
            if (canDeploy) {
                openDeploymentDialog();
            } else {
                openUndeployableDialog();
            }
        });
    }

    private void openUndeployableDialog() {
        Dialog undeployableDialog = new Dialog();
        Button upgradeButton = GuiUtils.getPrimaryButton("Upgrade to Pro now", click -> {
            segmentIntegrator.navigatedToPricingFromPolicyServerLimitPopup();
            getUI().ifPresent(ui -> ui.navigate(AccountUpgradeView.class));
            undeployableDialog.close();
        });
        Button checkExistingPolicyServerButton = new Button("Review and shut down your existing policy server", click -> {
            getUI().ifPresent(ui -> ui.navigate(ExperimentView.class, "" + 123)); // 123 should be replaced with the actual experiment id
            undeployableDialog.close();
        });
        Button cancelButton = new Button("Cancel", click -> undeployableDialog.close());
        undeployableDialog.add(
            new Paragraph("You've reached the limit for your free plan."),
            WrapperUtils.wrapVerticalWithNoPadding(
                upgradeButton,
                checkExistingPolicyServerButton,
                cancelButton
            )
        );
        undeployableDialog.open();
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

    private void openDeploymentDialog() {
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

        PolicyServerService.DeploymentStatus deploymentStatus = policyServerService.getPolicyServerStatus(experiment);
        if (deploymentStatus == PolicyServerService.DeploymentStatus.NOT_DEPLOYED) {
            policyServerService.triggerPolicyServerDeployment(experiment);
            segmentIntegrator.deployPolicyServer();
        }

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
                        segmentIntegrator.redeployPolicyServer();
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
                    final Anchor docsLink = new Anchor(url + "/docs", url + "/docs");
                    docsLink.setTarget("_blank");
                    final Button shutDownPolicyServerButton = new Button("Shut Down Policy Server", click -> {
                        dialog.close();
                        ConfirmationUtils.confirmationPopupDialog(
                            "Shut down policy server",
                            "This will shut down the deployed policy server for this experiment (id: "+experiment.getId()+"). You will be able to redeploy the policy server.",
                            "Shut down",
                            () -> {
                                policyServerService.destroyPolicyServerDeployment(experiment);
                                segmentIntegrator.shutDownPolicyServer();
                                setServePolicyButtonText(true);
                            });
                        });
                    dialogContent.add(
                            new H3("The Policy is Live"),
                            new Span("The policy is being served at this URL:"),
                            new CopyField(url, true)
                    );
                    if (userDAO != null) {
                        PathmindUser user = userDAO.findById(experiment.getProject().getPathmindUserId());
                        dialogContent.add(
                            new Span("Your access token:"),
                            new CopyField(user.getApiKey())
                        );
                    }
                    dialogContent.add(
                        new Paragraph(new Span("Read the docs for more details:"),
                                    new Html("<br/>"),
                                    docsLink),
                        WrapperUtils.wrapWidthFullBetweenHorizontal(
                            shutDownPolicyServerButton,
                            GuiUtils.getPrimaryButton("Close", click -> dialog.close()))
                    );
                    break;
                }
                case NOT_DEPLOYED: {

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
