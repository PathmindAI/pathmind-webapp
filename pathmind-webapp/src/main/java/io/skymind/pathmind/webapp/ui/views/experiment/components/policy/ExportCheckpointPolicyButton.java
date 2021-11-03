package io.skymind.pathmind.webapp.ui.views.experiment.components.policy;

import java.io.ByteArrayInputStream;
import java.util.function.Supplier;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.server.StreamRegistration;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;

import io.skymind.pathmind.db.dao.PolicyDAO;
import io.skymind.pathmind.services.PolicyFileService;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.utils.PolicyUtils;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.utils.ConfirmationUtils;

import static io.skymind.pathmind.shared.utils.PathmindStringUtils.removeInvalidChars;

public class ExportCheckpointPolicyButton extends Anchor {

    private Button exportButton;

    private SegmentIntegrator segmentIntegrator;
    private PolicyFileService policyFileService;
    private PolicyDAO policyDAO;
    private Policy policy;
    private String policyFilename;

    public ExportCheckpointPolicyButton(SegmentIntegrator segmentIntegrator, PolicyFileService policyFileService, PolicyDAO policyDAO, Supplier<Experiment> getExperimentSupplier) {
        super();
        this.policyFileService = policyFileService;
        this.policyDAO = policyDAO;
        this.segmentIntegrator = segmentIntegrator;

        setup();
    }

    private void setup() {
        exportButton = new Button("Export Checkpoint Policy");
        
        exportButton.addClickListener(evt -> {
            System.out.println("policy?"+policy);
            if (policy != null) {
                // TODO -> show dialog to tell user that this is a checkpoint policy and what to expect
                ConfirmationUtils.confirmationPopupDialog(
                    "Export Checkpoint Policy",
                    "The policy you are trying to download is from an interim checkpoint. The final policy will be available after the training has ended.",
                    "Download",
                    () -> {
                        StreamResource resource = getResourceStream(policyFilename);

                        final StreamRegistration regn = VaadinSession.getCurrent().getResourceRegistry().registerResource(resource);
                        getUI().ifPresent(ui -> 
                            ui.getCurrent().getPage().setLocation(regn.getResourceUri())
                        );
                    }
                );
                segmentIntegrator.checkpointPolicyExported();
            }
        });

        add(exportButton);
    }

    // TODO -> set policy using event bus subscriber

    public void setExperiment(Experiment experiment) {
        boolean hasPolicy = experiment.getBestPolicy() != null && experiment.getBestPolicy().hasFile();
        if (!hasPolicy) {
            setVisible(false);
            return;
        }

        System.out.println("policy in set experiment:"+policy);
        policy = policyDAO.getPolicy(experiment.getBestPolicy().getId());

        policyFilename = PolicyUtils.generatePolicyFileName(policy);
    }

    public String getPolicyFilename() {
        return policyFilename;
    }

    public Button getExportButton() {
        return exportButton;
    }

    private StreamResource getResourceStream(String filename) {
        return new StreamResource(removeInvalidChars(filename),
                () -> {
                    byte[] bytes = policyFileService.getCheckpointPolicyFile(policy.getId());
                    return new ByteArrayInputStream(bytes);
                });
    }

}
