package io.skymind.pathmind.webapp.ui.components.policy;

import java.io.ByteArrayInputStream;
import java.util.function.Supplier;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.server.StreamResource;
import io.skymind.pathmind.db.dao.PolicyDAO;
import io.skymind.pathmind.services.PolicyFileService;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.security.SecurityUtils;
import io.skymind.pathmind.shared.utils.PolicyUtils;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;

import static io.skymind.pathmind.shared.utils.PathmindStringUtils.removeInvalidChars;

public class ExportPolicyButton extends Anchor {

    private Button exportButton;

    private SegmentIntegrator segmentIntegrator;
    private PolicyFileService policyFileService;
    private PolicyDAO policyDAO;
    private Policy policy;
    private String policyFilename;

    public ExportPolicyButton(SegmentIntegrator segmentIntegrator, PolicyFileService policyFileService, PolicyDAO policyDAO, Supplier<Experiment> getExperimentSupplier) {
        super();
        this.policyFileService = policyFileService;
        this.policyDAO = policyDAO;
        this.segmentIntegrator = segmentIntegrator;

        setup();
    }

    private void setup() {
        exportButton = new Button("Export Policy");
        exportButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        exportButton.addClickListener(evt -> {
            if (policy != null) {
                policyDAO.updateExportedDate(policy.getId());
                segmentIntegrator.policyExported();
            }
        });

        add(exportButton);
    }

    public void setExperiment(Experiment experiment) {
        policy = policyDAO.getPolicyIfAllowed(experiment.getBestPolicy().getId(), SecurityUtils.getUserId()).orElse(null);

        if (policy == null) {
            setVisible(false);
            return;
        }

        policyFilename = PolicyUtils.generatePolicyFileName(policy);

        getElement().setAttribute("href", getResourceStream(policyFilename));
        getElement().setAttribute("download", true);
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
                    byte[] bytes = policyFileService.getFreezingPolicyFile(policy.getRunId());
                    if (bytes == null) {
                        bytes = policyFileService.getPolicyFile(policy.getId());
                    }
                    return new ByteArrayInputStream(bytes);
                });
    }

}
