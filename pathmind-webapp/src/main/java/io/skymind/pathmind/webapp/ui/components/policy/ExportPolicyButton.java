package io.skymind.pathmind.webapp.ui.components.policy;

import java.io.ByteArrayInputStream;
import java.util.function.Supplier;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.server.StreamResource;
import io.skymind.pathmind.db.dao.PolicyDAO;
import io.skymind.pathmind.services.PolicyFileService;
import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.utils.PolicyUtils;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;

import static io.skymind.pathmind.shared.utils.PathmindStringUtils.removeInvalidChars;

public class ExportPolicyButton extends Anchor {

    private Button exportButton;

    private PolicyFileService policyFileService;

    private Policy policy;
    private String policyFilename;

    // Hack -> Quick little solution to deal "same erasure" issue with lambda's.
    public interface ExperimentSupplier extends Supplier<Experiment> {
    }

    public interface PolicySupplier extends Supplier<Policy> {
    }

    public ExportPolicyButton(SegmentIntegrator segmentIntegrator, PolicyFileService policyFileService, PolicyDAO policyDAO, PolicySupplier getPolicySupplier) {
        super();
        this.policy = getPolicySupplier.get();
        setup(segmentIntegrator, policyFileService, policyDAO);
    }

    public ExportPolicyButton(SegmentIntegrator segmentIntegrator, PolicyFileService policyFileService, PolicyDAO policyDAO, ExperimentSupplier getExperimentSupplier) {
        super();
        this.policy = PolicyUtils.selectBestPolicy(getExperimentSupplier.get()).orElse(null);

        // Temporary solution until we hook up the eventbus. In the meantime we only make the decision to render on page reload.
        if (getExperimentSupplier.get().getTrainingStatusEnum() != RunStatus.Completed) {
            setVisible(false);
            return;
        }

        setup(segmentIntegrator, policyFileService, policyDAO);
    }

    private void setup(SegmentIntegrator segmentIntegrator, PolicyFileService policyFileService, PolicyDAO policyDAO) {

        // If there isn't even a policy at this point, such as when an experiment is starting, then there's
        // nothing to export. In the future we can add a subscriber to listen for the event at which point the
        // export policy button can become visible but for now since it's only internally we will just omit the button
        // and the support user can press the refresh button as the cost to add all this is not worth it compared
        // to getting the initial PR into production.
        if (policy == null) {
            setVisible(false);
            return;
        }

        this.policyFileService = policyFileService;
        policyFilename = PolicyUtils.generatePolicyFileName(policy);

        exportButton = new Button("Export Policy");
        exportButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        exportButton.setWidth("200px");
        exportButton.addClickListener(evt -> {
            policyDAO.updateExportedDate(policy.getId());
            segmentIntegrator.policyExported();
        });

        add(exportButton);
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
